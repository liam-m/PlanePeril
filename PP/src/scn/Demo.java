package scn;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

import pp.Main;

import lib.ButtonText;
import lib.RandomNumber;
import lib.jog.audio;
import lib.jog.audio.Music;
import lib.jog.Graphics;
import lib.jog.Graphics.Image;
import lib.jog.Input;
import lib.jog.window;
import cls.Aircraft;
import cls.Airport;
import cls.Altimeter;
import cls.FlightPlan;
import cls.HoldingWaypoint;
import cls.OrdersBox;
import cls.Waypoint;
import cls.Waypoint.WaypointType;

public class Demo extends Scene {

	// Position of things drawn to window
	private final int PLANE_INFO_X = 16;
	private final int PLANE_INFO_Y = window.height() - 120;
	private final int PLANE_INFO_W = window.width() / 4 - 16;
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

	private final static int TAKEOFF_DELAY = 5;

	// texts for the buttons in this class
	private final class Texts {
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
			new Waypoint(8, window.height() - ORDERSBOX_H - 40,
					WaypointType.ENTRY_EXIT, "100 Acre Woods"),

			// top right
			new Waypoint(window.width() - 40, 8, WaypointType.ENTRY_EXIT,
					"City of Rightson"),

			// bottom right
			new Waypoint(window.width() - 40, window.height() - ORDERSBOX_H
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
		background = Graphics.newImage("gfx" + File.separator + "map.png");
		music = audio.newMusic("sfx" + File.separator + "Gypsy_Shoegazer.ogg");
		music.play();

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

		aircraft_image = Graphics.newImage("gfx" + File.separator + "plane.png");

		ButtonText.Action manual = new ButtonText.Action() {
			@Override
			public void action() {
				toggleManualControl();
			}
		};

		manual_override_button = new ButtonText(Texts.TAKE_CONTROL, manual,
				(window.width() - 128) / 2, 32, 128, 32, 8, 4);

		// the action that is called once the land button is clicked.
		ButtonText.Action land = new ButtonText.Action() {
			@Override
			public void action() {
				// toggle land function
				toggleLand();
			}
		};

		land_button = new ButtonText(Texts.LAND, land,
				(window.width() - 500) / 2, 32, 128, 32, 8, 4);

		time_elapsed = 0;
		compass_dragged = false;
		selected_aircraft = null;
		selected_waypoint = null;
		selected_pathpoint = -1;

		altimeter = new Altimeter(ALTIMETER_X, ALTIMETER_Y, ALTIMETER_W,
				ALTIMETER_H);

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

			if (Input.isKeyDown(Input.KEY_LEFT) || Input.isKeyDown(Input.KEY_A)) {
				selected_aircraft.turnLeft(dt);
				is_manually_controlling = true;
			} else if (Input.isKeyDown(Input.KEY_RIGHT) || Input.isKeyDown(Input.KEY_D)) {
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
		if (key == Input.MOUSE_LEFT) {

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
							- Input.getMouseX();
					double dy = selected_aircraft.getPosition().y()
							- Input.getMouseY();
					int r = Aircraft.COMPASS_RADIUS;
					if (dx * dx + dy * dy < r * r) {
						compass_dragged = true;
					}
				}

			}
		}

		if (key == Input.MOUSE_RIGHT)
			deselectAircraft();

		altimeter.mousePressed(key, x, y);
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		if (selected_aircraft != null && manual_override_button.isMouseOver(x, y))
			manual_override_button.act();

		if (selected_aircraft != null && land_button.isMouseOver(x, y))
			land_button.act();

		if (key == Input.MOUSE_LEFT && airport.isMouseOver(x - 16, y - 16)) {
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

		if (key == Input.MOUSE_LEFT && selected_waypoint != null) {

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
			double dx = Input.getMouseX() - selected_aircraft.getPosition().x();
			double dy = Input.getMouseY() - selected_aircraft.getPosition().y();
			double new_heading = Math.atan2(dy, dx);

			selected_aircraft.setBearing(new_heading);
		}

		compass_dragged = false;
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

		case Input.KEY_S:
		case Input.KEY_DOWN:
			if (selected_aircraft != null)
				selected_aircraft.decreaseTargetAltitude();
			break;

		case Input.KEY_W:
		case Input.KEY_UP:
			if (selected_aircraft != null)
				selected_aircraft.increaseTargetAltitude();
			break;

		case Input.KEY_SPACE:
			toggleManualControl();
			break;

		case Input.KEY_F:
			toggleLand();
			break;

		case Input.KEY_LCRTL:
			generateFlight(false);
			break;

		case Input.KEY_ESCAPE:
			main.closeScene();
			break;

		case Input.KEY_F5:
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
		Graphics.setColour(Main.GREEN);
		Graphics.rectangle(false, 16, 16, window.width() - 32, window.height() - 144);

		Graphics.setViewport(16, 16, window.width() - 32, window.height() - 144);

		Graphics.setColour(255, 255, 255, 100);
		Graphics.draw(background, 0, 0);

		drawMap();

		Graphics.setViewport();

		if (selected_aircraft != null && selected_aircraft.isManuallyControlled()) {
			selected_aircraft.drawCompass();
		}

		orders_box.draw();
		altimeter.draw();
		drawPlaneInfo();

		Graphics.setColour(Main.GREEN);
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

		Graphics.setColour(255, 255, 255);

		for (Aircraft aircraft : aircraft_in_airspace) {
			aircraft.draw();
			if (aircraft.isMouseOver()) {
				aircraft.drawFlightPath(false);
			}
		}

		if (selected_aircraft != null) {

			// Flight Path
			selected_aircraft.drawFlightPath(true);
			Graphics.setColour(Main.GREEN);

			// Override Button
			Graphics.setColour(0, 0, 0);
			Graphics.rectangle(true, (window.width() - 128) / 2, 16, 128, 32);
			Graphics.setColour(Main.GREEN);
			Graphics.rectangle(false, (window.width() - 128) / 2, 16, 128, 32);
			manual_override_button.draw();

			// if aircraft is flying towards the airport (i.e. it's its
			// destination point, draw the land button)
			if (selected_aircraft.getFlightPlan().getDestination() instanceof Airport) {
				// Land Button with valid altitude
				Graphics.setColour(0, 0, 0);
				Graphics.rectangle(true, (window.width() - 500) / 2, 16, 128,
						32);
				Graphics.setColour(Main.GREEN);
				Graphics.rectangle(false, (window.width() - 500) / 2, 16, 128,
						32);
			
				land_button.draw();
			}

			Graphics.setColour(Main.GREEN);
		}

		if (selected_waypoint != null
				&& selected_aircraft.isManuallyControlled() == false) {
			selected_aircraft.drawModifiedPath(selected_pathpoint,
					Input.getMouseX() - 16, Input.getMouseY() - 16);
		}

		Graphics.setViewport();
		Graphics.setColour(Main.GREEN);
		
		Graphics.print(LOCATION_NAMES[0],
				location_waypoints[0].position().x() + 25, location_waypoints[0].position().y() + 10);
		Graphics.print(LOCATION_NAMES[1],
				location_waypoints[1].position().x() + 25, location_waypoints[1].position().y() + 10);
		Graphics.print(LOCATION_NAMES[2],
				location_waypoints[2].position().x() - 125, location_waypoints[2].position().y() + 10);
		Graphics.print(LOCATION_NAMES[3],
				location_waypoints[3].position().x() - 75, location_waypoints[3].position().y() + 10);

	}

