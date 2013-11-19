package cls;

import org.newdawn.slick.opengl.ImageData;

public class Aircraft {

	//Variables
	private double[] position, velocity;
	private static int separationRule = 1000;
	private boolean isManual;
	private String flightName;

	private Waypoint target, origin, destination;
	private Waypoint[] route;

	private ImageData image;

	//Constructor
	public Aircraft(String flightName, Waypoint destination, Waypoint origin,
						Waypoint target, ImageData image,
							double posX, double posY, double posZ,
								double velX, double velY, double velZ){

		this.flightName = flightName;

		this.destination = destination;
		this.origin = origin;

		/*Given an Origin Waypoint and a destination Waypoint
		 * find a path between them and create the aircraft's initial route*/
		this.route = findRoute(origin, destination);

		this.target = target;
		this.image = image;
		this.position = new double[] {posX, posY, posZ};
		this.velocity = new double[] {velX, velY, velZ};
		this.isManual = false;

	}

	// Getters //
	public double[] getPosition() {
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

	public void setPosition(double posX, double posY, double posZ) {
		this.position = new double[] {posX, posY, posZ};
	}

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
}
