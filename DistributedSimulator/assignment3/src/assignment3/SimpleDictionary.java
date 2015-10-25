package assignment3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SimpleDictionary {
	
	public class Index {
		public String word;
		public int startLine;
		
		public Index(String word, int startLine) {
			this.word = word;
			this.startLine = startLine;
		}
	}
	
	final static Charset ENCODING = StandardCharsets.UTF_8;
		
	private String _dictFile; 
	private Index[] _arraylst;
	
	public SimpleDictionary(String dictFile) throws IOException {
		this._dictFile = dictFile;
		parseWords();
	}
	
	public Index[] getIndex() {
		return _arraylst;
	}
	
	private void parseWords() throws IOException {
		ArrayList<Index> arraylst = new ArrayList<Index>();
		Path path = Paths.get(_dictFile);
		int n_line = 1;
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			String prev = "";
			String preWord = "";
			while ((line = reader.readLine()) != null) {
				if (prev.isEmpty() && !preWord.equalsIgnoreCase(line) && (line.matches("^[A-Z]+$") || line.startsWith("End of Project Gutenberg's Webster's"))) {
					arraylst.add(new Index(line, n_line));
					preWord = line;
				}
				n_line++;
				prev = line;
			}		
		}
		_arraylst = new Index[arraylst.size()];
		arraylst.toArray(_arraylst);
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
		int right = _arraylst.length - 1;
		while (left <= right) {
			int mid = (left + right) / 2;
			int compare = _arraylst[mid].word.compareTo(lookup);
			if (compare == 0) return mid;
			if (compare > 0) right = mid -1;
			else left = mid + 1;
		}
		return -1;		
	}
	
	public void printdef(PrintStream out, String lookup) throws IOException {
		int start = find(lookup);
		if (start == -1) return;
		printLines(out, _arraylst[start].startLine, _arraylst[start + 1].startLine);
	}
	
	public boolean contains(String lookup) throws IOException {
		return find(lookup) >= 0;
	}
	
	public int length() {
		return _arraylst.length - 1;
	}
}
