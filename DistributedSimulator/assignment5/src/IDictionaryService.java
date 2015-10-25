
import java.io.IOException;
import java.rmi.*;

public interface IDictionaryService extends Remote {
	public String getDef(String word) throws RemoteException, IOException;
	public boolean addWord(String word, String definition) throws RemoteException, IOException;
}
