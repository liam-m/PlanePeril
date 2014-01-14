package scn;

import java.io.File;
import java.util.Random;

import lib.jog.audio;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

import cls.Aircraft;
import cls.Waypoint;

import btc.Main;

public class Demo extends Scene {
	
	private final int PLANE_INFO_X = 16;
	private final int PLANE_INFO_Y = window.height() - 120;
	private final int PLANE_INFO_W = window.width()/4;
	private final int PLANE_INFO_H = 112;
	
	private final int ALTIMETER_X = PLANE_INFO_X + PLANE_INFO_W + 8;
	private final int ALTIMETER_Y = window.height() - 120;
	private final int ALTIMETER_W = 192;
	private final int ALTIMETER_H = 112;
	
	private final int ORDERSBOX_X = ALTIMETER_X + ALTIMETER_W + 8;
	private final static int ORDERSBOX_Y = window.height() - 120;
	private final int ORDERSBOX_W = window.width() - (ORDERSBOX_X + 16);
	private final static int ORDERSBOX_H = 112;
	
	public final static int DIFFICULTY_EASY = 0;
	public final static int DIFFICULTY_MEDIUM = 1;
	public final static int DIFFICULTY_HARD = 2;
	public static int difficulty = DIFFICULTY_EASY;
	
	private lib.OrdersBox ordersBox;
	private double timeElapsed;
	private Aircraft selectedAircraft;
	private Waypoint selectedWaypoint;
	private int selectedPathpoint;
	private java.util.ArrayList<Aircraft> aircraftInAirspace;
	private graphics.Image aircraftImage;
	private lib.ButtonText _manualOverrideButton;
	private boolean compassDragged;
	private cls.Altimeter altimeter;
	private static double flightGenerationInterval = 12;
	private double flightGenerationTimeElapsed = 6;
	private int maxAircraft = 4;
	private int controlAltitude = 30000;
	
	private final String[] LOCATION_NAMES = new String[] {
		"North West Top Leftonia",
		"100 Acre Woods",
		"City of Rightson",
		"South Sea",
	};
	
	public static Waypoint[] locationWaypoints = new Waypoint[] {
		/* A set of Waypoints which are origin / destination points */
		new Waypoint(8, 8, true), //top left
		new Waypoint(8, window.height() - ORDERSBOX_H - 40, true), //bottom left
		new Waypoint(window.width() - 40, 8, true), // top right
		new Waypoint(window.width() - 40, window.height() - ORDERSBOX_H - 40, true), //bottom right
	};

	public static Waypoint[] airspaceWaypoints = new Waypoint[] {		
		/* All waypoints in the airspace, including location Way Points*/
	
		//airspace waypoints
		new Waypoint(125, 70, false),   // 0
		new Waypoint(700, 100, false),  // 1
		new Waypoint(1040, 80, false),  // 2
		new Waypoint(670, 400, false),  // 3
		new Waypoint(1050, 400, false), // 4
		new Waypoint(250, 400, false),  // 5
		new Waypoint(200, 635, false),  // 6
		new Waypoint(500, 655, false),  // 7
		new Waypoint(800, 750, false),  // 8
		new Waypoint(1000, 750, false), // 9
		//destination/origin waypoints - present in this list for pathfinding.
		locationWaypoints[0],           // 10
		locationWaypoints[1],           // 11
		locationWaypoints[2],           // 12
		locationWaypoints[3],           // 13
	};

	public java.util.ArrayList<Aircraft> aircraftList() {
		return aircraftInAirspace;
	}

	public Demo(Main main, int difficulty) {
		super(main);
		Demo.difficulty = difficulty;
	}

	@Override
	public void start() {
		audio.Music music = audio.newMusic("sfx" + File.separator + "piano.ogg");
		music.play();
		ordersBox = new lib.OrdersBox(ORDERSBOX_X, ORDERSBOX_Y, ORDERSBOX_W, ORDERSBOX_H, 6);
		aircraftInAirspace = new java.util.ArrayList<Aircraft>();
		aircraftImage = graphics.newImage("gfx" + File.separator + "plane.png");
		lib.ButtonText.Action manual = new lib.ButtonText.Action() {
			@Override
			public void action() {
				// _selectedAircraft.manuallyControl();
				System.out.println("Assuming manual control of " + selectedAircraft.name() + ".");
				toggleManualControl();
			}
		};
		_manualOverrideButton = new lib.ButtonText("Take Control", manual, (window.width() - 128) / 2, 32, 128, 32, 8, 4);
		timeElapsed = 0;
		compassDragged = false;
		selectedAircraft = null;
		selectedWaypoint = null;
		selectedPathpoint = -1;
		
		_manualOverrideButton = new lib.ButtonText(" Take Control", manual, (window.width() - 128) / 2, 32, 128, 32, 8, 4);
		altimeter = new cls.Altimeter(ALTIMETER_X, ALTIMETER_Y, ALTIMETER_W, ALTIMETER_H);
		timeElapsed = 0;
		deselectAircraft();
		
		switch (difficulty){
		case 0:
			break;
		case 1:
			flightGenerationInterval = flightGenerationInterval / 1.3;
			break;
		case 2:
			flightGenerationInterval = flightGenerationInterval / 2;
			break;
		}
	}
	
