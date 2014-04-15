package scn;

import java.io.File;
import java.rmi.RemoteException;
import java.util.ArrayList;

import cls.Aircraft;
import cls.Airport;
import cls.FlightPlan;
import cls.Waypoint;

import lib.RandomNumber;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.audio.Sound;
import lib.jog.graphics.Image;
import lib.jog.window;

import pp.Main;
import rem.HostServer;
import rem.JoinClient;
import rem.MultiplayerServer;
import scn.SinglePlayer.Texts;

public class MultiplayerLeft extends Multiplayer {
	MultiplayerServer server;
	String their_address;

	
	public MultiplayerLeft(Main main, String my_name, String their_name, String their_address) {
		super(main, my_name, their_name, true);
		this.their_address = their_address;
	}
	
	@Override 
	public void start() {
		try {
			server = new MultiplayerServer(this, their_address, "player_1", 1730, 1731);
			Thread.sleep(3000);
			server.connect("player_2");
		} catch (RemoteException | InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void keyPressed(int key) {
		if (key == input.KEY_T) {
			generateFlight(false);
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

		// update airport timer
		left_airport.setTimeLeft((int) (next_take_off - timer));

		int index = 0;
		for (Aircraft plane : aircraft) {

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
				orders_box.addOrder("<<< Aircraft " + plane.getName() + " has landed safely at " + left_airport.getName());
			}
			// if aircraft has completed it's journey correctly
			if (plane.hasFinished()) {
				left_performance.changeValueBy(5);
				if (aircraft.get(index).equals(selected_aircraft)) {
					deselectAircraft();
				}
				aircraft.remove(index);
				index--;
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
			index++;
		}

		checkCollisions(dt);

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

			// Check if the aircraft is out of bounds. If true, remove aircraft
			// from play.
			if (selected_aircraft.isOutOfBounds()) {
				//TODO update this order to something witty
				orders_box.addOrder(">>> " + selected_aircraft.getName() + " is out of bounds, contact lost. Do better Comrade.");
				deselectAircraft();
				is_manually_controlling = false;
			}

		}

		flight_generation_time_elapsed += dt;
		if (flight_generation_time_elapsed >= flight_generation_interval) {
			flight_generation_time_elapsed -= flight_generation_interval;
			if (aircraft.size() < MAX_AIRCRAFT) {
				generateFlight(false);
			}
		}
		
		// Update from server
		for(Aircraft a : server.aircraft_queue) {
			aircraft.add(a);
		}
	}
	
	/**
	 * Handle mouse input
	 */
	@Override
	public void mousePressed(int key, int x, int y) {
		if (x > window.getWidth()/2 && y < Y_POSITION_OF_BOTTOM_ELEMENTS) // needs to be flipped for Right
			return;
		if (key == input.MOUSE_LEFT) {

			
			Aircraft new_selected = selected_aircraft;

			for (Aircraft a : aircraft) {
				if (a.isMouseOver(x - 16, y - 16)) { // TODO these -16 could be offsets?
					new_selected = a;
				}
			}

			if (new_selected != selected_aircraft) {
				deselectAircraft();
				selected_aircraft = new_selected;
			}

			altimeter.show(selected_aircraft);

			if (selected_aircraft != null) {

				for (Waypoint w : left_waypoints) {
					if (w.isMouseOver(x - 16, y - 16) && selected_aircraft.indexInFlightPath(w) > -1) {
						selected_waypoint = w;
						selected_pathpoint = selected_aircraft.indexInFlightPath(w);
					}
				}

				if (selected_waypoint == null
						&& selected_aircraft.isManuallyControlled()) {
					// If mouse is over compass
					double dx = selected_aircraft.getPosition().x() - input.getMouseX();
					double dy = selected_aircraft.getPosition().y() - input.getMouseY();
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
	
		if (selected_aircraft != null && land_button.isMouseOver(x, y))
			land_button.act();

		if (key == input.MOUSE_LEFT && left_airport.isMouseOver(x - 16, y - 16)) {
			// must wait at least 5 seconds between aircraft takeoff
			if (next_take_off - timer <= 0) {
				try {
					left_airport.takeoff();
					generateFlight(true);
					next_take_off = timer + TAKEOFF_DELAY;
				} catch (IllegalStateException e) {
					orders_box.addOrder("<<< There are no aircraft in the airport, Comrade.");
				}
			}
		}

		if (key == input.MOUSE_LEFT && selected_waypoint != null) {

			if (selected_aircraft.isManuallyControlled() == true) {

				return;

			} else {

				for (Waypoint w : left_waypoints) {

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
			for (int i = 0; i < left_waypoints.length; i++) {
				if (a.getFlightPlan().getOrigin().equals(left_waypoints[i])) {
					origin_index = i;
				}
				if (a.getFlightPlan().getDestination().equals(left_waypoints[i])) {
					destination_index = i;
				}
			}
			// Send to other player
			server.sendAircraft(from_airport, (int) a.getSpeed(), origin_index, destination_index, a.getTargetAltitudeIndex() ); 
		}
		
		
	}
	
	/**
 	 * List that helps to prevent spawning a plane in waypoint if any plane is currently going towards it 
 	 * or any plane is less than 300 from it.
 	 */
	private java.util.ArrayList<Waypoint> getAvailableEntryPoints() {
		java.util.ArrayList<Waypoint> available_entry_points = new java.util.ArrayList<Waypoint>();
		 
		for (Waypoint entry_point : left_entryexit_waypoints) {
			boolean is_available = true;
			for (Aircraft a : aircraft) {
				// Check if any plane is currently going towards the exit point/chosen originPoint
				// Check if any plane is less than what is defined as too close from the chosen originPoint
				if (a.getCurrentTarget().equals(entry_point.position()) || a.isCloseToEntry(entry_point.position())) {
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
		for (Waypoint entry_point : left_entryexit_waypoints) {
			base_id++;
			if (entry_point != left_airport) { // Airport is excluded as it is said initially whether it should come from airport or elsewhere
				for (int i = 1; i < Aircraft.altitude_list.size(); i++) { // Starts from 1 as the first in the altitude_list is 100 which is not considered
					boolean is_available = true;
					for (Aircraft a : aircraft) {
						// Check if any plane is currently going towards the exit point/chosen originPoint
						// Check if any plane's altitude is less than 300 different from altitude from altitude_list currently being checked and close to entry point at the same time 
						if (a.getCurrentTarget().equals(entry_point.position()) ||
								((Math.abs(a.getPosition().z() - Aircraft.altitude_list.get(i)) < 1000) && a.isCloseToEntry(entry_point.position()))) {
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
		int destination = RandomNumber.randInclusiveInt(0, left_entryexit_waypoints.length - 1);
		int origin = 0; // 0 is default, it is chosen later on (initialized as compiler would otherwise complain)
		Waypoint origin_point; 
		java.util.ArrayList<Waypoint> available_origins = getAvailableEntryPoints();
		
		if (fromAirport) {
			origin_point = left_airport;		
		}
		else {
			if (available_origins.isEmpty()) { // Creates a plane in waypoint with planes of different altitude than that of the new plane.
				if (getIdAvailableEntryPointsAltitudes().size() == 0)
					return null;
				java.util.ArrayList<Integer> available_id_entry_points_altitudes = getIdAvailableEntryPointsAltitudes();
				int id = available_id_entry_points_altitudes.get(RandomNumber.randInclusiveInt(0, available_id_entry_points_altitudes.size()-1));
				origin = id / 3; // Calculates id of a waypoint
				origin_point = left_entryexit_waypoints[origin]; 
				preferred_altitude_index = id % 3 + 1; // Calculates id for altitude_list (1 is added as the first item on the list is solely for creating from airports)
			}
			else { // Creates a plane in waypoint with no planes nearby
				origin_point = available_origins.get(RandomNumber.randInclusiveInt(0, available_origins.size()-1)); 
				for (int i = 0; i < left_entryexit_waypoints.length; i++) { // getting id for an entry point
					if (left_entryexit_waypoints[i].equals(origin_point)) {
						origin = i;
						break;
					}
				}
			}
		}
		
		// Making sure origin and destination aren't the same
		while (left_entryexit_waypoints[destination].equals(left_entryexit_waypoints[origin]) || fromAirport && left_entryexit_waypoints[destination] instanceof Airport) {
			destination = RandomNumber.randInclusiveInt(0, left_entryexit_waypoints.length - 1);
		}			
		
		Waypoint destination_point = left_entryexit_waypoints[destination];
		// Name
		String name = "";
		boolean name_is_taken = true;
		while (name_is_taken) {
			name = "Flight " + (int)(900 * Math.random() + 100);
			name_is_taken = false;
			for (Aircraft a : aircraft) {
				if (a.getName() == name) name_is_taken = true;
			}
		}
		return new Aircraft(name, aircraft_image, 32 + (int) (10 * Math.random()), 1, new FlightPlan(origin_point, 
				destination_point, this.left_waypoints, this.left_holding_waypoints, left_airport_takeoff_waypoint), preferred_altitude_index);
	}

	
}