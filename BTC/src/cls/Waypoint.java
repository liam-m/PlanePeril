package cls;

import lib.jog.graphics;

public class Waypoint {

	public enum WaypointType {
		ENTRY_EXIT, REGULAR, MIXED
	}

	/**
	 * Leniancy to allow mouse input to be accepted in a small area around the
	 * waypoint For ease of use.
	 */
	public final static int MOUSE_LENIANCY = 16;

	/**
	 * Radius of the waypoint icon
	 */
	final private int RADIUS = 8;

	/**
	 * Location of the waypoint
	 */
	protected final Vector position;

	/**
	 * Marks whether the waypoint is a point where planes may enter and exit the
	 * game airspace
	 */
	private final WaypointType type;

	private final String name;

	/**
	 * Constructor for waypoints
	 * 
	 * @param x
	 *            the x coord of the waypoint
	 * @param y
	 *            the y coord of the waypoint
	 * @param inputEntryOrExit
	 *            whether the waypoint is a point where planes may enter and
	 *            leave the airspace
	 */
	public Waypoint(double x, double y, WaypointType type, String name) {
		position = new Vector(x, y, 0);
		this.type = type;
		this.name = name;
	}

	public Waypoint(double x, double y) {
		position = new Vector(x, y, 0);
		this.type = WaypointType.REGULAR;
		this.name = "";
	}

	/**
	 * Gets the waypoint position
	 * 
	 * @return the position of the waypoint.
	 */
	public Vector position() {
		return position;
	}

	/**
	 * Checks if the mouse is over the waypoint, within MOUSE_LENIANCY
	 * 
	 * @param mx
	 *            the mouse's x location
	 * @param my
	 *            the mouse's y location
	 * @return whether the mouse is considered over the waypoint.
	 */
	public boolean isMouseOver(int mx, int my) {

		double dx = position.x() - mx;
		double dy = position.y() - my;

		return dx * dx + dy * dy < MOUSE_LENIANCY * MOUSE_LENIANCY;
	}

	/**
	 * @return Whether or not the waypoint is an entry or exit point for the
	 *         airspace.
	 */
	public WaypointType getType() {
		return this.type;
	}

	/**
	 * Gets the cost of travelling between this waypoint and another Used for
	 * pathfinding
	 * 
	 * @param fromPoint
	 *            The point to consider cost from, to this waypoint
	 * @return the distance (cost) between the two waypoints
	 */
	public double getCost(Waypoint fromPoint) {
		return position.sub(fromPoint.position()).magnitude();
	}

	/**
	 * Gets the cost between two waypoints
	 * 
	 * @param source
	 *            the source waypoint
	 * @param target
	 *            the target waypoint
	 * @return the cost between source and target
	 */
	public static double getCostBetween(Waypoint source, Waypoint target) {
		return target.getCost(source);
	}

	public String getName() {
		return this.name;
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
		graphics.circle(false, x, y, RADIUS);
		graphics.circle(true, x, y, RADIUS - 2);

	}

	public void draw() {
		draw(position.x(), position.y());
	}

}
