package scn;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import pp.Main;
import lib.ButtonText;
import lib.RandomNumber;
import lib.jog.audio;
import lib.jog.audio.Music;
import lib.jog.graphics;
import lib.jog.graphics.Image;
import lib.jog.input;
import lib.jog.window;
import cls.Aircraft;
import cls.Airport;
import cls.Altimeter;
import cls.FlightPlan;
import cls.HoldingWaypoint;
import cls.OrdersBox;
import cls.Waypoint;
import cls.Waypoint.WaypointType;

public class SinglePlayer extends Scene {

	//private Lives test_lives = new Lives(500, 500, 15);
	// Position of things drawn to window
	private final int PLANE_INFO_X = 16;
	private final int PLANE_INFO_Y = window.getHeight() - 120;
	private final int PLANE_INFO_W = window.getWidth() / 4 - 16;
	private final int PLANE_INFO_H = 112;

	private final int ALTIMETER_X = PLANE_INFO_X + PLANE_INFO_W + 8;
	private final int ALTIMETER_Y = window.getHeight() - 120;
	private final int ALTIMETER_W = 244;
	private final int ALTIMETER_H = 112;

	private final int ORDERSBOX_X = ALTIMETER_X + ALTIMETER_W + 8;
	private final static int ORDERSBOX_Y = window.getHeight() - 120;
	private final int ORDERSBOX_W = window.getWidth() - (ORDERSBOX_X + 16);
	private final static int ORDERSBOX_H = 112;

	// Static Final Ints for difficulty settings
	// Difficulty of demo scene determined by difficulty selection scene
	public final static int DIFFICULTY_EASY = 0;
	public final static int DIFFICULTY_MEDIUM = 1;
	public final static int DIFFICULTY_HARD = 2;
	public static int difficulty = DIFFICULTY_EASY;

	private final static int TAKEOFF_DELAY = 5;

	// texts for the buttons in this class
	public final class Texts {
		public final static String TAKE_CONTROL = "Take Control";
		public final static String REMOVE_CONTROL = "Remove Control";
		public final static String LAND = "Land";
	}

	// Orders box to print orders from ACTO to aircraft to
	private OrdersBox orders_box;

	// Cumulative score, added to upon completion of flightplan by aircraft.
	private int score;

	// Time since the scene began Could be used for score
	private double time_elapsed;

	// Time when the last takeoff occured
	private double next_take_off = 0 + TAKEOFF_DELAY;

	// The currently selected aircraft
	private Aircraft selected_aircraft;

	// Whether player is currently manually controlling
	private boolean is_manually_controlling;

	// The currently selected waypoint
	private Waypoint selected_waypoint;

	// Selected path point, in an aircraft's route, used for altering the route
	private int selected_pathpoint;

	// A list of aircraft present in the airspace
	private ArrayList<Aircraft> aircraft_in_airspace;

	// An image to be used for aircraft Expand to list of images for multiple
	// aircraft appearances
	private Image aircraft_image;

	// A button to start and end manual control of an aircraft
	private ButtonText manual_override_button;

	// Tracks if manual heading compass of a manually controller aircraft has
	// been dragged
	private ButtonText land_button;

	// Tracks if manual heading compass of a manually controller aircraft has
	// been dragged
	private boolean compass_dragged;

	// An altimeter to display aircraft altitidue, heading, etc.
	private Altimeter altimeter;

	// The interval in seconds to generate flights after
	private double flight_generation_interval;

	// The time eleapsed since the last flight was generated
	private double flight_generation_time_elapsed = 6;

	// Max aircraft in the airspace at once Change to 10 for Assessment 3.
	private final int max_aircraft = 20;

	// Music to play during the game scene
	private Music music;

	// The background to draw in the airspace
	private Image background;

	// The airport, should be one instance of it in general
	private Airport airport;
	
	// A list of location names for waypoint flavour
	private final static String[] LOCATION_NAMES = new String[] {
			"North West Top Leftonia", "100 Acre Woods", "City of Rightson",
			"South Sea", "Aerodromio Medved'" };

