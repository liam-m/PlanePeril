package scn;

import pp.Main;
import rem.HostServer;
import rem.JoinClient;

public class MultiplayerLeft extends Multiplayer {
	public MultiplayerLeft(Main main, HostServer host, JoinClient client) {
		super(main, host.getMyName(), host.getTheirName());
		
	}
}