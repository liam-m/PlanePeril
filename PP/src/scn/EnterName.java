package scn;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

import pp.Main;
import lib.ButtonText;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import lib.jog.audio.Sound;

public class EnterName extends Scene {
	String name = "";
	ButtonText[] buttons;
	
	int BUTTON_SEPARATION = 100;
	int BUTTON_WIDTH = 100;
	int BUTTON_HEIGHT = 25;
	int BUTTON_X = window.getWidth()/2 - BUTTON_WIDTH - BUTTON_SEPARATION/2;
	int BUTTON_Y = 500;

	public EnterName(Main main) {
		super(main);
	}
	
	private void validateName() {
		while (name.charAt(name.length()-1) == ' ')
			name = name.substring(0, name.length()-1);
		if (name.toLowerCase().equals("jack chapman")) {
			String[] firsts = new String[]{"Platt", "Flap", "Durk", "Gurt", "Wack", "Chap"};
			String[] lasts = new String[]{"Dap", "Chuck", "Nap", "Chup", "Bap", "Jack"};
			name = firsts[new Random().nextInt(firsts.length)]+" "+lasts[new Random().nextInt(lasts.length)]+"man";
		}
	}

	@Override
	public void mousePressed(int key, int x, int y) {
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		for (ButtonText button : buttons) {
			if (button.isMouseOver(x, y) && name.length() > 0) {
				validateName();
				button.act();
			}
		}
	}

	@Override
	public void keyPressed(int key) {
		if (key == input.KEY_BACKSPACE) {
			if (name.length() > 0)
				name = name.substring(0, name.length()-1);
		} else if ((key >= 16 && key <= 25) || (key >= 30 && key <= 38) || (key >= 44 && key <= 50)) { // a-z
			char c = Keyboard.getEventCharacter();
			name += c;
		} else if (key == Input.KEY_SPACE && name.length() > 0) { // Can't start with space
			name += ' ';
		} else if (key == Input.KEY_ESCAPE) {
			main.closeScene();
		}
	}

	@Override
	public void keyReleased(int key) {
	}

	@Override
	public void start() {
		buttons = new ButtonText[2];

		ButtonText.Action host = new ButtonText.Action() {
			@Override
			public void action() {
				if (name.length() > 0) {
					main.closeScene();
					main.setScene(new Host(main, name));
				}
			}
		};
		
		ButtonText.Action join = new ButtonText.Action() {
			@Override
			public void action() {
				if (name.length() > 0) {
					main.closeScene();
					main.setScene(new Join(main, name));
				}
			}
		};

		buttons[0] = new ButtonText("Host", host, BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		buttons[1] = new ButtonText("Join", join, BUTTON_X + BUTTON_WIDTH + BUTTON_SEPARATION, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
	}

	@Override
	public void update(double time_difference) {
	}

	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.printTextCentred("Enter your name:", window.getWidth() / 2, 200, 5, 100);
		graphics.printTextCentred(name, window.getWidth() / 2, 300, 5, 100);
		graphics.rectangle(false, BUTTON_X, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		graphics.rectangle(false, BUTTON_X+BUTTON_WIDTH+BUTTON_SEPARATION, BUTTON_Y, BUTTON_WIDTH, BUTTON_HEIGHT);
		
		for (ButtonText button : buttons) {
			button.draw();
		}
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
	}
}
