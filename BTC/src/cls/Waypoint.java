package cls;

import lib.jog.graphics;

public class Waypoint {
	
	final private int RADIUS = 8;
	
	private Point _position;
	
	public Waypoint(double x, double y){
		_position = new Point(x, y, 0);
	}
	
	public Point position() {
		return _position;
	}
	
	public void draw() {
		graphics.setColour(128, 0, 0, 128);
		graphics.circle(false, _position.x(), _position.y(), RADIUS);
		graphics.circle(true, _position.x(), _position.y(), RADIUS - 2);
	}

}
