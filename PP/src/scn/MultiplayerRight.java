package scn;

import java.rmi.RemoteException;

import pp.Main;
import rem.HostServer;
import rem.JoinClient;
import rem.MultiplayerServer;

public class MultiplayerRight extends Multiplayer {
	MultiplayerServer server;
	
	public MultiplayerRight(Main main, String my_name, String their_name, String their_address) {
		super(main, their_name, my_name, false);
		try {
			server = new MultiplayerServer(their_address, "player_2", 1731, 1730);
			Thread.sleep(3000);
			server.connect("player_1");
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void addAircraft() {
		// Add aircraft to self
		// Send to other
		// server.sendAircraft(); // Needs args
	}
}