package cls;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;

import scn.Demo;

public class Aircraft {

	/**
	 * The physical size of the plane in pixels. This determines crashes.
	 */
	public final static int RADIUS = 16;
	/**
	 * How far away (in pixels) the mouse can be from the plane but still select it.
	 */
	public final static int MOUSE_LENIANCY = 16; 
	
	/**
	 * How far (in pixels) planes have to be away to not cause a separation violation.
	 */
	public static int separationRule = 64;
	/**
	 * How much the plane can turn per second, in radians.
	 */
	private double turnSpeed = Math.PI / 4;
	/**
	 * The position of the plane.
	 */
	private Vector position;
	/**
	 * The velocity of the plane.
	 */
	private Vector velocity;
	/**
	 * Whether the plane is being manually controlled.
	 */
	private boolean isManuallyControlled;
	/**
	 * The flight name of the plane.
	 */
	private String flightName;
	/**
	 * The position the plane is currently flying towards (if not manually controlled).
	 */
	private Vector currentTarget;
	/**
	 * The name of the location the plane is flying from.
	 */
	private String originName;
	/**
	 * The name of the location the plane is flying to.
	 */
	private String destinationName;
	/**
	 * An array of waypoints from the plane's origin to its destination.
	 */
	private Waypoint[] route;
	/**
	 * The current stage the plane is at in its route.
	 */
	private int currentRouteStage;
	/**
	 * The off-screen point the plane will end up at before disappearing.
	 */
	private Vector destination;
	/**
	 * The image to be drawn representing the plane.
	 */
	private graphics.Image image;
	/**
	 * Whether the plane has reached its destination and can be disposed of.
	 */
	private boolean hasFinished;
	/**
	 * The angle the plane is currently turning by.
	 */
	private double currentlyTurningBy;
	/**
	 * A list of planes that are too near.
	 */
	
	private int difficulty = 0;
	
	private java.util.ArrayList<Aircraft> planesTooNear = new java.util.ArrayList<Aircraft>();

	/**
	 * Constructor for an aircraft.
	 * @param name the name of the flight.
	 * @param nameOrigin the name of the location from which the plane hails.
	 * @param nameDestination the name of the location to which the plane is going.
	 * @param originPoint the point to initialise the plane.
	 * @param destinationPoint the end point of the plane's route.
	 * @param img the image to draw to represent the plane.
	 * @param speed the speed the plane will travel at.
	 * @param sceneWaypoints the waypoints on the map.
	 */
	public Aircraft(String name, String nameOrigin, String nameDestination, Waypoint originPoint, Waypoint destinationPoint, graphics.Image img, double speed, Waypoint[] sceneWaypoints, int difficulty) {
		flightName = name;
		destinationName = nameOrigin;
		originName = nameDestination;
		
		// Find a route between the origin waypoint and the destination, via waypoints in the current scene.
		route = findGreedyRoute(originPoint, destinationPoint, sceneWaypoints);
		//set the current target waypoint to the first waypoint in the route, ie. begin flying towards origin
		currentTarget = route[0].position();
		image = img;
		position = originPoint.position(); //place the new aircraft on the origin waypoint
		
		int altitudeOffset = randInt(0,1);
		switch(altitudeOffset){
		case 0:
			this.changeAltitude(28000);
			break;
		case 1:
			this.changeAltitude(30000);
			break;
		}
		
		//Offsets the spawn location of the aircraft around the origin waypoint, for variety
		//This also prevents collisions between just-spawned aircraft and existing aircraft flying to the waypoint.
		int side = randInt(0, 1); // pick a random int to determine an offset around the spawnpoint.
		int offset = 0;
		switch (side){ //offset spawn point to left
		case 0:
			offset = randInt(-separationRule, -10);
			System.out.println("Offset by left");
			break;
		case 1: //offset spawn point to right
			offset = randInt(10, separationRule);
			System.out.println("Offset right");
			break;
		}
		System.out.println("Offset by " + offset);
		
		position = position.add(new Vector(offset, 0, 0));//offset spawn position. Helps avoid aircraft crashes very soon after spawn
		
		destination = destinationPoint.position();
		isManuallyControlled = false;
		double x = currentTarget.x() - position.x();
		double y = currentTarget.y() - position.y();
		velocity = new Vector(x, y, 0).normalise().scaleBy(speed);
		hasFinished = false;
		currentRouteStage = 0;
		currentlyTurningBy = 0;
		
		switch (difficulty){
		//adjust the aircraft's attributes according to the difficulty of the parent scene.
		// 0 has the easiest attributes (slower aircraft, more forgiving separation rules.
		// 2 has the hardest attributes (faster aircrft, least forgiving separation rules.
		case 0:
			separationRule = 64;
			break;
		case 1:
			separationRule = 96;
			velocity = velocity.scaleBy(2);
			break;
		case 2:
			separationRule = 128;
			velocity = velocity.scaleBy(3);
			//At high velocities, the aircraft is allowed to turn faster
			//this helps keep the aircraft on track.
			turnSpeed = Math.PI / 2;
			break;
		}
		
		
	}

