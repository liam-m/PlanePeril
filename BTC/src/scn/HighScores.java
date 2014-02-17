package scn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import lib.Serializer;
import lib.TextBox;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.window;
import btc.Main;

public class HighScores extends Scene {

	/**
	 * Text box to write the details of the game failure
	 */
	private TextBox textBox;

	private int keyPressed;

	public HighScores(Main main) {
		super(main);
	}

	/**
	 * loads the high scores screen including deserializing the scores file and
	 * displaying it on the screen sorted.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void start() {

		ArrayList<Integer> scores = new ArrayList<Integer>();

		// if file exists and is deserializeable
		if (Serializer.deserialize(Main.SCORES_FILE)) {
			scores = (ArrayList<Integer>) Serializer.getRecovered();
		}

		textBox = new lib.TextBox(64, 96, window.width() - 128,
				window.height() - 96, 32);
		textBox.addText("HIGH SCORES");

		// convert arraylist into regular array to make sorting simpler
		Integer[] scoresArray = scores.toArray(new Integer[scores.size()]);
		Arrays.sort(scoresArray, Collections.reverseOrder());

		// Display the 10 heighest scores.
		for (int i = 1; i < 11; i++) {
			textBox.delay(0.2);
			textBox.addText(i + ": " + scoresArray[i]);
		}

	}

	@Override
	/**
	 * If before explosion has finished, update the explosion
	 * otherwise, update text box instead
	 */
	public void update(double dt) {
		textBox.update(dt);
	}

	@Override
	public void mousePressed(int key, int x, int y) {
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
	}

	@Override
	/**
	 * Tracks if any keys are pressed when the game over screen begins
	 * Prevents the scene instantly ending due to a key press from previous scene
	 */
	public void keyPressed(int key) {
		keyPressed = key;
	}

	/**
	 * Ends the scene if any key is released , ie. press any key to continue
	 */
	@Override
	public void keyReleased(int key) {
		if (key == keyPressed) {
			main.closeScene();
		}
	}

	@Override
	/**
	 */
	public void draw() {
		graphics.setColour(Main.GREEN);

		textBox.draw();

		graphics.setColour(0, 128, 0, 255);
		graphics.printCentred("Press any key to continue", 0,
				window.height() - 256, 1, window.width());
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {

	}

}
