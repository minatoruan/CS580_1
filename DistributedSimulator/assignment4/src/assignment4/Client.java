package assignment4;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class Client {
	public InetAddress address;
	public int port;
	public String word;
	public String definition;
	public boolean isAdd;
	
	public Client(DatagramPacket dp) {
		String w = new String(dp.getData()).trim();
		
		address = dp.getAddress();
		port = dp.getPort();
		isAdd = w.startsWith("&") && w.indexOf(":") > 0;
		
		if (isAdd) {
			definition = getDefinition(new String(dp.getData()).trim());
			word = getWord(w);
		} else {
			word = w.toUpperCase();
		}
	}
	
	private String getWord(String str) {
		int i = str.indexOf(":");
		return str.substring(1, i).toUpperCase();
	}
	
	private String getDefinition(String str) {
		int i = str.indexOf(":");
		return str.substring(i+1);
	}	
}