	/**
	 * Allows access to the plane's current position.
	 * @return the plane's current position.
	 */
	public Vector position() {
		return position;
	}
	
	/**
	 * Allows access to the plane's name.
	 * @return the plane's name.
	 */
	public String name () {
		return flightName;
	}
	
	/**
	 * Allows access to the name of the location from which this plane hails.
	 * @return the origin's name.
	 */
	public String originName() {
		return originName;
	}
	
	/**
	 * Allows access to the name of the location to which this plane travels.
	 * @return the destination's name.
	 */
	public String destinationName() {
		return destinationName;
	}
	
	/**
	 * Allows access to whether the plane has reached its destination.
	 * @return true, if the plane is to be disposed. False, otherwise.
	 */
	public boolean isFinished() {
		return hasFinished;
	}
	
	/**
	 * Allows access to whether the plane is being manually controlled.
	 * @return true, if the plane is currently manually controlled. False, otherwise.
	 */
	public boolean isManuallyControlled() {
		return isManuallyControlled;
	}

	/**
	 * Calculates the angle from the plane's position, to its current target.
	 * @return an angle in radians to the plane's current target.
	 */
	private double angleToTarget() {
		return Math.atan2(currentTarget.y() - position.y(), currentTarget.x() - position.x());
	}
	
	/**
	 * Checks whether the plane lies outside of the airspace.
	 * @return true, if the plane is out of the airspace. False, otherwise.
	 */
	private boolean outOfBounds() {
		double x = position.x();
		double y = position.y();
		return (x < RADIUS || x > window.width() + RADIUS - 32 || y < RADIUS || y > window.height() + RADIUS - 144);
	}

	/**
	 * Calculates the angle at which the plane is travelling.
	 * @return the angle in radians of the plane's current velocity.
	 */
	public double bearing() {
		return Math.atan2(velocity.y(), velocity.x());
	}
	
	/**
	 * Allows access to the magnitude of the plane's velocity. 
	 * @return the speed at which the plane is currently going.
	 */
	public double speed() {
		return velocity.magnitude();
	}
	
	/**
	 * 
	 * @param point
	 * @return true, if the plane is near enough the point. False, otherwise.
	 */
	public boolean isAt(Vector point) {
		double dy = point.y() - position.y();
		double dx = point.x() - position.x();
		return dy*dy + dx*dx < 4*4;
	}
	
	/**
	 * Checks whether the angle at which the plane is turning is less than 0.
	 * @return true, if the plane is turning left (anti-clockwise). False, otherwise.
	 */
	public boolean isTurningLeft() {
		return currentlyTurningBy < 0;
	}
	
	/**
	 * Checks whether the angle at which the plane is turning is greater than 0.
	 * @return true, if the plane is turning right (clockwise). False, otherwise.
	 */
	public boolean isTurningRight() {
		return currentlyTurningBy > 0;
	}
	
	/**
	 * Checks the plane's route to see if a waypoint is included in it.
	 * @param waypoint the waypoint to check for.
	 * @return true, if the waypoint is in the plane's route. False, otherwise.
	 */
	public int flightPathContains(Waypoint waypoint) {
		int index = -1;
		for (int i = 0; i < route.length; i ++) {
			if (route[i] == waypoint) index = i;
		}
		return index;
	}
	