	/**
	 * draw the info of a selected plane in the scene GUI
	 */
	private void drawPlaneInfo() {
		Graphics.setColour(Main.GREEN);
		Graphics.rectangle(false, PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);

		if (selected_aircraft != null) {

			Graphics.setViewport(PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);
			Graphics.printTextCentred(selected_aircraft.getName(), 0, 5, 2, PLANE_INFO_W);

			// Altitude
			String altitude = String.format("%.0f", selected_aircraft.getPosition().z())+ "£";
			Graphics.print("Altitude:", 10, 40);
			Graphics.print(altitude, PLANE_INFO_W - 10 - altitude.length() * 8, 40);

			// Speed
			String speed = String.format("%.2f", selected_aircraft.getSpeed() * 1.687810) + "$";
			Graphics.print("Speed:", 10, 55);
			Graphics.print(speed, PLANE_INFO_W - 10 - speed.length() * 8, 55);

			// Origin
			Graphics.print("Origin:", 10, 70);
			Graphics.print(selected_aircraft.getFlightPlan().getOriginName(), PLANE_INFO_W - 10 - selected_aircraft.getFlightPlan().getOriginName().length() * 8, 70);

			// Destination
			Graphics.print("Destination:", 10, 85);
			Graphics.print(selected_aircraft.getFlightPlan().getDestinationName(), PLANE_INFO_W - 10 - selected_aircraft.getFlightPlan().getDestinationName().length() * 8, 85);
			Graphics.setViewport();
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
		Graphics.print("Score: " + score + whiteSpace(score) + timePlayed, window.width() - (timePlayed.length() * 8) - 150, padding_from_top);

		int planes = aircraft_in_airspace.size();

		Graphics.print(String.valueOf(aircraft_in_airspace.size()) + " plane" + (planes == 1 ? "" : "s") + " in the sky.", 32, padding_from_top);
	}

	/**
	 * Create a new aircraft object and introduce it to the airspace
	 */
	private void generateFlight(boolean fromAirport) {

		Aircraft a = createAircraft(fromAirport);

		if (fromAirport) {
			// start at altitude 100 and increase to next step
			a.setAltitude(100);
			a.increaseTargetAltitude();
		}

		orders_box.addOrder("<<< " + a.getName() + " incoming from " + a.getFlightPlan().getOriginName() + " heading towards " + a.getFlightPlan().getDestinationName() + ".");

		aircraft_in_airspace.add(a);
	}

	/**
	 * Handle nitty gritty of aircraft creating including randomisation of
	 * entry, exit, altitude, etc.
	 * 
	 * @return the create aircraft object
	 */
	private Aircraft createAircraft(boolean fromAirport) {
		int origin = RandomNumber.randInclusiveInt(0, location_waypoints.length - 2); // -2 to exclude the airport
		int destination = RandomNumber.randInclusiveInt(0, location_waypoints.length - 1);
		
		Waypoint originPoint = fromAirport ? airport : location_waypoints[origin];

		// Make sure origin and destination aren't the same
		while (location_waypoints[destination].equals(location_waypoints[origin]) || fromAirport && location_waypoints[destination] instanceof Airport) {
			destination = RandomNumber.randInclusiveInt(0, location_waypoints.length - 1);
		}

		Waypoint destinationPoint = location_waypoints[destination];

		String name = "";
		boolean nameTaken = true;

		while (nameTaken) {
			name = "Flight " + (int) (900 * Math.random() + 100);
			nameTaken = false;
			for (Aircraft a : aircraft_in_airspace) {
				if (a.getName() == name)
					nameTaken = true;
			}
		}

		return new Aircraft(name, aircraft_image,
				32 + (int) (10 * Math.random()), difficulty,
				aircraft_in_airspace, new FlightPlan(originPoint,
						destinationPoint, airspaceWaypoints, holding_waypoints, takeoffWaypoint));
	}

	@Override
	/**
	 * cleanly exit by stopping the scene's music
	 */
	public void close() {
		music.stopMusic();
	}

}
