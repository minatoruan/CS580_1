import java.rmi.Naming;
import java.rmi.NoSuchObjectException;

public class Assignment5 {

	public static void main(String[] args) throws NoSuchObjectException {
		try {		
			String dictionary = "bin/dictionary.txt";
			SimpleDictionary sd =  new SimpleDictionary(dictionary);

			System.out.printf("Loaded %d words from file\n", sd.length());
			System.out.println("Init server socket...");
			
			DictionaryServiceImpl objService = new DictionaryServiceImpl(sd);
			Naming.rebind("dictionary", objService);
			
			System.out.println("Server ready.");
		} catch(Exception e) {
			System.out.println("Exception: " + e);
		}
	}

}
