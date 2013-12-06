package scn;

import cls.Aircraft;
import lib.jog.graphics;
import lib.jog.window;
import btc.Main;

public class GameOver extends Scene {
	
	private lib.TextBox _textBox;
	private Aircraft _crashedPlane1;
	private Aircraft _crashedPlane2;
	private int _deaths;
	private int _injured;

	public GameOver(Main main, Aircraft plane1, Aircraft plane2) {
		super(main);
		_crashedPlane1 = plane1;
		_crashedPlane2 = plane2;
	}

	@Override
	public void start() {
		_deaths = (int)( Math.random() * 500) + 300;
		_injured = (int)( Math.random() * 90) + 10;
		_textBox = new lib.TextBox(64, 96, window.width() - 128, window.height() - 96, 32);
		_textBox.addText(String.valueOf(_deaths) + " people died in the crash.");
		_textBox.delay(0.4);
		_textBox.addText(String.valueOf(_injured) + " managed to escape the burning wreckage.");
		_textBox.delay(0.8);
		_textBox.addText("Those " + String.valueOf(_injured) + " were rushed to hospital with third degree burns, broken and dislocated limbs, and severe bruising.");
		_textBox.delay(0.4);
		_textBox.addText("They later died in hospital.");
		_textBox.delay(0.8);
		_textBox.addText("British Bearways is facing heavy legal pressure from the family and loved-ones of the dead and an investigation into the incident will be performed.");
		_textBox.delay(0.8);
		_textBox.addText("The inquery into your incompetance will lead to humanity discovering your true bear nature.");
		_textBox.delay(0.8);
		_textBox.addText("Your guilt for the deaths you caused, and your failure to pass as a human, will gnaw at you and you will have to revert to your drinking problem to attempt to cope.");
		_textBox.addText(" ");
		_textBox.addText(" ");
		_textBox.addText(" ");
		_textBox.addText(" ");
		_textBox.delay(0.8);
		String centredGameOver = "";
		for (int i = 0; i < (window.width() - 80) / 2; i ++) centredGameOver += " ";
		centredGameOver += "Game Over.";
		_textBox.addText(centredGameOver);
		_textBox.addText(" ");
		_textBox.addText(" ");
		_textBox.addText(" ");
		_textBox.addText(" ");
		_textBox.delay(0.2);
		String centredPrompt = "";
		for (int i = 0; i < (window.width() - 200) / 2; i ++) centredGameOver += " ";
		centredPrompt += "Press any key to continue";
		_textBox.addText(centredPrompt);
	}

	@Override
	public void update(double dt) {
		_textBox.update(dt);
	}

	@Override
	public void mousePressed(int key, int x, int y) {}

	@Override
	public void mouseReleased(int key, int x, int y) {}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {
		_main.closeScene();
	}

	@Override
	public void draw() {
		graphics.setColour(0, 128, 0);
		graphics.printCentred(_crashedPlane1.name() + " crashed into " + _crashedPlane2.name() + ".", 0, 32, 2, window.width());
		_textBox.draw();
	}

	@Override
	public void close() {
		
	}

}