	/**
	 * The set of waypoints in the airspace which are origins / destinations
	 */
	public final Waypoint[] location_waypoints = new Waypoint[] {
			/* A set of Waypoints which are origin / destination points */

			// top left
			new Waypoint(8, 8, WaypointType.ENTRY_EXIT,
					"North West Top Leftonia"),

			// bottom left
			new Waypoint(8, window.getHeight() - ORDERSBOX_H - 40,
					WaypointType.ENTRY_EXIT, "100 Acre Woods"),

			// top right
			new Waypoint(window.getWidth() - 40, 8, WaypointType.ENTRY_EXIT,
					"City of Rightson"),

			// bottom right
			new Waypoint(window.getWidth() - 40, window.getHeight() - ORDERSBOX_H
					- 40, WaypointType.ENTRY_EXIT, "South Sea"),

			// The airport
			airport = new Airport(949, 390, "Aerodromio Medved'"), };

	/**
	 * All waypoints in the airspace, INCLUDING locationWaypoints.
	 */
	public Waypoint[] airspaceWaypoints = new Waypoint[] {

			// airspace waypoints
			new Waypoint(160, 174), // 0
			new Waypoint(383, 481), // 1
			new Waypoint(502, 274), // 2
			new Waypoint(632, 125), // 3
			new Waypoint(949, 96), // 4
			new Waypoint(698, 430), // 4.1
			new Waypoint(909, 481), // 5

			// destination/origin waypoints - present in this list for
			// pathfinding.
			location_waypoints[0], // 10
			location_waypoints[1], // 11
			location_waypoints[2], // 12
			location_waypoints[3], // 13
			location_waypoints[4], // 14 - Airport
	};

	// All aircraft taking off must go through this waypoint, allows for
	// aircraft to take off in one direction all the time
	private final Waypoint takeoffWaypoint = new Waypoint(airport.position()
			.x() - 120, airport.position().y());

	// All aircraft that land must pass through this waypoint.
	private final HoldingWaypoint[] land_waypoints = {

			new HoldingWaypoint(airport.position().x() + 200, airport
					.position().y()),
			new HoldingWaypoint(airport.position().x() + 150, airport
					.position().y()),

	};

	/**
	 * Used for circling around airport
	 */
	public ArrayList<HoldingWaypoint> holding_waypoints = new ArrayList<HoldingWaypoint>();

	/**
	 * Constructor
	 * 
	 * @param main
	 *            the main containing the scene
	 * @param difficulty
	 *            the difficulty the scene is to be initialised with
	 */
	public SinglePlayer(Main main, int difficulty) {
		super(main);
		SinglePlayer.difficulty = difficulty;
	}

