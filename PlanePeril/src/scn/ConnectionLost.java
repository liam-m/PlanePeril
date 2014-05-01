package scn;

import pp.Main;
import lib.ButtonText;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import lib.jog.audio.Sound;

public class ConnectionLost extends Scene {
	ButtonText back_to_menu_button;
	
	public ConnectionLost(Main main) {
		super(main);
	}

	@Override
	public void mousePressed(int key, int mouse_x, int mouse_y) {
	}

	@Override
	public void mouseReleased(int key, int mouse_x, int mouse_y) {
		if (key == input.MOUSE_LEFT && back_to_menu_button.isMouseOver(mouse_x, mouse_y)) {
			main.closeScene();
		}
	}

	@Override
	public void keyPressed(int key) {
	}

	@Override
	public void keyReleased(int key) {
	}

	@Override
	public void start() {
		ButtonText.Action back_to_menu = new ButtonText.Action() {
			@Override
			public void action() {
				main.closeScene();
			}
		};
		
		back_to_menu_button = new ButtonText("Join", back_to_menu, window.getWidth()/2, 2*window.getHeight()/3, 200, 100, true, true);
	}

	@Override
	public void update(double time_difference) {
	}

	@Override
	public void draw() {
		graphics.printTextCentred("Connection lost :(", window.getWidth()/2, window.getHeight()/3, 5, 200);
		back_to_menu_button.draw();
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
	}
}