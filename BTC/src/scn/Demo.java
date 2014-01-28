package scn;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import lib.ButtonText;
import lib.RandomNumber;
import lib.jog.audio;
import lib.jog.audio.Music;
import lib.jog.graphics;
import lib.jog.graphics.Image;
import lib.jog.input;
import lib.jog.window;
import btc.Main;
import cls.Aircraft;
import cls.Altimeter;
import cls.OrdersBox;
import cls.Waypoint;

public class Demo extends Scene {
	
	// Position of things drawn to window   
	private final int PLANE_INFO_X = 16;
	private final int PLANE_INFO_Y = window.height() - 120;
	private final int PLANE_INFO_W = window.width()/4 - 16;
	private final int PLANE_INFO_H = 112;
	
	private final int ALTIMETER_X = PLANE_INFO_X + PLANE_INFO_W + 8;
	private final int ALTIMETER_Y = window.height() - 120;
	private final int ALTIMETER_W = 244;
	private final int ALTIMETER_H = 112;
	
	private final int ORDERSBOX_X = ALTIMETER_X + ALTIMETER_W + 8;
	private final static int ORDERSBOX_Y = window.height() - 120;
	private final int ORDERSBOX_W = window.width() - (ORDERSBOX_X + 16);
	private final static int ORDERSBOX_H = 112;
	
	// Static Final Ints for difficulty settings
	// Difficulty of demo scene determined by difficulty selection scene
	public final static int DIFFICULTY_EASY = 0;
	public final static int DIFFICULTY_MEDIUM = 1;
	public final static int DIFFICULTY_HARD = 2;
	public static int difficulty = DIFFICULTY_EASY;
	
	/**
	 * Orders box to print orders from ACTO to aircraft to
	 */
	private cls.OrdersBox ordersBox;
	
	/**
	 * Time since the scene began
	 * Could be used for score
	 */
	private double timeElapsed;
	/**
	 * The currently selected aircraft
	 */
	private Aircraft selectedAircraft;
	/**
	 * The currently selected waypoint
	 */
	private Waypoint selectedWaypoint;
	/**
	 * Selected path point, in an aircraft's route, used for altering the route
	 */
	private int selectedPathpoint;
	/**
	 * A list of aircraft present in the airspace
	 */
	private ArrayList<Aircraft> aircraftInAirspace;
	
	/**
	 * An image to be used for aircraft
	 * Expand to list of images for multiple aircraft appearances
	 */
	private Image aircraftImage;
	
	/**
	 * A button to start and end manual control of an aircraft
	 */
	private ButtonText manualOverrideButton;
	/**
	 * Tracks if manual heading compass of a manually controller aircraft has been dragged
	 */
	private boolean compassDragged;
	/**
	 * An altimeter to display aircraft altitidue, heading, etc.
	 */
	private Altimeter altimeter;
	/**
	 * The interval in seconds to generate flights after
	 */
	private static double flightGenerationInterval = 12;
	/**
	 * The time eleapsed since the last flight was generated
	 */
	private double flightGenerationTimeElapsed = 6;
	/**
	 * Max aircraft in the airspace at once
	 * Change to 10 for Assessment 3.
	 */
	private int maxAircraft = 2;
	/**
	 * The current control altitude of the ACTO
	 * initially 30,000
	 * only aircraft on or close to this altitude can be controlled
	 */
	private int controlAltitude = 30000;
	
	/**
	 * Music to play during the game scene
	 */
	private Music music;
	/**
	 * The background to draw in the airspace.
	 */
	private Image background;
	
	/**
	 * A list of location names for waypoint flavour
	 */
	private final String[] LOCATION_NAMES = new String[] {
		"North West Top Leftonia",
		"100 Acre Woods",
		"City of Rightson",
		"South Sea",
	};
	
	/**
	 * The set of waypoints in the airspace which are origins / destinations
	 */
	public static Waypoint[] locationWaypoints = new Waypoint[] {
		/* A set of Waypoints which are origin / destination points */
		new Waypoint(8, 8, true), //top left
		new Waypoint(8, window.height() - ORDERSBOX_H - 40, true), //bottom left
		new Waypoint(window.width() - 40, 8, true), // top right
		new Waypoint(window.width() - 40, window.height() - ORDERSBOX_H - 40, true), //bottom right
	};

