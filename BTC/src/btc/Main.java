package btc;

import org.lwjgl.Sys;

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

	private double _lastFrame;
	private double _dt;
	private scn.Scene _scene;
	
	public Main() {
		start();
		while(!window.isClosed()) {
			_dt = getDeltaTime();
			update(_dt);
			draw();
		}
		quit();
	}
	
	private void start() {
		window.initialise(TITLE, WIDTH, HEIGHT);
		graphics.initialise();
		audio.initialise();
		_lastFrame = 0;
		// _scene = new scn.Title(this);
		_scene.start();
	}
	
	private void update(double dt) {
		input.update(this);
		window.update();
		_scene.update(dt);
	}
	
	private double getDeltaTime() {
		double time = (double)(Sys.getTime()) / Sys.getTimerResolution();
	    double delta = (time - _lastFrame);
	    _lastFrame = time;
	    return delta;
	}
	
	private void draw() {
		_scene.draw();
	}
	
	private void quit() {
		_scene.close();
		window.dispose();
		audio.dispose();
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		_scene.mousePressed(key, x, y);
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		_scene.mouseReleased(key, x, y);
	}

	@Override
	public void keyPressed(int key) {
		_scene.keyPressed(key);
	}

	@Override
	public void keyReleased(int key) {
		_scene.keyReleased(key);
	}

}