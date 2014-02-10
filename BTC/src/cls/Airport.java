package cls;

import java.util.ArrayList;

import lib.RandomNumber;
import lib.jog.graphics;
import btc.Main;

public class Airport extends Waypoint {

	private final static int MAX_AIRCRAFT_NUMBER = 10;
	public final static int MOUSE_LENIANCY = 32;

	private ArrayList<Aircraft> aircraftList = new ArrayList<Aircraft>();

	public Airport(double x, double y, String name) {
		super(x, y, WaypointType.ENTRY_EXIT, name);
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
	public void insertAircraft(Aircraft aircraft) throws IllegalStateException {
		if (aircraftList.size() + 1 > MAX_AIRCRAFT_NUMBER) {
			throw new IllegalStateException(
					"Tried landing an aircraft into a full airport.");
		}

		aircraftList.add(aircraft);
	}

	public Aircraft takeoff() throws IllegalStateException {
		if (aircraftList.size() == 0)
			throw new IllegalStateException("No aircraft in airport");

		int i = RandomNumber.randInclusiveInt(0, aircraftList.size() - 1);
		Aircraft aircraft = aircraftList.get(i);
		aircraftList.remove(i);

		return aircraft;
	}

	public void drawAirportInfo() {
		graphics.setColour(Main.GREEN);

		graphics.print("Aircraft in: " + aircraftList.size(),
				position.x() - 40, position.y() + 25);

		graphics.print("Aero Medved'", position.x() - 40, position.y() + 15);
	}

}
