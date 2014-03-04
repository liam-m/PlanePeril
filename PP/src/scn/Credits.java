package scn;

import java.io.File;

import pp.Main;

import lib.jog.audio;
import lib.jog.audio.Music;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

public class Credits extends Scene {
	/**
	 * Default speed to scroll the credits
	 */
	private final static int SCROLL_SPEED = 64;

	/**
	 * The position to print the credits text at. Initially offscreen
	 */
	private double scroll_position;
	/**
	 * Music to play during the credits
	 */
	private Music music;

	/**
	 * Constructor
	 * 
	 * @param main The main containing the scene
	 */
	public Credits(Main main) {
		super(main);
	}

	/**
	 * Initialise music, set scroll position to offscreen
	 */
	@Override
	public void start() {
		scroll_position = -window.height();
		music = audio.newMusic("sfx" + File.separator + "piano.ogg");
		music.play();
	}

	/**
	 * Update the credits's scroll position
	 * Hurry the credits movement if certain keys pressed
	 */
	@Override
	public void update(double time_difference) {
		int speed = input.isKeyDown(input.KEY_SPACE) || input.isMouseDown(input.MOUSE_LEFT) ? 4 * SCROLL_SPEED : SCROLL_SPEED;
		scroll_position += speed * time_difference;

		if (scroll_position > 1100)
			scroll_position = -window.height();
	}

	/**
	 * print the credits based on the current scroll position
	 */
	@Override
	public void draw() {
		int gap = 64;
		int currentHeight = 0;

		graphics.setColour(Main.GREEN);
		graphics.push();
		graphics.translate(0, scroll_position);
		currentHeight += gap;
		graphics.printCentred(Main.TITLE, 0, currentHeight, 3,
				window.width());
		currentHeight += gap * 2;

		graphics.printCentred("Programming", 0, currentHeight, 2,
				window.width());
		graphics.printCentred("___________", 0, currentHeight + 8, 2,
				window.width());
		graphics.printCentred("__________", 4, currentHeight + 8, 2,
				window.width());

		currentHeight += gap;

		graphics.printCentred("Philip Drew", 0, currentHeight, 2,
				window.width() / 3);

		graphics.printCentred("Miguel Boland", window.width() / 3,
				currentHeight, 2, window.width() / 3);

		graphics.printCentred("Leslie Hor", 2 * window.width() / 3,
				currentHeight, 2, window.width() / 3);

		currentHeight += gap;

		graphics.printCentred("Paulius Imbrasas", 0, currentHeight, 2,
				window.width() / 3);

		graphics.printCentred("Joe Hamm", window.width() / 3, currentHeight, 2,
				window.width() / 3);

		graphics.printCentred("Jaidev Mandhiyan", 2 * window.width() / 3,
				currentHeight, 2, window.width() / 3);

		currentHeight += gap;

		graphics.printCentred("Josh Adams", 0, currentHeight, 2,
				window.width() / 3);

		graphics.printCentred("Huw Taylor", window.width() / 3, currentHeight,
				2, window.width() / 3);

		graphics.printCentred("Stephen Webb", 2 * window.width() / 3,
				currentHeight, 2, window.width() / 3);

		currentHeight += gap * 2;

		graphics.printCentred("Website", 0, currentHeight, 2, window.width());
		graphics.printCentred("_______", 0, currentHeight + 8, 2,
				window.width());
		graphics.printCentred("______", 4, currentHeight + 8, 2, window.width());

		currentHeight += gap;

		graphics.printCentred("Miguel Boland", 0, currentHeight, 2,
				window.width());

		currentHeight += gap;

		graphics.printCentred("Gareth Handley", 0, currentHeight, 2,
				window.width());

		currentHeight += gap * 2;

		graphics.printCentred("Testing", 0, currentHeight, 2, window.width());
		graphics.printCentred("_______", 0, currentHeight + 8, 2,
				window.width());
		graphics.printCentred("______", 4, currentHeight + 8, 2, window.width());

		currentHeight += gap;

		graphics.printCentred("Jaidev Mandhiyan", 0, currentHeight, 2,
				window.width() / 2);

		graphics.printCentred("Joe Hamm", window.width() / 2, currentHeight, 2,
				window.width() / 2);

		currentHeight += gap;

		graphics.printCentred("Leslie Hor", 0,
				currentHeight, 2, window.width() / 2);

		graphics.printCentred("Sanjit Samaddar", window.width() / 2,
				currentHeight, 2,
				window.width() / 2);

		currentHeight += gap;

		graphics.printCentred("Alex Stewart", 0,
				currentHeight, 2, window.width() / 2);

		currentHeight += gap * 2;

		graphics.printCentred("Music", 0, currentHeight, 2, window.width());
		graphics.printCentred("_____", 0, currentHeight + 8, 2, window.width());
		graphics.printCentred("____", 4, currentHeight + 8, 2, window.width());
		currentHeight += gap;
		graphics.printCentred("Gypsy Shoegazer", 0, currentHeight, 2,
				window.width() / 3);
		graphics.printCentred("Beep SFX", 2 * window.width() / 3,
				currentHeight, 2, window.width() / 3);
		currentHeight += gap / 2;
		graphics.printCentred("Kevin MacLeod", 0, currentHeight, 2,
				window.width() / 3);
		graphics.printCentred("Partners in Rhyme", 2 * window.width() / 3,
				currentHeight, 2, window.width() / 3);
		graphics.printCentred("FreeSound", window.width() / 3, currentHeight,
				2, window.width() / 3);
		currentHeight += gap * 2;

		graphics.printCentred("External Libraries", 0, currentHeight, 2,
				window.width());
		graphics.printCentred("__________________", 0, currentHeight + 8, 2,
				window.width());
		graphics.printCentred("_________________", 4, currentHeight + 8, 2,
				window.width());
		currentHeight += gap;
		graphics.printCentred("LWJGL", 0, currentHeight, 2, window.width() / 3);
		graphics.printCentred("Slick2D", window.width() / 3, currentHeight, 2,
				window.width() / 3);
		graphics.printCentred("JOG", 2 * window.width() / 3, currentHeight, 2,
				window.width() / 3);
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
	
	
	@Override
	public void mousePressed(int key, int x, int y) {
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
	}

	@Override
	public void keyPressed(int key) {
	}

	/**
	 * Exit to the title screen if escape is pressed
	 */
	@Override
	public void keyReleased(int key) {
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
	}
}