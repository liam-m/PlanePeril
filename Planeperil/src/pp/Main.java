package pp;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

import lib.Serializer;
import lib.jog.audio;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.input.EventHandler;
import lib.jog.window;

import org.lwjgl.Sys;
import org.newdawn.slick.Color;

import scn.Scene;
import scn.Title;

/**
 * <h1>Main</h1>
 * <p>
 * Main class that is run when file is run. 
 * Handles the scenes (gamestates)
 * </p>
 * 
 * @author Huw Taylor
 */
public class Main implements EventHandler {
	public static final int VIEWPORT_OFFSET_X = 16;
	public static final int VIEWPORT_OFFSET_Y = 40;
	
	public final static Color GREEN = new Color(0, 200, 0);
	public final static Color GREY = new Color(128, 128, 128);
	public final static Color RED = new Color(128, 0, 0);
	public final static Color BLUE = new Color(0, 0, 64);
	public final static Color LIGHT_BLUE = new Color(0, 64, 255);
	public final static Color ORANGE = new Color(64, 0, 0);
	
	public final static String SCORES_FILE = "scores.ser";

	public final static String TITLE = "Plane Peril";
	final private int WIDTH = 1280;
	final private int HEIGHT = 720;
	final private String[] ICON_FILENAMES = { 
			"gfx" + File.separator + "icon16.png",
			"gfx" + File.separator + "icon32.png",
			"gfx" + File.separator + "icon64.png"
	};

	private double last_frame_time;
	private Stack<Scene> scene_stack;
	private Scene current_scene;
	private int fps_counter;
	private long last_fps_time;

	/**
	 * Constructor for Main. Initialises the jog library classes, and then
	 * begins the game loop, calculating time between frames, and then when the
	 * window is closed it releases resources and closes the program
	 */
	public Main() {
		// Resize width and height based on display dimensions
		Rectangle window_bounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		int display_width = window_bounds.width;
		int display_height = window_bounds.height;

		float width_scale = display_width/(float)WIDTH;
		float height_scale = display_height/(float)HEIGHT;
		
		float scale = (float)(Math.min(width_scale, height_scale) * 0.99);	
		start((int)(WIDTH*scale), (int)(HEIGHT*scale));
		
		while (!window.isClosed()) {
			double time_difference = getTimeSinceLastFrame();
			update(time_difference);
			draw();
		}
		quit();
	}

	/**
	 * Creates window, initialises jog classes and sets starting values to
	 * variables.
	 */
	private void start(int width, int height) {
		window.initialiseWindow(TITLE, width, height);
		window.setIcon(ICON_FILENAMES);

		graphics.initialise();

		graphics.Font font = graphics.newBitmapFont("gfx" + File.separator + "font.png",
						"ABCDEFGHIJKLMNOPQRSTUVWXYZ abcdefghijklmnopqrstuvwxyz1234567890.,_-!?()[]><#~:;/\\^'\"{}£$@@@@@@@@");
		graphics.setFont(font);

		scene_stack = new Stack<Scene>();
		setScene(new Title(this));

		last_frame_time = (double) (Sys.getTime()) / Sys.getTimerResolution();
		last_fps_time = ((Sys.getTime() * 1000) / Sys.getTimerResolution()); // Set to current time

		if (!Serializer.deserialize(Main.SCORES_FILE)) {
			Serializer.serialize(Main.SCORES_FILE, new ArrayList<Integer>());
		}
	}

	/**
	 * Updates audio, input handling, the window, the current scene and FPS.
	 * 
	 * @param dt
	 *            the time elapsed since the last frame.
	 */
	private void update(double dt) {
		audio.update();
		input.update(this);
		window.update();
		current_scene.update(dt);
		updateFPS();
	}

	/**
	 * Calculates the time since the last frame in seconds as a
	 * double-precision floating point number.
	 * 
	 * @return the time in seconds since the last frame.
	 */
	private double getTimeSinceLastFrame() {
		double current_time = (double) (Sys.getTime()) / Sys.getTimerResolution();
		double delta = (current_time - last_frame_time);
		last_frame_time = current_time; // Update last frame time
		return delta;
	}

	/**
	 * Clears the graphical viewport and calls the draw function of the current
	 * scene.
	 */
	private void draw() {
		graphics.clear();
		current_scene.draw();
	}

	/**
	 * Closes the current scene, closes the window, releases the audio resources and quits the process
	 * window.
	 */
	public void quit() {
		current_scene.close();
		window.dispose();
		audio.dispose();
		System.exit(0);
	}

	/**
	 * Closes the current scene, adds new scene to scene stack and starts it
	 * 
	 * @param new_scene The scene to set as current scene
	 */
	public void setScene(Scene new_scene) {
		if (current_scene != null)
			current_scene.close();

		current_scene = scene_stack.push(new_scene); // Add new_scene to scene_stack and assign to current_scene
		current_scene.start();
	}

	/**
	 * Closes the current scene, pops it from the stack and sets the current scene to stop of stack.
	 */
	public void closeScene() {
		current_scene.close();
		scene_stack.pop();
		current_scene = scene_stack.peek();
	}

	/**
	 * Updates the fps - increments the fps counter
	 * If it has been over a second since the fps was updated, update it
	 */
	public void updateFPS() {
		long current_time = ((Sys.getTime() * 1000) / Sys.getTimerResolution());

		if (current_time - last_fps_time > 1000) { // Update once a second
			window.setTitle(TITLE + " - FPS: " + fps_counter);
			fps_counter = 0; // Reset the FPS counter
			last_fps_time += current_time - last_fps_time; // Add on time difference
		}
		fps_counter++;
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		current_scene.mousePressed(key, x, y);
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		current_scene.mouseReleased(key, x, y);
	}

	@Override
	public void keyPressed(int key) {
		current_scene.keyPressed(key);
	}

	@Override
	public void keyReleased(int key) {
		current_scene.keyReleased(key);
	}
	

	/**
	 * Creates a new instance of Main, starting a new game.
	 * 
	 * @param args Any command-line arguments.
	 */
	public static void main(String[] args) {
		new Main();
	}
}