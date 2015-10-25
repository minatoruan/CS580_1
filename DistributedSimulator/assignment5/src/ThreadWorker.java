import java.io.IOException;

public class ThreadWorker implements Runnable {
	private String word;
	private String[] bufferResults;
	private int index;
	private IDictionaryService service;
	
	public ThreadWorker (IDictionaryService service, String word, String[] bufferResults, int index) {
		this.service = service;
		this.word = word;
		this.bufferResults = bufferResults;
		this.index = index;
	}
	
	@Override
	public void run() {
		try {
			if (word.startsWith("&"))
				bufferResults[index] = Boolean.toString(service.addWord(getWord(word), getDefinition(word)));
			else
				bufferResults[index] = service.getDef(word);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getWord(String str) {
		int i = str.indexOf(":");
		return str.substring(1, i).toUpperCase();
	}
	
	private String getDefinition(String str) {
		int i = str.indexOf(":");
		return str.substring(i+1);
	}	 
}
