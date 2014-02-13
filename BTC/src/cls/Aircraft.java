package cls;

import java.io.File;
import java.util.ArrayList;

import lib.RandomNumber;
import lib.jog.audio;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.graphics.Image;
import lib.jog.input;
import lib.jog.window;

import org.newdawn.slick.Color;

import scn.Demo;
import btc.Main;
import cls.Waypoint.WaypointType;

/**
 * <h1>Aircraft</h1>
 * <p>
 * Represents an in-game aircraft. Calculates velocity, route-following, etc.
 * </p>
 */
public class Aircraft {

	/**
	 * The physical size of the plane in pixels. This determines crashes.
	 */
	public final static int RADIUS = 16;
	/**
	 * How far away (in pixels) the mouse can be from the plane but still select
	 * it.
	 */
	public final static int MOUSE_LENIANCY = 16;
	/**
	 * How large to draw the bearing circle.
	 */
	public final static int COMPASS_RADIUS = 64;
	/**
	 * How far (in pixels) planes have to be away to not cause a separation
	 * violation.
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
	 * Whether the plane is landing.
	 */
	private boolean isLanding;
	/**
	 * The flight name of the plane.
	 */
	private final String flightName;
	/**
	 * The position the plane is currently flying towards (if not manually
	 * controlled).
	 */
	private Waypoint currentTarget;
	/**
	 * The target the player has told the plane to fly at when manually
	 * controlled.
	 */
	private double manualBearingTarget;
	/**
	 * The name of the location the plane is flying from.
	 */
	private final String originName;
	/**
	 * The name of the location the plane is flying to.
	 */
	private final String destinationName;
	/**
	 * An array of waypoints from the plane's origin to its destination.
	 */
	private final Waypoint[] route;
	/**
	 * The current stage the plane is at in its route.
	 */
	private int currentRouteStage;
	/**
	 * The off-screen point the plane will end up at before disappearing.
	 */
	private final Waypoint destination;
	/**
	 * The image to be drawn representing the plane.
	 */
	private final Image image;
	/**
	 * Whether the plane has reached its destination and can be disposed of.
	 */
	private boolean hasFinished;
	/**
	 * The angle the plane is currently turning by.
	 */
	private double currentlyTurningBy;
	/**
	 * Holds a list of planes currently in violation of separation rules with
	 * this plane
	 */
	private final ArrayList<Aircraft> planesTooNear = new ArrayList<Aircraft>();
	/**
	 * Index of altidudeList for the value of the Altidude the aircraft desires
	 * to be at.
	 */
	private int targetAltitudeIndex;
	/**
	 * A list holding the list of possible altitudes for the aircraft.
	 */
	private ArrayList<Integer> altitudeList = new ArrayList<Integer>();
	/**
	 * the speed to climb or fall by. Default 300 for easy mode
	 */
	private int altitudeChangeSpeed = 300;

	/**
	 * Whether this aircraft is currently in the airport
	 */
	private boolean atAirport = false;

	/**
	 * Flags whether the collision warning sound has been played before. If set,
	 * plane will not play warning again until it the separation violation
	 * involving it ends
	 */
	private boolean collisionWarningSoundFlag = false;

	/**
	 * A warning sound to be played when the plane enters separation violation.
	 */
	private final static Sound WARNING_SOUND = audio.newSoundEffect("sfx"
			+ File.separator + "beep.ogg");
	/**
	 * The number of points an aircraft enters the airspace with.
	 */
	private int points;

	private boolean wasBreachingInLastFrame = false;

