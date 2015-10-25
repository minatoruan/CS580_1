import java.io.IOException;
import assignment4.SimpleDictionary;
import assignment4.UDPConcurentListener;

public class Assignment4 {
	
	public static void main(String[] args) throws IOException, InterruptedException {
		String dictionary = "bin/dictionary.txt";
		SimpleDictionary sd =  new SimpleDictionary(dictionary);

		System.out.printf("Loaded %d words from file\n", sd.length());
		System.out.println("Init server socket...");
		
		UDPConcurentListener listener2 = new UDPConcurentListener(sd);
	
		listener2.start();
		listener2.join();
		listener2.terminate();
		
	}
}
