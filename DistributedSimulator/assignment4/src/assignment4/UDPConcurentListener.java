package assignment4;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ConcurrentLinkedQueue;

import assignment4.Client;
import assignment4.ConcurentQueueDictionary;
import assignment4.SimpleDictionary;
import assignment4.UDPConcurentResponser;

public class UDPConcurentListener extends Thread {
	private SimpleDictionary sd;
	private DatagramSocket serverSocket;
	private int UDP_PORT = 9002;
	private int RECIEVE_BYTES = 1024;
	private int NUMBER_OF_THREADS = 10;
	private int NUMBER_OF_ADDERS = 4;
	
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
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void terminate() {
		if (serverSocket != null)
			System.out.println("UDPConcurentListener is shutdowned");
			serverSocket.disconnect();
			serverSocket.close();
	}
	
	private void initUDP() throws IOException, InterruptedException  {
		UDPConcurentResponser[] threads = new UDPConcurentResponser[NUMBER_OF_THREADS];
		UDPConcurentAdder[] threads2 = new UDPConcurentAdder[NUMBER_OF_ADDERS];
		
		ConcurentQueueDictionary queue = new ConcurentQueueDictionary();
		ConcurrentLinkedQueue<Client> addQueue = new ConcurrentLinkedQueue<Client>();
		
		serverSocket = new DatagramSocket(UDP_PORT);
		
		for(int i = 0; i < NUMBER_OF_THREADS; i++) {
			threads[i] = new UDPConcurentResponser(sd, serverSocket, queue);
			threads[i].start();
		}

		for(int i = 0; i < NUMBER_OF_ADDERS; i++) {
			threads2[i] = new UDPConcurentAdder(sd, serverSocket, addQueue);
			threads2[i].start();
		}	
		
		//new UDPConcurentAdder(sd, serverSocket, addQueue).start();
		
		System.out.printf("Started Work partitioning server (port: %d) - number of threads: %d\n", UDP_PORT, NUMBER_OF_THREADS);
	
		while(!serverSocket.isClosed()) {
			byte[] recieveData = new byte[RECIEVE_BYTES];
			DatagramPacket receivePacket = new DatagramPacket(recieveData, recieveData.length);
			serverSocket.receive(receivePacket);
			Client client = new Client(receivePacket);
			
			if (client.word.equalsIgnoreCase("!q")) {
				break;
			}
			if (client.isAdd) {
				addQueue.add(client);
				continue;
			}
					
			queue.add(client);
		}
		
		System.out.printf("Ended work partitioning server (port: %d) - number of threads: %d\n", UDP_PORT, NUMBER_OF_THREADS);
	}
}
