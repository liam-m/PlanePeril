package scn;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.lwjgl.input.Keyboard;

import lib.ButtonText;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import lib.jog.audio.Sound;
import pp.Main;

public class Join extends Scene {
	
	String this_address = null;
	private String their_address = "";
	
	private final int JOIN_X_POSITION = window.width() /2;
	private final int JOIN_Y_POSITION = 400;
	private final int JOIN_WIDTH = 100;
	private final int JOIN_HEIGHT = 50;
	
	protected Join(Main main, String player_name) {
		super(main);
		try {
			this_address = (InetAddress.getLocalHost().getHostAddress()).toString();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void start() {
		ButtonText.Action join = new ButtonText.Action() {
			@Override
			public void action() {
				//TODO send IP
			}
		};
		
	}
	@Override
	public void mousePressed(int key, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(int key) {
		if (key == input.KEY_ESCAPE) 
			main.closeScene();
		if (their_address.length() > 13) {
			if (key == input.KEY_BACKSPACE)
				their_address = their_address.substring(0, their_address.length()-1);
		} else {
			switch(key) {
				case input.KEY_0:
				case 82:
					their_address += '0';
					break;
				case input.KEY_1:
				case 79:
					their_address += '1';
					break;
				case input.KEY_2:
				case 80:
					their_address += '2';
					break;
				case input.KEY_3:
				case 81:
					their_address += '3';
					break;
				case input.KEY_4:
				case 75:
					their_address += '4';
					break;
				case input.KEY_5:
				case 76:
					their_address += '5';
					break;
				case input.KEY_6:
				case 77:
					their_address += '6';
					break;
				case input.KEY_7:
				case 71:
					their_address += '7';
					break;
				case input.KEY_8:
				case 72:
					their_address += '8';
					break;
				case input.KEY_9:
				case 73:
					their_address += '9';
					break;
				case Keyboard.KEY_DECIMAL:
				case 52:
					their_address += '.';
					break;
				case input.KEY_BACKSPACE:
					if (their_address.length() > 0)
						their_address = their_address.substring(0, their_address.length()-1);
					break;
			}			
		}
	}

	@Override
	public void keyReleased(int key) {
		// TODO Auto-generated method stub
		
	}

	

	@Override
	public void update(double time_difference) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
			
		graphics.printCentred("Enter IP: ", window.width() / 2, 200, 5, 100);
		graphics.printCentred(their_address, window.width() / 2, 300, 5, 100);
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
		// TODO Auto-generated method stub
		
	}

}
