package scn;

import java.awt.Color;
import java.io.File;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import cls.Aircraft;
import cls.Airport;
import cls.AirportControlBox;
import cls.Altimeter;
import cls.FlightPlan;
import cls.Lives;
import cls.OrdersBox;
import cls.HoldingWaypoint;
import cls.PerformanceBar;
import cls.Waypoint;
import cls.Waypoint.WaypointType;
import pp.Main;
import rem.MultiplayerServer;
import lib.ButtonText;
import lib.RandomNumber;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.graphics.Image;
import lib.jog.window;

public class Multiplayer extends Scene {
	private static final double DEDUCTION_TIME_DELAY = 0.5;
	final int MAX_AIRCRAFT = 20;
	final int TAKEOFF_DELAY = 5;
	
	String left_name, right_name;
	
	boolean is_left_player;
	public int hand_over_aircraft_waiting = 0;
	
	public Waypoint[] left_waypoints;
	public Waypoint[] right_waypoints;
	HoldingWaypoint[] left_airport_waypoints, right_airport_waypoints;
	public Airport left_airport = new Airport(window.getWidth()/4, window.getHeight()/2, "Aerodromio Leftved'");
	public Airport right_airport = new Airport((window.getWidth()*3)/4 + 100, window.getHeight()/2, "Aerodromio Rightved'");
	public Waypoint left_airport_takeoff_waypoint = new Waypoint(left_airport.position().x() - 120, left_airport.position().y());
	public Waypoint right_airport_takeoff_waypoint = new Waypoint(right_airport.position().x() - 120, right_airport.position().y());
	
	public ArrayList<Aircraft> aircraft = new ArrayList<Aircraft>();
	public Aircraft selected_aircraft;
	public Aircraft their_selected;
	
	public PerformanceBar left_performance;
	public PerformanceBar right_performance;
	public Lives left_lives;
	public Lives right_lives;
	
	OrdersBox orders_box;
	AirportControlBox airport_control_box;
	Altimeter altimeter;
	
	Image background;
	
	double timer = 0;
	double next_take_off = TAKEOFF_DELAY;
	
	boolean is_manually_controlling;
	boolean compass_dragged;
	Waypoint selected_waypoint;
	int selected_pathpoint;
	public Image aircraft_image;
	private double time_of_last_deduction = 0;
	
	double flight_generation_time_elapsed = 6;
	double flight_generation_interval = 4;
	double last_penalty_time= 0;
	
	MultiplayerServer server;
	String their_address;
	
	private Airport my_airport;
	private PerformanceBar my_performance;
	private Waypoint[] my_waypoints;
	private Lives my_lives;
	private Waypoint[] my_entryexit_waypoints;
	
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
	
	private final int LEFT_PERFOMANCE_X = 110;
	private final int LEFT_PERFOMANCE_Y = 8;
		
	private final int RIGHT_PERFOMANCE_X = window.getWidth() - LEFT_PERFOMANCE_X - PerformanceBar.getWidth(); 
	private final int RIGHT_PERFOMANCE_Y = 8;
		
	private final int LEFT_LIVES_X = window.getWidth()/2 - 100;
	private final int LEFT_LIVES_Y = 8;
		
	private final int RIGHT_LIVES_X = window.getWidth()/2 + 50;
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
			
			// Hand over points
			new Waypoint((window.getWidth()/2 - 20), window.getHeight()/3, WaypointType.ENTRY_EXIT),
			new Waypoint((window.getWidth()/2 - 20), (window.getHeight() *2)/3, WaypointType.ENTRY_EXIT),
			
			// Airport
			left_airport
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
			
			// Hand over points
			new Waypoint((window.getWidth()/2 + 20), window.getHeight()/3, WaypointType.ENTRY_EXIT),
			new Waypoint((window.getWidth()/2 + 20), (window.getHeight() *2)/3, WaypointType.ENTRY_EXIT),
						
