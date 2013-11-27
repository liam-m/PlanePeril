package scn;

import java.io.File;
import java.util.Random;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

import cls.Aircraft;
import cls.Waypoint;

import btc.Main;

public class Demo extends Scene {
	
	private final cls.Vector[] _locationPoints = new cls.Vector[] {
		new cls.Vector(-64, 32, 9000),
		new cls.Vector(-64, 64, 9000),
		new cls.Vector(window.width() + 32, 32, 9000),
		new cls.Vector(window.width() + 32, 64, 9000),
	};
	
	private final String[] _locationNames = new String[] {
		"North West Top Leftonia",
		"Bottom Left",
		"Top Right",
		"Bottom Right",
	};
	
	public static Waypoint[] _locationWayPoints = new Waypoint[] {
		/* A set of Waypoints which are origin / destination points */
		new Waypoint(-64, 32, true), //top left
		new Waypoint(-64, window.height(), true), //bottom left
		new Waypoint(window.width() + 32, 32, true), // top right
		new Waypoint(window.width() + 32, window.height(), true), //bottom right
	};

	public static Waypoint[] _waypoints = new Waypoint[] {		
		/* All waypoints in the airspace, including location Way Points*/
	
		//airspace waypoints
		new Waypoint(125, 70, false),//0
		new Waypoint(700, 100, false),//1
		new Waypoint(1050, 80, false),//2
		new Waypoint(670, 300, false),//3
		new Waypoint(1050, 400, false),//4
		new Waypoint(250, 400, false),//5
		new Waypoint(200, 635, false),//6
		new Waypoint(500, 655, false),//7
		new Waypoint(800, 750, false),//8
		new Waypoint(1200, 750, false),//9
		//destination/origin waypoints - present in this list for pathfinding.
		_locationWayPoints[0], //10
		_locationWayPoints[1], //11
		_locationWayPoints[2],//12
		_locationWayPoints[3],//13
	};
	
	private final int PLANE_INFO_X = 16;
	private final int PLANE_INFO_Y = window.height() - 120;
	private final int PLANE_INFO_W = window.width()/4;
	private final int PLANE_INFO_H = 112;
	
	private final int ALTIMETER_X = PLANE_INFO_X + PLANE_INFO_W + 8;
	private final int ALTIMETER_Y = window.height() - 120;
	private final int ALTIMETER_W = 192;
	private final int ALTIMETER_H = 112;
	
	private final int ORDERSBOX_X = ALTIMETER_X + ALTIMETER_W + 8;
	private final int ORDERSBOX_Y = window.height() - 120;
	private final int ORDERSBOX_W = window.width() - (ORDERSBOX_X + 16);
	private final int ORDERSBOX_H = 112;
	
	private lib.OrdersBox _ordersBox;
	private double _timer;
	private Aircraft _selectedAircraft;
	private Waypoint _selectedWaypoint;
	private int _selectedPathpoint;
	private java.util.ArrayList<Aircraft> _aircraft;
	private graphics.Image _airplaneImage;
	private lib.ButtonText _manualOverrideButton;
	private lib.Altimeter _altimeter;
	private static double flightGenerationInterval = 12;
	private double flightGenerationTimer = flightGenerationInterval;
	private int maxAircraft = 4;
	
	public Main main() {
		return _main;
	}
	
	public java.util.ArrayList<Aircraft> aircraftList() {
		return _aircraft;
	}

	public Demo(Main main) {
		super(main);
	}

	@Override
	public void start() {
		_ordersBox = new lib.OrdersBox(ORDERSBOX_X, ORDERSBOX_Y, ORDERSBOX_W, ORDERSBOX_H, 6);
		_aircraft = new java.util.ArrayList<Aircraft>();
		_airplaneImage = graphics.newImage("gfx" + File.separator + "plane.png");
		lib.ButtonText.Action manual = new lib.ButtonText.Action() {
			@Override
			public void action() {
				// _selectedAircraft.manuallyControl();
				System.out.println("Assuming manual control of " + _selectedAircraft.name() + ".");
				toggleManualControl();
			}
		};
		_manualOverrideButton = new lib.ButtonText("Take Control", manual, (window.width() - 128) / 2, 32, 128, 32, 8, 4);
		_timer = 0;
		_selectedAircraft = null;
		_selectedWaypoint = null;
		_selectedPathpoint = -1;
		
		_manualOverrideButton = new lib.ButtonText(" Take Control", manual, (window.width() - 128) / 2, 32, 128, 32, 8, 4);
		_altimeter = new lib.Altimeter(ALTIMETER_X, ALTIMETER_Y, ALTIMETER_W, ALTIMETER_H);
		_timer = 0;
		deselectAircraft();
	}
	