	/**
	 * Constructor for an aircraft.
	 * 
	 * @param name
	 *            the name of the flight.
	 * @param nameOrigin
	 *            the name of the location from which the plane hails.
	 * @param nameDestination
	 *            the name of the location to which the plane is going.
	 * @param originPoint
	 *            the point to initialise the plane.
	 * @param destinationPoint
	 *            the end point of the plane's route.
	 * @param img
	 *            the image to draw to represent the plane.
	 * @param speed
	 *            the speed the plane will travel at.
	 * @param sceneWaypoints
	 *            the waypoints on the map.
	 * @param difficulty
	 *            the difficulty the game is set to
	 */
	public Aircraft(String name, String nameDestination, String nameOrigin,
			Waypoint destinationPoint, Waypoint originPoint, Image img,
			double speed, Waypoint[] sceneWaypoints, int difficulty,
			ArrayList<HoldingWaypoint> holdingWaypoints,
			Waypoint takeoffWaypoint, ArrayList<Aircraft> aircraftList) {
		flightName = name;
		destinationName = nameDestination;
		originName = nameOrigin;
		image = img;

		// Find route
		route = findGreedyRoute(originPoint, destinationPoint, sceneWaypoints,
				holdingWaypoints);
		destination = destinationPoint;

		// place on spawn waypoint
		position = originPoint.position();

		// Add list of aircraft cruising heights.
		altitudeList.add(100);
		altitudeList.add(5000);
		altitudeList.add(10000);
		altitudeList.add(15000);
		targetAltitudeIndex = RandomNumber.randInclusiveInt(1,
				altitudeList.size() - 1);
		int altitude = altitudeList.get(targetAltitudeIndex);

		// Checking that if an aircraft is near the waypoint that the new
		// aircraft is to be spawned at and has the same altitude, the new
		// aircraft must choose a different altitude.
		for (Aircraft aircraft : aircraftList) {
			if (!this.equals(aircraft) && isWithin(aircraft, 300)
					&& altitude == aircraft.position.z()) {
				int newTargetAltitudeIndex = targetAltitudeIndex;
				while (newTargetAltitudeIndex == targetAltitudeIndex)
					newTargetAltitudeIndex = RandomNumber.randInclusiveInt(1,
							altitudeList.size() - 1);
				altitude = altitudeList.get(targetAltitudeIndex);
			}
		}

		position = position.add(new Vector(0, 0, altitude));

		// if origin is airport, use the takeoff waypoint as the first one
		if (originPoint instanceof Airport) {
			route[0] = takeoffWaypoint;
		}

		// Calculate initial velocity (direction)
		currentTarget = route[0];

		double x = currentTarget.position().x() - position.x();
		double y = currentTarget.position().y() - position.y();

		velocity = new Vector(x, y, 0).normalise().scaleBy(speed);
		isLanding = false;
		isManuallyControlled = false;
		hasFinished = false;
		currentRouteStage = 0;
		currentlyTurningBy = 0;
		manualBearingTarget = Double.NaN;

		// Speed up plane for higher difficulties
		switch (difficulty) {
		// adjust the aircraft's attributes according to the difficulty of the
		// parent scene.
		// 0 has the easiest attributes (slower aircraft, more forgiving
		// separation rules.
		// 2 has the hardest attributes (faster aircraft, least forgiving
		// separation rules.
		case Demo.DIFFICULTY_EASY:
			separationRule = 128;
			velocity = velocity.scaleBy(1.5);
			altitudeChangeSpeed = 800;
			points = 10;
			break;

		case Demo.DIFFICULTY_MEDIUM:
			separationRule = 96;
			velocity = velocity.scaleBy(2);
			turnSpeed = Math.PI / 3;
			altitudeChangeSpeed = 600;
			points = 15;
			break;

		case Demo.DIFFICULTY_HARD:
			separationRule = 64;
			velocity = velocity.scaleBy(3);
			// At high velocities, the aircraft is allowed to turn faster
			// this helps keep the aircraft on track.
			turnSpeed = Math.PI / 2;
			altitudeChangeSpeed = 400;
			points = 20;
			break;

		default:
			Exception e = new Exception("Invalid Difficulty : " + difficulty
					+ ".");
			e.printStackTrace();
		}
	}

	/**
	 * Allows access to the plane's current position.
	 * 
	 * @return the plane's current position.
	 */
	public Vector position() {
		return position;
	}

	/**
	 * Allows access to the plane's name.
	 * 
	 * @return the plane's name.
	 */
	public String name() {
		return flightName;
	}

	/**
	 * Allows access to the name of the location from which this plane hails.
	 * 
	 * @return the origin's name.
	 */
	public String originName() {
		return originName;
	}

	/**
	 * Allows access to the name of the location to which this plane travels.
	 * 
	 * @return the destination's name.
	 */
	public String destinationName() {
		return destinationName;
	}

	/**
	 * Allows access to whether the plane has reached its destination.
	 * 
	 * @return true, if the plane is to be disposed. False, otherwise.
	 */
	public boolean isFinished() {
		return hasFinished;
	}

