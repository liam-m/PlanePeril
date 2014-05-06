package scn;

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
import lib.RandomNumber;
import lib.SpriteAnimation;
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
	
	private int AIRPORT_X_OFFSET = 64; // Used to centre airports horizontally within region
	private int AIRPORT_Y_OFFSET = 32; // Used to centre airports vertically within region
	public Airport left_airport = new Airport(window.getWidth()/4-AIRPORT_X_OFFSET, window.getHeight()/2-AIRPORT_Y_OFFSET, "Airport");
	public Airport right_airport = new Airport((window.getWidth()*3)/4-AIRPORT_X_OFFSET, window.getHeight()/2-AIRPORT_Y_OFFSET, "Airport");
	
	public Waypoint[] left_waypoints;
	public Waypoint[] right_waypoints;
	HoldingWaypoint[] left_airport_waypoints, right_airport_waypoints;
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
	
	Image background, explosion, left_channel_image, right_channel_image;
	
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
	
	private SpriteAnimation my_explosion_animation, their_explosion_animation, left_channel, right_channel;
	
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
			new Waypoint(8, 8, WaypointType.ENTRY_EXIT, "Top Left"),
			new Waypoint((window.getWidth()/4), 8, WaypointType.ENTRY_EXIT, "Top Middle"),
			new Waypoint((window.getWidth()/2 - 20), 8, WaypointType.ENTRY_EXIT, "Top Right"),

			// bottom 3 entry/exit points
			new Waypoint(8, window.getHeight() - 190, WaypointType.ENTRY_EXIT, "Bottom Left"),
			new Waypoint((window.getWidth()/4), window.getHeight() - 190, WaypointType.ENTRY_EXIT, "Bottom Middle"),
			new Waypoint((window.getWidth()/2 - 20), window.getHeight() - 190, WaypointType.ENTRY_EXIT, "Bottom Right"),
			
			// Hand over points
			new Waypoint((window.getWidth()/2 - 20), window.getHeight()/3, WaypointType.ENTRY_EXIT, "Top Handover"),
			new Waypoint((window.getWidth()/2 - 20), (window.getHeight() *2)/3, WaypointType.ENTRY_EXIT, "Bottom Handover"),
			
			// Airport
			left_airport
	};
	
	public final Waypoint[] right_entryexit_waypoints = new Waypoint[] {
			/* A set of Waypoints which are origin / destination points */

			// top 3 entry/exit points
			new Waypoint((window.getWidth()/2 + 20), 8, WaypointType.ENTRY_EXIT, "Top Left"),
			new Waypoint(((window.getWidth()* 3)/4), 8, WaypointType.ENTRY_EXIT, "Top Middle"),
			new Waypoint(window.getWidth() - 40, 8, WaypointType.ENTRY_EXIT, "Top Right"),

			// bottom 3 entry/exit points
			new Waypoint((window.getWidth()/2 + 20), window.getHeight() - 190, WaypointType.ENTRY_EXIT, "Bottom Left"),
			new Waypoint(((window.getWidth()* 3)/4), window.getHeight() - 190, WaypointType.ENTRY_EXIT, "Bottom Middle"),
			new Waypoint(window.getWidth() - 40, window.getHeight() - 190, WaypointType.ENTRY_EXIT, "Bottom Right"),
			
			// Hand over points
			new Waypoint((window.getWidth()/2 + 20), window.getHeight()/3, WaypointType.ENTRY_EXIT, "Top Handover"),
			new Waypoint((window.getWidth()/2 + 20), (window.getHeight() *2)/3, WaypointType.ENTRY_EXIT, "Bottom Handover"),
						
			// Airport
			right_airport
	 };
	
	public ArrayList<HoldingWaypoint> left_holding_waypoints = new ArrayList<HoldingWaypoint>();

	public ArrayList<HoldingWaypoint> right_holding_waypoints = new ArrayList<HoldingWaypoint>();
	
	Waypoint my_outgoing_hand_over_point;
	Waypoint my_incoming_hand_over_point;
	
	public Multiplayer(Main main, String left_name, String right_name, String their_address, final boolean is_left) {
		super(main);
		this.left_name = left_name;
		this.right_name = right_name;
		this.background = graphics.newImage("gfx" + File.separator + "map.png");
		this.their_address = their_address;
		this.is_left_player = is_left;
		this.my_outgoing_hand_over_point = is_left ? left_entryexit_waypoints[6] : right_entryexit_waypoints[7];
		this.my_incoming_hand_over_point = is_left ? left_entryexit_waypoints[7] : right_entryexit_waypoints[6];
		
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

		timer = 0;
		compass_dragged = false;
		selected_aircraft = null;
		selected_waypoint = null;
		selected_pathpoint = -1;
		
		aircraft_image = graphics.newImage("gfx" + File.separator + "plane.png");
		explosion = graphics.newImage("gfx" + File.separator + "explosionFrames.png");
		left_channel_image = graphics.newImage("gfx" + File.separator + "ChevronFramesFlipped.png");
		right_channel_image = graphics.newImage("gfx" + File.separator + "ChevronFrames.png");
		left_channel = new SpriteAnimation(left_channel_image, (int)left_entryexit_waypoints[7].position().x(), (int)left_entryexit_waypoints[7].position().y() - 8, 2, 15, 15, 2, true); // amount of frames in each picture
		right_channel = new SpriteAnimation(right_channel_image, (int)left_entryexit_waypoints[6].position().x(), (int)left_entryexit_waypoints[6].position().y()- 8, 2, 15, 15, 2, true); // amount of frames in each picture

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
				orders_box.addOrder("<<< " + my_airport.getName() + " is full, divert aircraft Comrade!");
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
				orders_box.addOrder("<<< Aircraft " + aircraft.get(i).getName() + " has landed safely at " + my_airport.getName());
			}
			// if aircraft has completed its journey correctly or crashed
			if (aircraft.get(i).hasFinished()) {
				if (!aircraft.get(i).hasCrashed()) {
					if (isMine(aircraft.get(i))) {
						updatePerformance(5);
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
				} else {
					orders_box.addOrder("<<< MAYDAY MAYDAY WE ARE GOING DOWN!!");
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
			if (selected_aircraft.isManuallyControlled() && isInMyHalf(selected_aircraft)) { // Remove control if aircraft flies into other half
				toggleMyManualControl();
			}
			
			try {
				if (input.isKeyDown(input.KEY_LEFT) || input.isKeyDown(input.KEY_A)) {
					if (selected_aircraft.isManuallyControlled() || toggleMyManualControl()) { // Already manually controlled or you make it manually controlled
						selected_aircraft.turnLeft(dt);
						server.sendTurnLeft(dt);
					}
				} else if (input.isKeyDown(input.KEY_RIGHT) || input.isKeyDown(input.KEY_D)) {
					if (selected_aircraft.isManuallyControlled() || toggleMyManualControl()) {
						selected_aircraft.turnRight(dt);
						server.sendTurnRight(dt);
					}
				}
			} catch (RemoteException e) {
				connectionLost();
			}

			if (selected_aircraft.isOutOfBounds()) {
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
		
		// Update animation
		if (my_explosion_animation != null) {
			if (!my_explosion_animation.hasFinished()) { 
				my_explosion_animation.update(dt);
			} else {
				my_explosion_animation = null;
			}
		}
		
		if (their_explosion_animation != null) {
			if (!their_explosion_animation.hasFinished()) { 
				their_explosion_animation.update(dt);
			} else {
				their_explosion_animation = null;
			}
		}
		
		if (left_channel.hasFinished()) {
			left_channel = new SpriteAnimation(left_channel_image, (int)left_entryexit_waypoints[7].position().x(), (int)left_entryexit_waypoints[7].position().y(), 6, 16, 8, 16, true);
		} else {
			left_channel.update(dt);			
		}
		
		if (right_channel.hasFinished()) {
			right_channel = new SpriteAnimation(right_channel_image, (int)left_entryexit_waypoints[6].position().x(), (int)left_entryexit_waypoints[6].position().y(), 6, 16, 8, 16, true);
		} else {
			right_channel.update(dt);			
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
	 * Handle mouse input
	 */
	@Override
	public void mousePressed(int key, int x, int y) {
		// Must be before offsets are applied as outside the viewport
		altimeter.mousePressed(key, x, y);
		airport_control_box.mousePressed(key, x, y);
		my_airport.mousePressed(key, x, y);
		
		// Apply offsets so we don't need to keep writing x-Main.VIEWPORT_OFFSET_X
		x -= Main.VIEWPORT_OFFSET_X;
		y -= Main.VIEWPORT_OFFSET_Y;		
		
		if (key == input.MOUSE_LEFT) {
			// If clicked on an aircraft, set it as selected
			for (Aircraft a : aircraft) {
				if (a.isMouseOver(x, y) && isMine(a)) {
					deselectAircraft();
					selected_aircraft = a;
					try {
						server.sendSelected(selected_aircraft.getName());
					} catch (RemoteException e) {
						connectionLost();
					}
					break;
				}
			}
			altimeter.show(selected_aircraft);
			
			// Things that require an aircraft to be selected
			if (selected_aircraft != null) {
				for (Waypoint w : my_waypoints) {
					if (w.isMouseOver(x, y) && selected_aircraft.indexInFlightPath(w) > -1) {
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
				}
				
				if (selected_aircraft.getFlightPlan().getDestination().equals(my_outgoing_hand_over_point) && 
						my_outgoing_hand_over_point.isMouseOver(x, y)) {
					selected_aircraft.handOver();
					deselectAircraft();
					return;
				}
				
				if (!selected_aircraft.isLanding()) {
					if (my_airport.is_arrivals_clicked && selected_aircraft.getCurrentTarget() instanceof HoldingWaypoint) {
						Waypoint landing_waypoint = is_left_player ? left_holding_waypoints.get(0) : right_holding_waypoints.get(0);
						selected_aircraft.toggleLand(landing_waypoint);
					}
				}
			}
			
			if (my_airport.is_departures_clicked) {
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
		} else if (key == input.MOUSE_RIGHT) {
			deselectAircraft();
		}
	}
	
	@Override
	public void mouseReleased(int key, int x, int y) {
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
				server.sendAddAircraft(from_airport, a.getName(), (int)(a.getInitialSpeed()), origin_index, destination_index, a.getTargetAltitudeIndex(), a.isWaitingToBeHanded()); 
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
		
		for (Waypoint entry_point : my_entryexit_waypoints) {
			// Skip the "air channel" entry point  
			if (entry_point.equals(my_incoming_hand_over_point) || entry_point.equals(my_outgoing_hand_over_point)) {
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
			server.sendHandOver(aircraft.getName());
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
				origin_point = my_incoming_hand_over_point;
				while (my_entryexit_waypoints[destination].equals(my_outgoing_hand_over_point)) { // Prevent a plane that is handed over from being handed over again. It wouldn't really make sense to pass planes back and forth
					destination = RandomNumber.randInclusiveInt(0, my_entryexit_waypoints.length - 1);
				}
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
				my_entryexit_waypoints[destination].equals(my_incoming_hand_over_point)) {			
			destination = RandomNumber.randInclusiveInt(0, my_entryexit_waypoints.length - 1);
		}
		
		Waypoint destination_point = my_entryexit_waypoints[destination];
		
		boolean towards_handover_point = destination_point.equals(left_entryexit_waypoints[6]) || destination_point.equals(right_entryexit_waypoints[7]);

		String name = "";
		do { // Find a unique name
			name = "Flight " + RandomNumber.randInclusiveInt(100, 999);
		} while (nameTaken(name));
		
		if (is_left_player) {
			return new Aircraft(name, aircraft_image, 32 + (int) (10 * Math.random()), 1, new FlightPlan(origin_point, 
					destination_point, left_waypoints, left_holding_waypoints, left_airport_takeoff_waypoint), preferred_altitude_index, towards_handover_point);
		} else {
			return new Aircraft(name, aircraft_image, 32 + (int) (10 * Math.random()), 1, new FlightPlan(origin_point, 
					destination_point, right_waypoints, right_holding_waypoints, right_airport_takeoff_waypoint), preferred_altitude_index, towards_handover_point);
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
		
		// Draw effects
		graphics.setColour(128, 0, 0);
		
		if (my_explosion_animation != null) {
			my_explosion_animation.draw();
		}
		
		if (their_explosion_animation != null) {
			their_explosion_animation.draw();
		}

		graphics.setColour(Main.BLUE);
		if (is_left_player) {
			right_channel.draw();
			graphics.setColour((hand_over_aircraft_waiting > 0) ? Main.LIGHT_BLUE : Main.BLUE);
			left_channel.draw();
		} else {
			left_channel.draw();
			graphics.setColour((hand_over_aircraft_waiting > 0) ? Main.LIGHT_BLUE : Main.BLUE);
			right_channel.draw();
		}
	
		// Draw aircraft
		graphics.setColour(256, 256, 256, 128);
		
		for (Aircraft a : aircraft) {
			graphics.setColour(isMine(a) ? Main.GREEN : Main.GREY);
			a.draw();
			if (a.isMouseOver(input.getMouseX() - Main.VIEWPORT_OFFSET_X, input.getMouseY() - Main.VIEWPORT_OFFSET_Y)) {
				a.drawFlightPath(false);
			}
		}
		
		if (selected_aircraft != null) {
			selected_aircraft.drawFlightPath(true);
			graphics.setColour(Main.GREEN);
		}	
		
		if (selected_waypoint != null && selected_aircraft.isManuallyControlled() == false) {
			selected_aircraft.drawModifiedPath(selected_pathpoint, input.getMouseX() - Main.VIEWPORT_OFFSET_X, input.getMouseY() - Main.VIEWPORT_OFFSET_Y);
		}
		
		// Draw circles around handover waypoint to indicate the user should click there
		if (selected_aircraft != null && selected_aircraft.isWaitingToBeHanded() && selected_aircraft.getCurrentTarget().equals(selected_aircraft.getFlightPlan().getDestination())) {
			graphics.setColour(128, 0, 0, 128);
			graphics.circle(false, my_outgoing_hand_over_point.position().x(), my_outgoing_hand_over_point.position().y(), 20);
			graphics.circle(false, my_outgoing_hand_over_point.position().x(), my_outgoing_hand_over_point.position().y(), 30);
			graphics.setColour(Main.GREEN);
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
	 * @return If the manual control toggle was successful (allowed)
	 */
	private boolean toggleMyManualControl() {
		if (selected_aircraft == null)
			return false;

		if (isInMyHalf(selected_aircraft) && !is_manually_controlling) { // Don't allow player to manually control aircraft not in their half
				return false;
		}
		
		is_manually_controlling = !is_manually_controlling;
		selected_aircraft.toggleManualControl();
		try {
			server.sendToggleManualControl();
		} catch (RemoteException e) {
			connectionLost();
		}
		return true;
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
	 * Cause all planes in airspace to update collisions Catch and handle a
	 * resultant game over state
	 * 
	 * @param dt
	 *            delta time since last collision check
	 */
	public void checkCollisions(double dt) {
		for (Aircraft plane : aircraft) {
				int collision_state = plane.updateCollisions(dt, aircraft);
				int x = (int)plane.getPosition().x() - Main.VIEWPORT_OFFSET_X;
				int y = (int)plane.getPosition().y() - Main.VIEWPORT_OFFSET_Y;

				if (isMine(plane)) {
					if (collision_state >= 0) {
						loseALife();
						my_explosion_animation = new SpriteAnimation(explosion, x, y, 6, 16, 8, 4, false);
					}
				} else if (collision_state >= 0) {
					their_explosion_animation = new SpriteAnimation(explosion, x, y, 6, 16, 8, 4, false);
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
	
	private void connectionLost() {
		main.closeScene();
		main.setScene(new ConnectionLost(main));
	}
	
	private boolean isInMyHalf(Aircraft aircraft) {
		return is_left_player ? aircraft.getPosition().x() > window.getWidth()/2 : aircraft.getPosition().x() < window.getWidth()/2;
	}
}