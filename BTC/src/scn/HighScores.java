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
	 * initialises the random number of deaths, timer, and text box with strings
	 * to be written about the game failure
	 */
	@Override
	public void start() {

		ArrayList<Integer> scores = new ArrayList<Integer>();

		if (Serializer.deserialize(Main.SCORES_FILE)) {
			scores = (ArrayList<Integer>) Serializer.getRecovered();
		}

		textBox = new lib.TextBox(64, 96, window.width() - 128,
				window.height() - 96, 32);
		textBox.addText("HIGH SCORES");
		
		Integer[] scoresArray = scores.toArray(new Integer[scores.size()]);
		Arrays.sort(scoresArray, Collections.reverseOrder());

		int i = 1;
		for (Integer score : scoresArray) {
			textBox.delay(0.2);
			textBox.addText(i + ": " + score);
			i++;
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
			main.closeScene();
		}
	}

	@Override
	/**
	 * draws game over
	 * If explosion has finished, draw the textbox
	 * Otherwise, draw the planes and explosion
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