	/**
	 * Whether the aircraft is in the airport at the moment.
	 * 
	 * @return
	 */
	public boolean atAirport() {
		return atAirport;
	}

	/**
	 * Allows access to whether the plane is being manually controlled.
	 * 
	 * @return true, if the plane is currently manually controlled. False,
	 *         otherwise.
	 */
	public boolean isManuallyControlled() {
		return isManuallyControlled;
	}

	// Allow access to whether plane is in the process of landing
	public boolean isLanding() {
		return isLanding;
	}

	/**
	 * Calculates the angle from the plane's position, to its current target.
	 * 
	 * @return an angle in radians to the plane's current target.
	 */
	private double angleToTarget() {
		if (isManuallyControlled) {
			return (manualBearingTarget == Double.NaN) ? bearing()
					: manualBearingTarget;
		} else {
			return Math.atan2(currentTarget.position().y() - position.y(),
					currentTarget.position().x() - position.x());
		}
	}

	/**
	 * Checks whether the plane lies outside of the airspace.
	 * 
	 * @return true, if the plane is out of the airspace. False, otherwise.
	 */
	public boolean outOfBounds() {
		double x = position.x();
		double y = position.y();
		return (x < RADIUS || x > window.width() + RADIUS - 32 || y < RADIUS || y > window
				.height() + RADIUS - 144);
	}

	/**
	 * Calculates the angle at which the plane is travelling.
	 * 
	 * @return the angle in radians of the plane's current velocity.
	 */
	public double bearing() {
		return Math.atan2(velocity.y(), velocity.x());
	}

	/**
	 * Allows access to the magnitude of the plane's velocity.
	 * 
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
		return dy * dy + dx * dx < 4 * 4;
	}

	/**
	 * Checks whether the angle at which the plane is turning is less than 0.
	 * 
	 * @return true, if the plane is turning left (anti-clockwise). False,
	 *         otherwise.
	 */
	public boolean isTurningLeft() {
		return currentlyTurningBy < 0;
	}

	/**
	 * Checks whether the angle at which the plane is turning is greater than 0.
	 * 
	 * @return true, if the plane is turning right (clockwise). False,
	 *         otherwise.
	 */
	public boolean isTurningRight() {
		return currentlyTurningBy > 0;
	}

	/**
	 * Checks the plane's route to see if a waypoint is included in it.
	 * 
	 * @param waypoint
	 *            the waypoint to check for.
	 * @return true, if the waypoint is in the plane's route. False, otherwise.
	 */
	public int flightPathContains(Waypoint waypoint) {
		int index = -1;

		for (int i = 0; i < route.length; i++) {
			if (route[i] == waypoint)
				index = i;
		}

		return index;
	}

	/**
	 * Edits the plane's path by changing the waypoint it will go to at a
	 * certain stage in its route.
	 * 
	 * @param routeStage
	 *            the stage at which the new waypoint will replace the old.
	 * @param newWaypoint
	 *            the new waypoint to travel to.
	 */
	public void alterPath(int routeStage, Waypoint newWaypoint) {
		if ((routeStage == route.length - 1)
				&& (destination instanceof Airport)) {
			return;
		}
		route[routeStage] = newWaypoint;

		if (!isManuallyControlled)
			resetBearing();

		if (routeStage == currentRouteStage) {
			currentTarget = newWaypoint;
			// turnTowardsTarget(0);
		}
	}

	/**
	 * Checks whether the mouse cursor is over this plane.
	 * 
	 * @param mx
	 *            the x coordinate of the mouse cursor.
	 * @param my
	 *            the y coordinate of the mouse cursor.
	 * @return true, if the mouse is close enough to this plane. False,
	 *         otherwise.
	 */
	public boolean isMouseOver(int mx, int my) {
		double dx = position.x() - mx;
		double dy = position.y() - my;

		return dx * dx + dy * dy < MOUSE_LENIANCY * MOUSE_LENIANCY;
	}

	/**
	 * Calls {@link isMouseOver()} using {@link input.mouseX()} and {@link
	 * input.mouseY()} as the arguments.
	 * 
	 * @return true, if the mouse is close enough to this plane. False,
	 *         otherwise.
	 */
	public boolean isMouseOver() {
		return isMouseOver(input.mouseX(), input.mouseY());
	}

