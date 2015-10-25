package assignment4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UDPConcurentAdder extends Thread {
	private SimpleDictionary sd;
	private ConcurrentLinkedQueue<Client> queue;
	private DatagramSocket ss;
	
	public UDPConcurentAdder(SimpleDictionary sd, DatagramSocket ss, ConcurrentLinkedQueue<Client> queue) {
		this.sd = sd;
		this.queue = queue;
		this.ss = ss;
	}
	
	public void run() {
		Client client = null;
		String msg = "";
		while ((client = queue.poll()) != null || !ss.isClosed()) {
			try {
				if (client == null && (client = queue.poll()) == null) {
					sleep(100);
					continue;
				}

				System.out.printf("Thread %d - Adding word \"%s\"\n", this.getId(), client.word);
				
				if (!sd.contain(client.word)) {
					sd.addWord(client.word, client.definition);
					msg = "Adding word successfully.";
				} else 
					msg = "Word has already existed.";
			} catch (IOException e) {
				e.printStackTrace();
				msg = "Adding word unsuccessfully.";
			} catch (InterruptedException e) {
				e.printStackTrace();
				msg = "Adding word unsuccessfully.";
			} finally {
				if (client != null) sendMessage(client, msg);
			}
		}
	}
	
	private void sendMessage(Client client, String msg) {
		byte[] sendData = msg.getBytes();
		byte[] len = String.format("%s", sendData.length).getBytes();
		
		try {
			ss.send(new DatagramPacket(len, len.length, client.address, client.port));
			ss.send(new DatagramPacket(sendData, sendData.length, client.address, client.port));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
