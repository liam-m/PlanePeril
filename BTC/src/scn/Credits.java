package scn;

import java.io.File;

import lib.jog.audio;
import lib.jog.audio.Music;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import btc.Main;

public class Credits extends Scene {
	
	/**
	 * default speed to scroll the credits
	 */
	private final static int SCROLL_SPEED = 64;
	
	private float speed;
	/**
	 * The position to print the credits text at. Initially offscreen
	 */
	private double scrollPosition;
	/**
	 * Music to play during the credits
	 */
	private Music music;

	/**
	 * Constructor
	 * @param main the main containing the scene
	 */
	public Credits(Main main) {
		super(main);
	}
	
	/**
	 * INPUT HANDLERS
	 */
	@Override
	public void mousePressed(int key, int x, int y) {}

	@Override
	public void mouseReleased(int key, int x, int y) {}

	@Override
	public void keyPressed(int key) {}

	@Override
	/**
	 * exit to the title screen if escape is pressed
	 */
	public void keyReleased(int key) {
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
	}
	
	/**
	 * Init musis, and the credits text to be offscreen
	 */
	@Override
	public void start() {
		speed = 1f;
		scrollPosition = -window.height();
		music = audio.newMusic("sfx" + File.separator + "piano.ogg");
		music.play();
	}

	@Override
	/**
	 * update the credits's scroll position
	 * hurry the credits movement if certain keys pressed
	 */
	public void update(double dt) {
		boolean hurried = input.isKeyDown(input.KEY_SPACE) || input.isMouseDown(input.MOUSE_LEFT);

		speed = hurried ? 4f : 1f;

		scrollPosition += SCROLL_SPEED * dt * speed;

		if (scrollPosition > 1100)
			scrollPosition = -window.height();
	}

	@Override
	/**
	 * print the credits based on the current scroll position
	 */
	public void draw() {
		int gap = 64;
		int currentHeight = 0;

		graphics.setColour(0, 128, 0);
		graphics.push();
		graphics.translate(0, scrollPosition);
		currentHeight += gap;
		graphics.printCentred("Bear Traffic Controller", 0, currentHeight, 3, window.width());
		currentHeight += gap * 2;
		
		graphics.printCentred("Programming", 0, currentHeight, 2, window.width());
		graphics.printCentred("___________", 0, currentHeight + 8, 2, window.width());
		graphics.printCentred("__________", 4, currentHeight + 8, 2, window.width());
		currentHeight += gap;
		graphics.printCentred("Josh Adams", 0, currentHeight, 2, window.width()/3);
		graphics.printCentred("Huw Taylor", window.width()/3, currentHeight, 2, window.width()/3);
		graphics.printCentred("Stephen Webb", 2*window.width()/3, currentHeight, 2, window.width()/3);
		currentHeight += gap * 2;
		
		graphics.printCentred("Website", 0, currentHeight, 2, window.width());
		graphics.printCentred("_______", 0, currentHeight + 8, 2, window.width());
		graphics.printCentred("______", 4, currentHeight + 8, 2, window.width());
		currentHeight += gap;
		graphics.printCentred("Gareth Handley", 0, currentHeight, 2, window.width());
		currentHeight += gap * 2;
		
		graphics.printCentred("Testing", 0, currentHeight, 2, window.width());
		graphics.printCentred("_______", 0, currentHeight + 8, 2, window.width());
		graphics.printCentred("______", 4, currentHeight + 8, 2, window.width());
		currentHeight += gap;
		graphics.printCentred("Sanjit Samaddar", 0, currentHeight, 2, window.width()/2);
		graphics.printCentred("Alex Stewart", window.width()/2, currentHeight, 2, window.width()/2);
		currentHeight += gap * 2;

		graphics.printCentred("Music", 0, currentHeight, 2, window.width());
		graphics.printCentred("_____", 0, currentHeight + 8, 2, window.width());
		graphics.printCentred("____", 4, currentHeight + 8, 2, window.width());
		currentHeight += gap;
		graphics.printCentred("Gypsy Shoegazer", 0, currentHeight, 2, window.width()/3);
		graphics.printCentred("Beep SFX", 2*window.width()/3, currentHeight, 2, window.width()/3);
		currentHeight += gap / 2;
		graphics.printCentred("Kevin MacLeod", 0, currentHeight, 2, window.width()/3);
		graphics.printCentred("Partners in Rhyme", 2*window.width()/3, currentHeight, 2, window.width()/3);
		graphics.printCentred("FreeSound", window.width()/3, currentHeight, 2, window.width()/3);
		currentHeight += gap * 2;
		
		graphics.printCentred("External Libraries", 0, currentHeight, 2, window.width());
		graphics.printCentred("__________________", 0, currentHeight + 8, 2, window.width());
		graphics.printCentred("_________________", 4, currentHeight + 8, 2, window.width());
		currentHeight += gap;
		graphics.printCentred("LWJGL", 0, currentHeight, 2, window.width()/3);
		graphics.printCentred("Slick2D", window.width()/3, currentHeight, 2, window.width()/3);
		graphics.printCentred("JOG", 2*window.width()/3, currentHeight, 2, window.width()/3);
		currentHeight += gap * 2;
		
		graphics.pop();
	}

	@Override
	public void close() {
		music.stop();
	}

	@Override
	public void playSound(Sound sound) {
		
	}

}
