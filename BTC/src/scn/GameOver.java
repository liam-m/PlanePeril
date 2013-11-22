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
		_deaths = (int)( Math.random() * 200) + 100;
		_injured = (int)( Math.random() * 500) + 200;;
		_textBox = new lib.OrdersBox(64, 96, window.width() - 128, window.height() - 96, 28);
		_textBox.addOrder(String.valueOf(_deaths) + " people died in the crash.");
		_textBox.addOrder("                                 ");
		_textBox.addOrder(String.valueOf(_injured) + " were severely injured.");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("British Bearways is facing heavy legal pressure from the");
		_textBox.addOrder("family and loved-ones of the dead.");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("You are going to be fired and blacklisted within the ATCO ");
		_textBox.addOrder("community. You have no other skills and will have to resort");
		_textBox.addOrder("to a badly-paid job with menial repetative responsibilities.");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("Your guilt for the deaths you caused will gnaw at you and you");
		_textBox.addOrder("will develop a drinking problem to attempt to cope.");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("                                 ");
		_textBox.addOrder("   ");
		_textBox.addOrder("   ");
		_textBox.addOrder("   ");
		_textBox.addOrder("   ");
		_textBox.addOrder("   ");
		_textBox.addOrder("                           Game Over.");
		_textBox.addOrder("   ");
		_textBox.addOrder("   ");
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
		if (_textBox.isUpToDate()) _main.closeScene();
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