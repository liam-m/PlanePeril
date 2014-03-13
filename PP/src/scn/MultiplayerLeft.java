package scn;

import java.rmi.RemoteException;

import pp.Main;
import rem.HostServer;
import rem.JoinClient;
import rem.MultiplayerServer;

public class MultiplayerLeft extends Multiplayer {
	MultiplayerServer server;
	
	public MultiplayerLeft(Main main, String my_name, String their_name, String their_address) {
		super(main, my_name, their_name);
		try {
			server = new MultiplayerServer(their_address, "player_1");
			Thread.sleep(3000);
			server.connect("player_2");
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