package cls;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

import scn.Demo;

public class Aircraft {

	public final static int SEPARATION_RULE = 64;
	public final static int RADIUS = 16;
	public final static int MOUSE_LENIANCY = 16;
	
	private double turnSpeed = Math.PI / 4; // How much it can turn in per second.
	
	private Vector position;
	private Vector velocity;
	private boolean isManuallyControlled;
	private String flightName;
	private Vector currentTarget;
	private String originName, destinationName;
	private Waypoint[] route;
	private int currentRouteStage;
	private Vector destination;
	private graphics.Image image;
	private boolean hasFinished;
	private double currentlyTurningBy;
	private java.util.ArrayList<Aircraft> planesTooNear = new java.util.ArrayList<Aircraft>();

	//Constructor
	public Aircraft(String name, String nameOrigin, String nameDestination, Waypoint originPoint, Waypoint destinationPoint, graphics.Image img, double speed, Waypoint[] sceneWaypoints) {
		flightName = name;
		destinationName = nameOrigin;
		originName = nameDestination;
		
		// Find route
		//djikstraRoute(originPoint, destinationPoint, sceneWaypoints);
		route = findGreedyRoute(originPoint, destinationPoint, sceneWaypoints);
		
		
		currentTarget = route[0].position();
		image = img;
		position = originPoint.position(); //place on spawn waypoint
		int offset = new Random().nextInt((SEPARATION_RULE - (-SEPARATION_RULE))) + (-SEPARATION_RULE); //generate a small random offset
		System.out.println("Offset by " + offset);
		position = position.add(new Vector(offset, offset, 30000));//offset spawn position. Helps avoid aircraft crashes very soon after spawn
		
		destination = destinationPoint.position();
		isManuallyControlled = false;
		double x = currentTarget.x() - position.x();
		double y = currentTarget.y() - position.y();
		velocity = new Vector(x, y, 0).normalise().scaleBy(speed);
		hasFinished = false;
		currentRouteStage = 0;
		currentlyTurningBy = 0;
	}
	
	@Override
	public String toString() {
		String repr = "";
		repr += "<Aircraft> (" + flightName + ")\n";
		repr += "------------------\n";
		repr += "Origin:\t\t\t" + originName + " (" + position.x() + ", " + position.y() + ")\n";
		repr += "Destination:\t\t" + destinationName + " (" + destination.x() + ", " + destination.y() + ")\n";
		repr += "Flight Path:\t\t(" + currentTarget.x() + ", " + currentTarget.y() + "),\n";
		for (int i = 1; i < route.length; i ++) {
			repr += "\t\t\t(" + route[i].position().x() + ", " + route[i].position().y() + "),\n";
		}
		return repr;
	}
	
	public Vector position() {
		return position;
	}
	
	public String name () {
		return flightName;
	}
	
	public String originName() {
		return originName;
	}
	
	public String destinationName() {
		return destinationName;
	}
	
	public boolean isFinished() {
		return hasFinished;
	}
	
	public boolean isManuallyControlled() {
		return isManuallyControlled;
	}
	
	public int flightPathContains(Waypoint waypoint) {
		int index = -1;
		for (int i = 0; i < route.length; i ++) {
			if (route[i] == waypoint) index = i;
		}
		return index;
	}
	
	public void alterPath(int routeStage, Waypoint newWaypoint) {
		route[routeStage] = newWaypoint;
		if (!isManuallyControlled) resetBearing();
		if (routeStage == currentRouteStage){
			currentTarget = newWaypoint.position();
			turnTowardsTarget(0);
		}
	}
	
	public boolean isMouseOver(int mx, int my) {
		double dx = position.x() - mx;
		double dy = position.y() - my;
		return dx*dx + dy*dy < MOUSE_LENIANCY*MOUSE_LENIANCY;
	}
	public boolean isMouseOver() { return isMouseOver(input.mouseX(), input.mouseY()); }
	
	public void update(double dt) {
		if (hasFinished) return;
		
		// Update position
		Vector dv = velocity.scaleBy(dt);
		position = position.add(dv);
		
		currentlyTurningBy = 0;
		// Update input if manually controlled
		if (isManuallyControlled) {
			if (outOfBounds()) {
				hasFinished = true;
				return;
			}
			return;
		}

		// Update target
		if (isAt(currentTarget) && currentTarget.equals(destination)) {
			hasFinished = true;
		} else if (isAt(currentTarget) && (currentRouteStage == route.length-1)) {
			currentRouteStage ++;
			currentTarget = destination;
		} else if (isAt(currentTarget)) {
			currentRouteStage ++;
			currentTarget = route[currentRouteStage].position();
		}
		
		// Update bearing
		if ( Math.abs(angleToTarget() - bearing()) > 0.1 ) {
			turnTowardsTarget(dt);
		}
	}
	
	private double angleToTarget() {
		return Math.atan2(currentTarget.y() - position.y(), currentTarget.x() - position.x());
	}
	
	private boolean outOfBounds() {
		double x = position.x();
		double y = position.y();
		return (x < RADIUS || x > window.width() + RADIUS - 32 || y < RADIUS || y > window.height() + RADIUS - 144);
	}
	
	public boolean isTurningLeft() {
		return currentlyTurningBy < 0;
	}
	
	public boolean isTurningRight() {
		return currentlyTurningBy > 0;
	}
	
	public void turnLeft(double dt) {
		turnBy(-dt * turnSpeed);
	}
	
	public void turnRight(double dt) {
		turnBy(dt * turnSpeed);
	}

