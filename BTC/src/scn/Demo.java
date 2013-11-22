package scn;

<<<<<<< HEAD
import java.io.File;
=======
import java.util.ArrayList;
import java.util.Random;
>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

import cls.Aircraft;
import cls.Waypoint;

import btc.Main;

public class Demo extends Scene {
	
	private final cls.Vector[] _locationPoints = new cls.Vector[] {
		new cls.Vector(-64, 32, 9000),
		new cls.Vector(window.width() + 32, 32, 9000),
	};
	private final String[] _locationNames = new String[] {
		"Moonchoostoor",
		"Frooncoo",
	};
	public static Waypoint[] _waypoints = new Waypoint[] {
		/*
		new Waypoint(0, 128),
		new Waypoint(0, 256),
		new Waypoint(window.width() - 32, 128),
		new Waypoint(window.width() - 32, 256),
		new Waypoint(window.width() - 32 / 4, 64),
		new Waypoint(window.width() - 32 / 2, 64),
		new Waypoint(3 * window.width() - 32 / 4, 64),
		*/
		new Waypoint(344, 192),
		new Waypoint(256, 256),
		new Waypoint(128, 128),
	};
	
	private lib.OrdersBox _ordersBox;
	private double _timer;
<<<<<<< HEAD
	private Aircraft _selectedAircraft;
	private java.util.ArrayList<Aircraft> _aircraft;
	private graphics.Image _airplaneImage;
	private lib.ButtonText _manualOverrideButton;
	
	public Main main() {
		return _main;
	}
	
	public java.util.ArrayList<Aircraft> aircraftList() {
		return _aircraft;
	}
=======
	private cls.Aircraft _selectedAircraft;
	private ArrayList<cls.Aircraft> demoAircraft;
>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401

	public Demo(Main main) {
		super(main);
	}

	@Override
	public void start() {
		_ordersBox = new lib.OrdersBox(4, window.height() - 120, window.width()-8, 116, 6);
<<<<<<< HEAD
		_aircraft = new java.util.ArrayList<Aircraft>();
		_airplaneImage = graphics.newImage("gfx" + File.separator + "plane.png");
=======
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
		
>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401
		_timer = 0;
	}

	@Override
	public void update(double dt) {
		if (_aircraft.size() > 0) _main.score().addTime(dt); 
		_ordersBox.update(dt);
		_timer += dt;
<<<<<<< HEAD
		for (Aircraft plane : _aircraft) {
			plane.update(dt);
		}
		checkCollisions(dt);
		for (int i = _aircraft.size()-1; i >=0; i --) {
			if (_aircraft.get(i).isFinished()) {
				if (_aircraft.get(i) == _selectedAircraft) {
					_selectedAircraft = null;
				}
				_aircraft.remove(i);
				_main.score().addFlight();
			}
		}
		
		if (_selectedAircraft != null && input.isKeyDown(input.KEY_W)) {
			_selectedAircraft.update(-dt);
		}
		
	}
	
	private void checkCollisions(double dt) {
		for (Aircraft plane : _aircraft) {
			plane.updateCollisions(dt, this);
		}
	}
	
	public void gameOver(Aircraft plane1, Aircraft plane2) {
		_main.closeScene();
		_main.setScene(new GameOver(_main, plane1, plane2));
		_main.score().addGameOver();
=======
		for(int a = 0; a < demoAircraft.size(); a++){
			demoAircraft.get(a).followRoute();
		}
>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		if (key == input.MOUSE_LEFT) {
			for (Aircraft a : _aircraft) {
				if (a.isMouseOver(x-16, y-16)) {
					_selectedAircraft = a;
					System.out.println("Selected Flight " + a.name());
				}
			}
		}
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		
	}

	@Override
	public void keyPressed(int key) {
		
	}

	@Override
	public void keyReleased(int key) {
		switch (key) {
		
			case input.KEY_SPACE :
				generateFlight();
			break;
			
			case input.KEY_ESCAPE :
				_main.closeScene();
			break;
			
		}
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
<<<<<<< HEAD
		graphics.setColour(255, 255, 255);
		for (Aircraft aircraft : _aircraft) {
			aircraft.draw();
		}
		
=======
		
		for(int a = 0; a < demoAircraft.size(); a++){
			demoAircraft.get(a).draw();
		}

>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401
		graphics.setViewport();
		graphics.setColour(0, 128, 0);
		
		if (_selectedAircraft != null) {
			graphics.setColour(0, 0, 0);
			graphics.rectangle(true, (window.width() - 256) / 2, 32, 256, 32);
			graphics.setColour(0, 128, 0);
			graphics.rectangle(false, (window.width() - 256) / 2, 32, 256, 32);
			_manualOverrideButton.draw();
		}
		
		int hours = (int)(_timer / (60 * 60));
		int minutes = (int)(_timer / 60);
		minutes %= 60;
		double seconds = _timer % 60;
		java.text.DecimalFormat df = new java.text.DecimalFormat("00.00");
		String timePlayed = String.format("%d:%02d:", hours, minutes) + df.format(seconds); 
		graphics.print(timePlayed, window.width() - (timePlayed.length() * 8), 0);
		int planes = _aircraft.size();
		graphics.print(String.valueOf(_aircraft.size()) + " plane" + (planes == 1 ? "" : "s") + " in the sky.", 256, 0);
		graphics.print("Score: " + _main.score().calculate(), 0, 0);
	}
	
	private void generateFlight() {
<<<<<<< HEAD
		// Origin and Destination
		int o = (int)( Math.random() * _locationPoints.length);
		int d = 0;
		while (d == o) d = (int)( Math.random() * _locationPoints.length);
		String originName = _locationNames[o];
		String destinationName = _locationNames[d];
		cls.Vector origin = _locationPoints[o];
		cls.Vector destination = _locationPoints[d];
		// Name
		String name = "";
		boolean nameTaken = true;
		while (nameTaken) {
			name = "Flight " + (int)(900 * Math.random() + 100);
			nameTaken = false;
			for (Aircraft a : _aircraft) {
				if (a.name() == name) nameTaken = true;
			}
		}
		// Add to world
		Aircraft a = new Aircraft(name, originName, destinationName, origin, destination, _airplaneImage, 64);
		_aircraft.add(a);
		_ordersBox.addOrder("<<< " + name + " incoming from " + originName + " heading towards " + destinationName + ".");
=======
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
>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401
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