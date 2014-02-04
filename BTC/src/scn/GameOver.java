package scn;

import java.io.File;

import lib.SpriteAnimation;
import lib.TextBox;
import lib.jog.audio;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.graphics.Image;
import lib.jog.window;
import btc.Main;
import cls.Aircraft;
import cls.Vector;

public class GameOver extends Scene {
	/**
	 * Text box to write the details of the game failure
	 */
	private TextBox textBox;

	/**
	 * The two crashed aircraft, passed to the scene by the scene in which they
	 * crashed Used to position the explosion, and provide graphical feedback of
	 * how and where the player failed
	 */
	private final Aircraft crashedPlane1;
	private final Aircraft crashedPlane2;
	/**
	 * A randon number of deaths caused by the crash
	 */
	private int deaths;

	/**
	 * The position of the crash - the vector midpoint of the positions of the
	 * two crashed planes
	 */
	private final Vector crash;
	/**
	 * A sprite animation to handle the frame by frame drawing of the explosion
	 */
	private final SpriteAnimation explosionAnim;
	/**
	 * The explosion image to use for the animation
	 */
	private final Image explosion;

	private int keyPressed;

	/**
	 * Timer to allow for explosion and plane to be shown for a period, followed
	 * by the text box.
	 */
	private double timer;

	/**
	 * Constructor for the Game Over scene
	 * 
	 * @param main
	 *            the main containing the scene
	 * @param plane1
	 *            one of the planes involved in the crash
	 * @param plane2
	 *            the second plane involved in the crash
	 */
	public GameOver(Main main, Aircraft plane1, Aircraft plane2) {
		super(main);

		crashedPlane1 = plane1;
		crashedPlane2 = plane2;
		crash = new Vector(plane1.position().x(), plane1.position().y(), 0);

		int framesAcross = 8;
		int framesDown = 4;

		explosion = graphics.newImage("gfx" + File.separator
				+ "explosionFrames.png");
		Vector midPoint = crashedPlane1.position()
				.add(crashedPlane2.position()).scaleBy(0.5);
		Vector explosionPos = midPoint
				.sub(new Vector(explosion.width() / (framesAcross * 2),
						explosion.height() / (framesDown * 2), 0));

		explosionAnim = new SpriteAnimation(explosion, (int) explosionPos.x(),
				(int) explosionPos.y(), 6, 16, framesAcross, framesDown, false);
	}

	/**
	 * initialises the random number of deaths, timer, and text box with strings
	 * to be written about the game failure
	 */
	@Override
	public void start() {
		playSound(audio.newSoundEffect("sfx" + File.separator + "crash.ogg"));

		deaths = (int) (Math.random() * 500) + 300;
		timer = 0;

		textBox = new lib.TextBox(64, 96, window.width() - 128,
				window.height() - 96, 32);
		textBox.addText(String.valueOf(deaths) + " people died in the crash.");
		textBox.delay(0.4);
		textBox.addText("British Bearways is facing heavy legal pressure from the family and loved-ones of the dead and an investigation into the incident will be performed.");
		textBox.newline();
		textBox.delay(0.8);
		textBox.addText("The inquery into your incompetance will lead to humanity discovering your true bear nature.");
		textBox.newline();
		textBox.delay(0.8);
		textBox.addText("Your guilt for the deaths you caused, and your failure to pass as a human, will gnaw at you and you will have to revert to your drinking problem to attempt to cope.");
		textBox.newline();
		textBox.newline();
		textBox.delay(0.8);
		textBox.addText("With no income, there is no way your family can survive the fast-approaching winter months.");
		textBox.newline();
		textBox.newline();
		textBox.delay(0.8);
		textBox.addText("Game Over.");
	}

	@Override
	/**
	 * If before explosion has finished, update the explosion
	 * otherwise, update text box instead
	 */
	public void update(double dt) {
		if (explosionAnim.hasFinished()) {
			timer += dt;
			textBox.update(dt);
		} else {
			explosionAnim.update(dt);
		}
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
		graphics.printCentred(crashedPlane1.name() + " crashed into "
				+ crashedPlane2.name() + ".", 0, 32, 2, window.width());

		if (explosionAnim.hasFinished()) {

			textBox.draw();

		} else {

			crashedPlane1.draw((int) crashedPlane1.position().z());
			crashedPlane2.draw((int) crashedPlane1.position().z());
			Vector midPoint = crash.add(crashedPlane2.position()).scaleBy(0.5);
			double radius = 20;
			graphics.setColour(128, 0, 0);
			graphics.circle(false, midPoint.x(), midPoint.y(), radius);
			explosionAnim.draw();

		}

		int opacity = (int) (255 * Math.sin(timer));

		graphics.setColour(0, 128, 0, opacity);
		graphics.printCentred("Press any key to continue", 0,
				window.height() - 256, 1, window.width());
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
		sound.stop();
		sound.play();
	}

}