package scn;

import pp.Main;
import rem.HostServer;
import rem.JoinClient;

public class MultiplayerRight extends Multiplayer {
	public MultiplayerRight(Main main, HostServer host, JoinClient client) {
		super(main, host.getTheirName(), host.getMyName());

	}
}