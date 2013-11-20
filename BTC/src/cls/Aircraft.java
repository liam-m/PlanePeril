package cls;

import lib.jog.graphics;

import org.newdawn.slick.opengl.ImageData;

public class Aircraft {

	//Variables
	private double[] velocity; // A vector of X, Y, Z and a Speed.
	private Point position;
	private static int separationRule = 1000;
	private boolean isManual;
	private String flightName;

	private Waypoint target, origin, destination;
	private Waypoint[] route;
	private int routeIndex;

	private ImageData image;

	//Constructor
	public Aircraft(String flightName, Waypoint destination, Waypoint origin,
							/*ImageData image,*/
							double posX, double posY, double posZ){
		
		/* TO DO
		 * Aircraft don't need an origin waypoint since they enter from a screen edge.
		 * 'Origin' currently is synonymous with the first target waypoint - so it's pointless.
		 *  Will tidy it up later.
		 *  
		 *  Figure out a method of picking a (sensible) route between the entry of the plane from the screen edge and it's destination
		 *  Idea - Destination waypoints should be off screen and undrawn. Then a plane will route offscreen properly
		 *  ie it will leave the airspace by a screen edge as required.
		*/
		
		this.flightName = flightName;

		this.destination = destination;
		this.origin = origin;

		/*Given an Origin Waypoint and a destination Waypoint
		 * find a path between them and create the aircraft's initial route*/
		this.route = findRoute(origin, destination);
		
		// first waypoint to route to is the start of the route.
		this.target = route[0];
		this.routeIndex = 0;
		
		//this.image = image;
		this.position = new Point(posX, posY, posZ);
		//Find the vector from initial location to first waypoint in route
		this.velocity = adjustVelocity(this.position, this.target);
		
		this.isManual = false;

	}

	// Getters //
	public Point getPosition() {
		return this.position;
	}

	public double[] getVelocity() {
		return this.velocity;
	}

	public int getSeparationRule() {
		return Aircraft.separationRule;
	}

	public boolean getIsManual() {
		return this.isManual;
	}

	public String getFlightName() {
		return this.flightName;
	}

	public Waypoint getDestination() {
		return this.destination;
	}

	public Waypoint getOrigin(){
		return this.origin;
	}

	public Waypoint getTarget() {
		return this.target;
	}

	public Waypoint[] getRoute() {
		return route;
	}

	public ImageData getImage() {
		return this.image;
	}

	// Setters //

	public void setVelocity(double velX, double velY, double velZ) {
		this.velocity = new double[] {velX, velY, velZ};
	}

	public void setIsManual(boolean isManual) {
		this.isManual = isManual;
	}

	public void setTarget(Waypoint target) {
		this.target = target;
	}

	public void setImage(ImageData image) {
		this.image = image;
	}

	public void setRoute(Waypoint[] route) {
		this.route = route;
	}
	
	// Functions //
	public Waypoint[] findRoute(Waypoint origin, Waypoint destination) {
		/* Basic - go directly from origin to destination. No other path finding. 
		 * Hardcoded some of the waypoints from the Demo scene. Aircraft will act oddly if scene is changed.
		 * This was to allow testing of followRoute.
		 * */
		Waypoint[] route = new Waypoint[] {origin, new Waypoint(344, 192, 0), destination, new Waypoint(256, 256, 0)};
		return route;
	}
	
	private double[] adjustVelocity(Point position, Waypoint target){
		/* Given the current location and the location of the target, 
		 * find the vector which will route the aircraft to the target.
		 * Method is private as it should only be used internally
		 * by followRoute and when initialising the aircraft.
		 * */
		double[] velocity = {position.getX() - target.getPosition().getX(), 
								position.getY() - target.getPosition().getY(),
									position.getZ() - target.getPosition().getZ(),
									0.5};
		return velocity;
	}
	
	public void followRoute(){
		/* does not currently check for being on the right Z level */
		
		//get target waypoint pos
		double targetX = target.getPosition().getX();
		double targetY = target.getPosition().getY();
		double targetZ = target.getPosition().getZ();
		
		//Check if we're close on the X axis
		if (targetX - 5 <= this.position.getX() && this.position.getX() <= targetX + 5){
			//close on x axis, check if close on Y axis also
			if (targetY - 5 <= this.position.getY() && this.position.getY() <= targetY + 5){
				//close on both axis, target reached. Head to next waypoint.
				this.routeIndex = (routeIndex + 1) % route.length; // wrap around route list for now, just to test movement
				this.target = route[routeIndex];
				this.velocity = adjustVelocity(this.position, this.target); //get new vector to target
				
				/* Math.signum(a) returns 1 if a is positive, else it returns -1.
				 * Use of this allows the aircraft to route towards
				 * Waypoints which are upwards and/or leftwards of the current position
				 * REMEMBER: For drawing, Java considers 0,0 as the top left of the screen
				 * Hence we must take the opposite of Math.signum */
				this.position.setX(this.position.getX() + ((0 - Math.signum(this.velocity[0])) * this.velocity[3])); 
				this.position.setY(this.position.getY() + ((0 - Math.signum(this.velocity[1])) * this.velocity[3])); 
			} else {
				//only close on x axis, therefore route along y axis
				this.velocity = adjustVelocity(this.position, this.target);
				this.position.setY(this.position.getY() + ((0 - Math.signum(this.velocity[1])) * this.velocity[3])); 
			}
		} else {
			//not close on X axis, check if close on Y axis
			if (targetY - 5 <= this.position.getY() && this.position.getY() <= targetY + 5){
				//close on Y axis, route along X axis
				adjustVelocity(this.position, this.target);
				this.position.setX(this.position.getX() + ((0 - Math.signum(this.velocity[0])) * this.velocity[3])); 
			} else {
				//not close on either axis, route along both axis
				adjustVelocity(this.position, this.target);
				this.position.setX(this.position.getX() + ((0 - Math.signum(this.velocity[0])) * this.velocity[3])); 
				this.position.setY(this.position.getY() + ((0 - Math.signum(this.velocity[1])) * this.velocity[3])); 
			}
		} 
	
	}
	
	public void draw() {
		graphics.setColour(0, 128, 0, 128);
		graphics.circle(false, position.getX(), position.getY(), 6);
		graphics.circle(true, position.getX(), position.getY(), 4 - 2);
		graphics.print(flightName, position.getX() + 8, position.getY() + 1);
	}
}