	/**
	 * Edits the plane's path by changing the waypoint it will go to at a certain stage
	 * in its route.
	 * @param routeStage the stage at which the new waypoint will replace the old.
	 * @param newWaypoint the new waypoint to travel to.
	 */
	public void alterPath(int routeStage, Waypoint newWaypoint) {
		route[routeStage] = newWaypoint;
		if (!isManuallyControlled) resetBearing();
		if (routeStage == currentRouteStage){
			currentTarget = newWaypoint.position();
			turnTowardsTarget(0);
		}
	}
	
	/**
	 * Checks whether the mouse cursor is over this plane.
	 * @param mx the x coordinate of the mouse cursor.
	 * @param my the y coordinate of the mouse cursor.
	 * @return true, if the mouse is close enough to this plane. False, otherwise.
	 */
	public boolean isMouseOver(int mx, int my) {
		double dx = position.x() - mx;
		double dy = position.y() - my;
		return dx*dx + dy*dy < MOUSE_LENIANCY*MOUSE_LENIANCY;
	}
	/**
	 * Calls {@link isMouseOver()} using {@link input.mouseX()} and {@link input.mouseY()} as the arguments.
	 * @return true, if the mouse is close enough to this plane. False, otherwise.
	 */
	public boolean isMouseOver() { return isMouseOver(input.mouseX(), input.mouseY()); }
	
	/**
	 * Updates the plane's position and bearing, the stage of its route, and whether it has finished its flight.
	 * @param dt
	 */
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
	
	/**
	 * Turns the plane left.
	 * @param dt the time elapsed since the last frame.
	 */
	public void turnLeft(double dt) {
		turnBy(-dt * turnSpeed);
	}
	
	/**
	 * Turns the plane right.
	 * @param dt the time elapsed since the last frame.
	 */
	public void turnRight(double dt) {
		turnBy(dt * turnSpeed);
	}

	/**
	 * Turns the plane by a certain angle (in radians). Positive angles turn the plane clockwise.
	 * @param angle the angle by which to turn.
	 */
	private void turnBy(double angle) {
		currentlyTurningBy = angle;
		double cosA = Math.cos(angle);
		double sinA = Math.sin(angle);
		double x = velocity.x();
		double y = velocity.y();
		velocity = new Vector(x*cosA - y*sinA, y*cosA + x*sinA, velocity.z());
	}

	/**
	 * Turns the plane towards its current target. How much it turns is determined by the plane's
	 * {@link turnSpeed}.
	 * @param dt the time elapsed since the last frame.
	 */
	private void turnTowardsTarget(double dt) {
		// Get difference in angle
		double angleDifference = (angleToTarget() % (2 * Math.PI)) - (bearing() % (2 * Math.PI));
		boolean crossesPositiveNegativeDivide = angleDifference < -Math.PI * 7 / 8;
		// Correct difference
		angleDifference += Math.PI;
		angleDifference %= (2 * Math.PI);
		angleDifference -= Math.PI;
		// Get which way to turn.
		int angleDirection = (int)(angleDifference /= Math.abs(angleDifference));
		if (crossesPositiveNegativeDivide) angleDirection *= -1;  
		double angleMagnitude = Math.min(Math.abs((dt * turnSpeed)), Math.abs(angleDifference)); 
		turnBy(angleMagnitude * angleDirection);
	}
	
	/**
	 * Draws the plane and any warning circles if necessary. 
	 */
	public void draw(int controlAltitude) {
		double alpha = 255/((Math.abs(position.z() - controlAltitude) + 1000)/1000);
		double scale = 2*(position.z()/30000);
		graphics.setColour(128, 128, 128, alpha);
		graphics.draw(image, scale, position.x(), position.y(), bearing(), 8, 8);
		graphics.setColour(128, 128, 128, alpha/2.5);
		graphics.print(String.valueOf(position.z()) + "£", position.x()+8, position.y()-8);
		drawWarningCircles();
	}
	
	/**
	 * Draws warning circles around this plane and any others that are too near.
	 */
	private void drawWarningCircles() {
		for (Aircraft plane : planesTooNear) {
			Vector midPoint = position.add(plane.position).scaleBy(0.5);
			double radius = position.sub(midPoint).magnitude() * 2;
			graphics.setColour(128, 0, 0);
			graphics.circle(false, midPoint.x(), midPoint.y(), radius);
		}	
	}

	/**
	 * Draws lines starting from the plane, along its flight path to its destination.
	 */
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
	
