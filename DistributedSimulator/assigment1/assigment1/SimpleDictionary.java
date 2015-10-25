package assigment1;

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
	
	public class LineInfo {
		public int start;
		public int end;
		
		public LineInfo(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}	
	
	final static Charset ENCODING = StandardCharsets.UTF_8;
		
	private String _dictFile; 
	private Index[] _arraylst;
	
	public SimpleDictionary(String dictFile) throws IOException {
		this._dictFile = dictFile;
		parseWords();
	}
	
	private void parseWords() throws IOException {
		ArrayList<Index> arraylst = new ArrayList<Index>();
		Path path = Paths.get(_dictFile);
		int n_line = 1;
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (line.matches("^[A-Z\\-]+$") || line.startsWith("End of Project Gutenberg's Webster's"))
					arraylst.add(new Index(line, n_line));
				n_line++;
			}		
		}
		_arraylst = new Index[arraylst.size()];
		arraylst.toArray(_arraylst);
	}
	
	private LineInfo[] findLines(String lookup) {
		ArrayList<LineInfo> lst = new ArrayList<LineInfo>();
		for (int i = 0; i < _arraylst.length - 1; i++) {
			if (_arraylst[i].word.equalsIgnoreCase(lookup) == false) continue;
			lst.add(new LineInfo(_arraylst[i].startLine, _arraylst[i+1].startLine));
		}
		LineInfo[] rs = new LineInfo[lst.size()];
		lst.toArray(rs);
		return rs;
	}
	
	private void printLines(PrintStream out, LineInfo lineInfo) throws IOException {
		Path path = Paths.get(_dictFile);
		int n_line = 1;
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (n_line >= lineInfo.start) out.printf("\t%s\n", line);
				n_line += 1;
				if (n_line >= lineInfo.end) break;
			}
		}
	}	
	
	public void printdef(PrintStream out, String lookup) throws IOException {
		for (LineInfo li : findLines(lookup)) {
			printLines(out, li);
		}
	}
	
	public boolean contains(String lookup) throws IOException {
		for(int i = 0; i < _arraylst.length - 1; i ++)
			if (lookup.equalsIgnoreCase(_arraylst[i].word)) return true;
		return false;
	}
	
	public int length() {
		return _arraylst.length - 1;
	}
}
