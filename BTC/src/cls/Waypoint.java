package cls;

import lib.jog.graphics;

public class Waypoint {
	
	public final static int MOUSE_LENIANCY = 16;
	final private int RADIUS = 8;
	
	private Vector position;
	private boolean offScreen;
	
	public Waypoint(double x, double y, boolean offscreen){
		position = new Vector(x, y, 0);
		offScreen = offscreen;
	}
	
	public Vector position() {
		return position;
	}
	
	public boolean isMouseOver(int mx, int my) {
		double dx = position.x() - mx;
		double dy = position.y() - my;
		return dx*dx + dy*dy < MOUSE_LENIANCY*MOUSE_LENIANCY;
	}
	
	public boolean isOffscreen(){
		return offScreen;
	}
	
	public double getCost(Waypoint fromPoint){
		return position.sub(fromPoint.position()).magnitude();
	}
	
	public static double getCostBetween(Waypoint source, Waypoint target){
		return target.getCost(source);
	}
	
	public void draw() {
		graphics.setColour(128, 0, 0, 128);
		graphics.circle(false, position.x(), position.y(), RADIUS);
		graphics.circle(true, position.x(), position.y(), RADIUS - 2);
	}

}
