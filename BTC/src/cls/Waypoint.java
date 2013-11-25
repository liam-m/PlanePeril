package cls;

import lib.jog.graphics;

public class Waypoint {
	
	public final static int MOUSE_LENIANCY = 16;
	final private int RADIUS = 8;
	
	private Vector _position;
	private boolean offscreen;
	
	public Waypoint(double x, double y, boolean offscreen){
		_position = new Vector(x, y, 0);
		this.offscreen = offscreen;
	}
	
	public Vector position() {
		return _position;
	}
	
	public boolean isMouseOver(int mx, int my) {
		double dx = _position.x() - mx;
		double dy = _position.y() - my;
		return dx*dx + dy*dy < MOUSE_LENIANCY*MOUSE_LENIANCY;
	}
	
	public boolean isOffscreen(){
		return this.offscreen;
	}
	
	public double getCost(Waypoint fromPoint){
		double x = fromPoint.position().x() - this._position.x();

		double y = fromPoint.position().y() - this._position.y();

		double z = fromPoint.position().z() - this._position.z();

		Vector distance = new Vector(x,y,z);
		double cost = distance.magnitude();
		return cost;
	}
	
	public static double getCostBetween(Waypoint source, Waypoint target){
		double x = source.position().x() - target.position().x();
		double y = source.position().y() - target.position().y();
		double z = source.position().z() - target.position().z();
		Vector between = new Vector(x,y,z);
		double cost = between.magnitude();
		return cost;
	}
	
	public void draw() {
		graphics.setColour(128, 0, 0, 128);
		graphics.circle(false, _position.x(), _position.y(), RADIUS);
		graphics.circle(true, _position.x(), _position.y(), RADIUS - 2);
	}

}
