package assignment3;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Client {
	public static Client getClient(DatagramPacket dp) {
		Client client = new Client();
		client.address = dp.getAddress();
		client.port = dp.getPort();
		client.word = new String(dp.getData()).trim().toUpperCase();
		return client;
	}
	
	public InetAddress address;
	public int port;
	public String word;
}
