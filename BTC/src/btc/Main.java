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
	final private int WIDTH = 1280;
	final private int HEIGHT = 960;

	private double lastFrameTime;
	private double dt;
	private java.util.Stack<scn.Scene> sceneStack;
	private scn.Scene currentScene;
	private cls.Score _score;
	
	public Main() {
		start();
		while(!window.isClosed()) {
			dt = getDeltaTime();
			update(dt);
			draw();
		}
		quit();
	}
	
	private void start() {
		lastFrameTime = (double)(Sys.getTime()) / Sys.getTimerResolution();
		window.initialise(TITLE, WIDTH, HEIGHT);
		graphics.initialise();
		graphics.Font font = graphics.newBitmapFont("gfx" + File.separator + "font.png", "ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz1234567890.,_-!?()[]><#~:;/\\^'\"{}£$@@@@@@@@");
		graphics.setFont(font);
		sceneStack = new java.util.Stack<scn.Scene>();
		setScene(new scn.Title(this));
		_score = new cls.Score();
	}
	
	private void update(double dt) {
		input.update(this);
		window.update();
		currentScene.update(dt);
	}
	
	private double getDeltaTime() {
		double time = (double)(Sys.getTime()) / Sys.getTimerResolution();
	    double delta = (time - lastFrameTime);
	    lastFrameTime = time;
	    return delta;
	}
	
	private void draw() {
		graphics.clear();
		currentScene.draw();
	}
	
	public void quit() {
		currentScene.close();
		window.dispose();
		audio.dispose();
		System.exit(0);
	}
	
	public void setScene(scn.Scene newScene) {
		if (currentScene != null) currentScene.close();
		sceneStack.push(newScene);
		currentScene = sceneStack.peek();
		currentScene.start();
	}
	
	public void closeScene() {
		currentScene.close();
		sceneStack.pop();
		currentScene = sceneStack.peek(); 
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		currentScene.mousePressed(key, x, y);
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		currentScene.mouseReleased(key, x, y);
	}

	@Override
	public void keyPressed(int key) {
		currentScene.keyPressed(key);
	}

	@Override
	public void keyReleased(int key) {
		currentScene.keyReleased(key);
	}

	public cls.Score score() {
		return _score;
	}
	
}