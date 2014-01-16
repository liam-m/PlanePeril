package scn;

import java.io.File;

import cls.Aircraft;
import cls.Vector;
import lib.SpriteAnimation;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.graphics.Image;
import lib.jog.window;
import btc.Main;

public class GameOver extends Scene {
	
	private lib.TextBox textBox;
	private Aircraft crashedPlane1;
	private Aircraft crashedPlane2;
	private int deaths;
	private Vector crash;
	private SpriteAnimation explosionAnim;
	private Image explosion;
	private int keyPressed;
	private double timer;

	public GameOver(Main main, Aircraft plane1, Aircraft plane2) {
		super(main);
		crashedPlane1 = plane1;
		crashedPlane2 = plane2;
		crash = new Vector(plane1.position().x(), plane1.position().y(), 0);
//		playSound(audio.newSoundEffect("sfx" + File.separator + "crash.ogg"));
		explosion = graphics.newImage("gfx" + File.separator + "explosionFrames.png");
		Vector midPoint = crashedPlane1.position().add(crashedPlane2.position()).scaleBy(0.5);
		Vector explosionPos = midPoint.sub( new Vector(explosion.width()/72, explosion.height()/3, 0) );
		explosionAnim = new SpriteAnimation(explosion, (int) explosionPos.x(), (int) explosionPos.y(), 6, 18);
	}

	@Override
	public void start() {
		deaths = (int)( Math.random() * 500) + 300;
		timer = 0;
		textBox = new lib.TextBox(64, 96, window.width() - 128, window.height() - 96, 32);
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
	public void update(double dt) {
		if (explosionAnim.hasFinished()){
			timer += dt;
			textBox.update(dt);
		} else {
			explosionAnim.update(dt);
		}
	}

	@Override
	public void mousePressed(int key, int x, int y) {}

	@Override
	public void mouseReleased(int key, int x, int y) {}

	@Override
	public void keyPressed(int key) {
		keyPressed = key;
	}

	@Override
	public void keyReleased(int key) {
		if (key == keyPressed) {
			main.closeScene();
			main.closeScene();
		}
	}

	@Override
	public void draw() {
		graphics.setColour(0, 128, 0);
		graphics.printCentred(crashedPlane1.name() + 
				" crashed into " + crashedPlane2.name() + ".", 0, 32, 2, window.width());
		if (explosionAnim.hasFinished()) {
			textBox.draw();
		} else {
			crashedPlane1.draw((int) crashedPlane1.position().z());
			crashedPlane2.draw((int) crashedPlane1.position().z());
			Vector midPoint = crash.add(crashedPlane2.position()).scaleBy(0.5);
			double radius = 20;
			graphics.setColour(128,0,0);
			graphics.circle(false, midPoint.x(), midPoint.y(), radius);
			explosionAnim.draw();
		}
		int opacity = (int)(255 * Math.sin(timer));
		graphics.setColour(0, 128, 0, opacity);
		graphics.printCentred("Press any key to continue", 0, window.height() - 256, 1, window.width());
	}

	@Override
	public void close() {}

	@Override
	public void playSound(Sound sound) {
		sound.stop();
		sound.play();
	}

}