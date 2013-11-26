package cls;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

import scn.Demo;

public class Aircraft {

	public final static int SEPARATION_RULE = 32;
	public final static int RADIUS = 16;
	public final static int MOUSE_LENIANCY = 16;
	
	private double _speed;
	private double _turnSpeed = Math.PI / 2;
	
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
	public Aircraft(String flightName, String originName, String destinationName, Waypoint originPoint, Waypoint destinationPoint, graphics.Image image, double speed, Waypoint[] sceneWaypoints) {
		_flightName = flightName;
		_destinationName = destinationName;
		_originName = originName;
		
		// Find route
		//djikstraRoute(originPoint, destinationPoint, sceneWaypoints);
		_route = findGreedyRoute(originPoint, destinationPoint, sceneWaypoints);
		
		
		_target = _route[0].position();
		_image = image;
		_speed = speed;
		_position = originPoint.position(); //place on spawn waypoint
		int offset = new Random().nextInt((SEPARATION_RULE - (-SEPARATION_RULE))) + (-SEPARATION_RULE); //generate a small random offset
		System.out.println("Offset by " + offset);
		_position = _position.add(new Vector(offset, offset, 30000 + (int)(10000 * Math.random())));//offset spawn position. Helps avoid aircraft crashes very soon after spawn
		
		_destination = destinationPoint.position();
		_manualControl = false;
		double x = _target.x() - _position.x();
		double y = _target.y() - _position.y();
		_velocity = new Vector(x, y, 0).normalise().scaleBy(speed);
		_finished = false;
		_routeStage = 0;
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
	
	public String originName() {
		return _originName;
	}
	
	public String destinationName() {
		return _destinationName;
	}
	
	public boolean isFinished() {
		return _finished;
	}
	
	public boolean isManuallyControlled() {
		return _manualControl;
	}
	
	public int flightPathContains(Waypoint waypoint) {
		int index = -1;
		for (int i = 0; i < _route.length; i ++) {
			if (_route[i] == waypoint) index = i;
		}
		return index;
	}
	
	public void alterPath(int routeStage, Waypoint newWaypoint) {
		_route[routeStage] = newWaypoint;
		if (!_manualControl) resetBearing();
		if (routeStage == this._routeStage){
			_target = newWaypoint.position();
			turnTowards(newWaypoint.position().x(), newWaypoint.position().y());
		}
	}
	
	public boolean isMouseOver(int mx, int my) {
		double dx = _position.x() - mx;
		double dy = _position.y() - my;
		return dx*dx + dy*dy < MOUSE_LENIANCY*MOUSE_LENIANCY;
	}
	public boolean isMouseOver() { return isMouseOver(input.mouseX(), input.mouseY()); }
	
	public void update(double dt) {
		if (_finished) return;

		Vector dv = _velocity.scaleBy(dt);
		_position = _position.add(dv);
		
		if (_manualControl) {
			if (outOfBounds()) {
				_finished = true;
				return;
			}
			double angle = 0;
			if (input.isKeyDown(input.KEY_RIGHT)) {
				angle += dt;
			}
			if (input.isKeyDown(input.KEY_LEFT)) {
				angle -= dt;
			}
			if (angle != 0) turnBy(angle * _turnSpeed);
			return;
		}
		
		Vector oldTarget = _target;
		if (isAt(_target) && _target.equals(_destination)) {
			_finished = true;
		} else if (isAt(_target) && (_routeStage == _route.length-1)) {
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
	
	private boolean outOfBounds() {
		double x = _position.x();
		double y = _position.y();
		return (x < RADIUS || x > window.width() + RADIUS - 32 || y < RADIUS || y > window.height() + RADIUS - 144);
	}

	public void turnBy(double angle) {
		double cosA = Math.cos(angle);
		double sinA = Math.sin(angle);
		double x = _velocity.x();
		double y = _velocity.y();
		_velocity = new Vector(x*cosA - y*sinA, y*cosA + x*sinA, _velocity.z());
	}

	private void turnTowards(double tx, double ty) {
		double x = tx - _position.x();
		double y = ty - _position.y();
		_velocity = new Vector(x, y, 0).normalise().scaleBy(_speed);
	}
	
	public void draw() {
		graphics.setColour(128, 128, 128);
		graphics.draw(_image, _position.x(), _position.y(), bearing(), 8, 8);
		graphics.setColour(128, 128, 128, 96);
		graphics.print(String.valueOf(altitude()) + "£", _position.x()+8, _position.y()-8);
		graphics.setColour(0, 128, 128);
		graphics.circle(false, _position.x(), _position.y(), SEPARATION_RULE);
		graphics.setColour(128, 0, 0);
		graphics.circle(false, _position.x(), _position.y(), RADIUS);
	}
	
	private double altitude() {
		return _position.z();
	}
	
	public void drawFlightPath() {
		graphics.setColour(0, 128, 128);
		if (_target != _destination) {
			graphics.line(_position.x(), _position.y(), _route[_routeStage].position().x(), _route[_routeStage].position().y());
		}
		for (int i = _routeStage; i < _route.length-1; i++) {
			graphics.line(_route[i].position().x(), _route[i].position().y(), _route[i+1].position().x(), _route[i+1].position().y());	
		}
		if (_target == _destination) {
			graphics.line(_position.x(), _position.y(), _destination.x(), _destination.y());
		} else {
			graphics.line(_route[_route.length-1].position().x(), _route[_route.length-1].position().y(), _destination.x(), _destination.y());
		}
	}
	
	public double bearing() {
		double a = Math.acos( _velocity.x() / Math.sqrt(Math.pow(_velocity.x(), 2) + Math.pow(_velocity.y(), 2)) );
		if (_velocity.y() < 0) a *= -1;
		return a;
	}
	
	public double speed() {
		return _velocity.magnitude();
	}
	
	public boolean isAt(Vector point) {
		double dy = point.y() - _position.y();
		double dx = point.x() - _position.x();
		return dy*dy + dx*dx < 16;
	}
	
	private Waypoint[] findRandomRoute(Vector origin, Vector destination) {
		// Placeholder over-simplified version
		int n = 4;
		Waypoint[] route = new Waypoint[n];
		for (int i = 0; i < n; i ++ ) {
			route[i] = Demo._waypoints[(int)( Math.random() * Demo._waypoints.length )];
		}
		return route;
	}
	
	public Waypoint[] findGreedyRoute(Waypoint origin, Waypoint destination, Waypoint[] waypoints){
		// to hold the route as we generate it.
		ArrayList<Waypoint> selectedWaypoints = new ArrayList<Waypoint>();
		// initialise the origin as the first point in the route.
		//selectedWaypoints.add(origin);
		// to track our position as we generate the route. Initialise to the start of the route
		Waypoint currentPos = origin;
		//System.out.println("Begin route finding");
		//System.out.println("Entered from: " + currentPos.position().x() + " " + currentPos.position().y());
		// to track the closest next waypoint
		double cost = 99999999999999.0;
		Waypoint cheapest = null;
		//to track if the route is complete
		boolean atDestination = false;
		
		while (! atDestination) {
			//System.out.println("Entering findRoute while");
			for (Waypoint point : waypoints) {
				boolean skip = false;
				
				for (Waypoint routePoints : selectedWaypoints){
					// check we have not already selected the waypoint
					// if we have, skip evaluating the point
					// this protects the aircraft from getting stuck looping between points
					if (routePoints.position().equals(point.position())){
						skip = true; //flag to skip
						break; // no need to check rest of list, already found a match.
					}
				}
				// do not consider the waypoint we are currently at or the origin
				// do not consider offscreen waypoints which are not the destination
				// also skip if flagged as a previously selected waypoint
				if (skip == true | point.position().equals(currentPos.position()) | point.position().equals(origin.position())
						| (point.isOffscreen() == true && (point.position().equals(destination.position()) == false))){
				//	System.out.println("Skipped");
					skip = false; //reset flag
					continue;
	
				}  else  {
					//System.out.println("Evaluating point");
					/* get cost of visiting waypoint
					 * compare cost vs current cheapest
					 * if smaller, replace */	
					if (point.getCost(currentPos) < cost){
					//	System.out.println("cost: " + point.getCost(currentPos));
						//cheaper route found, update
						cheapest = point;
						cost = point.getCost(currentPos);
					}
				}
				
			} //end for - evaluated all waypoints
			//System.out.println("Exited findRoute for");
			//The cheapest waypoint must have been found
			assert cheapest != null : "The cheapest waypoint was not found";

			if (cheapest.position().equals(destination.position())){
				/* route has reached destination 
				 * break out of while loop*/
				atDestination = true;
				//System.out.println("Found destination");
			} else {
				//System.out.println("not destination");
			}
			
			
			//System.out.println("currentPos: " + currentPos.position().x() + " " + currentPos.position().y() + " " + currentPos.position().z());
			//System.out.println("Destination: "+ destination.position().x() + " "+ destination.position().y() + " "+ destination.position().z());
			// update the selected route
			// consider further points in route from the position of the selected point
			selectedWaypoints.add(cheapest);
			currentPos = cheapest;
			//resaturate cost for next loop
			cost = 99999999999.0;

		} //end while
		//System.out.println("Exited findRoute while");
		//create a Waypoint[] to hold the new route
		Waypoint[] route = new Waypoint[selectedWaypoints.size()];
		//fill route with the selected waypoints
		for (int i = 0; i < selectedWaypoints.size(); i++){
			route[i] = selectedWaypoints.get(i);
		}
		return route;
	}
	
	public void djikstraRoute(Waypoint origin, Waypoint destination, Waypoint[] sceneWaypoints){
		double[] distance = new double[sceneWaypoints.length]; //distance from source to waypoints
		boolean[] visited = new boolean[sceneWaypoints.length]; //to check if a waypoint has been visited
		Waypoint[] previous = new Waypoint[sceneWaypoints.length]; //previous waypoint in optimal path from source to a waypoint
		ArrayList<Waypoint> queue = new ArrayList<Waypoint>(); // Queue of waypoints to evaluate
		
		//initialisation
		for(int i = 0; i<sceneWaypoints.length; i++){
			distance[i] = 999999999999.0;
			visited[i] = false;
			//previous is initialised with all entries as null when it is created
		}
		
		distance[getIndex(origin, sceneWaypoints)] = 0; //distance from origin to itself is 0
		queue.add(origin);
		
		while(queue.isEmpty() == false){
			//System.out.println("Enter Djiksta While");
			//find waypoint in queue with smallest distance and which is unvisited
			double cost = 9999.0;
			Waypoint cheapest = queue.get(0);
			for (int i = 0; i < queue.size(); i++){
				//System.out.println("Evaluating Queue");
				if (distance[i] < cost && visited[i] == false){
					cheapest = queue.get(i);
					//System.out.println("Found cheaper");
				}
			}
			
			if (cheapest.position().equals(destination.position())){ //terminate if the next node is the destination
				//System.out.println("Found Destination");
				this._route = buildRoute(sceneWaypoints, previous, destination);
				break;
			} else {
				//System.out.println("Not dest");
				queue.remove(cheapest);
				int cheapestIndex = getIndex(cheapest, sceneWaypoints);
				visited[cheapestIndex] = true;
				double dist;

				for (Waypoint neighbour : sceneWaypoints) {
					//System.out.println("Evaluating neighbours");
					dist = distance[cheapestIndex] + Waypoint.getCostBetween(cheapest, neighbour); //accumulate shortest distance from origin
					int neighbourIndex = getIndex(neighbour, sceneWaypoints);

					if (dist < distance[neighbourIndex] && visited[neighbourIndex] == false){
						distance[neighbourIndex] = dist; //shortest dist from origin to neighbour
						previous[neighbourIndex] = cheapest;
						queue.add(neighbour); //insert into queue for processing
						//System.out.println("Neighbour added to Queue");
					} //end if
				} // end for
			} // end else
		} //end while
	}
	
	private Waypoint[] buildRoute(Waypoint[] sceneWaypoints, Waypoint[] previous, Waypoint destination){
		System.out.println("Begin buildRoute.");
		Stack<Waypoint> sequence = new Stack<Waypoint>();
		int currentIndex = getIndex(destination, sceneWaypoints);
		
		while (previous[currentIndex] != null) {
			System.out.println("Waypoints " + previous[currentIndex]);
			sequence.push(previous[currentIndex]); //build the sequence of waypoints back to the origin
			currentIndex = getIndex(previous[currentIndex], sceneWaypoints);
		}
		System.out.println("Stack size: " + sequence.size());
		
		//pop to build the route from origin to destination
		Waypoint[] route = new Waypoint[sequence.size()];
		
		for (int i = 0; i < sequence.size(); i++){
			route[i] = sequence.pop();
			System.out.println("[ROUTE] Waypoint: " + i + " Pos: " + route[i].position().x() + " " + route[i].position().y());
		}
		//finally add the destination
		route[route.length - 1] = destination;
		return route;
	}
	
	private int getIndex(Waypoint point, Waypoint[] sceneWaypoints){
		int index = 0;
		for (int i = 0; i < sceneWaypoints.length; i++){
			if (sceneWaypoints[i].equals(point)){
				index = i;
				break;
			}
		}
		return index;
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
	
	private boolean isWithin(Aircraft aircraft, int distance) {
		double dx = aircraft.position().x() - _position.x();
		double dy = aircraft.position().y() - _position.y();
		double dz = aircraft.position().z() - _position.z();
		return dx*dx + dy*dy + dz*dz < distance*distance;
	}

	public void toggleManualControl() {
		_manualControl = !_manualControl;
		if (!_manualControl) resetBearing();
	}
	
	private void resetBearing() {
		if (_routeStage < _route.length) {
			_target = _route[_routeStage].position();
		}
		turnTowards(_target.x(), _target.y());
	}
	
	public void climb() {
		changeAltitude(1000);
	}
	
	public void fall() {
		changeAltitude(-1000);
	}
	
	private void changeAltitude(int height) {
		_position = new Vector(_position.x(), _position.y(), _position.z() + height);
	}
	
}
