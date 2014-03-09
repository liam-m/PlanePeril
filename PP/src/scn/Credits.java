package scn;

import java.io.File;

import pp.Main;

import lib.jog.audio;
import lib.jog.audio.Music;
import lib.jog.audio.Sound;
import lib.jog.Graphics;
import lib.jog.Input;
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
		int speed = Input.isKeyDown(Input.KEY_SPACE) || Input.isMouseDown(Input.MOUSE_LEFT) ? 4 * SCROLL_SPEED : SCROLL_SPEED;
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

		Graphics.setColour(Main.GREEN);
		Graphics.push();
		Graphics.translate(0, scroll_position);
		currentHeight += gap;
		Graphics.printTextCentred(Main.TITLE, 0, currentHeight, 3,
				window.width());
		currentHeight += gap * 2;

		Graphics.printTextCentred("Programming", 0, currentHeight, 2,
				window.width());
		Graphics.printTextCentred("___________", 0, currentHeight + 8, 2,
				window.width());
		Graphics.printTextCentred("__________", 4, currentHeight + 8, 2,
				window.width());

		currentHeight += gap;

		Graphics.printTextCentred("Philip Drew", 0, currentHeight, 2,
				window.width() / 3);

		Graphics.printTextCentred("Miguel Boland", window.width() / 3,
				currentHeight, 2, window.width() / 3);

		Graphics.printTextCentred("Leslie Hor", 2 * window.width() / 3,
				currentHeight, 2, window.width() / 3);

		currentHeight += gap;

		Graphics.printTextCentred("Paulius Imbrasas", 0, currentHeight, 2,
				window.width() / 3);

		Graphics.printTextCentred("Joe Hamm", window.width() / 3, currentHeight, 2,
				window.width() / 3);

		Graphics.printTextCentred("Jaidev Mandhiyan", 2 * window.width() / 3,
				currentHeight, 2, window.width() / 3);

		currentHeight += gap;

		Graphics.printTextCentred("Josh Adams", 0, currentHeight, 2,
				window.width() / 3);

		Graphics.printTextCentred("Huw Taylor", window.width() / 3, currentHeight,
				2, window.width() / 3);

		Graphics.printTextCentred("Stephen Webb", 2 * window.width() / 3,
				currentHeight, 2, window.width() / 3);

		currentHeight += gap * 2;

		Graphics.printTextCentred("Website", 0, currentHeight, 2, window.width());
		Graphics.printTextCentred("_______", 0, currentHeight + 8, 2,
				window.width());
		Graphics.printTextCentred("______", 4, currentHeight + 8, 2, window.width());

		currentHeight += gap;

		Graphics.printTextCentred("Miguel Boland", 0, currentHeight, 2,
				window.width());

		currentHeight += gap;

		Graphics.printTextCentred("Gareth Handley", 0, currentHeight, 2,
				window.width());

		currentHeight += gap * 2;

		Graphics.printTextCentred("Testing", 0, currentHeight, 2, window.width());
		Graphics.printTextCentred("_______", 0, currentHeight + 8, 2,
				window.width());
		Graphics.printTextCentred("______", 4, currentHeight + 8, 2, window.width());

		currentHeight += gap;

		Graphics.printTextCentred("Jaidev Mandhiyan", 0, currentHeight, 2,
				window.width() / 2);

		Graphics.printTextCentred("Joe Hamm", window.width() / 2, currentHeight, 2,
				window.width() / 2);

		currentHeight += gap;

		Graphics.printTextCentred("Leslie Hor", 0,
				currentHeight, 2, window.width() / 2);

		Graphics.printTextCentred("Sanjit Samaddar", window.width() / 2,
				currentHeight, 2,
				window.width() / 2);

		currentHeight += gap;

		Graphics.printTextCentred("Alex Stewart", 0,
				currentHeight, 2, window.width() / 2);

		currentHeight += gap * 2;

		Graphics.printTextCentred("Music", 0, currentHeight, 2, window.width());
		Graphics.printTextCentred("_____", 0, currentHeight + 8, 2, window.width());
		Graphics.printTextCentred("____", 4, currentHeight + 8, 2, window.width());
		currentHeight += gap;
		Graphics.printTextCentred("Gypsy Shoegazer", 0, currentHeight, 2,
				window.width() / 3);
		Graphics.printTextCentred("Beep SFX", 2 * window.width() / 3,
				currentHeight, 2, window.width() / 3);
		currentHeight += gap / 2;
		Graphics.printTextCentred("Kevin MacLeod", 0, currentHeight, 2,
				window.width() / 3);
		Graphics.printTextCentred("Partners in Rhyme", 2 * window.width() / 3,
				currentHeight, 2, window.width() / 3);
		Graphics.printTextCentred("FreeSound", window.width() / 3, currentHeight,
				2, window.width() / 3);
		currentHeight += gap * 2;

		Graphics.printTextCentred("External Libraries", 0, currentHeight, 2,
				window.width());
		Graphics.printTextCentred("__________________", 0, currentHeight + 8, 2,
				window.width());
		Graphics.printTextCentred("_________________", 4, currentHeight + 8, 2,
				window.width());
		currentHeight += gap;
		Graphics.printTextCentred("LWJGL", 0, currentHeight, 2, window.width() / 3);
		Graphics.printTextCentred("Slick2D", window.width() / 3, currentHeight, 2,
				window.width() / 3);
		Graphics.printTextCentred("JOG", 2 * window.width() / 3, currentHeight, 2,
				window.width() / 3);
		currentHeight += gap * 2;

		Graphics.pop();
	}

	@Override
	public void close() {
		music.stopMusic();
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
		if (key == Input.KEY_ESCAPE) {
			main.closeScene();
		}
	}
}