package scn;

import java.io.File;
import java.util.ArrayList;

import cls.Aircraft;
import cls.Airport;
import cls.AirportControlBox;
import cls.Altimeter;
import cls.Lives;
import cls.OrdersBox;
import cls.HoldingWaypoint;
import cls.PerformanceBar;
import cls.Waypoint;
import cls.Waypoint.WaypointType;
import pp.Main;
import scn.SinglePlayer.Texts;
import lib.ButtonText;
import lib.jog.audio.Sound;
import lib.jog.audio;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.graphics.Image;
import lib.jog.window;

public class Multiplayer extends Scene {
	
	final int MAX_AIRCRAFT = 20;
	final int TAKEOFF_DELAY = 5;
	
	String left_name, right_name;
	
	boolean is_left;
	
	public Waypoint[] left_waypoints;
	public Waypoint[] right_waypoints;
	HoldingWaypoint[] left_airport_waypoints, right_airport_waypoints;
	public Airport left_airport = new Airport(449, 390, "Aerodromio Leftved'");
	public Airport right_airport = new Airport(949, 390, "Aerodromio Rightved'");
	public Waypoint left_airport_takeoff_waypoint = new Waypoint(left_airport.position().x() - 120, left_airport.position().y());
	public Waypoint right_airport_takeoff_waypoint = new Waypoint(right_airport.position().x() - 120, right_airport.position().y());
	
	public ArrayList<Aircraft> aircraft = new ArrayList<Aircraft>();
	Aircraft selected_aircraft;
	
	PerformanceBar left_performance, right_performance;
	Lives left_lives, right_lives;
	
	OrdersBox orders_box;
	AirportControlBox airport_control_box;
	Altimeter altimeter;
	
	Image background;
	
	double timer = 0;
	double next_take_off = TAKEOFF_DELAY;
	
	
	// Position of things drawn to window
	final int Y_POSITION_OF_BOTTOM_ELEMENTS = window.getHeight() - 120;
	
	private final int PLANE_INFO_X = 16;
	private final int PLANE_INFO_Y = Y_POSITION_OF_BOTTOM_ELEMENTS;
	private final int PLANE_INFO_W = window.getWidth() / 4 - 16;
	private final int PLANE_INFO_H = 112;
	
	private final int ALTIMETER_X = PLANE_INFO_X + PLANE_INFO_W + 8;
	private final int ALTIMETER_Y = Y_POSITION_OF_BOTTOM_ELEMENTS;
	private final int ALTIMETER_W = 244;
	private final int ALTIMETER_H = 112;
		
	private final int AIRPORT_CONTROL_BOX_X = ALTIMETER_X + ALTIMETER_W + 8;
	private final int AIRPORT_CONTROL_BOX_Y = Y_POSITION_OF_BOTTOM_ELEMENTS;
	private final int AIRPORT_CONTROL_BOX_W = 244;
	private final int AIRPORT_CONTROL_BOX_H = 112;

	private final int ORDERSBOX_X = AIRPORT_CONTROL_BOX_X + AIRPORT_CONTROL_BOX_W + 8;
	private final int ORDERSBOX_Y = Y_POSITION_OF_BOTTOM_ELEMENTS;
	private final int ORDERSBOX_W = window.getWidth() - (ORDERSBOX_X + 16);
	private final int ORDERSBOX_H = 112;
	
	private final int LEFT_PERFOMANCE_X = 100;
	private final int LEFT_PERFOMANCE_Y = 8;
		
	private final int RIGHT_PERFOMANCE_X = window.getWidth()/2 + 200;
	private final int RIGHT_PERFOMANCE_Y = 8;
		
	private final int LEFT_LIVES_X = window.getWidth()/2 - 200;
	private final int LEFT_LIVES_Y = 8;
		
	private final int RIGHT_LIVES_X = window.getWidth()/2 + 20;
	private final int RIGHT_LIVES_Y = 8;
		
	
		
	public final Waypoint[] left_entryexit_waypoints = new Waypoint[] {
			/* A set of Waypoints which are origin / destination points */

			// top 3 entry/exit points
			new Waypoint(8, 8, WaypointType.ENTRY_EXIT),
			new Waypoint((window.getWidth()/4), 8, WaypointType.ENTRY_EXIT),
			new Waypoint((window.getWidth()/2 - 20), 8, WaypointType.ENTRY_EXIT),

			// bottom 3 entry/exit points
			new Waypoint(8, window.getHeight() - 190, WaypointType.ENTRY_EXIT),
			new Waypoint((window.getWidth()/4), window.getHeight() - 190, WaypointType.ENTRY_EXIT),
			new Waypoint((window.getWidth()/2 - 20), window.getHeight() - 190, WaypointType.ENTRY_EXIT),
	};
	
