import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

public class SimpleDictionary {
	
	final static Charset ENCODING = StandardCharsets.UTF_8;
	final static String endDicLine = "End of Project Gutenberg's Webster's";
		
	private String _dictFile;
	private String _dictTemp = "bin/temp.txt";
	private Index[] _arraylst;
	private ReadWriteLock _lock;
	
	public SimpleDictionary(String dictFile) throws IOException {
		this._dictFile = dictFile;
		this._lock = new ReadWriteLock();
		this._arraylst = parseWords(_dictFile);
	}

	public void printdef(PrintStream out, String lookup) throws IOException {
		try {
			_lock.lockRead();
			int start = find(lookup);
			if (start == -1 || !_arraylst[start].word.equalsIgnoreCase(lookup)) return;
			printLines(out, _arraylst[start].startLine, _arraylst[start + 1].startLine);	
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			_lock.unlockRead();
		}
	}
	
	public int length() {
		return _arraylst.length - 1;
	}
	
	public boolean contain(String lookup) {
		try {
			_lock.lockRead();
			int start = find(lookup);
			return (start >= 0 && _arraylst[start].word.equalsIgnoreCase(lookup));
		}catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			_lock.unlockRead();
		}
		return false;
	}
	
	public void addWord(String word, String definition) throws IOException, InterruptedException {
		try {
			int startline = 0;
			int index = find(word);
			
			if (index == -1) startline = 1;
			else startline = _arraylst[index+1].startLine;
			
			Path pathRead = Paths.get(_dictFile);
			Path pathWrite = Paths.get(_dictTemp);
			int n_line = 1;
			try (BufferedReader reader = Files.newBufferedReader(pathRead, ENCODING)) {
				try (BufferedWriter writer = Files.newBufferedWriter(pathWrite, ENCODING, StandardOpenOption.CREATE)) {
					String line = null;
					while ((line = reader.readLine()) != null) {
						if (n_line == startline) {
							writer.write(word.toUpperCase());
							writer.newLine();
							writer.write(definition);
							writer.newLine();
							writer.newLine();
						}
					    writer.write(line);
						writer.newLine();
						n_line++;
					}
				}
			}
			
			_lock.lockWrite();
			Files.move(pathWrite, pathRead, StandardCopyOption.REPLACE_EXISTING);
			_arraylst = parseWords(_dictFile);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}finally {
			_lock.unlockWrite();	
		}
	}
	
	private Index[] parseWords(String fn) throws IOException {
		ArrayList<Index> arraylst = new ArrayList<Index>();
		Path path = Paths.get(fn);
		int n_line = 1;
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			String prev = "";
			String preWord = "";
			while ((line = reader.readLine()) != null) {
				if (prev.isEmpty() && !preWord.equalsIgnoreCase(line) && (line.matches("^[A-Z]+$") || line.startsWith(endDicLine))) {
					arraylst.add(new Index(line, n_line));
					preWord = line;
				}
				n_line++;
				prev = line;
			}
		}
		Index[] indexes = new Index[arraylst.size()];
		arraylst.toArray(indexes);
		return indexes;
	}

	private void printLines(PrintStream out, int start, int end) throws IOException {
		Path path = Paths.get(_dictFile);
		int n_line = 1;
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (n_line >= start) out.printf("\t%s\n", line);
				n_line += 1;
				if (n_line >= end) break;
			}
		}
	}

	private int find(String lookup) {	
		int left = 0; 
		int right = _arraylst.length - 2;
		while (left <= right) {
			int mid = (left + right) / 2;
			int compare = _arraylst[mid].word.compareTo(lookup);
			if (compare == 0) return mid;
			if (compare > 0) right = mid -1;
			else left = mid + 1;
		}
		return right;		
	}
}
