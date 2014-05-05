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
		scroll_position = -window.getHeight();
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
			scroll_position = -window.getHeight();
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
		graphics.printTextCentred(Main.TITLE, 0, currentHeight, 3,
				window.getWidth());
		currentHeight += gap * 2;

		graphics.printTextCentred("Game contributors", 0, currentHeight, 2,
				window.getWidth());
		graphics.printTextCentred("_________________", 0, currentHeight + 8, 2,
				window.getWidth());
		graphics.printTextCentred("_________________", 4, currentHeight + 8, 2,
				window.getWidth());

		currentHeight += gap;

		String[] people = {"Philip Drew", "Miguel Boland", "Leslie Hor", "Paulius Imbrasas", "Joe Hamm", "Jaidev Mandhiyan",
				"Josh Adams", "Huw Taylor", "Stephen Webb", "Gareth Handley", "Sanjit Samaddar", "Alex Stewart", "Liam Mullane",
				"Liam Wellacott", "Jakub Brezonak", "Adam Al-jidy", "Matt Munro", "Jack Chapman"};
		
		int index = 0;
		while (index < people.length) {
			
		graphics.printTextCentred(people[index], 0, currentHeight, 2,
				window.getWidth() / 3);
		index++;
		graphics.printTextCentred(people[index], window.getWidth() / 3,
				currentHeight, 2, window.getWidth() / 3);
		index++;
		graphics.printTextCentred(people[index], 2 * window.getWidth() / 3,
				currentHeight, 2, window.getWidth() / 3);
		index++;
		
		currentHeight += gap;
		}
		
		graphics.printTextCentred("Music", 0, currentHeight, 2, window.getWidth());
		graphics.printTextCentred("_____", 0, currentHeight + 8, 2, window.getWidth());
		graphics.printTextCentred("____", 4, currentHeight + 8, 2, window.getWidth());
		currentHeight += gap;
		graphics.printTextCentred("Gypsy Shoegazer", 0, currentHeight, 2,
				window.getWidth() / 3);
		graphics.printTextCentred("Beep SFX", 2 * window.getWidth() / 3,
				currentHeight, 2, window.getWidth() / 3);
		currentHeight += gap / 2;
		graphics.printTextCentred("Kevin MacLeod", 0, currentHeight, 2,
				window.getWidth() / 3);
		graphics.printTextCentred("Partners in Rhyme", 2 * window.getWidth() / 3,
				currentHeight, 2, window.getWidth() / 3);
		graphics.printTextCentred("FreeSound", window.getWidth() / 3, currentHeight,
				2, window.getWidth() / 3);
		currentHeight += gap * 2;

		graphics.printTextCentred("External Libraries", 0, currentHeight, 2,
				window.getWidth());
		graphics.printTextCentred("__________________", 0, currentHeight + 8, 2,
				window.getWidth());
		graphics.printTextCentred("_________________", 4, currentHeight + 8, 2,
				window.getWidth());
		currentHeight += gap;
		graphics.printTextCentred("LWJGL", 0, currentHeight, 2, window.getWidth() / 3);
		graphics.printTextCentred("Slick2D", window.getWidth() / 3, currentHeight, 2,
				window.getWidth() / 3);
		graphics.printTextCentred("JOG", 2 * window.getWidth() / 3, currentHeight, 2,
				window.getWidth() / 3);
		currentHeight += gap * 2;

		graphics.pop();
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
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
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