	private void turnBy(double angle) {
		currentlyTurningBy = angle;
		double cosA = Math.cos(angle);
		double sinA = Math.sin(angle);
		double x = velocity.x();
		double y = velocity.y();
		velocity = new Vector(x*cosA - y*sinA, y*cosA + x*sinA, velocity.z());
	}

	private void turnTowardsTarget(double dt) {
		// Get difference in angle
		double angleDifference = (angleToTarget() % (2 * Math.PI)) - (bearing() % (2 * Math.PI));
		boolean crossesPosativeNegativeDivide = angleDifference < -Math.PI * 7 / 8;
		// Correct difference
		angleDifference += Math.PI;
		angleDifference %= (2 * Math.PI);
		angleDifference -= Math.PI;
		// Get which way to turn.
		int angleDirection = (int)(angleDifference /= Math.abs(angleDifference));
		if (crossesPosativeNegativeDivide) angleDirection *= -1;  
		double angleMagnitude = Math.min(Math.abs((dt * turnSpeed)), Math.abs(angleDifference)); 
		turnBy(angleMagnitude * angleDirection);
	}
	
	public void draw() {
		graphics.setColour(128, 128, 128);
		graphics.draw(image, position.x(), position.y(), bearing(), 8, 8);
		graphics.setColour(128, 128, 128, 96);
		graphics.print(String.valueOf(altitude()) + "£", position.x()+8, position.y()-8);
		drawWarningCircles();
	}
	
	private void drawWarningCircles() {
		for (Aircraft plane : planesTooNear) {
			Vector midPoint = position.add(plane.position).scaleBy(0.5);
			double radius = position.sub(midPoint).magnitude() * 2;
			graphics.setColour(128, 0, 0);
			graphics.circle(false, midPoint.x(), midPoint.y(), radius);
		}
//		if (planesTooNear.size() > 0){
//			graphics.setColour(128,0,0);
//			graphics.print("!", _position.x(), _position.y() - 64, 8);
//		}
//		graphics.setColour(0, 128, 128);
//		graphics.circle(false, _position.x(), _position.y(), SEPARATION_RULE);
//		graphics.setColour(128, 0, 0);
//		graphics.circle(false, _position.x(), _position.y(), RADIUS);		
	}
	
	private double altitude() {
		return position.z();
	}
	
	public void drawFlightPath() {
		graphics.setColour(0, 128, 128);
		if (currentTarget != destination) {
			graphics.line(position.x(), position.y(), route[currentRouteStage].position().x(), route[currentRouteStage].position().y());
		}
		for (int i = currentRouteStage; i < route.length-1; i++) {
			graphics.line(route[i].position().x(), route[i].position().y(), route[i+1].position().x(), route[i+1].position().y());	
		}
		if (currentTarget == destination) {
			graphics.line(position.x(), position.y(), destination.x(), destination.y());
		} else {
			graphics.line(route[route.length-1].position().x(), route[route.length-1].position().y(), destination.x(), destination.y());
		}
	}
	
	public double bearing() {
		return Math.atan2(velocity.y(), velocity.x());
	}
	
	public double speed() {
		return velocity.magnitude();
	}
	
	public boolean isAt(Vector point) {
		double dy = point.y() - position.y();
		double dx = point.x() - position.x();
		return dy*dy + dx*dx < 16;
	}
	
	private Waypoint[] findRandomRoute(Vector origin, Vector destination) {
		// Placeholder over-simplified version
		int n = 4;
		Waypoint[] route = new Waypoint[n];
		for (int i = 0; i < n; i ++ ) {
			route[i] = Demo.airspaceWaypoints[(int)( Math.random() * Demo.airspaceWaypoints.length )];
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
					if (point.getCost(currentPos) + 0.5 * Waypoint.getCostBetween(point, destination) < cost){
					//	System.out.println("cost: " + point.getCost(currentPos));
						//cheaper route found, update
						cheapest = point;
						cost = point.getCost(currentPos) + 0.5 * Waypoint.getCostBetween(point, destination);
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
		for(int i = 0; i < sceneWaypoints.length; i++){
			distance[i] = sceneWaypoints[i].getCost(origin);
			visited[i] = false;
		}
		
		queue.add(origin);
		
		while(!queue.isEmpty()){
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
				route = buildRoute(sceneWaypoints, previous, destination);
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
		planesTooNear.clear();
		for (Aircraft plane : scene.aircraftList()) {
			if (plane != this && isWithin(plane, RADIUS)) {
				scene.gameOver(this, plane);
				hasFinished = true;
			} else if (plane != this && isWithin(plane, SEPARATION_RULE)) {
				scene.main().score().addTimeViolated(dt);
				planesTooNear.add(plane);
			}
		}
	}
	
	private boolean isWithin(Aircraft aircraft, int distance) {
		double dx = aircraft.position().x() - position.x();
		double dy = aircraft.position().y() - position.y();
		double dz = aircraft.position().z() - position.z();
		return dx*dx + dy*dy + dz*dz < distance*distance;
	}

	public void toggleManualControl() {
		isManuallyControlled = !isManuallyControlled;
		if (!isManuallyControlled) resetBearing();
	}
	
	private void resetBearing() {
		if (currentRouteStage < route.length) {
			currentTarget = route[currentRouteStage].position();
		}
		turnTowardsTarget(0);
	}
	
	public void climb() {
		changeAltitude(1000);
	}
	
	public void fall() {
		changeAltitude(-1000);
	}
	
	private void changeAltitude(int height) {
		position = new Vector(position.x(), position.y(), position.z() + height);
	}
	
}