	/**
	 * All waypoints in the airspace, INCLUDING locationWaypoints.
	 */
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
	/**
	 * Constructor
	 * @param main the main containing the scene
	 * @param difficulty the difficulty the scene is to be initialised with
	 */
	public Demo(Main main, int difficulty) {
		super(main);
		Demo.difficulty = difficulty;
	}

	@Override
	/**
	 * Initialise and begin music, init background image and scene variables.
	 * Shorten flight generation timer according to difficulty
	 */
	public void start() {
		background = graphics.newImage("gfx" + File.separator + "map.png");
		music = audio.newMusic("sfx" + File.separator + "Gypsy_Shoegazer.ogg");
		music.play();

		ordersBox = new OrdersBox(ORDERSBOX_X, ORDERSBOX_Y, ORDERSBOX_W,
				ORDERSBOX_H, 6);

		aircraftInAirspace = new ArrayList<Aircraft>();

		aircraftImage = graphics.newImage("gfx" + File.separator + "plane.png");

		lib.ButtonText.Action manual = new lib.ButtonText.Action() {
			@Override
			public void action() {
				// _selectedAircraft.manuallyControl();
				toggleManualControl();
			}
		};

		manualOverrideButton = new ButtonText("Take Control", manual,
				(window.width() - 128) / 2, 32, 128, 32, 8, 4);
		timeElapsed = 0;
		compassDragged = false;
		selectedAircraft = null;
		selectedWaypoint = null;
		selectedPathpoint = -1;
		
		manualOverrideButton = new ButtonText(" Take Control", manual,
				(window.width() - 128) / 2, 32, 128, 32, 8, 4);
		altimeter = new Altimeter(ALTIMETER_X, ALTIMETER_Y, ALTIMETER_W,
				ALTIMETER_H);
		deselectAircraft();
		
		switch (difficulty){
		// Set attributes according to the selected difficulty
		// Flights spawn more often on harder difficulties.
		case DIFFICULTY_EASY:
			break;
		case DIFFICULTY_MEDIUM:
			flightGenerationInterval = flightGenerationInterval / 1.3;
			break;
		case DIFFICULTY_HARD:
			flightGenerationInterval = flightGenerationInterval / 1.6;
			break;
		}
	}
	
	/**
	 * Getter for aircraft list
	 * @return the arrayList of aircraft in the airspace
	 */
	public ArrayList<Aircraft> aircraftList() {
		return aircraftInAirspace;
	}
	
	/**
	 * Causes a selected aircraft to call methods to toggle manual control
	 */
	private void toggleManualControl() {
		if (selectedAircraft == null)
			return;
		selectedAircraft.toggleManualControl();

		manualOverrideButton.setText( (selectedAircraft.isManuallyControlled() ? "Remove" : " Take") + " Control");
	}
	
	/**
	 * Causes an aircraft to call methods to handle deselection
	 */
	private void deselectAircraft() {
		if (selectedAircraft != null && selectedAircraft.isManuallyControlled()) {
			selectedAircraft.toggleManualControl();
			manualOverrideButton.setText(" Take Control");
		}

		selectedAircraft = null;
		selectedWaypoint = null; 
		selectedPathpoint = -1;
		altimeter.hide();
	}
	
