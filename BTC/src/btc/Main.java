package btc;

import java.io.File;

import org.lwjgl.Sys;

import lib.jog.audio;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

/**
 * <h1>Main</h1>
 * <p>Main class that is run when file is run. Main handles the scenes (gamestates).</p>
 * @author Huw Taylor
 */
public class Main implements input.EventHandler {

	/**
	 * Creates a new instance of Main, starting a new game.
	 * @param args any command-line arguments.
	 */
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
	
	/**
	 * Constructor for Main. Initialises the jog library classes, and then
	 * begins the game loop, calculating time between frames, and then when
	 * the window is closed it releases resources and closes the programme
	 * successfully.
	 */
	public Main() {
		start();
		while(!window.isClosed()) {
			dt = getDeltaTime();
			update(dt);
			draw();
		}
		quit();
	}
	
	/**
	 * Creates window, initialises jog classes and sets starting values to variables.
	 */
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
	
	/**
	 * Updates input handling, the window and the current scene.
	 * @param dt the time elapsed since the last frame.
	 */
	private void update(double dt) {
		input.update(this);
		window.update();
		currentScene.update(dt);
	}
	
	/**
	 * Calculates the time taken since the last tick in seconds as a double-precision floating point number.
	 * @return the time in seconds since the last frame.
	 */
	private double getDeltaTime() {
		double time = (double)(Sys.getTime()) / Sys.getTimerResolution();
	    double delta = (time - lastFrameTime);
	    lastFrameTime = time;
	    return delta;
	}
	
	/**
	 * Clears the graphical viewport and calls the draw function of the current scene.
	 */
	private void draw() {
		graphics.clear();
		currentScene.draw();
	}
	
	/**
	 * Closes the current scene, releases the audio resources and closes the window.
	 */
	public void quit() {
		currentScene.close();
		window.dispose();
		audio.dispose();
		System.exit(0);
	}
	
	/**
	 * 
	 * @param newScene
	 */
	public void setScene(scn.Scene newScene) {
		if (currentScene != null) currentScene.close();
		sceneStack.push(newScene);
		currentScene = sceneStack.peek();
		currentScene.start();
	}
	
	/**
	 * Closes the current scene and pops it from the stack.
	 */
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