	/**
	 * Updates the plane's position and bearing, the stage of its route, and
	 * whether it has finished its flight.
	 * 
	 * @param dt
	 * @throws Exception
	 */
	public void update(double dt) throws IllegalStateException {
		if (hasFinished)
			return;
		if (altitudeList.get(targetAltitudeIndex) + 100 >= (int) this.position
				.z()
				&& altitudeList.get(targetAltitudeIndex) - 100 <= (int) this.position
						.z()) {
			this.position.setZ(altitudeList.get(targetAltitudeIndex));
			changeAltitude(0);
		} else if (altitudeList.get(targetAltitudeIndex) > (int) this.position
				.z()) {
			climb();
		} else {
			fall();
		}

		// Update position
		Vector dv = velocity.scaleBy(dt);
		position = position.add(dv);

		currentlyTurningBy = 0;

		// Update target waypoint
		if (isAt(currentTarget.position()) && currentTarget.equals(destination)) {
			hasFinished = true;
			if (destination instanceof Airport) {
				((Airport) destination).insertAircraft(this);
				atAirport = true;
			}

		} else if (isAt(currentTarget.position())
				&& (currentRouteStage == route.length - 1)) {
			currentRouteStage++;
			currentTarget = destination;

		} else if (isAt(currentTarget.position())) {
			if (currentTarget instanceof HoldingWaypoint) {

				// Changes the current waypoint to the next holding waypoint in
				// the airport circle
				this.alterPath(this.flightPathContains(currentTarget),
						((HoldingWaypoint) currentTarget).getNextWaypoint());
			} else {
				currentRouteStage++;
				currentTarget = route[currentRouteStage];
			}
		}

		// Update bearing
		if (Math.abs(angleToTarget() - bearing()) > 0.01) {
			turnTowardsTarget(dt);
		}
	}

	/**
	 * Turns the plane left.
	 * 
	 * @param dt
	 *            the time elapsed since the last frame.
	 */
	public void turnLeft(double dt) {
		turnBy(-dt * turnSpeed);
		manualBearingTarget = Double.NaN;
	}

	/**
	 * Turns the plane right.
	 * 
	 * @param dt
	 *            the time elapsed since the last frame.
	 */
	public void turnRight(double dt) {
		turnBy(dt * turnSpeed);
		manualBearingTarget = Double.NaN;
	}

	/**
	 * Turns the plane by a certain angle (in radians). Positive angles turn the
	 * plane clockwise.
	 * 
	 * @param angle
	 *            the angle by which to turn.
	 */
	private void turnBy(double angle) {
		currentlyTurningBy = angle;

		double cosA = Math.cos(angle);
		double sinA = Math.sin(angle);

		double x = velocity.x();
		double y = velocity.y();

		velocity = new Vector(x * cosA - y * sinA, y * cosA + x * sinA,
				velocity.z());
	}

	/**
	 * Turns the plane towards its current target. How much it turns is
	 * determined by the plane's {@link turnSpeed}.
	 * 
	 * @param dt
	 *            the time elapsed since the last frame.
	 */
	private void turnTowardsTarget(double dt) {
		// Get difference in angle
		double angleDifference = (angleToTarget() % (2 * Math.PI))
				- (bearing() % (2 * Math.PI));
		boolean crossesPositiveNegativeDivide = angleDifference < -Math.PI * 7 / 8;

		// Correct difference
		angleDifference += Math.PI;
		angleDifference %= (2 * Math.PI);
		angleDifference -= Math.PI;

		// Get which way to turn.
		int angleDirection = (int) (angleDifference /= Math
				.abs(angleDifference));

		if (crossesPositiveNegativeDivide)
			angleDirection *= -1;

		double angleMagnitude = Math.min(Math.abs((dt * turnSpeed)),
				Math.abs(angleDifference));

		turnBy(angleMagnitude * angleDirection);
	}

	/**
	 * Draws the plane and any warning circles if necessary.
	 */
	public void draw() {
		float alpha = 255;
		float scale = 2;

		Color grey = new Color(128, 128, 128, 255);

		// draws the aircraft itself
		graphics.setColour(grey);
		graphics.draw(image, scale, position.x(), position.y(), bearing(), 8, 8);

		// draw the altitude near the aircraft
		// £ is rendered as cursive "ft" for mysterious reasons
		graphics.setColour(grey);
		graphics.print(String.format("%.0f", position.z()) + "£",
				position.x() - 22, position.y() + 15);

		// draw the 'land me' message once an aircraft is circling the airport
		if (currentTarget instanceof HoldingWaypoint) {
			graphics.setColour(grey);
			graphics.print("Land me!", position.x() - 28, position.y() - 22);
		}

		drawWarningCircles();
	}

