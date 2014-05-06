package scn;

import java.io.File;

import pp.Main;

import lib.SpriteAnimation;
import lib.TextBox;
import lib.jog.audio;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.graphics.Image;
import lib.jog.input;
import lib.jog.window;
import cls.Aircraft;
import cls.Vector;

public class GameOver extends Scene {
	private TextBox text_box; // Text box to write the details of the game failure
	private final SpriteAnimation explosion_animation; // A sprite animation to handle the frame by frame drawing of the explosion
	private final Image explosion = graphics.newImage("gfx" + File.separator + "explosionFrames.png"); // The explosion image to use for the animation
	private double timer = 0; // Timer to allow for explosion and plane to be shown for a period, followed by the text box.
	
	// The two crashed aircraft, passed to the scene by the scene in which they
	// crashed Used to position the explosion, and provide graphical feedback of
	// how and where the player failed
	private final Aircraft crashed_plane_1;
	private final Aircraft crashed_plane_2;
	private final Vector crash_position; // The position of the crash - the vector midpoint of the positions of the two crashed planes
	private int num_deaths = (int)(Math.random() * 500) + 300; // A random number of deaths caused by the crash
	private int score; // To store value of the score passed to this class in the constructor by the previous screen.

	/**
	 * Constructor for the Game Over scene
	 * 
	 * @param main The main containing the scene
	 * @param plane_1 One of the planes involved in the crash
	 * @param plane_2 The second plane involved in the crash
	 */
	public GameOver(Main main, Aircraft plane_1, Aircraft plane_2, int score) {
		super(main);
		this.score = score;
		crashed_plane_1 = plane_1;
		crashed_plane_2 = plane_2;
		crash_position = new Vector(plane_1.getPosition().x(), plane_1.getPosition().y(), 0);

		int framesAcross = 8;
		int framesDown = 4;

		Vector midPoint = crashed_plane_1.getPosition().add(crashed_plane_2.getPosition()).scaleBy(0.5);
		Vector explosionPos = midPoint.sub(new Vector(explosion.getWidth() / (framesAcross * 2), explosion.getHeight() / (framesDown * 2), 0));

		explosion_animation = new SpriteAnimation(explosion, (int) explosionPos.x(), (int) explosionPos.y(), 6, 16, framesAcross, framesDown, false);
	}

	/**
	 * Initialises the random number of deaths, timer, and text box with strings
	 * to be written about the game failure
	 */
	@Override
	public void start() {
		playSound(audio.newSoundEffect("sfx" + File.separator + "crash.ogg"));

		text_box = new lib.TextBox(64, 96, window.getWidth() - 128,
				window.getHeight() - 96, 32);
		text_box.addText(String.valueOf(num_deaths) + " people died in the crash.");
		text_box.delay(0.4);
		text_box.addText("British Bearways is facing heavy legal pressure from the family and loved-ones of the dead and an investigation into the incident will be performed.");
		text_box.newline();
		text_box.delay(0.4);
		text_box.addText("The inquery into your incompetance will lead to humanity discovering your true bear nature.");
		text_box.newline();
		text_box.delay(0.4);
		text_box.addText("Your guilt for the deaths you caused, and your failure to pass as a human, will gnaw at you and you will have to revert to your drinking problem to attempt to cope.");
		text_box.newlines(2);
		text_box.delay(0.4);
		text_box.addText("With no income, there is no way your family can survive the fast-approaching winter months.");
		text_box.newlines(2);
		text_box.delay(0.4);
		text_box.newline();
		text_box.addText("Game Over.");
		text_box.delay(0.5);
		text_box.addText("You Lose.");
	}

	@Override
	/**
	 * If before explosion has finished, update the explosion
	 * otherwise, update text box instead
	 */
	public void update(double time_difference) {
		if (explosion_animation.hasFinished()) {
			timer += time_difference;
			text_box.update(time_difference);
		} else {
			explosion_animation.update(time_difference);
		}
	}

	@Override
	public void mousePressed(int key, int x, int y) {
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
	}

	/**
	 * Ends the scene if space, return or escape key is released.
	 */
	@Override
	public void keyPressed(int key) {
		if (key == input.KEY_SPACE || key == input.KEY_RETURN || key == input.KEY_ESCAPE) {
			main.closeScene();
		}
	}

	@Override
	public void keyReleased(int key) {
	}

	@Override
	/**
	 * Draws game over
	 * If explosion has finished, draw the textbox
	 * Otherwise, draw the planes and explosion
	 */
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.printTextCentred(crashed_plane_1.getName() + " crashed into "
				+ crashed_plane_2.getName() + ".", 0, 32, 2, window.getWidth());

		graphics.setColour(Main.GREEN);
		graphics.printTextCentred("You managed to earn " + this.score
				+ " rubles before making your fatal error.", 0, 64, 2, window.getWidth());

		if (explosion_animation.hasFinished()) {
			text_box.draw();
		} else {
			crashed_plane_1.draw();
			crashed_plane_2.draw();
			Vector midPoint = crash_position.add(crashed_plane_2.getPosition()).scaleBy(0.5);
			double radius = 20;
			graphics.setColour(128, 0, 0);
			graphics.circle(false, midPoint.x(), midPoint.y(), radius);
			explosion_animation.draw();
		}

		int opacity = (int) (255 * Math.sin(timer));

		graphics.setColour(0, 128, 0, opacity);
		graphics.printTextCentred("Press space to continue", 0,	window.getHeight() - 256, 1, window.getWidth());
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
		sound.stopSound();
		sound.playSound();
	}
}