package cls;

import lib.jog.graphics;

public class Waypoint {
	
	final private int RADIUS = 8;
	
	private Vector _position;
	
<<<<<<< HEAD
	public Waypoint(double x, double y){
		_position = new Vector(x, y, 0);
	}
	
	public Vector position() {
=======
	public Waypoint(double x, double y, double z){
		_position = new Point(x, y, z);
	}
	
	public Point getPosition() {
>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401
		return _position;
	}
	
	public void draw() {
		graphics.setColour(128, 0, 0, 128);
		graphics.circle(false, _position.getX(), _position.getY(), RADIUS);
		graphics.circle(true, _position.getX(), _position.getY(), RADIUS - 2);
	}

}
