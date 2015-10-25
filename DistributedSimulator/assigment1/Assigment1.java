import java.io.IOException;
import java.util.Scanner;

import assigment1.SimpleDictionary;

public class Assigment1 {
	
	public static String Sentence = "After the signatures are folded and gathered they move into the bindery In the middle of last century there were still many trade binders standalone binding companies which did no printing specializing in binding alone At that time because of the dominance of letterpress printing typesetting and printing took place in one location and binding in a different factory When type was all metal a typical worth of book worth of type would be bulky fragile and heavy The less it was moved in this condition the better so printing would be carried out in the same location as the typesetting Printed sheets on the other hand could easily be moved Now because of increasing computerization of preparing a book for the printer the typesetting part of the job has flowed upstream where it is done either by separately contracting companies working for the publisher by the publishers themselves or even by the authors Mergers in the book manufacturing industry mean that it is now unusual to find a bindery which is not also involved in book printing and vice versa";
	
	public static void main(String[] args) throws IOException {
		Scanner inputReader = new Scanner(System.in);
		SimpleDictionary sd =  new SimpleDictionary("bin/dictionary.txt");
		System.out.printf("Loaded %d words from file\n", sd.length());
		
		
		String[] words = Sentence.split(" ");
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < words.length; i++) {
			sd.printdef(System.out, words[i]);
		}
		long endTime   = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.printf("Total time: %d", totalTime);
		
		
		/*
		while (true) {
			System.out.print("Enter your word (leave empty to exit): ");
			String input = inputReader.nextLine();
			if (input.equalsIgnoreCase("")) break;
			if (sd.contains(input) == true) {
				System.out.println("\tWord found");
				sd.printdef(System.out, input);
			} else
				System.out.println("\tWord not found");
		}
		*/
		inputReader.close();
	}

}
