package cls;

import java.util.ArrayList;

public class Airport extends Waypoint {

	private final static int MAX_AIRCRAFT_NUMBER = 10;

	private ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();

	public Airport(double x, double y) {
		super(x, y, WaypointType.ENTRY_EXIT);
		// TODO: Change type to WaypointType.MXIED, requires updates in
		// flightplan generations
	}

	/**
	 * Inserts an aircraft into the airport, done by reference.
	 * 
	 * @param aircraft
	 *            Aircraft to insert
	 * @throws IllegalStateException
	 *             if insertion will overflow airport
	 */
	public void insertAircraft(Aircraft aircraft) throws Exception {
		if (aircraftList.size() + 1 > MAX_AIRCRAFT_NUMBER) {
			throw new IllegalStateException(
					"Tried landing an aircraft into a full airport.");
		}

		aircraftList.add(aircraft);
	}

}
