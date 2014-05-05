package scn;

import pp.Main;
import lib.ButtonText;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

public class GameOverMult extends Scene {
	boolean win;
	ButtonText play_again, exit;
	
	
	// 	public ButtonText(String text, Action action, int x_coordinate, int y_coordinate, int width, int height, int x_offset, int y_offset, boolean border, boolean centred) {

	public GameOverMult(final Main main, boolean win) {
		super(main);
		this.win = win;
		
		ButtonText.Action replay = new ButtonText.Action() {
			@Override
			public void action() {
				main.closeScene();
				main.setScene(new EnterName(main));
			}
		};
		play_again = new ButtonText("play again", replay, window.getWidth()/2 -100, window.getHeight()/2, 100, 30, true, true);
		
		ButtonText.Action exit_action = new ButtonText.Action() {
			@Override
			public void action() {
				main.closeScene();
			}
		};
		exit = new ButtonText("exit", exit_action, window.getWidth()/2 +100, window.getHeight()/2, 100, 30, true, true);
		
	}
	
	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		String text = win ? "You Win!" : "You Lose!";
		graphics.printTextCentred(text, window.getWidth()/4, 200, 20, 750);
		graphics.printTextCentred("Press a button to play again or exit", window.getWidth()/2 , window.getHeight() - 200, 3, 200);
		play_again.draw();
		exit.draw();

	}
	@Override
	public void mousePressed(int key, int mouse_x, int mouse_y) {
		if (key == input.MOUSE_LEFT) {
			if (play_again.isMouseOver(mouse_x, mouse_y)) {
				play_again.act();
			} else if (exit.isMouseOver(mouse_x, mouse_y)){
				exit.act();
			}
		}

	}

	@Override
	public void mouseReleased(int key, int mouse_x, int mouse_y) {
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