	private void toggleManualControl() {
		if (selectedAircraft == null) return;
		selectedAircraft.toggleManualControl();
		_manualOverrideButton.setText( (selectedAircraft.isManuallyControlled() ? "Remove" : " Take") + " Control");
	}
	
	private void deselectAircraft() {
		System.out.println("Deselecting Aircraft");
		if (selectedAircraft != null && selectedAircraft.isManuallyControlled()) {
			selectedAircraft.toggleManualControl();
			_manualOverrideButton.setText(" Take Control");
		}
		selectedAircraft = null;
		selectedWaypoint = null; 
		selectedPathpoint = -1;
		altimeter.hide();
	}

	@Override
	public void update(double dt) {
		timeElapsed += dt;
		ordersBox.update(dt);
		for (Aircraft plane : aircraftInAirspace) {
			plane.update(dt);
		}
		checkCollisions(dt);
		for (int i = aircraftInAirspace.size()-1; i >=0; i --) {
			if (aircraftInAirspace.get(i).isFinished()) {
				if (aircraftInAirspace.get(i) == selectedAircraft) {
					deselectAircraft();
				}
				aircraftInAirspace.remove(i);
			}
		}
		altimeter.update(dt);
		
		if (selectedAircraft != null && selectedAircraft.isManuallyControlled()) {
			if (input.isKeyDown(input.KEY_LEFT)) {
				selectedAircraft.turnLeft(dt);
			} else if (input.isKeyDown(input.KEY_RIGHT)) {
				selectedAircraft.turnRight(dt);
			}
		}
		
		flightGenerationTimeElapsed += dt;
		if(flightGenerationTimeElapsed >= flightGenerationInterval){
			flightGenerationTimeElapsed -= flightGenerationInterval;
			if (aircraftInAirspace.size() < maxAircraft){
				generateFlight();
			}
			
		}
	}
	
	private void checkCollisions(double dt) {
		for (Aircraft plane : aircraftInAirspace) {
			plane.updateCollisions(dt, this);
		}
	}
	
	public void gameOver(Aircraft plane1, Aircraft plane2) {
		main.closeScene();
		main.setScene(new GameOver(main, plane1, plane2));
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		if (key == input.MOUSE_LEFT) {
			Aircraft newSelected = selectedAircraft;
			for (Aircraft a : aircraftInAirspace) {
				if (a.isMouseOver(x-16, y-16) && a.position().z() == controlAltitude) {
					newSelected = a;
				}
			}
			if (newSelected != selectedAircraft) {
				deselectAircraft();
				selectedAircraft = newSelected;
			}
			altimeter.show(selectedAircraft);
			if (selectedAircraft != null) {
				for (Waypoint w : airspaceWaypoints) {
					if (w.isMouseOver(x-16, y-16) && selectedAircraft.flightPathContains(w) > -1) {
						selectedWaypoint = w;
						selectedPathpoint = selectedAircraft.flightPathContains(w);
					}
				}
				if (selectedWaypoint == null && selectedAircraft.isManuallyControlled()) {
					// If mouse is over compass
					double dx = selectedAircraft.position().x() - input.mouseX();
					double dy = selectedAircraft.position().y() - input.mouseY();
					int r = Aircraft.COMPASS_RADIUS;
					if (dx*dx + dy*dy < r*r) {
						compassDragged = true;
					}
				}
			}
		}
		if (key == input.MOUSE_RIGHT) deselectAircraft();
		altimeter.mousePressed(key, x, y);
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		if (selectedAircraft != null && _manualOverrideButton.isMouseOver(x, y)) _manualOverrideButton.act();
		if (key == input.MOUSE_LEFT && selectedWaypoint != null) {
			for (Waypoint w : airspaceWaypoints) {
				if (w.isMouseOver(x-16, y-16)) {
					selectedAircraft.alterPath(selectedPathpoint, w);
					ordersBox.addOrder(">>> Flight " + selectedAircraft.name() + " please alter your course");
					ordersBox.addOrder("<<< Roger that. Altering course now.");
					selectedPathpoint = -1;
					selectedWaypoint = null;
				} else {
					selectedWaypoint = null;
				}
			}
		}
		if (selectedAircraft == null) {
			if (key == input.MOUSE_WHEEL_UP && controlAltitude < 30000)
				controlAltitude += 2000;
			if (key == input.MOUSE_WHEEL_DOWN && controlAltitude > 28000)
				controlAltitude -= 2000;
		}
		altimeter.mouseReleased(key, x, y);
		if (compassDragged && selectedAircraft != null) {
			double dx = input.mouseX() - selectedAircraft.position().x();
			double dy = input.mouseY() - selectedAircraft.position().y();
			double newHeading = Math.atan2(dy, dx);
			selectedAircraft.setBearing(newHeading);
		}
		compassDragged = false;
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
				main.closeScene();
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
		
		if (selectedAircraft != null && selectedAircraft.isManuallyControlled()) {
			selectedAircraft.drawCompass();
		}
		
		ordersBox.draw();
		altimeter.draw();
		drawPlaneInfo();
		
		graphics.setColour(0, 128, 0);
		drawScore();
	}
	
