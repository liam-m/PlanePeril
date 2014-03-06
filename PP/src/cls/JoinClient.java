package cls;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JoinClient {
	int server_port = 1729;
	
	public String connect(String server_address, String my_address, String my_name) {
		HostInterface host_interface;
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(server_address, server_port);
			host_interface = (HostInterface)(registry.lookup("host_server"));
			return host_interface.connect(my_address, my_name);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			return null;
		}
	}
}