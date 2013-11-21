package scn;

import cls.Aircraft;
import lib.jog.graphics;
import lib.jog.window;
import btc.Main;

public class GameOver extends Scene {
	
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
	}

	@Override
	public void update(double dt) {
		
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
		graphics.printCentred(String.valueOf(_deaths) + " people died in the crash.", 0, 128, 1, window.width());
		graphics.printCentred(String.valueOf(_injured) + " were severly injured.", 0, 160, 1, window.width());
	}

	@Override
	public void close() {
		
	}

}