	/**
	 * Draws the compass around this plane
	 */
	public void drawCompass() {

		graphics.setColour(Main.GREEN);
		graphics.circle(false, position.x() + 16, position.y() + 16,
				COMPASS_RADIUS);

		for (int i = 0; i < 360; i += 60) {
			double r = Math.toRadians(i - 90);
			double x = position.x() + 16 + (1.1 * COMPASS_RADIUS * Math.cos(r));
			double y = position.y() + 14 + (1.1 * COMPASS_RADIUS * Math.sin(r));
			if (i > 170)
				x -= 24;
			if (i == 180)
				x += 12;
			graphics.print(String.valueOf(i), x, y);
		}

		double x, y;

		if (isManuallyControlled && input.isMouseDown(input.MOUSE_LEFT)) {
			graphics.setColour(0, 128, 0, 128);
			double r = Math.atan2(input.mouseY() - position.y(), input.mouseX()
					- position.x());
			x = 16 + position.x() + (COMPASS_RADIUS * Math.cos(r));
			y = 16 + position.y() + (COMPASS_RADIUS * Math.sin(r));
			graphics.line(position.x() + 16, position.y() + 16, x, y);
			graphics.line(position.x() + 15, position.y() + 16, x, y);
			graphics.line(position.x() + 16, position.y() + 15, x, y);
			graphics.line(position.x() + 17, position.y() + 16, x, y);
			graphics.line(position.x() + 17, position.y() + 17, x, y);
			graphics.setColour(0, 128, 0, 16);
		}

		x = 16 + position.x() + (COMPASS_RADIUS * Math.cos(bearing()));
		y = 16 + position.y() + (COMPASS_RADIUS * Math.sin(bearing()));

		graphics.line(position.x() + 16, position.y() + 16, x, y);
		graphics.line(position.x() + 15, position.y() + 16, x, y);
		graphics.line(position.x() + 16, position.y() + 15, x, y);
		graphics.line(position.x() + 17, position.y() + 16, x, y);
		graphics.line(position.x() + 17, position.y() + 17, x, y);

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
	 * Draws lines starting from the plane, along its flight path to its
	 * destination.
	 */
	public void drawFlightPath() {
		graphics.setColour(0, 128, 128);

		if (currentTarget != destination) {
			graphics.line(position.x(), position.y(), route[currentRouteStage]
					.position().x(), route[currentRouteStage].position().y());
		}

		for (int i = currentRouteStage; i < route.length - 1; i++) {
			graphics.line(route[i].position().x(), route[i].position().y(),
					route[i + 1].position().x(), route[i + 1].position().y());
		}

		if (currentTarget == destination) {
			graphics.line(position.x(), position.y(), destination.position()
					.x(), destination.position().y());
		} else {
			graphics.line(route[route.length - 1].position().x(),
					route[route.length - 1].position().y(), destination
							.position().x(), destination.position().y());
		}
	}

	/**
	 * Visually represents the pathpoint being moved.
	 * 
	 * @param mouseX
	 *            current position of mouse
	 * @param mouseY
	 *            current position of mouse
	 */
	public void drawModifiedPath(int modified, double mouseX, double mouseY) {
		graphics.setColour(0, 128, 128, 128);

		if (currentRouteStage > modified - 1) {
			graphics.line(position().x(), position().y(), mouseX, mouseY);
		} else {
			graphics.line(route[modified - 1].position().x(),
					route[modified - 1].position().y(), mouseX, mouseY);
		}

		if (currentTarget == destination) {

			graphics.line(mouseX, mouseY, destination.position().x(),
					destination.position().y());

		} else {

			int index = modified + 1;

			if (index == route.length) { // modifying final waypoint in route
				// line drawn to final waypoint
				graphics.line(mouseX, mouseY, destination.position().x(),
						destination.position().y());
			} else {
				graphics.line(mouseX, mouseY, route[index].position().x(),
						route[index].position().y());
			}

		}
	}

	/**
	 * Creates a sensible route from an origin to a destination from an array of
	 * waypoints. Waypoint costs are considered according to distance from
	 * current aircraft location Costs are further weighted by distance from
	 * waypoint to destination.
	 * 
	 * @param origin
	 *            the waypoint from which to begin.
	 * @param destination
	 *            the waypoint at which to end.
	 * @param waypoints
	 *            the waypoints to be used.
	 * @return a sensible route between the origin and the destination, using a
	 *         sensible amount of waypoint.
	 */
	public Waypoint[] findGreedyRoute(Waypoint origin, Waypoint destination,
			Waypoint[] waypoints, ArrayList<HoldingWaypoint> holdingWaypoints) {
		// to hold the route as we generate it.
		ArrayList<Waypoint> selectedWaypoints = new ArrayList<Waypoint>();

		// initialise the origin as the first point in the route.
		// selectedWaypoints.add(origin);
		// to track our position as we generate the route. Initialise to the
		// start of the route
		Waypoint currentPos = origin;

		// to track the closest next waypoint
		double cost = 99999999999999.0;
		Waypoint cheapest = null;
		// to track if the route is complete
		boolean atDestination = false;

		while (!atDestination) {
			for (Waypoint point : waypoints) {
				boolean skip = false;

				for (Waypoint routePoints : selectedWaypoints) {
					// check we have not already selected the waypoint
					// if we have, skip evaluating the point
					// this protects the aircraft from getting stuck looping
					// between points
					if (routePoints.position().equals(point.position())) {
						skip = true; // flag to skip
						break; // no need to check rest of list, already found a
								// match.
					}
				}
				// do not consider the waypoint we are currently at or the
				// origin
				// do not consider offscreen waypoints which are not the
				// destination
				// also skip if flagged as a previously selected waypoint
				if (skip == true
						|| point.position().equals(currentPos.position())
						|| point.position().equals(origin.position())
						|| (point.getType() == WaypointType.ENTRY_EXIT && (point
								.position().equals(destination.position()) == false))) {
					skip = false; // reset flag
					continue;

				} else {
					/*
					 * get cost of visiting waypoint compare cost vs current
					 * cheapest if smaller, replace
					 */
					if (point.getCost(currentPos) + 0.5
							* Waypoint.getCostBetween(point, destination) < cost) {
						// cheaper route found, update
						cheapest = point;
						cost = point.getCost(currentPos) + 0.5
								* Waypoint.getCostBetween(point, destination);
					}
				}

			} // end for - evaluated all waypoints
				// The cheapest waypoint must have been found
			assert cheapest != null : "The cheapest waypoint was not found";

			if (cheapest.position().equals(destination.position())) {
				/*
				 * route has reached destination break out of while loop
				 */
				atDestination = true;
			}
			// update the selected route
			// consider further points in route from the position of the
			// selected point
			// remove previous instances of a point
			selectedWaypoints.remove(cheapest);
			selectedWaypoints.add(cheapest);
			currentPos = cheapest;
			// resaturate cost for next loop
			cost = 99999999999.0;

		} // end while

		if (destination instanceof Airport) {
			Waypoint nearestHoldingWaypoint = holdingWaypoints.get(3);
			double cheapestCost = holdingWaypoints.get(0).getCost(origin);

			for (HoldingWaypoint holdingPoint : holdingWaypoints) {
				if (holdingPoint.getCost(origin) < cheapestCost) {
					nearestHoldingWaypoint = holdingPoint;
					cheapestCost = holdingPoint.getCost(origin);
				}
			}

			selectedWaypoints.add(selectedWaypoints.size() - 1,
					nearestHoldingWaypoint);
		}

		// create a Waypoint[] to hold the new route
		Waypoint[] route = new Waypoint[selectedWaypoints.size()];

		// fill route with the selected waypoints
		for (int i = 0; i < selectedWaypoints.size(); i++) {
			route[i] = selectedWaypoints.get(i);
		}

		return route;
	}

	/**
	 * Updates the amount of planes that are too close, violating the separation
	 * rules, and also checks for crashes.
	 * 
	 * @param dt
	 *            the time elapsed since the last frame.
	 * @param scene
	 *            the game scene object.
	 * @return 0 if no collisions, 1 if separation violation, 2 if crash
	 */
	public int updateCollisions(double dt, ArrayList<Aircraft> aircraftList) {
		planesTooNear.clear();

		for (int i = 0; i < aircraftList.size(); i++) {

			Aircraft plane = aircraftList.get(i);

			if (plane != this && isWithin(plane, RADIUS)) {

				hasFinished = true;

				return i;

			} else if (plane != this && isWithin(plane, separationRule)) {
				// When separation rules are breached
				planesTooNear.add(plane);

				if (collisionWarningSoundFlag == false) {
					collisionWarningSoundFlag = true;
					WARNING_SOUND.play();
				}
				if (wasBreachingInLastFrame == false) {
					wasBreachingInLastFrame = true;
					points -= 5;
				}
			}
		}

		if (planesTooNear.isEmpty()) {
			collisionWarningSoundFlag = false;
			wasBreachingInLastFrame = false;
		}
		return -1;
	}

	/**
	 * Checks whether an aircraft is within a certain distance from this one.
	 * 
	 * @param aircraft
	 *            the aircraft to check.
	 * @param distance
	 *            the distance within which to care about.
	 * @return true, if the aircraft is within the distance. False, otherwise.
	 */
	private boolean isWithin(Aircraft aircraft, int distance) {

		double dx = aircraft.position().x() - position.x();
		double dy = aircraft.position().y() - position.y();
		double dz = aircraft.position().z() - position.z();

		return dx * dx + dy * dy + dz * dz < distance * distance;
	}

	/**
	 * Toggles the state of whether this plane is manually controlled.
	 */
	public void toggleManualControl() {
		isManuallyControlled = !isManuallyControlled;

		if (!isManuallyControlled)
			resetBearing();
	}

	public void setManualControl(boolean manual) {
		isManuallyControlled = manual;
	}

	/**
	 * Triggered when land button is pressed. Causes plane to immediately target
	 * airport, and changes other aircraft properties shown below.
	 * 
	 * The command to land an aircraft cannot be reneged upon.
	 */
	public void toggleLand() {
		isLanding = !isLanding;
		if (isLanding) {
			currentRouteStage++;
			currentTarget = destination;
			targetAltitudeIndex = 0;
			velocity = velocity.scaleBy(0.7);
		}
	}

	/**
	 * Changes the direction the plane is going towards.
	 * 
	 * @param newHeading
	 */
	public void setBearing(double newHeading) {
		manualBearingTarget = newHeading;
	}

	/**
	 * Resets the direction towards which the plane will head.
	 */
	private void resetBearing() {
		if (currentRouteStage < route.length) {
			currentTarget = route[currentRouteStage];
		}

		turnTowardsTarget(0);
	}

	/**
	 * Increases the plane's altitude to altitudeList.get(targetAltitudeIndex).
	 */
	public void climb() {
		if (position.z() < altitudeList.get(targetAltitudeIndex))
			changeAltitude(altitudeChangeSpeed);
		if (position.z() >= altitudeList.get(targetAltitudeIndex)) {
			changeAltitude(0);
			position = new Vector(position.x(), position.y(),
					altitudeList.get(targetAltitudeIndex));
		}
	}

	/**
	 * Decreases the plane's altitude to altitudeList.get(targetAltitudeIndex).
	 */
	public void fall() {
		if (position.z() > altitudeList.get(targetAltitudeIndex))
			changeAltitude(-altitudeChangeSpeed);

		if (position.z() <= altitudeList.get(targetAltitudeIndex)) {
			position = new Vector(position.x(), position.y(),
					altitudeList.get(targetAltitudeIndex));
		}
	}

	/**
	 * Changes the plane's altitude by a given amount.
	 * 
	 * @param height
	 *            the height by which to change altitude.
	 */
	private void changeAltitude(int height) {
		velocity.setZ(height);
	}

	/**
	 * Decrements the targetAltitudeIndex by 1.
	 */
	public void decreaseTargetAltitude() {
		if (targetAltitudeIndex <= 1)
			return;
		else {
			targetAltitudeIndex--;
		}
	}

	public void increaseTargetAltitude() {
		if (targetAltitudeIndex == 3)
			return;
		else {
			targetAltitudeIndex++;
		}
	}

	public void setAltitude(int altitude) {
		position.setZ(altitude);
	}

	public int getPoints() {
		return this.points;
	}

	public Waypoint getDestination() {
		return destination;
	}
}