	@Override
	/**
	 * Initialise and begin music, init background image and scene variables.
	 * Shorten flight generation timer according to difficulty
	 */
	public void start() {
		background = graphics.newImage("gfx" + File.separator + "map.png");
		music = audio.newMusic("sfx" + File.separator + "Gypsy_Shoegazer.ogg");
		//music.play();

		flight_generation_interval = 8;

		// Initialise Holding Waypoints, positions are relative to the airport.
		// these are used to circle the airport.
		holding_waypoints.add(new HoldingWaypoint(airport.position().x() - 150,
				airport.position().y() - 100));
		holding_waypoints.add(new HoldingWaypoint(airport.position().x() + 150,
				airport.position().y() - 100));
		holding_waypoints.add(new HoldingWaypoint(airport.position().x() + 150,
				airport.position().y() + 100));
		holding_waypoints.add(new HoldingWaypoint(airport.position().x() - 150,
				airport.position().y() + 100));

		// Initialise values of setNextWaypoint for each holding waypoint.
		holding_waypoints.get(0).setNextWaypoint(holding_waypoints.get(1));
		holding_waypoints.get(1).setNextWaypoint(holding_waypoints.get(2));
		holding_waypoints.get(2).setNextWaypoint(holding_waypoints.get(3));
		holding_waypoints.get(3).setNextWaypoint(holding_waypoints.get(0));

		land_waypoints[0].setNextWaypoint(land_waypoints[1]);
		land_waypoints[1].setNextWaypoint(airport);

		orders_box = new OrdersBox(ORDERSBOX_X, ORDERSBOX_Y, ORDERSBOX_W,
				ORDERSBOX_H, 6);

		aircraft_in_airspace = new ArrayList<Aircraft>();

		aircraft_image = graphics.newImage("gfx" + File.separator + "plane.png");

		ButtonText.Action manual = new ButtonText.Action() {
			@Override
			public void action() {
				toggleManualControl();
			}
		};
		
		altimeter = new Altimeter(ALTIMETER_X, ALTIMETER_Y, ALTIMETER_W,
				ALTIMETER_H);

		manual_override_button = new ButtonText(Texts.TAKE_CONTROL, manual,
				(window.getWidth() - 128) / 2, 32, 128, 32, 8, 4, true, true);

		// the action that is called once the land button is clicked.
		ButtonText.Action land = new ButtonText.Action() {
			@Override
			public void action() {
				// toggle land function
				toggleLand();
			}
		};

		land_button = new ButtonText(Texts.LAND, land,
				(window.getWidth() - 500) / 2, 32, 128, 32, 8, 4, true, true);

		time_elapsed = 0;
		compass_dragged = false;
		selected_aircraft = null;
		selected_waypoint = null;
		selected_pathpoint = -1;

		switch (difficulty) {
		// Set attributes according to the selected difficulty
		// Flights spawn more often on harder difficulties.
		case DIFFICULTY_EASY:
			flight_generation_interval = flight_generation_interval / 1.3;
		case DIFFICULTY_MEDIUM:
			flight_generation_interval = flight_generation_interval / 2.0;
			airport.insertAircraft(createAircraft(true));
			airport.insertAircraft(createAircraft(true));
			break;
		case DIFFICULTY_HARD:
			flight_generation_interval = flight_generation_interval / 2.0;
			airport.insertAircraft(createAircraft(true));
			airport.insertAircraft(createAircraft(true));
			airport.insertAircraft(createAircraft(true));
			airport.insertAircraft(createAircraft(true));
			break;
		}
	}

	/**
	 * Getter for aircraft list
	 * 
	 * @return the arrayList of aircraft in the airspace
	 */
	public ArrayList<Aircraft> aircraftList() {
		return aircraft_in_airspace;
	}

	/**
	 * Causes a selected aircraft to call methods to toggle manual control
	 */
	private void toggleManualControl() {
		if (selected_aircraft == null)
			return;

		is_manually_controlling = !is_manually_controlling;
		selected_aircraft.toggleManualControl();

		manual_override_button.setText(selected_aircraft.isManuallyControlled() ? Texts.REMOVE_CONTROL : Texts.TAKE_CONTROL);
	}

	/**
	 * Causes a selected aircraft to call methods to land
	 */
	private void toggleLand() {
		if (selected_aircraft == null)
			return;

		if (selected_aircraft.isLanding())
			return;

		selected_aircraft.toggleLand(land_waypoints[0]);

	}

	/**
	 * Causes an aircraft to call methods to handle deselection
	 */
	private void deselectAircraft() {
		if (selected_aircraft != null && selected_aircraft.isManuallyControlled()) {
			selected_aircraft.toggleManualControl();
			manual_override_button.setText(Texts.TAKE_CONTROL);
		}

		selected_aircraft = null;
		selected_waypoint = null;
		selected_pathpoint = -1;
		altimeter.hide();
	}

