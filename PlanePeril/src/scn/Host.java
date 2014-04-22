package scn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;


import pp.Main;
import rem.HostServer;
import rem.JoinClient;
import lib.ButtonText;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import lib.jog.audio.Sound;

public class Host extends Scene {
	String this_address = null;
	HostServer host_server;
	
	String player_name;
	String dot = "";
	private int waiting_dot = 0;
	
	public Host(Main main, String player_name) {
		super(main);
		this.player_name = player_name;
		try {
			this_address = (InetAddress.getLocalHost().getHostAddress()).toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			host_server = new HostServer(player_name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public String getPlayerName() {
		return this.player_name;
	}
	
	public void connected(String their_name, String their_address) {
		//main.setScene(new MultiplayerLeft(main, player_name, their_name, their_address));
	}
	
	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.printTextCentred("Welcome!:", window.getWidth() / 2, 100, 5, 100);
		graphics.printTextCentred(player_name, window.getWidth() / 2, 200, 10, 100);
		graphics.printTextCentred("Your IP: "+this_address, window.getWidth()/2, 300, 5, 200);

		if (waiting_dot++ > 25) {
			waiting_dot = 0;
			if(dot.length() == 10) {
				dot = "";
			} else {
				dot += '.'; 
			}
		}
			
		graphics.printTextCentred("Waiting for player", window.getWidth() / 2, 800, 5, 100);
		graphics.printTextCentred(dot, window.getWidth() / 2, 850, 5, 100);
	}

	@Override
	public void mousePressed(int key, int x, int y) {
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
	}

	@Override
	public void keyPressed(int key) {
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
	}

	@Override
	public void keyReleased(int key) {
	}

	@Override
	public void start() {
	}

	@Override
	public void update(double time_difference) {
		if (host_server.hasConnected()) {
			String their_name = host_server.getTheirName();
			String their_address = host_server.getTheirAddress();
			main.closeScene();
			main.setScene(new Multiplayer(main, player_name, their_name, their_address, true));
		}
	}
	
	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
	}
}