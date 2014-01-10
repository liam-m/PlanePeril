package cls;

import lib.jog.graphics;

public class Waypoint {
	
	public final static int MOUSE_LENIANCY = 16;
	final private int RADIUS = 8;
	
	private Vector position;
	private boolean entryOrExit;
	
	public Waypoint(double x, double y, boolean inputEntryOrExit){
		position = new Vector(x, y, 0);
		entryOrExit = inputEntryOrExit;
	}
	
	public Vector position() {
		return position;
	}
	
	public boolean isMouseOver(int mx, int my) {
		double dx = position.x() - mx;
		double dy = position.y() - my;
		return dx*dx + dy*dy < MOUSE_LENIANCY*MOUSE_LENIANCY;
	}
	
	public boolean isEntryOrExit(){
		return this.entryOrExit;
	}
	
	public double getCost(Waypoint fromPoint){
		return position.sub(fromPoint.position()).magnitude();
	}
	
	public static double getCostBetween(Waypoint source, Waypoint target){
		return target.getCost(source);
	}
	
	public void draw(double x, double y) {
		graphics.setColour(128, 0, 0, 128); 
		graphics.circle(false, x, y, RADIUS);
		graphics.circle(true, x, y, RADIUS - 2);
	}

	public void draw() {
		draw(position.x(), position.y());
	}

}
