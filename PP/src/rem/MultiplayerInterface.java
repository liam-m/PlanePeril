package rem;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MultiplayerInterface extends Remote {
	void addAircraft(boolean x) throws RemoteException;
}