			// Airport
			right_airport
	 };
	
	public ArrayList<HoldingWaypoint> left_holding_waypoints = new ArrayList<HoldingWaypoint>();

	public ArrayList<HoldingWaypoint> right_holding_waypoints = new ArrayList<HoldingWaypoint>();
	
	public Multiplayer(Main main, String left_name, String right_name, String their_address, final boolean is_left) {
		super(main);
		this.left_name = left_name;
		this.right_name = right_name;
		this.background = graphics.newImage("gfx" + File.separator + "map.png");
		this.their_address = their_address;
		this.is_left_player = is_left;
		
		left_waypoints = new Waypoint[]{
			new Waypoint(100, 100),
			new Waypoint(100, 300),
			new Waypoint(100, 500),
			new Waypoint(400, 100),
			new Waypoint(400, 300),
			new Waypoint(400, 500),
			
			left_entryexit_waypoints[0],
			left_entryexit_waypoints[1],
			left_entryexit_waypoints[2],
			left_entryexit_waypoints[3],
			left_entryexit_waypoints[4],
			left_entryexit_waypoints[5],
			left_entryexit_waypoints[6],
			left_entryexit_waypoints[7],
			left_airport
		};
		right_waypoints = new Waypoint[]{
			new Waypoint((window.getWidth()/2) + 200, 100),
			new Waypoint((window.getWidth()/2) + 200, 300),
			new Waypoint((window.getWidth()/2) + 200, 500),
			new Waypoint((window.getWidth()/2) + 500, 100),
			new Waypoint((window.getWidth()/2) + 500, 300),
			new Waypoint((window.getWidth()/2) + 500, 500),
				
			right_entryexit_waypoints[0],
			right_entryexit_waypoints[1],
			right_entryexit_waypoints[2],
			right_entryexit_waypoints[3],
			right_entryexit_waypoints[4],
			right_entryexit_waypoints[5],
			right_entryexit_waypoints[6],
			right_entryexit_waypoints[7],
			right_airport
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
		
		if (is_left_player) {
			my_airport = left_airport;
			my_performance = left_performance;
			my_waypoints = left_waypoints;
			my_lives = left_lives;
			my_entryexit_waypoints = left_entryexit_waypoints;
		} else {
			my_airport = right_airport;
			my_performance = right_performance;
			my_waypoints = right_waypoints;
			my_lives = right_lives;
			my_entryexit_waypoints = right_entryexit_waypoints;
		}
		
		airport_control_box = new AirportControlBox(AIRPORT_CONTROL_BOX_X, AIRPORT_CONTROL_BOX_Y, AIRPORT_CONTROL_BOX_W, AIRPORT_CONTROL_BOX_H, my_airport);
		
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
				if(is_left_player) {
					toggleLand(left_holding_waypoints.get(0));
				} else {
					toggleLand(right_holding_waypoints.get(0));
				}
				
			}
		};
		
		int x_pos = is_left_player ? window.getWidth()/4 : (window.getWidth()*3)/4; // vary where the land button appears for each player

		timer = 0;
		compass_dragged = false;
		selected_aircraft = null;
		selected_waypoint = null;
		selected_pathpoint = -1;
		
		aircraft_image = graphics.newImage("gfx" + File.separator + "plane.png");
	}
	
	@Override
	public void start() {
		String my_reg, their_reg;
		int my_port, their_port;
		if (is_left_player) {
			my_reg = "player_1";
			their_reg = "player_2";
			my_port = 1730;
			their_port = 1731;
		} else {
			my_reg = "player_2";
			their_reg = "player_1";
			my_port = 1731;
			their_port = 1730;
		}
		try {
			server = new MultiplayerServer(this, is_left_player, their_address, my_reg, my_port, their_port);
			try {
				Thread.sleep(3000); // Wait for opponent's server to be ready, 3000 is probably excessive
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			server.connect(their_reg);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
			connectionLost();
		}
	}
	
	/**
	 * Update all objects within the scene, ie aircraft, orders box altimeter.
	 * Cause collision detection to occur Generate a new flight if flight
	 * generation interval has been exceeded.
	 */
	@Override
	public void update(double dt) {
		timer += dt;
		orders_box.update(dt);

		// update airports
		my_airport.setTimeLeft((int) (next_take_off - timer));
		my_airport.update(dt, aircraft, is_left_player);
		
		for (int i=0; i<aircraft.size(); i++) {
			// Added a try/catch construct to make sure we catch when the
			// aircraft is inserted into a full airport
			try {
				aircraft.get(i).update(dt);
			} catch (IllegalStateException e) {
				orders_box.addOrder("<<< Aerodromio Medved' is full, divert aircraft Comrade!");
				return;
			}
			
			if (isMine(aircraft.get(i))) {
				if (!aircraft.get(i).planes_too_near.isEmpty()) {
					if(timer - time_of_last_deduction >= DEDUCTION_TIME_DELAY) {
						time_of_last_deduction = timer;
						updatePerformance(-10);
					}
				}
			}
			
			// if aircraft landed
			if (aircraft.get(i).isAtAirport()) {
				orders_box.addOrder("<<< Aircraft " + aircraft.get(i).getName() + " has landed safely at " + left_airport.getName());
			}
			// if aircraft has completed it's journey correctly
			if (aircraft.get(i).hasFinished()) {
				if (isMine(aircraft.get(i))) {
					updatePerformance(5);
					Waypoint my_outgoing_hand_over_point = is_left_player ? left_entryexit_waypoints[6] : right_entryexit_waypoints[7];
					if (aircraft.get(i).getFlightPlan().getDestination().equals(my_outgoing_hand_over_point)) {
						handOver(aircraft.get(i));
					} else {
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
				if (aircraft.get(i).equals(selected_aircraft)) {
					deselectAircraft();
				}
				// make sure you notify the server of which aircraft is to be removed BEFORE it is removed
				try {
					server.sendRemoveAircraft(aircraft.get(i).getName());
				} catch (RemoteException e) {
					connectionLost();
				}
				aircraft.remove(i);
				i--; // Removed one as want to have same index next time through loop
			}
		}

		checkCollisions(dt);

		altimeter.update(dt);

		if (selected_aircraft != null) {
			try {
				if (input.isKeyDown(input.KEY_LEFT) || input.isKeyDown(input.KEY_A)) {
					if (!selected_aircraft.isManuallyControlled()) {
						toggleMyManualControl();
					}
					selected_aircraft.turnLeft(dt);
					server.sendTurnLeft(dt);
				} else if (input.isKeyDown(input.KEY_RIGHT) || input.isKeyDown(input.KEY_D)) {
					if (!selected_aircraft.isManuallyControlled()) {
						toggleMyManualControl();
					}
					selected_aircraft.turnRight(dt);
					server.sendTurnRight(dt);
				}
			} catch (RemoteException e) {
				connectionLost();
			}

			// allows to take control by just pressing left/right or A/D
			//selected_aircraft.setManualControl(is_manually_controlling); // Think this is redundant 

			if (isOutOfBounds(selected_aircraft)) {
				//TODO update this order to something witty
				orders_box.addOrder(">>> " + selected_aircraft.getName() + " is out of bounds, contact lost. Do better Comrade.");
				deselectAircraft();
			}

		}

		flight_generation_time_elapsed += dt;
		if (flight_generation_time_elapsed >= flight_generation_interval) {
			flight_generation_time_elapsed -= flight_generation_interval;
			if (aircraft.size() < MAX_AIRCRAFT) {
				if (0 == RandomNumber.randInclusiveInt(0, 9)) { // 1 in 10 chance of spawning at the airport
					my_airport.insertAircraft(createAircraft(false));
				} else {
					generateFlight(false);
				}
			}
		}
		
		if (my_performance.isEmpty()) {
			loseALife();
			my_performance.setToMax();
			updatePerformance(my_performance.getCurrentValue()); 
		}
		
		// Update from server
		for(Aircraft a : server.aircraft_queue) {
			aircraft.add(a);
		}
		server.aircraft_queue.clear();
		
		for (Aircraft a : server.removal_queue) {
			for (int i = 0; i < aircraft.size(); i++) {
				if (a.equals(aircraft.get(i))) {
					aircraft.remove(i);
					break;
				}
			}
		}
		server.removal_queue.clear();
		applyTakeOffPenalty();
	}
	
	
	public void applyTakeOffPenalty () {
		if ((timer - last_penalty_time > 1) && (my_airport.shouldPenalize())) {
			updatePerformance(-1);
			last_penalty_time = timer;
		}		
	}

	/**
	 * Input is invalid if it is both on the wrong side of the screen AND input is above the bottom of the airspace
	 * 
	 * @param x position
	 * @param y position
	 * @return
	 */
	public boolean isInputValid(int x, int y) {
		return (is_left_player ? x <= window.getWidth()/2 : x >= window.getWidth()/2) // Clicked on player's half of the screen
				||  y > Y_POSITION_OF_BOTTOM_ELEMENTS; // Clicked on bottom buttons
	}
	
	/**
	 * Handle mouse input
	 */
	@Override
	public void mousePressed(int key, int x, int y) {
		if (!isInputValid(x, y)) {	
			return;
		} 
		
		if (key == input.MOUSE_LEFT) {
			Aircraft new_selected = selected_aircraft;

			for (Aircraft a : aircraft) {
				if (a.isMouseOver(x - Main.VIEWPORT_OFFSET_X, y - Main.VIEWPORT_OFFSET_Y)) {
					new_selected = a;
				}
			}

			if (new_selected != selected_aircraft) {
				deselectAircraft();
				selected_aircraft = new_selected;
				try {
					server.sendSelected(selected_aircraft.getName());
				} catch (RemoteException e) {
					connectionLost();
				}
			}

			altimeter.show(selected_aircraft);
			
			if (selected_aircraft != null) {
				for (Waypoint w : my_waypoints) {
					if (w.isMouseOver(x - Main.VIEWPORT_OFFSET_X, y - Main.VIEWPORT_OFFSET_Y) && selected_aircraft.indexInFlightPath(w) > -1) {
						selected_waypoint = w;
						selected_pathpoint = selected_aircraft.indexInFlightPath(w);
					}
				}

				if (selected_waypoint == null && selected_aircraft.isManuallyControlled()) {
					// If mouse is over compass
					double dx = selected_aircraft.getPosition().x() - input.getMouseX();
					double dy = selected_aircraft.getPosition().y() - input.getMouseY();
					int r = Aircraft.COMPASS_RADIUS;
					if (dx * dx + dy * dy < r * r) {
						compass_dragged = true;
					}
					
					// bar
				}
			}
		}

		if (key == input.MOUSE_RIGHT)
			deselectAircraft();

		altimeter.mousePressed(key, x, y);
		airport_control_box.mousePressed(key, x, y);
		
		// handle airport input
		my_airport.mousePressed(key, x, y);
		
		if (selected_aircraft != null) {
			if (my_airport.is_arrivals_clicked) {
				if (is_left_player) {
					selected_aircraft.toggleLand(left_holding_waypoints.get(0));
				} else {
					selected_aircraft.toggleLand(right_holding_waypoints.get(0));
				}
			} else if (my_airport.is_departures_clicked) {
				// must wait at least 5 seconds between aircraft takeoff
				if (next_take_off - timer <= 0) {
					try {
						my_airport.takeoff();
						generateFlight(true);
						next_take_off = timer + TAKEOFF_DELAY;
						airport_control_box.signal_take_off = false;
					} catch (IllegalStateException e) {
						orders_box.addOrder("<<< There are no aircraft in the airport, Comrade.");
					}
				}
			}
		}
	}
	
	@Override
	public void mouseReleased(int key, int x, int y) {
		if (!isInputValid(x, y)) {
			if (selected_waypoint != null && key == input.MOUSE_LEFT) {
				selected_waypoint = null;
			}
			return;
		}
		
		if (selected_aircraft != null) //TODO landing
		

		airport_control_box.mouseReleased(key, x, y);
		if (key == input.MOUSE_LEFT && airport_control_box.signal_take_off) {
			// must wait at least 5 seconds between aircraft takeoff
			if (next_take_off - timer <= 0) {
				try {
					my_airport.takeoff();
					generateFlight(true);
					next_take_off = timer + TAKEOFF_DELAY;
					airport_control_box.signal_take_off = false;
				} catch (IllegalStateException e) {
					orders_box.addOrder("<<< There are no aircraft in the airport, Comrade.");
				}
			}
		}
		
		if (key == input.MOUSE_LEFT && selected_waypoint != null) {
			if (selected_aircraft.isManuallyControlled()) {
				return;
			} else {
				selected_waypoint = null;
				for (int i = 0; i < my_waypoints.length; i++) {
					if (my_waypoints[i].isMouseOver(x - Main.VIEWPORT_OFFSET_X, y - Main.VIEWPORT_OFFSET_Y)) {
						selected_aircraft.alterPath(selected_pathpoint, my_waypoints[i]);
						try {
							server.sendAlterPath(selected_pathpoint, i);
						} catch (RemoteException e) {
							connectionLost();
						}
						orders_box.addOrder(">>> " + selected_aircraft.getName() + " please alter your course");
						orders_box.addOrder("<<< Roger that. Altering course now.");
						selected_pathpoint = -1;
						break;
					}
				}

			}
		}

		altimeter.mouseReleased(key, x, y);
		my_airport.mouseReleased(key, x, y);
		
		if (compass_dragged && selected_aircraft != null) {
			double dx = input.getMouseX() - selected_aircraft.getPosition().x();
			double dy = input.getMouseY() - selected_aircraft.getPosition().y();
			double new_heading = Math.atan2(dy, dx);

			selected_aircraft.setBearing(new_heading);
		}

		compass_dragged = false;
	}
	

	public void generateFlight(boolean from_airport) {
		Aircraft a = createAircraft(from_airport);
		if (a != null) {
			// Add aircraft to self
			if (from_airport) {
				// start at altitude 100 and increase to next step
				a.setAltitude(100);
				a.increaseTargetAltitude(); 
			}

			orders_box.addOrder("<<< " + a.getName() + " incoming from " + a.getFlightPlan().getOriginName() + " heading towards " + a.getFlightPlan().getDestinationName() + ".");
			aircraft.add(a);
			
			int origin_index = 0;
			int destination_index = 0;
			
			for (int i = 0; i < my_entryexit_waypoints.length; i++) {
				if (a.getFlightPlan().getOrigin().equals(my_entryexit_waypoints[i])) {
						origin_index = i;
				}
				if (a.getFlightPlan().getDestination().equals(my_entryexit_waypoints[i])) {
						destination_index = i;
				}
			}
			
			// Send to other player
			try {
				server.sendAddAircraft(from_airport, a.getName(), (int)(a.getInitialSpeed()), origin_index, destination_index, a.getTargetAltitudeIndex() ); 
			} catch (RemoteException e) {
				connectionLost();
			}
		}	
	}
	
	/**
 	 * List that helps to prevent spawning a plane in waypoint if any plane is currently going towards it 
 	 * or any plane is less than 300 from it.
 	 */
	private ArrayList<Waypoint> getAvailableEntryPoints() {
		ArrayList<Waypoint> available_entry_points = new ArrayList<Waypoint>();
		Waypoint my_incoming_hand_over = is_left_player ? left_entryexit_waypoints[7] : right_entryexit_waypoints[6];	
		Waypoint my_outgoing_hand_over = is_left_player ? left_entryexit_waypoints[6] : right_entryexit_waypoints[7];	
		
		for (Waypoint entry_point : my_entryexit_waypoints) {
			// Skip the "air channel" entry point  
			if (entry_point.equals(my_incoming_hand_over) || entry_point.equals(my_outgoing_hand_over)) {
				continue;
			}
			boolean is_available = true;
			for (Aircraft a : aircraft) {
				// Check if any plane is currently going towards the exit point/chosen originPoint
				// Check if any plane is less than what is defined as too close from the chosen originPoint
				if (a.getCurrentTarget().equals(entry_point.position()) || a.isCloseToEntry(entry_point.position())) {
					is_available = false;
					break;
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
	private ArrayList<Integer> getIdAvailableEntryPointsAltitudes() {
		ArrayList<Integer> available_id_entry_points_altitudes = new ArrayList<Integer>();
		
		int base_id = -1;
		// checks all waypoints and all altitudes
		for (Waypoint entry_point : my_entryexit_waypoints) {
			base_id++;
			if (entry_point != my_airport) { // Airport is excluded as it is said initially whether it should come from airport or elsewhere
				for (int i = 1; i < Aircraft.altitude_list.size(); i++) { // Starts from 1 as the first in the altitude_list is 100 which is not considered
					boolean is_available = true;
					for (Aircraft a : aircraft) {
						// Check if any plane is currently going towards the exit point/chosen originPoint
						// Check if any plane's altitude is less than 300 different from altitude from altitude_list currently being checked and close to entry point at the same time 
						if (a.getCurrentTarget().equals(entry_point.position()) ||
								((Math.abs(a.getPosition().z() - Aircraft.altitude_list.get(i)) < 1000) && a.isCloseToEntry(entry_point.position()))) {
							is_available = false;
							break;
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
	
	private void handOver(Aircraft aircraft) {
		orders_box.addOrder("<<< Handing " + aircraft.getName() + " over to your opponent's airspace");
			
		// Send to other player
		try {
			server.sendHandOver();
		} catch (RemoteException e) {
			connectionLost();
		}
	}

	/**
	 * A system of creating planes designed to be fair for the player. Planes are created either from airport or elsewhere based on parameter. 
	 * Also planes are primarily created from waypoints with no planes near them. Secondarily, planes are created in entry points such that no plane is near the altitude 
	 * that a newly created plane has. If it cannot create a plane due to constraints, it returns null.
	 * @return the create aircraft object
	 */
	private Aircraft createAircraft(boolean fromAirport) {
		int preferred_altitude_index = -1;
		int destination = RandomNumber.randInclusiveInt(0, my_entryexit_waypoints.length - 1);
		int origin = 0; // 0 is default, it is chosen later on (initialized as compiler would otherwise complain)
		Waypoint origin_point; 
		ArrayList<Waypoint> available_origins = getAvailableEntryPoints();
		
		if (fromAirport) {
			origin_point = my_airport;		
		} else if (hand_over_aircraft_waiting > 0) {
				hand_over_aircraft_waiting--;
				origin_point= is_left_player ? left_entryexit_waypoints[7] : right_entryexit_waypoints[6];
		} else {
			if (available_origins.isEmpty()) { // Creates a plane in waypoint with planes of different altitude than that of the new plane.
				if (getIdAvailableEntryPointsAltitudes().size() == 0)
					return null;
				ArrayList<Integer> available_id_entry_points_altitudes = getIdAvailableEntryPointsAltitudes();
				int id = available_id_entry_points_altitudes.get(RandomNumber.randInclusiveInt(0, available_id_entry_points_altitudes.size()-1));
				origin = id / 3; // Calculates id of a waypoint
				origin_point = my_entryexit_waypoints[origin]; 
				preferred_altitude_index = id % 3 + 1; // Calculates id for altitude_list (1 is added as the first item on the list is solely for creating from airports)
			} else { // Creates a plane in waypoint with no planes nearby
				origin_point = available_origins.get(RandomNumber.randInclusiveInt(0, available_origins.size()-1)); 
				for (int i = 0; i < my_entryexit_waypoints.length; i++) { // getting id for an entry point
					if (my_entryexit_waypoints[i].equals(origin_point)) {
						origin = i;
						break;
					}
				}
			}
		}
		
		// Making sure origin and destination aren't the same and the destination is not the incoming air channel
		while (my_entryexit_waypoints[destination].equals(my_entryexit_waypoints[origin]) || 
				fromAirport && my_entryexit_waypoints[destination] instanceof Airport ||
				my_entryexit_waypoints[destination].equals(is_left_player ? left_entryexit_waypoints[7] : right_entryexit_waypoints[6])) {
			
			destination = RandomNumber.randInclusiveInt(0, my_entryexit_waypoints.length - 1);
		}			
		
		Waypoint destination_point = my_entryexit_waypoints[destination];

		String name = "";
		do { // Find a unique name
			name = "Flight " + RandomNumber.randInclusiveInt(100, 999);
		} while (nameTaken(name));
		
		if (is_left_player) {
			return new Aircraft(name, aircraft_image, 32 + (int) (10 * Math.random()), 1, new FlightPlan(origin_point, 
					destination_point, left_waypoints, left_holding_waypoints, left_airport_takeoff_waypoint), preferred_altitude_index);
		} else {
			return new Aircraft(name, aircraft_image, 32 + (int) (10 * Math.random()), 1, new FlightPlan(origin_point, 
					destination_point, right_waypoints, right_holding_waypoints, right_airport_takeoff_waypoint), preferred_altitude_index);
		}		
	}
	
	private boolean nameTaken(String name) {
		for (Aircraft a : aircraft) {
			if (a.getName().equals(name))
				return true;
		}
		return false;
	}
	
	@Override
	public void keyPressed(int key) {
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
	}
	
	@Override
	public void keyReleased(int key) {
		try {
			switch (key) {
	
			case input.KEY_S:
			case input.KEY_DOWN:
				if (selected_aircraft != null) {
					selected_aircraft.decreaseTargetAltitude();
					server.sendChangeAltitude(false);
				}
				break;
	
			case input.KEY_W:
			case input.KEY_UP:
				if (selected_aircraft != null) {
					selected_aircraft.increaseTargetAltitude();
					server.sendChangeAltitude(true);				
				}
				break;
	
			case input.KEY_SPACE:
				toggleMyManualControl();
				break;			
			}
		} catch (RemoteException e) {
			connectionLost();
		}
	}

	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.printText(left_name, 10, 10, 2);
		graphics.printText(right_name, window.getWidth()-(right_name.length()*17), 10, 2);
		graphics.rectangle(false, Main.VIEWPORT_OFFSET_X, Main.VIEWPORT_OFFSET_Y, window.getWidth() - 32, window.getHeight() - 180);
		
		graphics.setViewport(Main.VIEWPORT_OFFSET_X, Main.VIEWPORT_OFFSET_Y, window.getWidth() - 32, window.getHeight() - 180);
		graphics.setColour(255, 255, 255, 100);
		graphics.draw(background, 0, 0);
		
		for (int i=0; i<left_waypoints.length-1; i++) { // Should be same length as right_waypoints
			left_waypoints[i].draw();
			right_waypoints[i].draw();
		}
		
		for (HoldingWaypoint l : left_holding_waypoints) {
			l.draw();
		}
		
		for (HoldingWaypoint r : right_holding_waypoints) {
			r.draw();
		}
		
		left_airport.draw();
		right_airport.draw();
		
		graphics.setColour(Main.BLUE);
		if (is_left_player) {
			graphics.rectangle(true, left_entryexit_waypoints[6].position().x(), left_entryexit_waypoints[6].position().y(), 50, 20);
			graphics.setColour((hand_over_aircraft_waiting > 0) ? Main.LIGHT_BLUE : Main.BLUE);
			graphics.rectangle(true, left_entryexit_waypoints[7].position().x(), left_entryexit_waypoints[7].position().y(), 50, 20);
		} else {
			graphics.rectangle(true, left_entryexit_waypoints[7].position().x(), left_entryexit_waypoints[7].position().y(), 50, 20);
			graphics.setColour((hand_over_aircraft_waiting > 0) ? Main.LIGHT_BLUE : Main.BLUE);
			graphics.rectangle(true, left_entryexit_waypoints[6].position().x(), left_entryexit_waypoints[6].position().y(), 50, 20);
		}
		
		graphics.setColour(256, 256, 256, 128);
		
		for (Aircraft a : aircraft) {
			if (a.getPosition().x() < window.getWidth()/2) {
				graphics.setColour(is_left_player ? Main.GREEN : Main.GREY); 
			} else {
				graphics.setColour(is_left_player ? Main.GREY : Main.GREEN);
			}
			a.draw();
			if (a.isMouseOver(input.getMouseX() - Main.VIEWPORT_OFFSET_X, input.getMouseY() - Main.VIEWPORT_OFFSET_Y)) {
				a.drawFlightPath(false);
			}
		}
		
		if (selected_aircraft != null) {
			
			selected_aircraft.drawFlightPath(true);
			graphics.setColour(Main.GREEN);
			
			// if aircraft is flying towards the airport (i.e. it's its
			// destination point, draw the land button)
			if (selected_aircraft.getFlightPlan().getDestination() instanceof Airport) {
				
			}
			
			graphics.setColour(Main.GREEN);
		}	
		
		if (selected_waypoint != null && selected_aircraft.isManuallyControlled() == false) {
			//TODO offset values should be placed into a constant
			selected_aircraft.drawModifiedPath(selected_pathpoint, input.getMouseX() - Main.VIEWPORT_OFFSET_X, input.getMouseY() - Main.VIEWPORT_OFFSET_Y);
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
		server.close();
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
	private void toggleMyManualControl() {
		if (selected_aircraft == null)
			return;

		is_manually_controlling = !is_manually_controlling;
		selected_aircraft.toggleManualControl();
		try {
			server.sendToggleManualControl();
		} catch (RemoteException e) {
			connectionLost();
		}
	}
	
	

	/**
	 * Causes an aircraft to call methods to handle deselection
	 */
	public void deselectAircraft() {
		if (selected_aircraft != null && selected_aircraft.isManuallyControlled()) {
			toggleMyManualControl();
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

				if (isMine(plane)) {
					if (collision_state >= 0) {
						//playSound(audio.newSoundEffect("sfx" + File.separator + "crash.ogg"));
						//TODO lives need to be subtracted. is it possible for both players to be in the wrong? A crash is between two or more planes! (dont decrement twice)
						loseALife();
						return;
					}
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
	public void gameOver(boolean win) {
		main.closeScene();
		//TODO what happens on game over?
		main.setScene(new GameOverMult(main, win));
	}
	
	public void loseALife() {
		my_lives.decrement();
		try {
			server.sendRemoveLife();
		} catch (RemoteException e) {
			connectionLost();
		}
		
		if (my_lives.getLives() == 0) {
			gameOver(false);
		}
	}

	public void updatePerformance(int value) {
		my_performance.changeValueBy(value);
		try {
			server.sendChangePerformance(value);
		} catch (RemoteException e) {
			connectionLost();
		}
	}

	private boolean isMine(Aircraft aircraft) {
		if (aircraft.getFlightPlan().getDestination().equals(my_airport)) {
			return true;
		}
		for (Waypoint w : my_entryexit_waypoints) {
			if (aircraft.getFlightPlan().getDestination().equals(w)) {
				return true;
			}
		}
		return false;
	}
	
	private boolean isOutOfBounds(Aircraft aircraft) {
		return aircraft.isOutOfBounds() || (is_left_player ? aircraft.getPosition().x() > window.getWidth()/2 : aircraft.getPosition().x() < window.getWidth()/2);
	}
	
	private void connectionLost() {
		main.closeScene();
		main.setScene(new ConnectionLost(main));
	}
}