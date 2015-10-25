
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.*;
import java.rmi.server.*;


public class DictionaryServiceImpl extends UnicastRemoteObject implements IDictionaryService {

	private static final long serialVersionUID = 1L;
	private SimpleDictionary sd;
	private static Object obj = new Object();

	public DictionaryServiceImpl(SimpleDictionary sd) throws RemoteException {
		super();
		this.sd = sd;
	}
		
	@Override
	public String getDef(String word) throws RemoteException, IOException {
		System.out.printf("Thread %d - search for word \"%s\"\n", Thread.currentThread().getId(), word);
		return printDefinition(word.toUpperCase());
	}

	@Override
	public boolean addWord(String word, String definition) throws RemoteException, IOException {
		boolean result = false;
		try {
			System.out.printf("Thread %d - Adding word \"%s\"\n", Thread.currentThread().getId(), word);
			if (!sd.contain(word)) {
				synchronized (obj) {
					sd.addWord(word.toUpperCase(), definition);
				}
				result = true;
			} else 
				result = false;
		} catch (IOException e) {
			e.printStackTrace();
			result = false;
		} catch (InterruptedException e) {
			e.printStackTrace();
			result = false;
		}
		return result; 
	}
	
	private String printDefinition(String input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		sd.printdef(ps, input);
		if (baos.size() == 0) {
			ps.println(String.format("\tWord not found\n\t%s", input.toUpperCase()));
		}
		return baos.toString(SimpleDictionary.ENCODING.toString());
	}
}
