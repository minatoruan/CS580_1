package assignment4;

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
	
	public void add(Client client) {
		String word = client.word;
		synchronized(this) {
			if (requests.containsKey(word))
				requests.get(word).add(client);
			else {
				ArrayList<Client> list = new ArrayList<Client> ();
				list.add(client);
				requests.put(word, list);
				queue.add(word);
			}
		}
	}
	
	public ArrayList<Client> poll() {
		synchronized(this) {
			return requests.remove(queue.poll());
		}
	}
}
