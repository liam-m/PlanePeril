package scn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;

import org.lwjgl.input.Keyboard;


import lib.ButtonText;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import lib.jog.audio.Sound;
import pp.Main;
import rem.HostServer;
import rem.JoinClient;

public class Join extends Scene {
	
	String this_address = null;
	private String their_address = "";
	
	String player_name;
	String their_name = "";
	
	ButtonText join_button;
	
	JoinClient join_client;
	
	boolean could_not_connect = false;
	
	private final int JOIN_X_POSITION = window.getWidth() /2;
	private final int JOIN_Y_POSITION = 800;
	private final int JOIN_WIDTH = 100;
	private final int JOIN_HEIGHT = 25;
	
	public Join(Main main, String player_name) {
		super(main);
		this.player_name = player_name;
		try {
			this_address = (InetAddress.getLocalHost().getHostAddress()).toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		
		join_client = new JoinClient();
	}
	
	@Override
	public void start() {
		ButtonText.Action join = new ButtonText.Action() {
			@Override
			public void action() {
				could_not_connect = false;
				try {
					their_name = join_client.connect(their_address, this_address, player_name);
					main.setScene(new MultiplayerRight(main, player_name, their_name, their_address));
				} catch (RemoteException e) {
					could_not_connect = true;
				}
			}
		};

		join_button = new ButtonText("Join", join, JOIN_X_POSITION, JOIN_Y_POSITION, JOIN_WIDTH, JOIN_HEIGHT, true);

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
		graphics.printTextCentred(player_name, window.getWidth() / 2, 300, 10, 100);
		
		graphics.printTextCentred("Enter IP: ", window.getWidth() / 2, 500, 5, 100);
		graphics.printTextCentred(their_address, window.getWidth() / 2, 600, 5, 100);
		
		if (could_not_connect) {
			graphics.printTextCentred("Could not find opponent", window.getWidth() / 2, 700, 5, 100);
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