	/**
	 * Update all objects within the scene, ie aircraft, orders box altimeter.
	 * Cause collision detection to occur
	 * Generate a new flight if flight generation interval has been exceeded.
	 */
	@Override
	public void update(double dt) {
		timeElapsed += dt;
		ordersBox.update(dt);

		for (Aircraft plane : aircraftInAirspace) {
			plane.update(dt);
			if (plane.isFinished()){
				switch (RandomNumber.randInclusiveInt(0, 2)){
				case 0:
					ordersBox.addOrder("<<< Thank you Comrade");
					break;
				case 1:
					ordersBox.addOrder("<<< Well done Comrade");
					break;
				case 2:
					ordersBox.addOrder("<<< Many thanks Comrade");
					break;
				}
			}
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
			if (selectedAircraft.outOfBounds()) {
				ordersBox.addOrder(">>> " + selectedAircraft.name() + " out of bounds, returning to route");
				deselectAircraft();
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
	
	/**
	 * Cause all planes in airspace to update collisions
	 * Catch and handle a resultant game over state
	 * @param dt delta time since last collision check
	 */
	private void checkCollisions(double dt) {
		for (Aircraft plane : aircraftInAirspace) {
			int collisionState = plane.updateCollisions(dt, aircraftList());

			if (collisionState >= 0) {
				gameOver(plane, aircraftList().get(collisionState));
				return;
			}
		}
	}
	
	@Override
	public void playSound(audio.Sound sound){
		sound.stop();
		sound.play();
	}
	
	/**
	 * Handle a game over caused by two planes colliding
	 * Create a gameOver scene and make it the current scene
	 * @param plane1 the first plane involved in the collision
	 * @param plane2 the second plane in the collision
	 */
	public void gameOver(Aircraft plane1, Aircraft plane2) {
		playSound(audio.newSoundEffect("sfx" + File.separator + "crash.ogg"));
		main.closeScene();
		main.setScene(new GameOver(main, plane1, plane2));
	}
	
	/**
	 * Causes the scene to pause execution for the specified number of seconds
	 * @param seconds the number of seconds to wait.
	 */
	@Deprecated
	public void wait(int seconds){
		long startTime, endTime;
		startTime = System.currentTimeMillis();
		endTime = startTime + (seconds * 1000);
		
		while (startTime < endTime){
			startTime = System.currentTimeMillis();
		}
		
		return;
	}

	/**
	 * Handle mouse input
	 */
	@Override
	public void mousePressed(int key, int x, int y) {
		if (key == input.MOUSE_LEFT) {

			Aircraft newSelected = selectedAircraft;

			for (Aircraft a : aircraftInAirspace) {
				if (a.isMouseOver(x-16, y-16) && aircraftSelectableAtAltitude(a, controlAltitude)) {
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

		if (key == input.MOUSE_RIGHT)
			deselectAircraft();

		altimeter.mousePressed(key, x, y);
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		if (selectedAircraft != null && manualOverrideButton.isMouseOver(x, y))
			manualOverrideButton.act();

		if (key == input.MOUSE_LEFT && selectedWaypoint != null) {

			if (selectedAircraft.isManuallyControlled() == true){

				return;

			} else {

				for (Waypoint w : airspaceWaypoints) {

					if (w.isMouseOver(x-16, y-16)) {
						selectedAircraft.alterPath(selectedPathpoint, w);
						ordersBox.addOrder(">>> " + selectedAircraft.name() + " please alter your course");
						ordersBox.addOrder("<<< Roger that. Altering course now.");
						selectedPathpoint = -1;
						selectedWaypoint = null;
					} else {
						selectedWaypoint = null;
					}
				}

			}
		}
		
		if (key == input.MOUSE_WHEEL_UP && controlAltitude < 30000)
			controlAltitude += 2000;

		if (key == input.MOUSE_WHEEL_DOWN && controlAltitude > 28000)
			controlAltitude -= 2000;

		int altitudeState = 0;

		if (selectedAircraft != null) {
			altitudeState = selectedAircraft.altitudeState();
		}

		altimeter.mouseReleased(key, x, y);

		if (selectedAircraft != null) {
			if (altitudeState != selectedAircraft.altitudeState()) {
				ordersBox.addOrder(">>> " + selectedAircraft.name() + ", please adjust your altitude");
				ordersBox.addOrder("<<< Roger that. Altering altitude now.");
			}
		}

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
	/**
	 * handle keyboard input
	 */
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
			
			case input.KEY_F5 :
				Aircraft a1 = createAircraft();
				Aircraft a2 = createAircraft();
				gameOver(a1, a2);
			break;
			
		}
	}
	
	/**
	 * Draw the scene GUI and all drawables within it, e.g. aircraft and waypoints
	 */
	@Override
	public void draw() {
		graphics.setColour(0, 128, 0);
		graphics.rectangle(false, 16, 16, window.width() - 32, window.height() - 144);
		
		graphics.setViewport(16, 16, window.width() - 32, window.height() - 144);
		graphics.setColour(255, 255, 255, 32);
		graphics.draw(background, 0, 0);
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
	
	/**
	 * draw waypoints, and route of a selected aircraft between waypoints
	 * print waypoint names next to waypoints
	 */
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
			manualOverrideButton.draw();
			
			selectedAircraft.drawFlightPath();
			graphics.setColour(0, 128, 0);
			
		}
		
		if (selectedWaypoint != null && selectedAircraft.isManuallyControlled() == false) {
			selectedAircraft.drawModifiedPath(selectedPathpoint, input.mouseX() - 16, input.mouseY() - 16);
		}
		
		graphics.setViewport();
		graphics.setColour(0, 128, 0);
		graphics.print(LOCATION_NAMES[0], locationWaypoints[0].position().x() + 25, locationWaypoints[0].position().y() + 10);
		graphics.print(LOCATION_NAMES[1], locationWaypoints[1].position().x() + 25, locationWaypoints[1].position().y() + 10);
		graphics.print(LOCATION_NAMES[2], locationWaypoints[2].position().x() - 125, locationWaypoints[2].position().y() + 10);
		graphics.print(LOCATION_NAMES[3], locationWaypoints[3].position().x() - 75, locationWaypoints[3].position().y() + 10);

	}
	
	/**
	 * draw the info of a selected plane in the scene GUI
	 */
	private void drawPlaneInfo() {
		graphics.setColour(0, 128, 0);
		graphics.rectangle(false, PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);

		if (selectedAircraft != null) {
			graphics.setViewport(PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);
			graphics.printCentred(selectedAircraft.name(), 0, 5, 2, PLANE_INFO_W);
			// Altitude
			String altitude = String.format("%.0f", selectedAircraft.position().z()) + "£";
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
	
	/**
	 * draw a readout of the time the game has been played for, aircraft in the sky, etc.
	 * Hint: for assessment 3, this could be used to print the player's current score.
	 */
	private void drawScore() {
		int hours = (int)(timeElapsed / (60 * 60));
		int minutes = (int)(timeElapsed / 60);

		minutes %= 60;

		double seconds = timeElapsed % 60;

		DecimalFormat df = new DecimalFormat("00.00");

		String timePlayed = String.format("%d:%02d:", hours, minutes) + df.format(seconds); 
		graphics.print(timePlayed, window.width() - (timePlayed.length() * 8 + 32), 0);

		int planes = aircraftInAirspace.size();

		graphics.print(String.valueOf(aircraftInAirspace.size()) + " plane" + (planes == 1 ? "" : "s") + " in the sky.", 32, 0);
		graphics.print("Control Altitude: " + String.valueOf(controlAltitude), 544, 0);
	}
	
	/**
	 * Create a new aircraft object and introduce it to the airspace
	 */
	private void generateFlight() {
		Aircraft a = createAircraft();
		ordersBox.addOrder("<<< " + a.name() + " incoming from " + a.originName() + " heading towards " + a.destinationName() + ".");
		aircraftInAirspace.add(a);
	}
	
	/**
	 * Handle nitty gritty of aircraft creating
	 * including randomisation of entry, exit, altitude, etc.
	 * @return the create aircraft object
	 */
	private Aircraft createAircraft() {
		// Origin and Destination
		int o = RandomNumber.randInclusiveInt(0, locationWaypoints.length - 1);
		int d = RandomNumber.randInclusiveInt(0, locationWaypoints.length - 1);

		while (LOCATION_NAMES[d] == LOCATION_NAMES[o]){
			d = RandomNumber.randInclusiveInt(0, locationWaypoints.length - 1);
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

		return new Aircraft(name, destinationName, originName, destinationPoint, originPoint, aircraftImage, 32 + (int)(10 * Math.random()), airspaceWaypoints, difficulty);
	}
	
	/**
	 * Decide which aircraft are selectable at the current control altitude
	 * Aircraft must be on the current control altitude, or changing altitude towards it
	 * @param a an aircraft to be checked for selectability
	 * @param altitude the current control altitude
	 * @return whether or not the aircraft is selectable at the current control altitude
	 */
	private boolean aircraftSelectableAtAltitude(Aircraft a, int altitude) {
		if (a.position().z() == altitude)
			return true;

		if (a.position().z() < altitude
				&& a.altitudeState() == Aircraft.ALTITUDE_CLIMB)
			return true;

		if (a.position().z() > altitude
				&& a.altitudeState() == Aircraft.ALTITUDE_FALL)
			return true;

		return false;
	}
	
	@Override
	/**
	 * cleanly exit by stopping the scene's music
	 */
	public void close() {
		music.stop();
	}

}
