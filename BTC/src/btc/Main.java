package btc;

import java.io.File;

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
	private java.util.Stack<scn.Scene> _sceneStack;
	private scn.Scene _scene;
	private cls.Score _score;
	
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
		_lastFrame = (double)(Sys.getTime()) / Sys.getTimerResolution();
		window.initialise(TITLE, WIDTH, HEIGHT);
		graphics.initialise();
		graphics.Font font = graphics.newBitmapFont("gfx" + File.separator + "font.png", "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz1234567890.,_-!?()[]><#~:;/\\^'\"{}£$@@@@@@@@");
		graphics.setFont(font);
		_sceneStack = new java.util.Stack<scn.Scene>();
		setScene(new scn.Title(this));
		_score = new cls.Score();
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
		graphics.clear();
		_scene.draw();
	}
	
	public void quit() {
		_scene.close();
		window.dispose();
		audio.dispose();
		System.exit(0);
	}
	
	public void setScene(scn.Scene newScene) {
		if (_scene != null) _scene.close();
		_sceneStack.push(newScene);
		_scene = _sceneStack.peek();
		_scene.start();
	}
	
	public void closeScene() {
		_scene.close();
		_sceneStack.pop();
		_scene = _sceneStack.peek(); 
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

	public cls.Score score() {
		return _score;
	}
	
}