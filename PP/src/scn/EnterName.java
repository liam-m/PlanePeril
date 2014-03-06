package scn;

import pp.Main;
import lib.jog.audio.Sound;

public class EnterName extends Scene {
	String name = "";

	public EnterName(Main main) {
		super(main);
	}

	@Override
	public void mousePressed(int key, int x, int y) {
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
	}

	@Override
	public void keyPressed(int key) {
		main.setScene(new Join(main, name)); // For now, pressing any key takes you to the Join scene
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
	public void draw() {
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
	}
}
