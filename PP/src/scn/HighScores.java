package scn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import pp.Main;

import lib.Serializer;
import lib.TextBox;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.window;

public class HighScores extends Scene {
	private static final int MAX_NUM_SCORES_TO_DISPLAY = 10;
	private TextBox text_box; // Text box to write the details of the game failure

	public HighScores(Main main) {
		super(main);
	}

	/**
	 * Loads the high scores screen including deserializing the scores file and
	 * displaying it on the screen sorted.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void start() {
		ArrayList<Integer> scores = new ArrayList<Integer>();

		if (Serializer.deserialize(Main.SCORES_FILE)) { // If file exists and is deserializeable
			scores = (ArrayList<Integer>) Serializer.getRecovered();
		}

		text_box = new lib.TextBox(64, 96, window.getWidth() - 128, window.getHeight() - 96, 32);
		text_box.addText("HIGH SCORES");

		// Convert ArrayList into regular array to make sorting simpler
		Integer[] scoresArray = scores.toArray(new Integer[scores.size()]);
		Arrays.sort(scoresArray, Collections.reverseOrder());
		
		int num_scores_to_display = scores.size() < MAX_NUM_SCORES_TO_DISPLAY ? scores.size()-1 : MAX_NUM_SCORES_TO_DISPLAY; 
		for (int i = 1; i <= num_scores_to_display; i++) {
			text_box.delay(0.2);
			text_box.addText(i + ": " + scoresArray[i]);
		}
	}

	/**
	 * If before explosion has finished, update the explosion
	 * otherwise, update text box instead
	 */
	@Override
	public void update(double time_difference) {
		text_box.update(time_difference);
	}

	@Override
	public void mousePressed(int key, int x, int y) {
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
	}

	/**
	 * Tracks if any keys are pressed when the game over screen begins
	 * Prevents the scene instantly ending due to a key press from previous scene
	 */
	@Override
	public void keyPressed(int key) {
	}

	/**
	 * Ends the scene if any key is released , ie. press any key to continue
	 */
	@Override
	public void keyReleased(int key) {
		main.closeScene();
	}

	@Override
	/**
	 */
	public void draw() {
		graphics.setColour(Main.GREEN);

		text_box.draw();

		graphics.setColour(0, 128, 0, 255);
		graphics.printTextCentred("Press any key to continue", 0, window.getHeight() - 256, 1, window.getWidth());
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {

	}
}