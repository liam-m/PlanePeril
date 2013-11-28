package scn;

import cls.Aircraft;
import lib.jog.graphics;
import lib.jog.window;
import btc.Main;

public class GameOver extends Scene {
	
	private lib.OrdersBox _textBox;
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
		_textBox = new lib.OrdersBox(64, 96, window.width() - 128, window.height() - 96, 32);
		_textBox.addOrder(String.valueOf(_deaths) + " people died in the crash.");
		_textBox.addOrder("                                 ");
		_textBox.addOrder(String.valueOf(_injured) + " managed to escape the burning wreckage.                                          ");
		_textBox.addOrder("Those " + String.valueOf(_injured) + " were rushed to hospital with third degree burns, broken and dislocated limbs, and severe bruising.");
		_textBox.addOrder("They later died in hospital.");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("British Bearways is facing heavy legal pressure from the family and loved-ones of the dead.");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("The investigation into your incompetance will lead to humanity discovering your true bear nature.");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("Your guilt for the deaths you caused, and your failure to pass as a human, will gnaw at you and you will revert to your drinking problem to attempt to cope.");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("   ");
		_textBox.addOrder("                           Game Over.");
		_textBox.addOrder("           ");
		_textBox.addOrder("   ");
		_textBox.addOrder("   ");
		_textBox.addOrder("   ");
		_textBox.addOrder("                    Press any key to continue");
	}

	@Override
	public void update(double dt) {
		_textBox.update(dt);
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