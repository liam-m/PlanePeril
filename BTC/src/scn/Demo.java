package scn;

import java.util.ArrayList;
import java.util.Random;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

import cls.Aircraft;
import cls.Waypoint;

import btc.Main;

public class Demo extends Scene {
	
	private lib.OrdersBox _ordersBox;
	private Waypoint[] _waypoints;
	private double _timer;
	private cls.Aircraft _selectedAircraft;
	private ArrayList<cls.Aircraft> demoAircraft;

	public Demo(Main main) {
		super(main);
	}

	@Override
	public void start() {
		_ordersBox = new lib.OrdersBox(4, window.height() - 120, window.width()-8, 116, 6);
		int viewportWidth = window.width() - 32;
		
		_waypoints = new Waypoint[10];
		_waypoints[0] = new Waypoint(0, 128, 0);
		_waypoints[1] = new Waypoint(0, 256, 0);
		_waypoints[2] = new Waypoint(viewportWidth, 128, 0);
		_waypoints[3] = new Waypoint(viewportWidth, 256, 0);
		_waypoints[4] = new Waypoint(viewportWidth / 4, 0, 0);
		_waypoints[5] = new Waypoint(viewportWidth / 2, 0, 0);
		_waypoints[6] = new Waypoint(3 * viewportWidth / 4, 0, 0);
		_waypoints[7] = new Waypoint(344, 192, 0);
		_waypoints[8] = new Waypoint(256, 256, 0);
		_waypoints[9] = new Waypoint(128, 128, 0);
		
		demoAircraft = new ArrayList<cls.Aircraft>();
		
		Aircraft testAircraft = new Aircraft("Flight 404", _waypoints[9], _waypoints[0], 0, 0, 0);
		demoAircraft.add(testAircraft);
		
		_timer = 0;
	}

	@Override
	public void update(double dt) {
		_ordersBox.update(dt);
		_timer += dt;
		for(int a = 0; a < demoAircraft.size(); a++){
			demoAircraft.get(a).followRoute();
		}
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
		
		for(int a = 0; a < demoAircraft.size(); a++){
			demoAircraft.get(a).draw();
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
		System.out.println("Generating new Flight");
		int X, Y, Z;
		X = 0; 
		Y = 0; 
		Z = 0;
		// pick random entry waypoint
		// Do _waypoints.length - 1 as randInt is inclusive!
		Waypoint origin = _waypoints[randInt(0, _waypoints.length - 1)];
		//pick a random destination
		Waypoint destination = _waypoints[randInt(0, _waypoints.length - 1)];
		//NOTE - no prevention of origin equalling destination currently.
		
		//Pick a random edge of the screen to enter from
		int edge = randInt(0, 3);
		switch (edge){
		case 0: // Top
			X = window.width() / 2;
			Y = 0;
			Z = 0;
			break;
		case 1: // Right
			X = window.width();
			Y = window.height()/2;
			Z = 0;
			break;
		case 2: // Bottom
			X = window.width() / 2;
			Y = window.height() - 144;
			Z = 0;
			break;
		case 3: // Left
			X = 0;
			Y = window.height() / 2;
			Z = 0;
			break;
		}
		
		// create plane, position it offscreen heading towards said waypoint
		Aircraft aircraft = new Aircraft(("Flight " + Integer.toString(randInt(1, 999))), destination, origin, X, Y, Z);
		demoAircraft.add(aircraft);
		// warn player of incoming plane
	}

	public static int randInt(int min, int max){
		/* Generate and return a random integer number between min and max (inclusive).
		 * Used for generating random nos. for generateFlight. */
		Random rand = new Random();
	    int randomNum = rand.nextInt((max - min) + 1) + min;
		return randomNum;
	}
	
	@Override
	public void close() {
		
	}

}