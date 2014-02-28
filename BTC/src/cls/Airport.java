package cls;

import java.util.ArrayList;

import pp.Main;

import lib.RandomNumber;
import lib.jog.graphics;

public class Airport extends Waypoint {

	// Maximum number of aircraft that can be in the airport at once. If
	// exceeded, game ends.
	private final static int MAX_AIRCRAFT_NUMBER = 5;

	private final static int TEXT_POSITION = 55;
	private final static int TEXT_OFFSET = 10;

	public final static int MOUSE_LENIANCY = 32;

	private int timeLeft = 0;

	private ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();

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
	}

	/**
	 * Set how much time left until the next takeoff is allowed. As it counted
	 * in the Demo class, one must pass it here on every iteration. Later on
	 * displayed near the airport as text.
	 * 
	 * @param timeLeft
	 */
	public void setTimeLeft(int timeLeft) {
		// avoid the timer becoming lower than 0 i.e. negative
		if (timeLeft >= 0) {
			this.timeLeft = timeLeft;
		}
	}

	/**
	 * Inserts an aircraft into the airport, done by reference.
	 * 
	 * @param aircraft
	 *            to insert
	 * @throws IllegalStateException
	 *             if insertion will overflow airport
	 */
	public void insertAircraft(Aircraft aircraft) throws IllegalStateException {
		if (aircraftList.size() + 1 > MAX_AIRCRAFT_NUMBER) {
			throw new IllegalStateException(
					"Tried landing an aircraft into a full airport.");
		}

		aircraftList.add(aircraft);
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
		if (aircraftList.size() == 0)
			throw new IllegalStateException("No aircraft in airport");

		int i = RandomNumber.randInclusiveInt(0, aircraftList.size() - 1);

		Aircraft aircraft = aircraftList.get(i);
		aircraftList.remove(i);

		return aircraft;
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
		graphics.print("Aircraft in: " + aircraftList.size(),
				position.x() - 40, position.y() - (TEXT_POSITION - TEXT_OFFSET));

		// hide the timer if it is 0.
		if (timeLeft != 0) {
			graphics.print("Takeoff in: " + this.timeLeft, position.x() - 40,
					position.y() - (TEXT_POSITION - TEXT_OFFSET * 2));
		}

	}

}