	public final Waypoint[] right_entryexit_waypoints = new Waypoint[] {
			/* A set of Waypoints which are origin / destination points */

			// top 3 entry/exit points
			new Waypoint((window.getWidth()/2 + 20), 8, WaypointType.ENTRY_EXIT),
			new Waypoint(((window.getWidth()* 3)/4), 8, WaypointType.ENTRY_EXIT),
			new Waypoint(window.getWidth() - 40, 8, WaypointType.ENTRY_EXIT),

			// bottom 3 entry/exit points
			new Waypoint((window.getWidth()/2 + 20), window.getHeight() - 190, WaypointType.ENTRY_EXIT),
			new Waypoint(((window.getWidth()* 3)/4), window.getHeight() - 190, WaypointType.ENTRY_EXIT),
			new Waypoint(window.getWidth() - 40, window.getHeight() - 190, WaypointType.ENTRY_EXIT),
	 };
	
	// All aircraft taking off must go through this waypoint, allows for
	// aircraft to take off in one direction all the time
	private final Waypoint left_takeoff_waypoint = new Waypoint(left_airport.position().x() - 120, left_airport.position().y());

	// All aircraft that land must pass through this waypoint.
	private final HoldingWaypoint[] left_land_waypoints = {
		new HoldingWaypoint(left_airport.position().x() + 200, left_airport.position().y()),
		new HoldingWaypoint(left_airport.position().x() + 150, left_airport.position().y()),
	};
	
	public ArrayList<HoldingWaypoint> left_holding_waypoints = new ArrayList<HoldingWaypoint>();
	
	
	
	// Same functionality as above for the 2nd player's airport
	private final Waypoint right_takeoff_waypoint = new Waypoint(right_airport.position().x() - 120, right_airport.position().y());

	private final HoldingWaypoint[] right_land_waypoints = {
		new HoldingWaypoint(right_airport.position().x() + 200, right_airport.position().y()),
		new HoldingWaypoint(right_airport.position().x() + 150, right_airport.position().y()),
	};
	
	public ArrayList<HoldingWaypoint> right_holding_waypoints = new ArrayList<HoldingWaypoint>();;
	
	boolean is_manually_controlling;
	ButtonText land_button;
	boolean compass_dragged;
	Waypoint selected_waypoint;
	int selected_pathpoint;
	public Image aircraft_image;
	
	double flight_generation_time_elapsed = 6;
	double flight_generation_interval = 4;
	
