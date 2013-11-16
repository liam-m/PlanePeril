package btc;

import lib.jog.audio;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

public class Main implements input.EventHandler {

	public static void main(String[] args) {
		new Main();
	}
	
	final private String TITLE = "Bear Traffic Controller";
	final private int WIDTH = 640;
	final private int HEIGHT = 480;

	private double dt;
	
	public Main() {
		start();
		while(!window.isClosed()) {
			update(dt);
			draw();
		}
		quit();
	}
	
	private void start() {
		window.initialise(TITLE, WIDTH, HEIGHT);
		graphics.initialise();
		audio.initialise();
	}
	
	private void update(double dt) {
		input.update(this);
		window.update();
	}
	
	private void draw() {
		
	}
	
	private void quit() {
		window.dispose();
		audio.dispose();
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

}
