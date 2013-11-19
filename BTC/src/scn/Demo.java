package scn;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

import cls.Waypoint;

import btc.Main;

public class Demo extends Scene {
	
	private lib.OrdersBox _ordersBox;
	private Waypoint[] _waypoints;
	private double _timer;
	private cls.Aircraft _selectedAircraft;

	public Demo(Main main) {
		super(main);
	}

	@Override
	public void start() {
		_ordersBox = new lib.OrdersBox(4, window.height() - 120, window.width()-8, 116, 6);
		int viewportWidth = window.width() - 32;
		_waypoints = new Waypoint[10];
		_waypoints[0] = new Waypoint(0, 128);
		_waypoints[1] = new Waypoint(0, 256);
		_waypoints[2] = new Waypoint(viewportWidth, 128);
		_waypoints[3] = new Waypoint(viewportWidth, 256);
		_waypoints[4] = new Waypoint(viewportWidth / 4, 0);
		_waypoints[5] = new Waypoint(viewportWidth / 2, 0);
		_waypoints[6] = new Waypoint(3 * viewportWidth / 4, 0);
		_waypoints[7] = new Waypoint(344, 192);
		_waypoints[8] = new Waypoint(256, 256);
		_waypoints[9] = new Waypoint(128, 128);
		_timer = 0;
	}

	@Override
	public void update(double dt) {
		_ordersBox.update(dt);
		_timer += dt;
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
		if (key == input.KEY_SPACE) generateFlight();
	}

	@Override
	public void draw() {
		graphics.setColour(0, 128, 0);
		graphics.rectangle(false, 16, 16, window.width() - 32, window.height() - 144);
		_ordersBox.draw();
		graphics.setViewport(16, 16, window.width() - 32, window.height() - 144);
		for (Waypoint waypoint : _waypoints) {
			waypoint.draw();
		}
		graphics.setViewport();
		graphics.setColour(0, 128, 0);
		
		if (_selectedAircraft != null) {
			graphics.setColour(0, 0, 0);
			graphics.rectangle(true, (window.width() - 256) / 2, 32, 256, 32);
			graphics.setColour(0, 128, 0);
			graphics.rectangle(false, (window.width() - 256) / 2, 32, 256, 32);
		}
		
		int hours = (int)(_timer / (60 * 60));
		int minutes = (int)(_timer / 60);
		minutes %= 60;
		double seconds = _timer % 60;
		java.text.DecimalFormat df = new java.text.DecimalFormat("00.00");
		String timePlayed = String.format("%d:%02d:", hours, minutes) + df.format(seconds); 
		graphics.print(timePlayed, 0, 0);
	}
	
	private void generateFlight() {
		// pick random entry waypoint
		// create plane, position it offscreen heading towards said waypoint
		// pick exit point and between points if necessary
		// warn play of incoming plane
	}

	@Override
	public void close() {
		
	}

}