	public Multiplayer(Main main, String left_name, String right_name, final boolean is_left) {
		super(main);
		this.left_name = left_name;
		this.right_name = right_name;
		this.background = graphics.newImage("gfx" + File.separator + "map.png");
		this.is_left = is_left;
		
		left_waypoints = new Waypoint[]{
			new Waypoint(100, 150),
			new Waypoint(100, 400),
			new Waypoint(100, 700),
			new Waypoint(400, 100),
			new Waypoint(400, 300),
			new Waypoint(400, 500),
			
			left_entryexit_waypoints[0],
			left_entryexit_waypoints[1],
			left_entryexit_waypoints[2],
			left_entryexit_waypoints[3],
			left_entryexit_waypoints[4],
			left_entryexit_waypoints[5]
		};
		right_waypoints = new Waypoint[]{
			new Waypoint((window.getWidth()/2) + 100, 100),
			new Waypoint((window.getWidth()/2) + 100, 300),
			new Waypoint((window.getWidth()/2) + 100, 500),
			new Waypoint((window.getWidth()/2) + 400, 100),
			new Waypoint((window.getWidth()/2) + 400, 300),
			new Waypoint((window.getWidth()/2) + 400, 500),
				
			right_entryexit_waypoints[0],
			right_entryexit_waypoints[1],
			right_entryexit_waypoints[2],
			right_entryexit_waypoints[3],
			right_entryexit_waypoints[4],
			right_entryexit_waypoints[5]
		};
		
		left_holding_waypoints.add( new HoldingWaypoint(left_airport.position().x() - 150, left_airport.position().y() - 100));
		left_holding_waypoints.add( new HoldingWaypoint(left_airport.position().x() + 150, left_airport.position().y() - 100));
		left_holding_waypoints.add( new HoldingWaypoint(left_airport.position().x() + 150, left_airport.position().y() + 100));
		left_holding_waypoints.add( new HoldingWaypoint(left_airport.position().x() - 150, left_airport.position().y() + 100));
	
		right_holding_waypoints.add( new HoldingWaypoint(right_airport.position().x() - 150, right_airport.position().y() - 100));
		right_holding_waypoints.add( new HoldingWaypoint(right_airport.position().x() + 150, right_airport.position().y() - 100));
		right_holding_waypoints.add( new HoldingWaypoint(right_airport.position().x() + 150, right_airport.position().y() + 100));
		right_holding_waypoints.add( new HoldingWaypoint(right_airport.position().x() - 150, right_airport.position().y() + 100));
	
		altimeter = new Altimeter(ALTIMETER_X, ALTIMETER_Y, ALTIMETER_W, ALTIMETER_H);
		orders_box = new OrdersBox(ORDERSBOX_X, ORDERSBOX_Y, ORDERSBOX_W, ORDERSBOX_H, 6);
		left_performance = new PerformanceBar(LEFT_PERFOMANCE_X, LEFT_PERFOMANCE_Y);
		right_performance = new PerformanceBar(RIGHT_PERFOMANCE_X,RIGHT_PERFOMANCE_Y);
		left_lives = new Lives(LEFT_LIVES_X, LEFT_LIVES_Y);
		right_lives = new Lives(RIGHT_LIVES_X, RIGHT_LIVES_Y);
		if (is_left) {
			airport_control_box = new AirportControlBox(AIRPORT_CONTROL_BOX_X, AIRPORT_CONTROL_BOX_Y, AIRPORT_CONTROL_BOX_W, AIRPORT_CONTROL_BOX_H, left_airport);
		} else {
			airport_control_box = new AirportControlBox(AIRPORT_CONTROL_BOX_X, AIRPORT_CONTROL_BOX_Y, AIRPORT_CONTROL_BOX_W, AIRPORT_CONTROL_BOX_H, right_airport);
		}
		
		// Initialise values of setNextWaypoint for each holding waypoint.
		left_holding_waypoints.get(0).setNextWaypoint(left_holding_waypoints.get(1));
		left_holding_waypoints.get(1).setNextWaypoint(left_holding_waypoints.get(2));
		left_holding_waypoints.get(2).setNextWaypoint(left_holding_waypoints.get(3));
		left_holding_waypoints.get(3).setNextWaypoint(left_holding_waypoints.get(0));
		
		right_holding_waypoints.get(0).setNextWaypoint(right_holding_waypoints.get(1));
		right_holding_waypoints.get(1).setNextWaypoint(right_holding_waypoints.get(2));
		right_holding_waypoints.get(2).setNextWaypoint(right_holding_waypoints.get(3));
		right_holding_waypoints.get(3).setNextWaypoint(right_holding_waypoints.get(0));
	
		
		// the action that is called once the land button is clicked.
		ButtonText.Action land = new ButtonText.Action() {
			@Override
			public void action() {
				// toggle land function
				if(is_left) {
					toggleLand(left_holding_waypoints.get(0));
				} else {
					toggleLand(right_holding_waypoints.get(0));
				}
				
			}
		};
		
		land_button = new ButtonText(Texts.LAND, land,
				(window.getWidth() - 500) / 2, 32, 128, 32, 8, 4);

		timer = 0;
		compass_dragged = false;
		selected_aircraft = null;
		selected_waypoint = null;
		selected_pathpoint = -1;
		
		aircraft_image = graphics.newImage("gfx" + File.separator + "plane.png");
	}
	
	@Override
	public void start() {

	}

