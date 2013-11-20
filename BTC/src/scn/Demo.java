package scn;

import java.io.File;

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
	private Aircraft _selectedAircraft;
	private java.util.ArrayList<Aircraft> _aircraft;
	private graphics.Image _airplaneImage;

	public Demo(Main main) {
		super(main);
	}

	@Override
	public void start() {
		_ordersBox = new lib.OrdersBox(4, window.height() - 120, window.width()-8, 116, 6);
		_aircraft = new java.util.ArrayList<Aircraft>();
		_airplaneImage = graphics.newImage("gfx" + File.separator + "plane.png");
		_timer = 0;
	}

	@Override
	public void update(double dt) {
		_ordersBox.update(dt);
		_timer += dt;
		for (Aircraft plane : _aircraft) {
			plane.update(dt);
		}
		for (int i = _aircraft.size()-1; i >=0; i --) {
			if (_aircraft.get(i).isFinished()) _aircraft.remove(i);
		}
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		if (key == input.MOUSE_LEFT) {
			for (Aircraft a : _aircraft) {
				if (a.isMouseOver(x, y)) {
					_selectedAircraft = a;
					System.out.println("Selected Flight " + a.name());
				}
			}
		}
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
				_main.setScene(new Title(_main));
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
		graphics.setColour(255, 255, 255);
		for (Aircraft aircraft : _aircraft) {
			aircraft.draw();
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
		graphics.print(timePlayed, window.width() - (timePlayed.length() * 8), 0);
		int planes = _aircraft.size();
		graphics.print(String.valueOf(_aircraft.size()) + " plane" + (planes == 1 ? "" : "s") + " in the sky.", 256, 0);
		graphics.print("Score: ", 0, 0);
	}
	
	private void generateFlight() {
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
	}

	@Override
	public void close() {
		
	}

}