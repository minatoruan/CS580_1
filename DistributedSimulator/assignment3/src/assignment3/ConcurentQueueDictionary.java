package assignment3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class ConcurentQueueDictionary {
	private Queue<String> queue;
	private HashMap<String, ArrayList<Client>> requests;
	
	public ConcurentQueueDictionary() {
		queue = new LinkedList<String>();
		requests = new HashMap<String, ArrayList<Client>>();
	}
	
	public synchronized void add(Client client) {
		String word = client.word;
		
		if (requests.containsKey(word))
			requests.get(word).add(client);
		else {
			ArrayList<Client> list = new ArrayList<Client> ();
			list.add(client);
			requests.put(word, list);
			queue.add(word);
		}
	}
	
	public synchronized ArrayList<Client> poll() {
		String word = queue.poll();
		if (word == null) return null;
		return requests.remove(word);
	}
}
