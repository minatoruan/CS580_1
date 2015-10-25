import java.rmi.Naming;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Assignment5Client {

	private static final String Sentence = "test After the signatures are folded and gathered they move into the bindery In the middle of last century there were still many trade binders standalone binding companies which did no printing specializing in binding alone At that time because of the dominance of letterpress printing typesetting and printing took place in one location and binding in a different factory When type was all metal a typical worth of book worth of type would be bulky fragile and heavy The less it was moved in this condition the better so printing would be carried out in the same location as the typesetting Printed sheets on the other hand could easily be moved Now because of increasing computerization of preparing a book for the printer the typesetting part of the job has flowed upstream where it is done either by separately contracting companies working for the publisher by the publishers themselves or even by the authors Mergers in the book manufacturing industry mean that it is now unusual to find a bindery which is not also involved in book printing and vice versa ABBBA NEWWORDD NEWWORD mountebank Antioxidant";
	private static final String[] NewWords =  new String[]  {"&MOUNTEBANK:Definition:	(noun) A hawker of quack medicines who attracts customers with stories, jokes, or tricks.",
                                                "&mountebank:Definition:	(noun) A hawker of quack medicines who attracts customers with stories, jokes, or tricks.",
                                                "&Glucophage:Definition:	A trademark for the drug metformin.",
                                                "&Antioxidant:Definition:	Any substance that reduces the damage caused by oxidation, such as the harm caused by free radicals."};
			
	//private static final String Sentence = "MOUNTEBANK mountebank Glucophage Antioxidant";		
	public static void main(String[] args) {
		try {
			String serverURL = "rmi://localhost/dictionary";
			IDictionaryService service = (IDictionaryService) Naming.lookup(serverURL);
			run(service);
			
		} catch(Exception ex) {
			System.out.println("Exception: " + ex);
		}
		
	}

	public static void run(IDictionaryService service) {
		ExecutorService executor = Executors.newFixedThreadPool(5);
		String[] words = Sentence.split(" ");
		String[] bufferResults = new String[words.length];
		String[] bufferAddingResults = new String[NewWords.length];
		int idx = 0;
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < words.length; i++) {
			executor.execute(new ThreadWorker(service, words[i], bufferResults, i));
			if (idx < NewWords.length && i % 6 == 0) {
				executor.execute(new ThreadWorker(service, NewWords[idx], bufferAddingResults, idx));
				idx ++;
			}
		}

		executor.shutdown();
		
		while (!executor.isTerminated()){}
		for(int i = 0; i < words.length; i++) {
			System.out.printf("Look for word: %s\n", words[i]);
			System.out.println(bufferResults[i]);
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.printf("Total time1: %d\n", totalTime);
		
		/*for(int i = 0; i < bufferAddingResults.length; i++) {
			System.out.printf("Adding for word %s - %s\n", NewWords[i], bufferAddingResults[i]);
		}
		
		*/
		System.out.println("Finished all threads");
	}
}
