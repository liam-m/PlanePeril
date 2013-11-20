package cls;

import lib.jog.graphics;

public class Waypoint {
	
	final private int RADIUS = 8;
	
	private Point _position;
	
	public Waypoint(double x, double y, double z){
		_position = new Point(x, y, z);
	}
	
	public Point getPosition() {
		return _position;
	}
	
	public void draw() {
		graphics.setColour(128, 0, 0, 128);
		graphics.circle(false, _position.getX(), _position.getY(), RADIUS);
		graphics.circle(true, _position.getX(), _position.getY(), RADIUS - 2);
	}

}