	private void toggleManualControl() {
		if (_selectedAircraft == null) return;
		_selectedAircraft.toggleManualControl();
		_manualOverrideButton.setText( (_selectedAircraft.isManuallyControlled() ? "Remove" : " Take") + " Control");
	}
	
	private void deselectAircraft() {
		System.out.println("Deselecting Aircraft");
		if (_selectedAircraft != null && _selectedAircraft.isManuallyControlled()) {
			_selectedAircraft.toggleManualControl();
			_manualOverrideButton.setText(" Take Control");
		}
		_selectedAircraft = null;
		_selectedWaypoint = null; 
		_selectedPathpoint = -1;
		_altimeter.hide();
	}

	@Override
	public void update(double dt) {
		_timer += dt;
		if (_aircraft.size() > 0){
			_main.score().addTime(dt); 
		}
		_ordersBox.update(dt);
		for (Aircraft plane : _aircraft) {
			plane.update(dt);
		}
		checkCollisions(dt);
		for (int i = _aircraft.size()-1; i >=0; i --) {
			if (_aircraft.get(i).isFinished()) {
				if (_aircraft.get(i) == _selectedAircraft) {
					deselectAircraft();
				}
				_aircraft.remove(i);
				_main.score().addFlight();
			}
		}
		_altimeter.update(dt);
		
		if (_selectedAircraft != null && _selectedAircraft.isManuallyControlled()) {
			if (input.isKeyDown(input.KEY_LEFT)) {
				_selectedAircraft.turnLeft(dt);
			} else if (input.isKeyDown(input.KEY_RIGHT)) {
				_selectedAircraft.turnRight(dt);
			}
		}
		
		flightGenerationTimer += dt;
		if(flightGenerationTimer >= flightGenerationInterval){
			flightGenerationTimer -= flightGenerationInterval;
			if (_aircraft.size() < maxAircraft){
				generateFlight();
			}
			
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
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		if (key == input.MOUSE_LEFT) {
			Aircraft newSelected = _selectedAircraft;
			for (Aircraft a : _aircraft) {
				if (a.isMouseOver(x-16, y-16)) {
					newSelected = a;
				}
			}
			if (newSelected != _selectedAircraft) {
				deselectAircraft();
				_selectedAircraft = newSelected;
			}
			_altimeter.show(_selectedAircraft);
			for (Waypoint w : _waypoints) {
				if (_selectedAircraft != null){
					if (w.isMouseOver(x-16, y-16) && _selectedAircraft.flightPathContains(w) > -1) {
						_selectedWaypoint = w;
						_selectedPathpoint = _selectedAircraft.flightPathContains(w);
					}
				}
			}
			for (Waypoint w : _waypoints) {
				if (w.isMouseOver(x-16, y-16) && _selectedAircraft.flightPathContains(w) > -1) {
					_selectedWaypoint = w;
					_selectedPathpoint = _selectedAircraft.flightPathContains(w);
				}
			}
		}
		if (key == input.MOUSE_RIGHT) deselectAircraft();
		_altimeter.mousePressed(key, x, y);
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		if (_selectedAircraft != null && _manualOverrideButton.isMouseOver(x, y)) _manualOverrideButton.act();
		if (key == input.MOUSE_LEFT && _selectedWaypoint != null) {
			for (Waypoint w : _waypoints) {
				if (w.isMouseOver(x-16, y-16)) {
					_selectedAircraft.alterPath(_selectedPathpoint, w);
					_selectedPathpoint = -1;
					_selectedWaypoint = null;
				}
			}
		}
		_altimeter.mouseReleased(key, x, y);
	}

	@Override
	public void keyPressed(int key) {
		
	}

	@Override
	public void keyReleased(int key) {
		switch (key) {
		
			case input.KEY_SPACE :
				toggleManualControl();
			break;
			
			case input.KEY_LCRTL :
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
		
		graphics.setViewport(16, 16, window.width() - 32, window.height() - 144);
		drawMap();
		graphics.setViewport();
		
		_ordersBox.draw();
		_altimeter.draw();
		
		drawPlaneInfo();
		
		graphics.setColour(0, 128, 0);
		
		drawScore();
	}
	
	private void drawMap() {
		for (Waypoint waypoint : _waypoints) {
			waypoint.draw();
		}
		graphics.setColour(255, 255, 255);
		for (Aircraft aircraft : _aircraft) {
			aircraft.draw();
		}
		
		if (_selectedAircraft != null) {
			// Flight Path
			_selectedAircraft.drawFlightPath();
			graphics.setColour(0, 128, 0);
			// Override Button
			graphics.setColour(0, 0, 0);
			graphics.rectangle(true, (window.width() - 128) / 2, 16, 128, 32);
			graphics.setColour(0, 128, 0);
			graphics.rectangle(false, (window.width() - 128) / 2, 16, 128, 32);
			_manualOverrideButton.draw();
			
			_selectedAircraft.drawFlightPath();
			graphics.setColour(0, 128, 0);
			
		}
		
		graphics.setViewport();
		graphics.setColour(0, 128, 0);
		
			// Change Altitude
	}
	
	private void drawPlaneInfo() {
		graphics.rectangle(false, PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);
		if (_selectedAircraft != null) {
			graphics.setViewport(PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);
			graphics.printCentred(_selectedAircraft.name(), 0, 5, 2, PLANE_INFO_W);
			// Altitude
			String altitude = String.valueOf(_selectedAircraft.position().z()) + "£";
			graphics.print("Altitude:", 10, 40);
			graphics.print(altitude, PLANE_INFO_W - 10 - altitude.length()*8, 40);
			// Speed
			String speed = String.format("%.2f", _selectedAircraft.speed() * 1.687810) + "$";
			graphics.print("Speed:", 10, 55);
			graphics.print(speed, PLANE_INFO_W - 10 - speed.length()*8, 55);
			// Origin
			graphics.print("Origin:", 10, 70);
			graphics.print(_selectedAircraft.originName(), PLANE_INFO_W - 10 - _selectedAircraft.originName().length()*8, 70);
			// Destination
			graphics.print("Destination:", 10, 85);
			graphics.print(_selectedAircraft.destinationName(), PLANE_INFO_W - 10 - _selectedAircraft.destinationName().length()*8, 85);
			graphics.setViewport();
		}
	}
	
	private void drawScore() {
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
		// Origin and Destination
		int o = randInt(0, _locationWayPoints.length);
		int d = randInt(0, _locationWayPoints.length);
		while (_locationNames[d] == _locationNames[o]){
			d = randInt(0, _locationWayPoints.length);
		}
		String originName = _locationNames[o];
		String destinationName = _locationNames[d];
		
/*		int side = randInt(0,1);
		switch (side){
		case 0://enter from left, leave from right
			break;
		case 1://enter from right, leave from left
			break;
		}*/
		
		Waypoint originPoint = _locationWayPoints[o];
		Waypoint destinationPoint = _locationWayPoints[d];
		
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
		_ordersBox.addOrder("<<< " + name + " incoming from " + originName + " heading towards " + destinationName + ".");
		System.out.println("<<< " + name + " incoming from " + originName + " heading towards " + destinationName + ".");
		Aircraft a = new Aircraft(name, originName, destinationName, originPoint, destinationPoint, _airplaneImage, 32 + (int)(10 * Math.random()), _waypoints);
		_aircraft.add(a);
	}
	
	/**
	 * Generates a random integer between min and max, in the range [min, max)
	 * @param min the lower boundary (included) for the random integer
	 * @param max the upper boundary (not included) for the random integer
	 * @return a random integer
	 */
	private int randInt(int min, int max){
		Random rand = new Random();
		return rand.nextInt((max - min)) + min;
	}

	@Override
	public void close() {
		
	}

}