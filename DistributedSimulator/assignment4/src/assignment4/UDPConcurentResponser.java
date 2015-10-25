package assignment4;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

import assignment4.Client;
import assignment4.ConcurentQueueDictionary;
import assignment4.SimpleDictionary;

public class UDPConcurentResponser extends Thread {
	private SimpleDictionary sd;
	private DatagramSocket ss;
	private ConcurentQueueDictionary queue;

	public UDPConcurentResponser(SimpleDictionary sd, DatagramSocket ss, ConcurentQueueDictionary queue) {
		this.sd = sd;
		this.queue = queue;
		this.ss = ss;
	}
	
	public void run() {
		ArrayList<Client> list = null;
		while ((list = queue.poll()) != null || !ss.isClosed()) {
			try {
				if (list == null && (list = queue.poll()) == null) {
					sleep(100);
					continue;
				}
				
				String word = list.get(0).word;
				System.out.printf("Thread %d - search for word \"%s\"\n", this.getId(), word);
				
				byte[] sendData = printDefinition(word);
				byte[] len = String.format("%s", sendData.length).getBytes();
				
				for(Client client : list) {				
					ss.send(new DatagramPacket(len, len.length, client.address, client.port));
					ss.send(new DatagramPacket(sendData, sendData.length, client.address, client.port));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			list = null;
		}
	}
	
	private byte[] printDefinition(String input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		sd.printdef(ps, input);
		if (baos.size() == 0) 
			ps.println(String.format("\tWord not found\n\t%s", input.toUpperCase()));
		return baos.toByteArray();
	}
}
