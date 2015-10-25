package assignment3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

public class UDPListener extends Thread {
	private SimpleDictionary sd;
	private DatagramSocket serverSocket;
	private int UDP_PORT = 9001;
	private int RECIEVE_BYTES = 20;
	private int NUMBER_OF_THREADS = 10;
	
	public UDPListener(SimpleDictionary sd) {
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
			System.out.println("UDPListener is shutdowned");
			serverSocket.disconnect();
			serverSocket.close();
	}
	
	private void initUDP() throws IOException  {
		serverSocket = new DatagramSocket(UDP_PORT);
		ConcurrentLinkedQueue<Client> queue = new ConcurrentLinkedQueue<Client>();
			
		for(int i = 0; i < NUMBER_OF_THREADS; i++) {
			new UDPResponser(sd, serverSocket, queue).start();
		}
		
		System.out.printf("Started Task of bags server (port: %d) - number of threads: %d\n", UDP_PORT, NUMBER_OF_THREADS);
			
		while(!serverSocket.isClosed()) {
			byte[] recieveData = new byte[RECIEVE_BYTES];
			DatagramPacket receivePacket = new DatagramPacket(recieveData, recieveData.length);
			serverSocket.receive(receivePacket);
			
			Client client = Client.getClient(receivePacket);
			if (client.word.equalsIgnoreCase("!q")) break;
			queue.add(client);
		}
		System.out.printf("Ended Task of bags server (port: %d) - number of threads: %d\n", UDP_PORT, NUMBER_OF_THREADS);
	}
}
