package assignment3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UDPResponser extends Thread {
	
	private SimpleDictionary sd;
	private ConcurrentLinkedQueue<Client> queue;
	private DatagramSocket ss;

	public UDPResponser(SimpleDictionary sd, DatagramSocket serverSocket, ConcurrentLinkedQueue<Client> queue) {
		this.sd = sd;
		this.queue = queue;
		this.ss = serverSocket;
	}
	
	public void run() {
		Client client;
		try {
			while (!ss.isClosed()) {
				client = queue.poll();
				if (client == null) {
					sleep(100);
					continue;
				}				
				
				String word = client.word;
				System.out.printf("Thread %d - search for word \"%s\"\n", this.getId(), word);
				byte[] sendData = printDefinition(word);
		        byte[] len = String.format("%s", sendData.length).getBytes();
		        
		        ss.send(new DatagramPacket(len, len.length, client.address, client.port));
		        ss.send(new DatagramPacket(sendData, sendData.length, client.address, client.port));				
			}
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}  catch (IOException e) {
			e.printStackTrace();
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
