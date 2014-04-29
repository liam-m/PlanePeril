package scn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.lwjgl.input.Keyboard;


import lib.ButtonText;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import lib.jog.audio.Sound;
import pp.Main;
import rem.HostInterface;

public class Join extends Scene {
	
	String this_address = null;
	private static String their_address = "";
	
	String player_name;
	String their_name = "";
	
	ButtonText join_button;
	
	boolean could_not_connect = false;
	boolean attempting_to_join = false;
	
	private final int JOIN_WIDTH = 100;
	private final int JOIN_HEIGHT = 25;
	private final int JOIN_X_POSITION = (window.getWidth() /2) - (JOIN_WIDTH/2);
	private final int JOIN_Y_POSITION = 800;
	
	
	int server_port = 1729;
	
	public Join(Main main, String player_name) {
		super(main);
		this.player_name = player_name;
		try {
			this_address = (InetAddress.getLocalHost().getHostAddress()).toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	public void connectToOpponent() throws RemoteException {
		HostInterface host_interface;
		Registry registry;
		try {
			registry = LocateRegistry.getRegistry(their_address, server_port);
			host_interface = (HostInterface)(registry.lookup("host_server"));
			their_name = host_interface.connect(this_address, player_name);
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start() {
		ButtonText.Action join = new ButtonText.Action() {
			@Override
			public void action() {
				could_not_connect = false;
				attempting_to_join = true;
				try {
					connectToOpponent();
					main.closeScene();
					main.setScene(new Multiplayer(main, their_name, player_name, their_address, false));
				} catch (RemoteException e) {
					could_not_connect = true;
					attempting_to_join = false;
				}
			}
		};

		join_button = new ButtonText("Join", join, JOIN_X_POSITION, JOIN_Y_POSITION, JOIN_WIDTH, JOIN_HEIGHT, true, true);

	}
	
	@Override
	public void mousePressed(int key, int x, int y) {
		if (join_button.isMouseOver(x, y)) {
			join_button.act();
		}		
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
	}

	@Override
	public void keyPressed(int key) {
		could_not_connect = false;
		int address_length = their_address.length();
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		} else if (key == input.KEY_BACKSPACE) {
			if (address_length > 0)
				their_address = their_address.substring(0, address_length-1);
		} else if (key == input.KEY_RETURN || key == input.KEY_NUMPADENTER) {
			join_button.act();
		} else if (address_length < 15) { // Max IP length is 15 - 256.256.256.256
			if ((key >= input.KEY_1 && key <= input.KEY_0) || (key >= input.KEY_7_NP && key <= input.KEY_0_NP && key != 74 && key != 78)) { // Number key
				if (address_length <= 2 || their_address.charAt(address_length-3) == '.' || their_address.charAt(address_length-2) == '.' || their_address.charAt(address_length-1) == '.') // Can't have more than 3 numbers without a '.'
					their_address += Keyboard.getEventCharacter();
			} else if (key == input.KEY_DECIMAL || key == input.KEY_PERIOD) {
				if (address_length > 0 && their_address.charAt(address_length-1) != '.') // Can't start with '.', can't have two '.'s in a row
					their_address += '.';
			}
		}
	}

	@Override
	public void keyReleased(int key) {
	}

	@Override
	public void update(double time_difference) {
	}

	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.printTextCentred("Welcome!:", window.getWidth() / 2, 100, 5, 100);
		graphics.printTextCentred(player_name, window.getWidth() / 2, 225, 10, 100);
		
		graphics.printTextCentred("Enter IP: ", window.getWidth() / 2, 400, 5, 100);
		graphics.printTextCentred(their_address, window.getWidth() / 2, 525, 5, 100);
		
		if (could_not_connect) {
			graphics.printTextCentred("Could not find opponent", window.getWidth() / 2, 700, 5, 100);
		} 
		if (attempting_to_join) {
			graphics.printTextCentred("Attempting to join...", window.getWidth() /2, 700, 5, 100);
		}
		
		join_button.draw();
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
	}
}
