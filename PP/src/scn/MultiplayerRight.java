package scn;

import java.rmi.RemoteException;

import pp.Main;
import rem.HostServer;
import rem.JoinClient;
import rem.MultiplayerServer;

public class MultiplayerRight extends Multiplayer {
	MultiplayerServer server;
	
	public MultiplayerRight(Main main, String my_name, String their_name, String their_address) {
		super(main, their_name, my_name);
		try {
			server = new MultiplayerServer(their_address);
			Thread.sleep(1000);
			server.connect();
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void addAircraft() {
		// Add aircraft to self
		// Send to other
		server.sendAircraft();
	}
}