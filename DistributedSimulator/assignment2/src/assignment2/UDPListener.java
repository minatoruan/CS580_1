package assignment2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPListener extends Thread {
	private SimpleDictionary sd;
	private DatagramSocket serverSocket;
	private int UDP_PORT = 9001;
	private int SEND_BYTES = 1024 * 3;
	private int RECIEVE_BYTES = 20;
	private String dictionary = "bin/dictionary.txt";
	
	@Override
	public void run() {
		try {
			sd =  new SimpleDictionary(dictionary);
			System.out.printf("Loaded %d words from file\n", sd.length());
			System.out.printf("Press enter to exit\n", sd.length());
			initUDP();
		}catch (SocketException e) { 
			System.out.println("Socket is closed");
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	public void terminate() {
		if (serverSocket != null)
			serverSocket.close();
	}
	
	private void initUDP() throws IOException  {
		serverSocket = new DatagramSocket(UDP_PORT);
		while(true) {
			byte[] recieveData = new byte[RECIEVE_BYTES];
			DatagramPacket receivePacket = new DatagramPacket(recieveData, recieveData.length);
			serverSocket.receive(receivePacket);
			
			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			
			String word = new String(receivePacket.getData()).trim();
			System.out.printf("Look up \"%s\"\n", word);
			byte[] sendData = printDefinition(word);
            
            byte[] len = String.format("%s", sendData.length).getBytes();
            
            DatagramPacket sendPacket = null;
            
            sendPacket = new DatagramPacket(len, len.length, IPAddress, port);
            serverSocket.send(sendPacket);
            
            sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            serverSocket.send(sendPacket);
		}
	}
	
	private byte[] printDefinition(String input) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PrintStream ps = new PrintStream(baos);
		if (sd.contains(input) == true) {
			ps.println("\tWord found");
			sd.printdef(ps, input);
		} else
			ps.println("\tWord not found");
		
		return baos.toByteArray();
	}			
	
}