	/**
	 * Visually represents the pathpoint being moved.
	 * @param mouseX current position of mouse
	 * @param mouseY current position of mouse
	 */
	public void drawModifiedPath(int modified, double mouseX, double mouseY) {
		graphics.setColour(0, 128, 128, 128);
		if (currentRouteStage > modified-1) {
			graphics.line(position().x(), position().y(), mouseX, mouseY);
		} else {
			graphics.line(route[modified-1].position().x(), route[modified-1].position().y(), mouseX, mouseY);
		}
		if (currentTarget == destination) {
			graphics.line(mouseX, mouseY, destination.x(), destination.y());
		} else {
			graphics.line(mouseX, mouseY, route[modified+1].position().x(), route[modified+1].position().y());
		}
	}
	
	@Deprecated
	private Waypoint[] findRandomRoute(Vector origin, Vector destination) {
		// Placeholder over-simplified version
		int n = 4;
		Waypoint[] route = new Waypoint[n];
		for (int i = 0; i < n; i ++ ) {
			route[i] = Demo.airspaceWaypoints[(int)( Math.random() * Demo.airspaceWaypoints.length )];
		}
		return route;
	}
	
	/**
	 * Creates a sensible route from an origin to a destination from an array of waypoints. 
	 * @param origin the waypoint from which to begin.
	 * @param destination the waypoint at which to end.
	 * @param waypoints the waypoints to be used.
	 * @return the optimal route between the origin and the destination, using a sensible amount of waypoint.
	 */
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
						| (point.isEntryOrExit() == true && (point.position().equals(destination.position()) == false))){
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
	
	@Deprecated
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
	
	/**
	 * 
	 * @param sceneWaypoints
	 * @param previous
	 * @param destination
	 * @return
	 */
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
	
	/**
	 * 
	 * @param point
	 * @param sceneWaypoints
	 * @return
	 */
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

	/**
	 * Updates the amount of planes that are too close, violating the separation rules,
	 * and also checks for crashes.
	 * @param dt the time elapsed since the last frame.
	 * @param scene the game scene object.
	 */
	public void updateCollisions(double dt, scn.Demo scene) {
		planesTooNear.clear();
		for (Aircraft plane : scene.aircraftList()) {
			if (plane != this && isWithin(plane, RADIUS)) {
				scene.gameOver(this, plane);
				hasFinished = true;
			} else if (plane != this && isWithin(plane, separationRule)) {
				planesTooNear.add(plane);
			}
		}
	}
	
	/**
	 * Checks whether an aircraft is within a certain distance from this one.
	 * @param aircraft the aircraft to check.
	 * @param distance the distance within which to care about.
	 * @return true, if the aircraft is within the distance. False, otherwise.
	 */
	private boolean isWithin(Aircraft aircraft, int distance) {
		double dx = aircraft.position().x() - position.x();
		double dy = aircraft.position().y() - position.y();
		double dz = aircraft.position().z() - position.z();
		return dx*dx + dy*dy + dz*dz < distance*distance;
	}

	/**
	 * Toggles the state of whether this plane is manually controlled.
	 */
	public void toggleManualControl() {
		isManuallyControlled = !isManuallyControlled;
		if (!isManuallyControlled) resetBearing();
	}
	
	/**
	 * Resets the direction towards which the plane will head.
	 */
	private void resetBearing() {
		if (currentRouteStage < route.length) {
			currentTarget = route[currentRouteStage].position();
		}
		turnTowardsTarget(0);
	}
	
	/**
	 * Increases the plane's altitude.
	 */
	public void climb() {
		if (position.z() < 30000)
			changeAltitude(2000);
	}
	
	/**
	 * Decreases the plane's altitude.
	 */
	public void fall() {
		if (position.z() > 28000)
			changeAltitude(-2000);
	}
	
	/**
	 * Changes the plane's altitude by a given amount.
	 * @param height the height by which to change altitude.
	 */
	private void changeAltitude(int height) {
		position = new Vector(position.x(), position.y(), position.z() + height);
	}
	
	private static int randInt(int min, int max){
		/**
		 * Generates a random integer between min and max, in the range [min, max]
		 * This method is inclusive of min AND max.
		 * @param min the lower boundary (included) for the random integer
		 * @param max the upper boundary (included) for the random integer
		 * @return a random integer
		 */
		Random rand = new Random();
		return rand.nextInt((max - min) + 1) + min;
	}

}
