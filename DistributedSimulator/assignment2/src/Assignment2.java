import java.io.IOException;
import assignment2.UDPListener;

public class Assignment2 {

	public static void main(String[] args) throws IOException {
		//System.out.println("Init server socket...");
		UDPListener udp = new UDPListener(); 
		udp.start();
		System.in.read();
		udp.terminate();
	}

}
