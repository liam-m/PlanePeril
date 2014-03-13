package scn;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;

import cls.Aircraft;
import cls.FlightPlan;

import lib.jog.graphics;
import lib.jog.audio.Sound;
import lib.jog.graphics.Image;

import pp.Main;
import rem.HostServer;
import rem.JoinClient;
import rem.MultiplayerServer;

public class MultiplayerLeft extends Multiplayer {
	MultiplayerServer server;
	
	public MultiplayerLeft(Main main, String my_name, String their_name, String their_address) {
		super(main, my_name, their_name);
		try {
			server = new MultiplayerServer(their_address, "player_1", 1730, 1731);
			Thread.sleep(3000);
			server.connect("player_2");
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void update(double dt) {
		
	}
	
	public void generateAircraft() {
		// Add aircraft to self
		addAircraft();
		// Send to other
		//server.sendAircraft(); // Needs args
	}
	
	public void addAircraft() {
		
	}

	@Override
	public void mousePressed(int key, int mouse_x, int mouse_y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(int key, int mouse_x, int mouse_y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void playSound(Sound sound) {
		// TODO Auto-generated method stub
		
	}
}