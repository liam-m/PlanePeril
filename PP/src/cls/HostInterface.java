package cls;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface HostInterface extends Remote {
	String connect(String address, String their_name) throws RemoteException;
}