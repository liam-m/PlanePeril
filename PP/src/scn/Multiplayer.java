package scn;

import pp.Main;
import lib.jog.audio.Sound;
import lib.jog.graphics;

public class Multiplayer extends Scene {
	String player_name, their_name, their_address;
	
	public Multiplayer(Main main, String player_name, String their_address, String their_name) {
		super(main);
		this.player_name = player_name;
		this.their_name = their_name;
		this.their_address = their_address;
	}

	@Override
	public void mousePressed(int key, int x, int y) {
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
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
	public void draw() {
		graphics.printCentred(player_name, 100, 100, 5, 100);
		graphics.printCentred(their_name, 100, 200, 5, 100);
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
	}
}