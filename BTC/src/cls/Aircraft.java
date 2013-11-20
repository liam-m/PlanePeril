package Entities;

import org.newdawn.slick.opengl.ImageData;

public class Aircraft {

	//Variables
	private double[] velocity;
	private Position position;
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
							double posX, double posY, double posZ,
								double velX, double velY, double velZ){

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
		this.position = new Position(posX, posY, posZ);
		this.velocity = new double[] {velX, velY, velZ};
		this.isManual = false;

	}

	// Getters //
	public Position getPosition() {
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
		/* Basic - go directly from origin to destination. No other path finding. */
		route = new Waypoint[] {origin, destination};
		return route;
	}
	
	public void followRoute(){
		/* follow the defined route.
		 * Update position
		 * check position against target waypoint
		 * if close to target, get next target and route towards it.
		 * else target remains the same until reached */
		
		//check if close to target waypoint
		double targetX = target.getPosition().getX();
		double targetY = target.getPosition().getY();
		double targetZ = target.getPosition().getZ();
		
		if (this.position.getX() == (targetX +- 3) && this.position.getY() == (targetY +- 3)
				&& this.position.getZ() == (targetZ +- 3)) {
			//target reached, therefore route to next target
			this.routeIndex ++;
			this.target = route[routeIndex];
			// alter velocity to head towards new target
			this.velocity = new double[] {0, 0, 0};
		}
		
		//update position
		this.position.setX(this.position.getX() + this.velocity[0]);
		this.position.setY(this.position.getY() + this.velocity[1]);
		this.position.setZ(this.position.getZ() + this.velocity[2]);
		
	}
}