	/**
	 * Update all objects within the scene, ie aircraft, orders box altimeter.
	 * Cause collision detection to occur Generate a new flight if flight
	 * generation interval has been exceeded.
	 */
	@Override
	public void update(double dt) {
		time_elapsed += dt;
		orders_box.update(dt);

		// update airport timer
		airport.setTimeLeft((int) (next_take_off - time_elapsed));

		for (Aircraft plane : aircraft_in_airspace) {

			// Added a try/catch construct to make sure we catch when the
			// aircraft is inserted into a full airport
			try {
				plane.update(dt);
			} catch (IllegalStateException e) {
				orders_box.addOrder("<<< Aerodromio Medved' is full, divert aircraft Comrade!");

				// simple way to end the game if the airport is full
				Aircraft a1 = createAircraft(false);
				Aircraft a2 = createAircraft(true);

				gameOver(a1, a2);
				return;
			}

			// if aircraft landed
			if (plane.isAtAirport()) {
				orders_box.addOrder("<<< Aircraft " + plane.getName() + " has landed safely at Aerodromio Medved'");
			}
			// if aircraft has completed it's journey correctly
			if (plane.hasFinished()) {
				// add points held by aircraft to score, take no action if the
				// points are in negatives.
				if (plane.getNumPoints() <= 0) {
				} else {
					score += plane.getNumPoints();
				}
				switch (RandomNumber.randInclusiveInt(0, 2)) {
				case 0:
					orders_box.addOrder("<<< Thank you Comrade");
					break;
				case 1:
					orders_box.addOrder("<<< Well done Comrade");
					break;
				case 2:
					orders_box.addOrder("<<< Many thanks Comrade");
					break;
				}
			}
		}

		checkCollisions(dt);

		for (int i = aircraft_in_airspace.size() - 1; i >= 0; i--) {

			if (aircraft_in_airspace.get(i).hasFinished()) {

				if (aircraft_in_airspace.get(i) == selected_aircraft) {
					deselectAircraft();
				}

				aircraft_in_airspace.remove(i);
			}

		}

		altimeter.update(dt);

		if (selected_aircraft != null) {

			if (input.isKeyDown(input.KEY_LEFT) || input.isKeyDown(input.KEY_A)) {
				selected_aircraft.turnLeft(dt);
				is_manually_controlling = true;
			} else if (input.isKeyDown(input.KEY_RIGHT) || input.isKeyDown(input.KEY_D)) {
				selected_aircraft.turnRight(dt);
				is_manually_controlling = true;
			}

			// allows to take control by just pressing left/right or A/D
			selected_aircraft.setManualControl(is_manually_controlling);

			// update manual control button text
			manual_override_button.setText(selected_aircraft.isManuallyControlled() ? Texts.REMOVE_CONTROL : Texts.TAKE_CONTROL);
			// Check if the aircraft is out of bounds. If true, remove aircraft
			// from play.
			if (selected_aircraft.isOutOfBounds()) {
				orders_box.addOrder(">>> " + selected_aircraft.getName() + " is out of bounds, contact lost. Do better Comrade.");
				aircraft_in_airspace.remove(selected_aircraft);
				deselectAircraft();
			}

		}

		flight_generation_time_elapsed += dt;
		if (flight_generation_time_elapsed >= flight_generation_interval) {
			flight_generation_time_elapsed -= flight_generation_interval;
			if (aircraft_in_airspace.size() < max_aircraft) {
				generateFlight(false);
			}
		}
	}

	/**
	 * Cause all planes in airspace to update collisions Catch and handle a
	 * resultant game over state
	 * 
	 * @param dt
	 *            delta time since last collision check
	 */
	private void checkCollisions(double dt) {
		for (Aircraft plane : aircraft_in_airspace) {
			int collision_state = plane.updateCollisions(dt, aircraftList());

			if (collision_state >= 0) {
				gameOver(plane, aircraftList().get(collision_state));
				return;
			}
		}
	}

	@Override
	public void playSound(audio.Sound sound) {
		sound.stopSound();
		sound.playSound();
	}

	/**
	 * Handle a game over caused by two planes colliding Create a gameOver scene
	 * and make it the current scene
	 * 
	 * @param plane1
	 *            the first plane involved in the collision
	 * @param plane2
	 *            the second plane in the collision
	 */
	public void gameOver(Aircraft plane1, Aircraft plane2) {
		playSound(audio.newSoundEffect("sfx" + File.separator + "crash.ogg"));
		main.closeScene();
		main.setScene(new GameOver(main, plane1, plane2, score));
	}

