package scn;

import lib.jog.input;
import lib.jog.window;
import btc.Main;

public class Demo extends Scene {
	
	private lib.OrdersBox _ordersBox;

	public Demo(Main main) {
		super(main);
	}

	@Override
	public void start() {
		_ordersBox = new lib.OrdersBox(0, window.height() - 128, window.width(), 128, 6);
	}

	@Override
	public void update(double dt) {
		_ordersBox.update(dt);
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
		if (key == input.KEY_UP) {
			_ordersBox.addOrder(">> Flight 215, prepare for course correction.");
			_ordersBox.addOrder(">> Alter heading by 21 degrees starboard.");
		}
		if (key == input.KEY_DOWN) {
			_ordersBox.addOrder("<< Roger that control. Correcting course now. Flight 215 Out.");
		}
		if (key == input.KEY_LEFT) {
			_ordersBox.addOrder(">> Flight 215, increase altitude by 1000£");
		}
		if (key == input.KEY_RIGHT) {
			_ordersBox.addOrder(">> Flight 215, decrease speed to 20$");
		}
	}

	@Override
	public void draw() {
		_ordersBox.draw();
	}

	@Override
	public void close() {
		
	}

}