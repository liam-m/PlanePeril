package cls;

import lib.jog.graphics;

public class Waypoint {
	
	public final static int MOUSE_LENIANCY = 16;
	final private int RADIUS = 8;
	
	private Vector _position;
	
	public Waypoint(double x, double y){
		_position = new Vector(x, y, 0);
	}
	
	public Vector position() {
		return _position;
	}
	
	public boolean isMouseOver(int mx, int my) {
		double dx = _position.x() - mx;
		double dy = _position.y() - my;
		return dx*dx + dy*dy < MOUSE_LENIANCY*MOUSE_LENIANCY;
	}
	
	public void draw() {
		graphics.setColour(128, 0, 0, 128);
		graphics.circle(false, _position.x(), _position.y(), RADIUS);
		graphics.circle(true, _position.x(), _position.y(), RADIUS - 2);
	}

}