	/**
	 * Handle mouse input
	 */
	@Override
	public void mousePressed(int key, int x, int y) {
		if (key == input.MOUSE_LEFT) {

			Aircraft new_selected = selected_aircraft;

			for (Aircraft a : aircraft_in_airspace) {
				if (a.isMouseOver(x - 16, y - 16)) {
					new_selected = a;
				}
			}

			if (new_selected != selected_aircraft) {
				deselectAircraft();
				selected_aircraft = new_selected;
			}

			altimeter.show(selected_aircraft);

			if (selected_aircraft != null) {

				for (Waypoint w : airspaceWaypoints) {
					if (w.isMouseOver(x - 16, y - 16) && selected_aircraft.indexInFlightPath(w) > -1) {
						selected_waypoint = w;
						selected_pathpoint = selected_aircraft.indexInFlightPath(w);
					}
				}

				if (selected_waypoint == null
						&& selected_aircraft.isManuallyControlled()) {
					// If mouse is over compass
					double dx = selected_aircraft.getPosition().x()
							- input.getMouseX();
					double dy = selected_aircraft.getPosition().y()
							- input.getMouseY();
					int r = Aircraft.COMPASS_RADIUS;
					if (dx * dx + dy * dy < r * r) {
						compass_dragged = true;
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
		if (selected_aircraft != null && manual_override_button.isMouseOver(x, y))
			manual_override_button.act();

		if (selected_aircraft != null && land_button.isMouseOver(x, y))
			land_button.act();

		if (key == input.MOUSE_LEFT && airport.isMouseOver(x - 16, y - 16)) {
			// must wait at least 5 seconds between aircraft takeoff
			if (next_take_off - time_elapsed <= 0) {
				try {
					airport.takeoff();
					generateFlight(true);
					next_take_off = time_elapsed + TAKEOFF_DELAY;
				} catch (IllegalStateException e) {
					orders_box.addOrder("<<< There are no aircraft in the airport, Comrade.");
				}
			}
		}

		if (key == input.MOUSE_LEFT && selected_waypoint != null) {
			if (selected_aircraft.isManuallyControlled() == true) {
				return;
			} else {
				for (Waypoint w : airspaceWaypoints) {
					if (w.isMouseOver(x - 16, y - 16)) {
						selected_aircraft.alterPath(selected_pathpoint, w);
						orders_box.addOrder(">>> " + selected_aircraft.getName() + " please alter your course");
						orders_box.addOrder("<<< Roger that. Altering course now.");
						selected_pathpoint = -1;
						selected_waypoint = null;
					} else {
						selected_waypoint = null;
					}
				}

			}
		}

		altimeter.mouseReleased(key, x, y);

		if (compass_dragged && selected_aircraft != null) {
			double dx = input.getMouseX() - selected_aircraft.getPosition().x();
			double dy = input.getMouseY() - selected_aircraft.getPosition().y();
			double new_heading = Math.atan2(dy, dx);

			selected_aircraft.setBearing(new_heading);
		}

		compass_dragged = false;
	}

	@Override
	public void keyPressed(int key) {
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
	}

	/**
	 * Handle keyboard input
	 */
	@Override
	public void keyReleased(int key) {
		switch (key) {

		case input.KEY_S:
		case input.KEY_DOWN:
			if (selected_aircraft != null)
				selected_aircraft.decreaseTargetAltitude();
			break;

		case input.KEY_W:
		case input.KEY_UP:
			if (selected_aircraft != null)
				selected_aircraft.increaseTargetAltitude();
			break;

		case input.KEY_SPACE:
			toggleManualControl();
			break;

		case input.KEY_F:
			toggleLand();
			break;

		case input.KEY_LCRTL:
			generateFlight(false);
			break;

		case input.KEY_F5:
			Aircraft a1 = createAircraft(false);
			Aircraft a2 = createAircraft(true);
			gameOver(a1, a2);
			break;
			
		}
	}

	/**
	 * Draw the scene GUI and all drawables within it, e.g. aircraft and
	 * waypoints
	 */
	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.rectangle(false, 16, 16, window.getWidth() - 32, window.getHeight() - 144);

		graphics.setViewport(16, 16, window.getWidth() - 32, window.getHeight() - 144);

		graphics.setColour(255, 255, 255, 100);
		graphics.draw(background, 0, 0);
		
		drawMap();

		graphics.setViewport();

		if (selected_aircraft != null && selected_aircraft.isManuallyControlled()) {
			selected_aircraft.drawCompass();
		}

		orders_box.draw();
		altimeter.draw();
		drawPlaneInfo();

		graphics.setColour(Main.GREEN);
		drawScore();
	}

	/**
	 * draw waypoints, and route of a selected aircraft between waypoints print
	 * waypoint names next to waypoints
	 */
	private void drawMap() {
		for (Waypoint waypoint : airspaceWaypoints) {
			waypoint.draw();
		}

		for (HoldingWaypoint waypoint : holding_waypoints) {
			waypoint.draw();
		}

		airport.drawAirportInfo();

		graphics.setColour(255, 255, 255);

		for (Aircraft aircraft : aircraft_in_airspace) {
			aircraft.draw();
			if (aircraft.isMouseOver()) {
				aircraft.drawFlightPath(false);
			}
		}

		if (selected_aircraft != null) {

			// Flight Path
			selected_aircraft.drawFlightPath(true);
			graphics.setColour(Main.GREEN);

			// Override Button
			graphics.setColour(0, 0, 0);
			graphics.rectangle(true, (window.getWidth() - 128) / 2, 16, 128, 32);
			graphics.setColour(Main.GREEN);
			manual_override_button.draw();

			// if aircraft is flying towards the airport (i.e. it's its
			// destination point, draw the land button)
			if (selected_aircraft.getFlightPlan().getDestination() instanceof Airport) {
				// Land Button with valid altitude
				graphics.setColour(0, 0, 0);
				graphics.rectangle(true, (window.getWidth() - 500) / 2, 16, 128, 32);
				graphics.setColour(Main.GREEN);
			
				land_button.draw();
			}

			graphics.setColour(Main.GREEN);
		}

		if (selected_waypoint != null
				&& selected_aircraft.isManuallyControlled() == false) {
			selected_aircraft.drawModifiedPath(selected_pathpoint,
					input.getMouseX() - 16, input.getMouseY() - 16);
		}

		graphics.setViewport();
		graphics.setColour(Main.GREEN);
		
		graphics.print(LOCATION_NAMES[0],
				location_waypoints[0].position().x() + 25, location_waypoints[0].position().y() + 10);
		graphics.print(LOCATION_NAMES[1],
				location_waypoints[1].position().x() + 25, location_waypoints[1].position().y() + 10);
		graphics.print(LOCATION_NAMES[2],
				location_waypoints[2].position().x() - 125, location_waypoints[2].position().y() + 10);
		graphics.print(LOCATION_NAMES[3],
				location_waypoints[3].position().x() - 75, location_waypoints[3].position().y() + 10);

	}

	/**
	 * draw the info of a selected plane in the scene GUI
	 */
	private void drawPlaneInfo() {
		graphics.setColour(Main.GREEN);
		graphics.rectangle(false, PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);

		if (selected_aircraft != null) {

			graphics.setViewport(PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);
			graphics.printTextCentred(selected_aircraft.getName(), 0, 5, 2, PLANE_INFO_W);

			// Altitude
			String altitude = String.format("%.0f", selected_aircraft.getPosition().z())+ "£";
			graphics.print("Altitude:", 10, 40);
			graphics.print(altitude, PLANE_INFO_W - 10 - altitude.length() * 8, 40);

			// Speed
			String speed = String.format("%.2f", selected_aircraft.getSpeed() * 1.687810) + "$";
			graphics.print("Speed:", 10, 55);
			graphics.print(speed, PLANE_INFO_W - 10 - speed.length() * 8, 55);

			// Origin
			graphics.print("Origin:", 10, 70);
			graphics.print(selected_aircraft.getFlightPlan().getOriginName(), PLANE_INFO_W - 10 - selected_aircraft.getFlightPlan().getOriginName().length() * 8, 70);

			// Destination
			graphics.print("Destination:", 10, 85);
			graphics.print(selected_aircraft.getFlightPlan().getDestinationName(), PLANE_INFO_W - 10 - selected_aircraft.getFlightPlan().getDestinationName().length() * 8, 85);
			graphics.setViewport();
		}
	}

	/**
	 * Whitespace Concatenation maker
	 * 
	 */
	private static String whiteSpace(int text) {
		String spaces = "";

		int score_length = 5 - Integer.toString(text).length();

		for (int i = 0; i < score_length; i++) {
			spaces += " ";
		}

		return spaces;
	}

	/**
	 * Draw the score and the timer
	 */
	private void drawScore() {
		int hours = (int) (time_elapsed / (60 * 60));
		int minutes = (int) (time_elapsed / 60);

		// padding for all of the top text so it doesn't touch the edge of the
		// window
		int padding_from_top = 4;

		minutes %= 60;

		double seconds = time_elapsed % 60;

		DecimalFormat df = new DecimalFormat("00.00");

		String timePlayed = String.format("%d:%02d:", hours, minutes)
				+ df.format(seconds);
		graphics.print("Score: " + score + whiteSpace(score) + timePlayed, window.getWidth() - (timePlayed.length() * 8) - 150, padding_from_top);

		int planes = aircraft_in_airspace.size();

		graphics.print(String.valueOf(aircraft_in_airspace.size()) + " plane" + (planes == 1 ? "" : "s") + " in the sky.", 32, padding_from_top);
	}

	/**
	 * Create a new aircraft object and introduce it to the airspace
	 */
	private void generateFlight(boolean fromAirport) {

		Aircraft a = createAircraft(fromAirport);
		if (a != null) {
			if (fromAirport) {
				// start at altitude 100 and increase to next step
				a.setAltitude(100);
				a.increaseTargetAltitude();
			}

			orders_box.addOrder("<<< " + a.getName() + " incoming from " + a.getFlightPlan().getOriginName() + " heading towards " + a.getFlightPlan().getDestinationName() + ".");
			aircraft_in_airspace.add(a);
		}
	}
	
 	/**
 	 * List that helps to prevent spawning a plane in waypoint if any plane is currently going towards it 
 	 * or any plane is less than 300 from it.
 	 */
	private java.util.ArrayList<Waypoint> getAvailableEntryPoints() {
		java.util.ArrayList<Waypoint> available_entry_points = new java.util.ArrayList<Waypoint>();
		 
		for (Waypoint entry_point : location_waypoints) {
			boolean is_available = true;
			for (Aircraft aircraft : aircraft_in_airspace) {
				// Check if any plane is currently going towards the exit point/chosen originPoint
				// Check if any plane is less than what is defined as too close from the chosen originPoint
				if (aircraft.getCurrentTarget().equals(entry_point.position()) || aircraft.isCloseToEntry(entry_point.position())) {
					is_available = false;
		 		}	
		 	}
			if (is_available) {
				available_entry_points.add(entry_point);
		 	}	
		}
		return available_entry_points;
	}
	
 	/**
 	 * Advanced version of getAvailableEntryPoints() that returns array of single ids (as a combination of waypoint and altitude ids). As opposed to 
 	 * declining entry point even if the altitude difference is high, it returns the altitude levels for specific waypoints that are 
 	 * valid for creating planes in them. Mod 3 of result gives offset in altitude_list and /3 gives specific entry point.
 	 */
	private java.util.ArrayList<Integer> getIdAvailableEntryPointsAltitudes() {
		java.util.ArrayList<Integer> available_id_entry_points_altitudes = new java.util.ArrayList<Integer>();
		int base_id = -1;
		// checks all waypoints and all altitudes
		for (Waypoint entry_point : location_waypoints) {
			base_id++;
			if (entry_point != airport) { // Airport is excluded as it is said initially whether it should come from airport or elsewhere
				for (int i = 1; i < Aircraft.altitude_list.size(); i++) { // Starts from 1 as the first in the altitude_list is 100 which is not considered
					boolean is_available = true;
					for (Aircraft aircraft : aircraft_in_airspace) {
						// Check if any plane is currently going towards the exit point/chosen originPoint
						// Check if any plane's altitude is less than 300 different from altitude from altitude_list currently being checked and close to entry point at the same time 
						if (aircraft.getCurrentTarget().equals(entry_point.position()) ||
								((Math.abs(aircraft.getPosition().z() - Aircraft.altitude_list.get(i)) < 1000) && aircraft.isCloseToEntry(entry_point.position()))) {
							is_available = false;
						}	
					}
					if (is_available) { 
						available_id_entry_points_altitudes.add(base_id * 3 + i - 1); // -1 because altitude_list starts from index 1 (as opposed to usual 0) 
					}
				}

			}
		}
		return available_id_entry_points_altitudes;
	}
	
	/**
	 * A system of creating planes designed to be fair for the player. Planes are created either from airport or elsewhere based on parameter. 
	 * Also planes are primarily created from waypoints with no planes near them. Secondarily, planes are created in entry points such that no plane is near the altitude 
	 * that a newly created plane has. If it cannot create a plane due to constraints, it returns null.
	 * @return the create aircraft object
	 */
	private Aircraft createAircraft(boolean fromAirport) {
		int preferred_altitude_index = -1;
		int destination = RandomNumber.randInclusiveInt(0, location_waypoints.length - 1);
		int origin = 0; // 0 is default, it is chosen later on (initialized as compiler would otherwise complain)
		Waypoint origin_point; 
		java.util.ArrayList<Waypoint> available_origins = getAvailableEntryPoints();
		
		if (fromAirport) {
			origin_point = airport;		
		}
		else {
			if (available_origins.isEmpty()) { // Creates a plane in waypoint with planes of different altitude than that of the new plane.
				if (getIdAvailableEntryPointsAltitudes().size() == 0)
					return null;
				java.util.ArrayList<Integer> available_id_entry_points_altitudes = getIdAvailableEntryPointsAltitudes();
				int id = available_id_entry_points_altitudes.get(RandomNumber.randInclusiveInt(0, available_id_entry_points_altitudes.size()-1));
				origin = id / 3; // Calculates id of a waypoint
				origin_point = location_waypoints[origin]; 
				preferred_altitude_index = id % 3 + 1; // Calculates id for altitude_list (1 is added as the first item on the list is solely for creating from airports)
			}
			else { // Creates a plane in waypoint with no planes nearby
				origin_point = available_origins.get(RandomNumber.randInclusiveInt(0, available_origins.size()-1)); 
				for (int i = 0; i < location_waypoints.length; i++) { // getting id for an entry point
					if (location_waypoints[i].equals(origin_point)) {
						origin = i;
						break;
					}
				}
			}
		}
		
		// Making sure origin and destination aren't the same
		while (location_waypoints[destination].equals(location_waypoints[origin]) || fromAirport && location_waypoints[destination] instanceof Airport) {
			destination = RandomNumber.randInclusiveInt(0, location_waypoints.length - 1);
		}			
		
		Waypoint destination_point = location_waypoints[destination];
		// Name
		String name = "";
		boolean name_is_taken = true;
		while (name_is_taken) {
			name = "Flight " + (int)(900 * Math.random() + 100);
			name_is_taken = false;
			for (Aircraft a : aircraft_in_airspace) {
				if (a.getName() == name) name_is_taken = true;
			}
		}
		return new Aircraft(name, aircraft_image, 32 + (int) (10 * Math.random()), difficulty, new FlightPlan(origin_point, 
				destination_point, airspaceWaypoints, holding_waypoints, takeoffWaypoint), preferred_altitude_index);
	}

	@Override
	/**
	 * cleanly exit by stopping the scene's music
	 */
	public void close() {
		music.stopMusic();
	}

}
