package cls;

import lib.jog.Graphics;

/**
 * A special case of a waypoint. Used to circle the airport. <br>
 * Every holding waypoint as a "next" waypoint which become the target of the
 * aircraft once it reached the "current" holding waypoint.
 * 
 * @author Leslie, Philip
 * 
 */
public class HoldingWaypoint extends Waypoint {

	private Waypoint next_waypoint;

	public HoldingWaypoint(double x, double y) {
		super(x, y);
	}

	/**
	 * Set the next waypoint, should be done only once.
	 * 
	 * @param next_waypoint
	 */
	public void setNextWaypoint(Waypoint next_waypoint) {
		this.next_waypoint = next_waypoint;
	}

	/**
	 * Get the next waypoint
	 * 
	 * @return Waypoint next waypoint
	 */
	public Waypoint getNextWaypoint() {
		return this.next_waypoint;
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
		Graphics.setColour(128, 0, 0, 128);
		Graphics.printText("X", x - 8, y - 8, 2);

	}

	public void draw() {
		draw(position.x(), position.y());
	}
}
