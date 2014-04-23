package cls;

import java.io.File;
import java.util.ArrayList;

import pp.Main;

import lib.RandomNumber;
import lib.jog.graphics;
import lib.jog.graphics.Image;

public class Airport extends Waypoint {

	private final static int MAX_AIRCRAFT_NUMBER = 3;

	private final static int TEXT_POSITION = 55;
	private final static int TEXT_OFFSET = 10;

	public final static int MOUSE_LENIANCY = 32;

	private int time_Left = 0;

	private ArrayList<Aircraft> aircraft_list = new ArrayList<Aircraft>();

	private Image airport;

	private double x_location;

	private double y_location;

	/**
	 * An airport. In general, it is basically a waypoint (more specifically and
	 * exit point) with few additions: an aircraft and land and takeoff from it. <br>
	 * <br>
	 * It should be noted that the holding waypoints (rendered as X's) are
	 * relative in position to the airport.
	 * 
	 * @param x
	 *            x-coordinate for the airport
	 * @param y
	 *            y-coordinate for the airport
	 * @param name
	 */
	public Airport(double x, double y, String name) {
		super(x, y, WaypointType.ENTRY_EXIT, name);
		x_location = x;
		y_location = y;
		loadImage();
	}
	
	public Airport(double x, double y, String name, boolean testing) {
		super(x, y, WaypointType.ENTRY_EXIT, name);
		x_location = x;
		y_location = y;
		if (!testing) {
			loadImage();
		}
	}

	/**
	 * Set how much time left until the next takeoff is allowed. As it counted
	 * in the Demo class, one must pass it here on every iteration. Later on
	 * displayed near the airport as text.
	 * 
	 * @param time_left
	 */
	public void setTimeLeft(int time_left) {
		// avoid the timer becoming lower than 0 i.e. negative
		if (time_left >= 0) {
			this.time_Left = time_left;
		}
	}

	/**
	 * Inserts an aircraft into the airport, done by reference. if the list is already full then nothing happens
	 * 
	 * @param aircraft
	 *            to insert
	 */
	public void insertAircraft(Aircraft aircraft) {
		if (aircraft_list.size() < MAX_AIRCRAFT_NUMBER) {
			aircraft_list.add(aircraft);
		}
	}

	/**
	 * Forces an aircraft to take off. Selects one from the list randomly.
	 * Removes it from the list afterwards.
	 * 
	 * @return Aircraft one of the aircraft that were held in the airport. This
	 *         aircraft can be used instead of generating a new one.
	 * @throws IllegalStateException
	 *             if there are no aircraft in the airport
	 */
	public Aircraft takeoff() throws IllegalStateException {
		if (aircraft_list.size() == 0)
			throw new IllegalStateException("No aircraft in airport");

		return aircraft_list.remove(0);
	}

	/**
	 * Draws all the airport information: <br>
	 * <br>
	 * Name, <br>
	 * Number of <b>aircraft in </b> <br>
	 * if the time to next takeoff is more than 0 seconds (i.e. there has been a
	 * takeoff recently), show how many <b>seconds left util next allowed
	 * takeoff</b>
	 */
	public void drawAirportInfo() {
		graphics.setColour(Main.GREEN);

		// print the name of the airport
		graphics.print("Aero Medved'", position.x() - 40, position.y()
				- TEXT_POSITION);

		// print how many aircraft are currently in the airport
		graphics.print("Aircraft in: " + aircraft_list.size(),
				position.x() - 40, position.y() - (TEXT_POSITION - TEXT_OFFSET));

		// hide the timer if it is 0.
		if (time_Left != 0) {
			graphics.print("Takeoff in: " + this.time_Left, position.x() - 40,
					position.y() - (TEXT_POSITION - TEXT_OFFSET * 2));
		}

	}
	
	public void loadImage() {
		airport = graphics.newImage("gfx" + File.separator + "Airport.png");
	}
	
	@Override
	public void draw() {
		graphics.draw(airport, x_location-airport.getWidth()/2, y_location-airport.getHeight()/2);
	}

	public int getMaxAircraft() {
		return MAX_AIRCRAFT_NUMBER;
	}
	
	public int getNumAircraft() {
		return this.aircraft_list.size();
	}
	
	public ArrayList<Aircraft> getAircraft() {
		return this.aircraft_list;
	}
}
