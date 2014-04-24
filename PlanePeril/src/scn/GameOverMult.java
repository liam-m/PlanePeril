package scn;

import java.awt.Window;

import pp.Main;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

public class GameOverMult extends Scene {

	boolean win;
	
	public GameOverMult(Main main, boolean win) {
		super(main);
		this.win = win;
	}
	
	@Override
	public void draw() {
		graphics.setColour(main.GREEN);
		if (win) {
			graphics.printTextCentred("You Win!", window.getWidth() / 4, 200, 20, 750);
		} else {
			graphics.printTextCentred("You lose!", window.getWidth() / 4, 200, 20, 750);
		}
		graphics.printTextCentred("press esc to return to the main menu", window.getWidth() /2 , window.getHeight() - 200, 3, 200);

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
		main.closeScene();
	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(double time_difference) {
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
