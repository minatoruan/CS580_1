import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import assignment3.SimpleDictionary;
import assignment3.SimpleDictionary.Index;
import assignment3.UDPConcurentListener;
import assignment3.UDPListener;

public class Assignment3 {
	
	//private static boolean run = true;

	public static void main(String[] args) throws IOException, InterruptedException {	
		String dictionary = "bin/dictionary.txt";
		SimpleDictionary sd =  new SimpleDictionary(dictionary);
		System.out.printf("Loaded %d words from file\n", sd.length());
		System.out.println("Init server socket...");
		
		UDPListener listener1 = new UDPListener(sd);
		UDPConcurentListener listener2 = new UDPConcurentListener(sd);
		
		listener1.start();
		listener2.start();
		
		listener1.join();
		listener2.join();
		
		listener1.terminate();
		listener2.terminate();
	}
	/*
	public static void test(SimpleDictionary sd) {
		Index[] indexes = sd.getIndex();
		List<String> words = new ArrayList<String>();
		for(Index index : indexes) {
			if (words.contains(index.word) == true) 
				System.out.printf("%s %d", index.word, index.startLine);
			else
				words.add(index.word);
		}
	}
	
	public static void test1() throws InterruptedException {
		Thread t = new Thread() {
            public void run() {
                // infinite loop
                while (run) {
                    try {
                        Thread.sleep(1000);
                        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
                        System.out.printf("Number of threads: %d\n", threadSet.size());
                        
                        
                    } catch (InterruptedException e) {
                    }
                    // as long as this line printed out, you know it is alive.
                    System.out.println("thread is running...");
                }
            }
        };
        t.start();
	}
	*/
}
