package scn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import cls.HostServer;

import pp.Main;
import lib.ButtonText;
import lib.jog.graphics;
import lib.jog.window;
import lib.jog.audio.Sound;

public class Host extends Scene {
	String this_address = null;
	private String their_address = "";
	boolean connected = false;
	String their_name;
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
			host_server = new HostServer(this, player_name);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	public void connected(String address, String name) {
		connected = true;
		their_address = address;
		their_name = name;
	}
	
	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.printCentred("Welcome!:", window.width() / 2, 100, 5, 100);
		graphics.printCentred(player_name, window.width() / 2, 300, 10, 100);
		graphics.printCentred("Your IP: "+this_address, window.width()/2, 400, 5, 200);

		if (waiting_dot++ > 25) {
			waiting_dot = 0;
			if(dot.length() == 10) {
				dot = "";
			} else {
				dot += '.'; 
			}
		}
		
		graphics.printCentred("Waiting for player", window.width() / 2, 800, 5, 100);
		graphics.printCentred(dot, window.width() / 2, 850, 5, 100);
		
		if (connected) {
			graphics.printCentred("Connected to:", window.width() / 2, 400, 4, 100);
			graphics.printCentred(their_name, window.width() / 2, 500, 4, 100);
			graphics.printCentred(their_address, window.width() / 2, 600, 4, 100);
		}
	}

	@Override
	public void mousePressed(int key, int x, int y) {
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
	}

	@Override
	public void keyPressed(int key) {
	}

	@Override
	public void keyReleased(int key) {
	}

	@Override
	public void start() {
	}

	@Override
	public void update(double time_difference) {
	}

	
	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
	}
}