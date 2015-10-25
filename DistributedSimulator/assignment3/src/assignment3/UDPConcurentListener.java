package assignment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPConcurentListener extends Thread {
	private SimpleDictionary sd;
	private DatagramSocket serverSocket;
	private int UDP_PORT = 9002;
	private int RECIEVE_BYTES = 20;
	private int NUMBER_OF_THREADS = 10;
	private ConcurentQueueDictionary queue;
	
	
	public UDPConcurentListener(SimpleDictionary sd) {
		this.sd = sd;
	}
	
	@Override
	public void run() {
		try {
			initUDP();
		}catch (SocketException e) { 
			System.out.format("Error when open the socket: %s\n", e.getMessage());
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void terminate() {
		if (serverSocket != null)
			System.out.println("UDPConcurentListener is shutdowned");
			serverSocket.disconnect();
			serverSocket.close();
	}
	
	private void initUDP() throws IOException  {
		queue = new ConcurentQueueDictionary();
		serverSocket = new DatagramSocket(UDP_PORT);
		
		for(int i = 0; i < NUMBER_OF_THREADS; i++) {
			new UDPConcurentResponser(sd, serverSocket, queue).start();
		}
		
		System.out.printf("Started Work partitioning server (port: %d) - number of threads: %d\n", UDP_PORT, NUMBER_OF_THREADS);
	
		while(!serverSocket.isClosed()) {
			byte[] recieveData = new byte[RECIEVE_BYTES];
			DatagramPacket receivePacket = new DatagramPacket(recieveData, recieveData.length);
			serverSocket.receive(receivePacket);
			String word = new String(receivePacket.getData()).trim().toUpperCase();
			
			if (word.equalsIgnoreCase("!q")) {
				break;
			}
					
			queue.add(Client.getClient(receivePacket));
		}
		
		System.out.printf("Ended work partitioning server (port: %d) - number of threads: %d\n", UDP_PORT, NUMBER_OF_THREADS);
	}
}