	private void drawMap() {
		for (Waypoint waypoint : airspaceWaypoints) {
			waypoint.draw();
		}
		graphics.setColour(255, 255, 255);
		for (Aircraft aircraft : aircraftInAirspace) {
			aircraft.draw(controlAltitude);
		}
		
		if (selectedAircraft != null) {
			// Flight Path
			selectedAircraft.drawFlightPath();
			graphics.setColour(0, 128, 0);
			// Override Button
			graphics.setColour(0, 0, 0);
			graphics.rectangle(true, (window.width() - 128) / 2, 16, 128, 32);
			graphics.setColour(0, 128, 0);
			graphics.rectangle(false, (window.width() - 128) / 2, 16, 128, 32);
			_manualOverrideButton.draw();
			
			selectedAircraft.drawFlightPath();
			graphics.setColour(0, 128, 0);
			
		}
		
		if (selectedWaypoint != null) {
			selectedAircraft.drawModifiedPath(selectedPathpoint, input.mouseX() - 16, input.mouseY() - 16);
		}
		
		graphics.setViewport();
		graphics.setColour(0, 128, 0);
		graphics.print(LOCATION_NAMES[0], locationWaypoints[0].position().x() + 25, locationWaypoints[0].position().y() + 10);
		graphics.print(LOCATION_NAMES[1], locationWaypoints[1].position().x() + 25, locationWaypoints[1].position().y() + 10);
		graphics.print(LOCATION_NAMES[2], locationWaypoints[2].position().x() - 125, locationWaypoints[2].position().y() + 10);
		graphics.print(LOCATION_NAMES[3], locationWaypoints[3].position().x() - 75, locationWaypoints[3].position().y() + 10);

			// Change Altitude
	}
	
	private void drawPlaneInfo() {
		graphics.rectangle(false, PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);
		if (selectedAircraft != null) {
			graphics.setViewport(PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);
			graphics.printCentred(selectedAircraft.name(), 0, 5, 2, PLANE_INFO_W);
			// Altitude
			String altitude = String.valueOf(selectedAircraft.position().z()) + "£";
			graphics.print("Altitude:", 10, 40);
			graphics.print(altitude, PLANE_INFO_W - 10 - altitude.length()*8, 40);
			// Speed
			String speed = String.format("%.2f", selectedAircraft.speed() * 1.687810) + "$";
			graphics.print("Speed:", 10, 55);
			graphics.print(speed, PLANE_INFO_W - 10 - speed.length()*8, 55);
			// Origin
			graphics.print("Origin:", 10, 70);
			graphics.print(selectedAircraft.originName(), PLANE_INFO_W - 10 - selectedAircraft.originName().length()*8, 70);
			// Destination
			graphics.print("Destination:", 10, 85);
			graphics.print(selectedAircraft.destinationName(), PLANE_INFO_W - 10 - selectedAircraft.destinationName().length()*8, 85);
			graphics.setViewport();
		}
	}
	
	private void drawScore() {
		int hours = (int)(timeElapsed / (60 * 60));
		int minutes = (int)(timeElapsed / 60);
		minutes %= 60;
		double seconds = timeElapsed % 60;
		java.text.DecimalFormat df = new java.text.DecimalFormat("00.00");
		String timePlayed = String.format("%d:%02d:", hours, minutes) + df.format(seconds); 
		graphics.print(timePlayed, window.width() - (timePlayed.length() * 8), 0);
		int planes = aircraftInAirspace.size();
		graphics.print(String.valueOf(aircraftInAirspace.size()) + " plane" + (planes == 1 ? "" : "s") + " in the sky.", 256, 0);
		graphics.print("Control Altitude: " + String.valueOf(controlAltitude), 650, 0);
	}
	
	private void generateFlight() {
		// Origin and Destination
		int o = randInt(0, locationWaypoints.length);
		int d = randInt(0, locationWaypoints.length);
		while (LOCATION_NAMES[d] == LOCATION_NAMES[o]){
			d = randInt(0, locationWaypoints.length);
		}
		String originName = LOCATION_NAMES[o];
		String destinationName = LOCATION_NAMES[d];
				
		Waypoint originPoint = locationWaypoints[o];
		Waypoint destinationPoint = locationWaypoints[d];
		
		// Name
		String name = "";
		boolean nameTaken = true;
		while (nameTaken) {
			name = "Flight " + (int)(900 * Math.random() + 100);
			nameTaken = false;
			for (Aircraft a : aircraftInAirspace) {
				if (a.name() == name) nameTaken = true;
			}
		}
		// Add to world
		ordersBox.addOrder("<<< " + name + " incoming from " + originName + " heading towards " + destinationName + ".");
		System.out.println("<<< " + name + " incoming from " + originName + " heading towards " + destinationName + ".");
		Aircraft a = new Aircraft(name, originName, destinationName, originPoint, destinationPoint, aircraftImage, 32 + (int)(10 * Math.random()), airspaceWaypoints, difficulty);
		aircraftInAirspace.add(a);
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
