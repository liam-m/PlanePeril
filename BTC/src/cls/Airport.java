package cls;

public class Airport extends Waypoint {

	private final static int MAX_AIRCRAFT_NUMBER = 10;

	private int aircraftIn = 0;

	public Airport(double x, double y, boolean inputEntryOrExit) {
		super(x, y, inputEntryOrExit);

	}

	/**
	 * 
	 * @param a
	 *            Aircraft to insert
	 * @return
	 */
	public boolean insertAircraft(Aircraft a) {
		if (aircraftIn + 1 > MAX_AIRCRAFT_NUMBER) {
			return false;
		}

		return true;
	}

}
