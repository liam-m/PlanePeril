package cls;

import lib.jog.graphics;

/**
 * A special case of a waypoint. Used to circle the airport. <br>
 * Every holding waypoint as a "next" waypoint which become the target of the
 * aircraft once it reached the "current" holding waypoint.
 * 
 * @author Leslie, Philip
 * 
 */
public class HoldingWaypoint extends Waypoint {

	private Waypoint nextWaypoint;

	public HoldingWaypoint(double x, double y) {
		super(x, y);
	}

	/**
	 * Set the next waypoint, should be done only once.
	 * 
	 * @param nextWaypoint
	 */
	public void setNextWaypoint(Waypoint nextWaypoint) {
		this.nextWaypoint = nextWaypoint;
	}

	/**
	 * Get the next waypoint
	 * 
	 * @return Waypoint next waypoint
	 */
	public Waypoint getNextWaypoint() {
		return this.nextWaypoint;
	}

	/**
	 * draws the waypoint
	 * 
	 * @param x
	 *            the x location to draw at
	 * @param y
	 *            the y location to draw at
	 */
	public void draw(double x, double y) {
		graphics.setColour(128, 0, 0, 128);
		graphics.print("X", x - 8, y - 8, 2);

	}

	public void draw() {
		draw(position.x(), position.y());
	}
}
