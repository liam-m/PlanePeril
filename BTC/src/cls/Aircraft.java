package cls;

import lib.jog.graphics;
<<<<<<< HEAD
import lib.jog.input;

import scn.Demo;

public class Aircraft {
=======

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
>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401

	public final static int SEPARATION_RULE = 32;
	public final static int RADIUS = 8;
	public final static int MOUSE_LENIANCY = 16;
	
	private double _speed;
	
	private Vector _position;
	private Vector _velocity;
	private boolean _manualControl;
	private String _flightName;
	private Vector _target;
	private String _originName, _destinationName;
	private Waypoint[] _route;
	private int _routeStage;
	private Vector _destination;
	private graphics.Image _image;
	private boolean _finished;

	//Constructor
<<<<<<< HEAD
	public Aircraft(String flightName, String originName, String destinationName, Vector origin, Vector destination, graphics.Image image, double speed) {
		_flightName = flightName;
		_destinationName = destinationName;
		_originName = originName;
		_route = findRoute(origin, destination);
		_target = _route[0].position();
		_image = image;
		_speed = speed;
		_position = origin;
		_destination = destination;
		_manualControl = false;
		double x = _target.x() - _position.x();
		double y = _target.y() - _position.y();
		_velocity = new Vector(x, y, 0).normalise().mul(speed);
		_finished = false;
		_routeStage = 0;
=======
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
>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401
	}
	
	@Override
	public String toString() {
		String repr = "";
		repr += "<Aircraft> (" + _flightName + ")\n";
		repr += "------------------\n";
		repr += "Origin:\t\t\t" + _originName + " (" + _position.x() + ", " + _position.y() + ")\n";
		repr += "Destination:\t\t" + _destinationName + " (" + _destination.x() + ", " + _destination.y() + ")\n";
		repr += "Flight Path:\t\t(" + _target.x() + ", " + _target.y() + "),\n";
		for (int i = 1; i < _route.length; i ++) {
			repr += "\t\t\t(" + _route[i].position().x() + ", " + _route[i].position().y() + "),\n";
		}
		return repr;
	}
	
	public Vector position() {
		return _position;
	}
	
	public String name () {
		return _flightName;
	}
	
	public boolean isFinished() {
		return _finished;
	}
	
	public boolean isManuallyController() {
		return _manualControl;
	}
	
	public boolean isMouseOver(int mx, int my) {
		double dx = _position.x() - mx;
		double dy = _position.y() - my;
		return dx*dx + dy*dy < MOUSE_LENIANCY*MOUSE_LENIANCY;
	}
	public boolean isMouseOver() { return isMouseOver(input.mouseX(), input.mouseY()); }
	
	public void update(double dt) {
		if (_finished) return;

		Vector dv = _velocity.mul(dt);
		_position = _position.add(dv);
		
		Vector oldTarget = _target;
		if (isAt(_target) && _target.equals(_destination)) {
			_finished = true;
		} else if (isAt(_target) && _target.equals(_route[_route.length-1].position())) {
			_routeStage ++;
			_target = _destination;
		} else if (isAt(_target)) {
			_routeStage ++;
			_target = _route[_routeStage].position();
		}
		if (oldTarget != _target) {
			turnTowards(_target.x(), _target.y());
		}
	}
<<<<<<< HEAD
	
	private void turnTowards(double tx, double ty) {
		double x = tx - _position.x();
		double y = ty - _position.y();
		_velocity = new Vector(x, y, 0).normalise().mul(_speed);
	}
	
	public void draw() {
		graphics.setColour(128, 128, 128);
		graphics.draw(_image, _position.x(), _position.y(), bearing(), 8, 8);
		graphics.setColour(0, 128, 128);
		graphics.circle(false, _position.x(), _position.y(), SEPARATION_RULE);
=======

	// Setters //

	public void setVelocity(double velX, double velY, double velZ) {
		this.velocity = new double[] {velX, velY, velZ};
>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401
	}
	
	public double bearing() {
		double a = Math.acos( _velocity.x() / Math.sqrt(Math.pow(_velocity.x(), 2) + Math.pow(_velocity.y(), 2)) );
		if (_velocity.y() < 0) a *= -1;
		return a;
	}
	
	public boolean isAt(Vector point) {
		double dy = point.y() - _position.y();
		double dx = point.x() - _position.x();
		return dy*dy + dx*dx < 16;
	}
	
	private Waypoint[] findRoute(Vector origin, Vector destination) {
		// Placeholder over-simplified version
		int n = 4;
		Waypoint[] route = new Waypoint[n];
		for (int i = 0; i < n; i ++ ) {
			route[i] = Demo._waypoints[(int)( Math.random() * Demo._waypoints.length )];
		}
		return route;
	}

	public void updateCollisions(double dt, scn.Demo scene) {
		for (Aircraft plane : scene.aircraftList()) {
			if (plane != this && isWithin(plane, RADIUS)) {
				scene.gameOver(this, plane);
				_finished = true;
			} else if (plane != this && isWithin(plane, SEPARATION_RULE)) {
				scene.main().score().addTimeViolated(dt);
			}
		}
	}
	
<<<<<<< HEAD
	private boolean isWithin(Aircraft aircraft, int distance) {
		double dx = aircraft.position().x() - _position.x();
		double dy = aircraft.position().y() - _position.y();
		return dx*dx + dy*dy < distance*distance;
	}
	
}
=======
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
>>>>>>> af27040f71019e7ec36ed12d2b1f5ad0d0368401
