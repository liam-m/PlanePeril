package scn;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;


import pp.Main;
import rem.HostServer;
import lib.ButtonText;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import lib.jog.audio.Sound;

public class Host extends Scene {
	String this_address = null;
	HostServer host_server;
	
	String player_name;
	String dots = "";
	private int waiting_dot = 0;
	
	private int difficulty = 0; // Default easy
	
	private ButtonText[] buttons;
	
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
	}
	
	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.printTextCentred("Welcome!:", window.getWidth() / 2, 100, 5, 100);
		graphics.printTextCentred(player_name, window.getWidth() / 2, 200, 10, 100);
		graphics.printTextCentred("Your IP: "+this_address, (window.getWidth()/2 - (200/4)), 350, 5, 200);
		
		String[] diffs = new String[]{"Easy", "Medium", "Hard"};
		graphics.printTextCentred(diffs[difficulty], window.getWidth() / 2, 500, 4, 100);
		
		for (ButtonText button : buttons) {
			button.draw();
		}

		graphics.setColour(Main.GREEN); // Hovered buttons may change colour
		graphics.printTextCentred("Waiting for player", window.getWidth() / 2, 800, 5, 100);

		if (waiting_dot++ > 25) {
			waiting_dot = 0;
			if(dots.length() == 10) {
				dots = "";
			} else {
				dots += '.'; 
			}
		}
		graphics.printTextCentred(dots, window.getWidth() / 2, 850, 5, 100);
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		if (key == input.MOUSE_LEFT) {
			for (ButtonText button : buttons) {
				if (button.isMouseOver(x, y)) {
					button.act();
				}
			}			
		}
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
	
	private void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
		this.host_server.setDifficulty(difficulty); // Notify host server of change so that other player can get difficulty
	}

	@Override
	public void start() {
		buttons = new ButtonText[3];

		ButtonText.Action easy = new ButtonText.Action() {
			@Override
			public void action() {
				setDifficulty(0);
			}
		};

		buttons[0] = new ButtonText("Easy", easy, window.getWidth() / 4, 2 * window.getHeight() / 3, 128, 16, true, true);

		ButtonText.Action medium = new ButtonText.Action() {
			@Override
			public void action() {
				setDifficulty(1);
			}
		};

		buttons[1] = new lib.ButtonText("Medium", medium, window.getWidth() / 2, 2 * window.getHeight() / 3, 128, 16, true, true);

		ButtonText.Action hard = new ButtonText.Action() {
			@Override
			public void action() {
				setDifficulty(2);
			}
		};
		
		buttons[2] = new lib.ButtonText("Hard", hard, 3 * window.getWidth() / 4, 2 * window.getHeight() / 3, 128, 16, true, true);
	}

	@Override
	public void update(double time_difference) {
		if (host_server.hasConnected()) {
			String their_name = host_server.getTheirName();
			String their_address = host_server.getTheirAddress();
			main.closeScene();
			main.setScene(new Multiplayer(main, player_name, their_name, their_address, true, difficulty));
		}
	}
	
	@Override
	public void close() {
		host_server.close();
	}

	@Override
	public void playSound(Sound sound) {
	}
}