	@Override
	public void keyPressed(int key) {
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
		if (key == input.KEY_Q) {
			left_lives.decrementLives();
		}
		if (key == input.KEY_E) {
			left_performance.changeValueBy(10);
		}
		if (key == input.KEY_R) {
			left_performance.changeValueBy(-10);
		}
		if (key == input.KEY_T) {
			
		}
	}
	
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
			
		}
	}

	@Override
	public void update(double time_difference) {
	
	}

	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.printText(left_name, 10, 10, 2);
		graphics.printText(right_name, window.getWidth()-(right_name.length()*17), 10, 2);
		graphics.rectangle(false, 16, 40, window.getWidth() - 32, window.getHeight() - 180);
		
		graphics.setViewport(16, 40, window.getWidth() - 32, window.getHeight() - 180);
		graphics.setColour(255, 255, 255, 100);
		graphics.draw(background, 0, 0);
		
		for (int i=0; i<left_waypoints.length; i++) { // Should be same length
			left_waypoints[i].draw();
			right_waypoints[i].draw();
		}
		
		for (HoldingWaypoint l : left_holding_waypoints) {
			l.draw();
		}
		
		for (HoldingWaypoint r : right_holding_waypoints) {
			r.draw();
		}
		
		graphics.setColour(256, 256, 256, 128);
		left_airport.draw();
		right_airport.draw();
		
		for (Aircraft a : aircraft) {
			a.draw();
			if (a.isMouseOver()) {
				a.drawFlightPath(false);
			}
		}
		
		if (selected_aircraft != null) {
			
			selected_aircraft.drawFlightPath(true);
			graphics.setColour(Main.GREEN);
			
			// if aircraft is flying towards the airport (i.e. it's its
			// destination point, draw the land button)
			if (selected_aircraft.getFlightPlan().getDestination() instanceof Airport) {
					// Land Button with valid altitude
					graphics.setColour(0, 0, 0);
					graphics.rectangle(true, (window.getWidth() - 500) / 2, 16, 128, 32);
					graphics.setColour(Main.GREEN);
					graphics.rectangle(false, (window.getWidth() - 500) / 2, 16, 128, 32);
						
					land_button.draw();
			}
			
			graphics.setColour(Main.GREEN);
		}	
		
		if (selected_waypoint != null && selected_aircraft.isManuallyControlled() == false) {
			//TODO offset values should be placed into a constant
			selected_aircraft.drawModifiedPath(selected_pathpoint, input.getMouseX() - 16, input.getMouseY() - 16);
		}
		
		graphics.setViewport();
		
		altimeter.draw();
		airport_control_box.draw();
		orders_box.draw();
		drawPlaneInfo();
		
		left_performance.draw();
		right_performance.draw();
		
		graphics.setColour(Main.GREEN);
		left_lives.draw();
		right_lives.draw();
	}
	
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

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
		sound.stopSound();
		sound.playSound();
	}
	
	public double getTime() {
		return timer;
	}
	
	/**
	 * Causes a selected aircraft to call methods to toggle manual control
	 */
	private void toggleManualControl() {
		if (selected_aircraft == null)
			return;

		is_manually_controlling = !is_manually_controlling;
		selected_aircraft.toggleManualControl();

	}
	
	

	/**
	 * Causes an aircraft to call methods to handle deselection
	 */
	public void deselectAircraft() {
		if (selected_aircraft != null && selected_aircraft.isManuallyControlled()) {
			selected_aircraft.toggleManualControl();
		}

		selected_aircraft = null;
		selected_waypoint = null;
		selected_pathpoint = -1;
		altimeter.hide();
	}
	
	/**
	 * Causes a selected aircraft to call methods to land
	 */
	private void toggleLand(HoldingWaypoint land_waypoint) {
		if (selected_aircraft == null)
			return;

		if (selected_aircraft.isLanding())
			return;

		selected_aircraft.toggleLand(land_waypoint);

	}
	
	/**
	 * Cause all planes in airspace to update collisions Catch and handle a
	 * resultant game over state
	 * 
	 * @param dt
	 *            delta time since last collision check
	 */
	public void checkCollisions(double dt) {
		for (Aircraft plane : aircraft) {
			int collision_state = plane.updateCollisions(dt, aircraft);

			if (collision_state >= 0) {
				playSound(audio.newSoundEffect("sfx" + File.separator + "crash.ogg"));
				//TODO lives need to be subtracted. is it possible for both players to be in the wrong? A crash is between two or more planes! (dont decrement twice)
			
				return;
			}
		}
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
		main.closeScene();
		//TODO what happens on game over?
	}

	@Override
	public void mousePressed(int key, int mouse_x, int mouse_y) {

		
	}

	@Override
	public void mouseReleased(int key, int mouse_x, int mouse_y) {
	
		
	}
}