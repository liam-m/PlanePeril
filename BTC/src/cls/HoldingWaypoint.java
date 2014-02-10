package cls;

import lib.jog.graphics;

public class HoldingWaypoint extends Waypoint {

	private Waypoint nextWaypoint;

	public HoldingWaypoint(double x, double y) {
		super(x, y);

	}

	public void setNextWaypoint(Waypoint nextWaypoint) {
		this.nextWaypoint = nextWaypoint;
	}

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
