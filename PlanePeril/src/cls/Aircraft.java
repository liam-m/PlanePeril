package cls;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import lib.RandomNumber;
import lib.jog.audio;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.graphics.Image;
import lib.jog.input;
import lib.jog.window;

import pp.Main;

import scn.SinglePlayer;

/**
 * <h1>Aircraft</h1>
 * <p>Represents an in-game aircraft. Calculates velocity, route-following, etc.</p>
 */
public class Aircraft {
	public final static int RADIUS = 16; // The physical size of the plane in pixels. This determines crashes.
	public final static int MOUSE_LENIANCY = 16; // How far away (in pixels) the mouse can be from the plane but still select it
	public final static int COMPASS_RADIUS = 64; // How large to draw the bearing circle.

	private final class Texts { // Text to show above the aircraft
		public final static String LAND_ME = "Land me!";
		public final static String LANDING = "Landing";
	}
	private String infoText = Texts.LAND_ME; // Which info text to display now, from Aircraft.Texts
	
	private boolean collision_warning_sound_flag = false; // Flags whether the collision warning sound has been played before. If set, plane will not play warning again until it the separation violation involving it ends
	private final static Sound WARNING_SOUND = audio.newSoundEffect("sfx" + File.separator + "beep.ogg"); // A warning sound to be played when the plane enters separation violation.
	private boolean was_breaching_in_last_frame = false;
	
	public int minimum_separation_distance = 128;
	private int num_points; // The number of points (score) an aircraft enters the airspace with.
	private final static float LANDING_SPEED = 0.6f; // Scalar for the velocity which is imposed upon landing	

	private double turning_speed = Math.PI / 4; // How much the plane can turn per second, in radians.
	private int altitude_change_speed = 300; // the speed to climb or fall by. Default 300 for easy mode
	private final String name; // An array of waypoints from the plane's origin to its destination.
	private final Image image; // The image to be drawn representing the plane.

	private FlightPlan flight_plan;
	private int current_route_stage = 0; // The current stage the plane is at in its flight_plan.getRoute().

	private Vector position, velocity;
	private boolean is_manually_controlled = false;
	private boolean is_landing = false;
	private boolean has_finished = false; // Whether the plane has reached its destination and can be disposed of.
	private Waypoint current_target; // The position the plane is currently flying towards
	private double manual_bearing_target = Double.NaN; // The target the player has told the plane to fly at when manually controlled.
	private double current_turning_angle = 0; // The angle the plane is currently turning by.
	private boolean is_at_airport = false; // Whether this aircraft is currently in the airport

	public final ArrayList<Aircraft> planes_too_near = new ArrayList<Aircraft>(); // List of planes currently in violation of separation rules with this plane

	private int target_altitude_index; // Index of altidudeList for the value of the Altidude the aircraft desires to be at.
	public int getTargetAltitudeIndex() {
		return target_altitude_index;
	}

	public static ArrayList<Integer> altitude_list; // A list holding the list of possible altitudes for the aircraft.

	private double initial_speed;
	
	public double getInitialSpeed() {
		return initial_speed;
	}
	
	/**
	 * Constructor for an aircraft.
	 * 
	 * @param name The name of the flight.
	 * @param img The image to draw to represent the plane.
	 * @param speed The speed the plane will travel at.
	 * @param difficulty Difficulty of the game, changes speed of aircraft and starting points for each aircraft
	 * @param flight_plan The flightplan, has the destination, origin and used to generate the actual route
	 * @param preferred_altitude_index used when plane needs to be created in specific altitude. Set to -1 or less for choosing altitude randomly.
	 */
	public Aircraft(String name, Image img, int speed, int difficulty, FlightPlan flight_plan, int preferred_altitude_index) {
		this.name = name;
		this.flight_plan = flight_plan;
		this.image = img;
		this.initial_speed = speed;

		this.position = flight_plan.getOrigin().position(); // Place on spawn waypoint

		// Add list of aircraft cruising heights. First entry only used when aircraft is landing/taking off.
		altitude_list = new ArrayList<Integer>(Arrays.asList(100, 5000, 10000, 15000));
		
		this.target_altitude_index = preferred_altitude_index;
		if (this.target_altitude_index < 0)
			this.target_altitude_index = RandomNumber.randInclusiveInt(1, altitude_list.size() - 1);

		int altitude = altitude_list.get(target_altitude_index);

		this.position = position.add(new Vector(0, 0, altitude));

		this.current_target = flight_plan.getRoute()[0]; // Calculate initial velocity (direction)

		double x = current_target.position().x() - position.x();
		double y = current_target.position().y() - position.y();

		this.velocity = new Vector(x, y, 0).normalise().scaleBy(speed);

		// Speed up plane for higher difficulties
		switch (difficulty) {
			case SinglePlayer.DIFFICULTY_EASY:
				this.velocity = velocity.scaleBy(1.0);
				this.altitude_change_speed = 800;
				this.num_points = 10;
				break;
	
			case SinglePlayer.DIFFICULTY_MEDIUM:
				this.velocity = velocity.scaleBy(2);
				this.turning_speed = Math.PI / 3;
				this.altitude_change_speed = 600;
				this.num_points = 15;
				break;
	
			case SinglePlayer.DIFFICULTY_HARD:
				this.velocity = velocity.scaleBy(3);
				this.turning_speed = Math.PI / 2; // At high velocities, the aircraft is allowed to turn faster - this helps keep the aircraft on track.
				this.altitude_change_speed = 400;
				this.num_points = 20;
				break;
	
			default:
				Exception e = new Exception("Invalid Difficulty : " + difficulty + ".");
				e.printStackTrace();
		}
	}

	/**
	 * Allows access to the plane's current position.
	 * @return the plane's current position.
	 */
	public Vector getPosition() {
		return position;
	}

	/**
	 * Allows access to the plane's current target.
	 * @return current_target
	 */
	public Waypoint getCurrentTarget() {
		return current_target;
	}
	
	/**
	 * Allows access to the plane's name.
	 * @return the plane's name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Allows access to whether the plane has reached its destination.
	 * 
	 * @return true, if the plane is to be disposed. False, otherwise.
	 */
	public boolean hasFinished() {
		return has_finished;
	}

	/**
	 * Whether the aircraft is in the airport at the moment.
	 * 
	 * @return whether the aircraft is at the airport
	 */
	public boolean isAtAirport() {
		return is_at_airport;
	}

	/**
	 * Allows access to whether the plane is being manually controlled.
	 * 
	 * @return True, if the plane is currently manually controlled. False, otherwise.
	 */
	public boolean isManuallyControlled() {
		return is_manually_controlled;
	}

	/**
	 * Allow access to whether plane is in the process of landing
	 * 
	 * @return
	 */
	public boolean isLanding() {
		return is_landing;
	}

	/**
	 * Calculates the angle from the plane's position, to its current target.
	 * @return an angle in radians to the plane's current target.
	 */
	private double angleToTarget() {
		if (is_manually_controlled) {
			return (manual_bearing_target == Double.NaN) ? getBearing() : manual_bearing_target;
		} else {
			return Math.atan2(current_target.position().y() - position.y(),	current_target.position().x() - position.x());
		}
	}

	/**
	 * Checks whether the plane lies outside of the airspace.
	 * @return true, if the plane is out of the airspace. False, otherwise.
	 */
	public boolean isOutOfBounds() {
		double x = position.x();
		double y = position.y();
		return (x < 0 || x > window.getWidth() - 32 || y < 0 || y > window.getHeight() - 144);
	}

	/**
	 * Checks if an aircraft is close to its entry point (sent as parameter).
	 * @param position position of an entry point
	 * @return true if closer than a minimum distance
	 */
	public boolean isCloseToEntry(Vector position) {
		double x = this.getPosition().x() - position.x();
		double y = this.getPosition().y() - position.y();
		return x*x + y*y <= 300*300;
	}
	
	/**
	 * Calculates the angle at which the plane is travelling.
	 * @return the angle in radians of the plane's current velocity.
	 */
	public double getBearing() {
		return Math.atan2(velocity.y(), velocity.x());
	}

	/**
	 * Allows access to the magnitude of the plane's velocity.
	 * @return the speed at which the plane is currently going.
	 */
	public double getSpeed() {
		return velocity.magnitude();
	}
	
	/**
	 * Get how many points the aircraft has accumulated after all the breaches
	 * 
	 * @return points
	 */
	public int getNumPoints() {
		return this.num_points;
	}

	/**
	 * The aircraft's flight plan
	 * 
	 * @return Flight plan
	 */
	public FlightPlan getFlightPlan() {
		return flight_plan;
	}

	/**
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
	 * @return true, if the plane is turning left (anti-clockwise). False, otherwise.
	 */
	public boolean isTurningLeft() {
		return current_turning_angle < 0;
	}

	/**
	 * Checks whether the angle at which the plane is turning is greater than 0.
	 * 
	 * @return true, if the plane is turning right (clockwise). False, otherwise.
	 */
	public boolean isTurningRight() {
		return current_turning_angle > 0;
	}

	/**
	 * Checks the plane's route to see if a waypoint is included in it.
	 * 
	 * @param waypoint The waypoint to check for.
	 * @return The index of the waypoint in the flight path. If it is not in the flight path, -1.
	 */
	public int indexInFlightPath(Waypoint waypoint) {
		for (int i = 0; i < flight_plan.getRoute().length; i++) {
			if (flight_plan.getRoute()[i] == waypoint)
				return i;
		}
		return -1;
	}
	
	/**
	 * Changes the plane's altitude by a given amount.
	 * 
	 * @param height The height by which to change altitude.
	 */
	private void changeAltitude(int height) {
		velocity.setZ(height);
	}

	/**
	 * Decrements the targetAltitudeIndex by 1.
	 */
	public void decreaseTargetAltitude() {
		if (target_altitude_index > 1)
			target_altitude_index--;
	}

	/**
	 * Increases the target altitude by an index of 1.
	 */
	public void increaseTargetAltitude() {
		if (target_altitude_index < 3)
			target_altitude_index++;
	}

	/**
	 * Manually set the altitude to a specific value. Used when an aircraft takes off.
	 * 
	 * @param altitude
	 */
	public void setAltitude(int altitude) {
		position.setZ(altitude);
	}
	
	/**
	 * Changes the direction the plane is going towards.
	 * 
	 * @param new_heading
	 */
	public void setBearing(double new_heading) {
		manual_bearing_target = new_heading;
	}

	/**
	 * Resets the direction towards which the plane will head.
	 */
	private void resetBearing() {
		if (current_route_stage < flight_plan.getRoute().length) {
			current_target = flight_plan.getRoute()[current_route_stage];
			turnTowardsTarget(0);
		}
	}

	/**
	 * Edits the plane's path by changing the waypoint it will go to at a
	 * certain stage in its flight_plan.getRoute().
	 * 
	 * @param route_stage The stage at which the new waypoint will replace the old.
	 * @param new_waypoint The new waypoint to travel to.
	 */
	public void alterPath(int route_stage, Waypoint new_waypoint) {
		if (route_stage != flight_plan.getRoute().length-1 || !(flight_plan.getDestination() instanceof Airport)) {
			flight_plan.getRoute()[route_stage] = new_waypoint;

			if (!is_manually_controlled)
				resetBearing();
			if (route_stage == current_route_stage)
				current_target = new_waypoint;
		}
	}

	/**
	 * Checks whether the mouse cursor is over this plane.
	 * 
	 * @param mouse_x The x coordinate of the mouse cursor.
	 * @param mouse_y The y coordinate of the mouse cursor.
	 * @return true, if the mouse is close enough to this plane. False, otherwise.
	 */
	public boolean isMouseOver(int mouse_x, int mouse_y) {
		double dx = position.x() - mouse_x;
		double dy = position.y() - mouse_y;

		return dx * dx + dy * dy < MOUSE_LENIANCY * MOUSE_LENIANCY;
	}

	/**
	 * Calls {@link isMouseOver()} using {@link input.mouseX()} and {@link
	 * input.mouseY()} as the arguments.
	 * 
	 * @return true, if the mouse is close enough to this plane. False, otherwise.
	 */
	public boolean isMouseOver() {
		return isMouseOver(input.getMouseX(), input.getMouseY());
	}

	/**
	 * Updates the plane's position and bearing, the stage of its route, and
	 * whether it has finished its flight.
	 * 
	 * @param time_difference
	 * @throws IllegalStateException When landing an aircraft that will overflow the airport
	 */
	public void update(double time_difference) throws IllegalStateException {
		if (has_finished)
			return;

		// Handles aircrafts' altitude, comparing the aircrafts' altitude to its target altitude.
		if (altitude_list.get(target_altitude_index) + 100 >= (int) this.position.z() &&
				altitude_list.get(target_altitude_index) - 100 <= (int) this.position.z()) {
			this.position.setZ(altitude_list.get(target_altitude_index));
			changeAltitude(0);
		} else if (altitude_list.get(target_altitude_index) > (int) this.position.z()) {
			climb();
		} else {
			fall();
		}

		// Update position
		Vector dv = velocity.scaleBy(time_difference);
		position = position.add(dv);

		current_turning_angle = 0;

		// Update target waypoint
		if (isAt(current_target.position()) && current_target.equals(flight_plan.getDestination())) {
			has_finished = true;
			if (flight_plan.getDestination() instanceof Airport) {
				((Airport) flight_plan.getDestination()).insertAircraft(this);
				is_at_airport = true;
			}
		} else if (isAt(current_target.position()) && (current_route_stage == flight_plan.getRoute().length-1)) {
			current_route_stage++;
			current_target = flight_plan.getDestination();
		} else if (isAt(current_target.position())) {
			// handles what happens when the aircraft is circling the airport
			if (current_target instanceof HoldingWaypoint) {
				// Changes the current waypoint to the next holding waypoint in
				// the airport circle
				this.alterPath(this.indexInFlightPath(current_target), ((HoldingWaypoint) current_target).getNextWaypoint());
			} else {
				current_route_stage++;
				current_target = flight_plan.getRoute()[current_route_stage];
			}
		}

		// Update bearing
		if (Math.abs(angleToTarget() - getBearing()) > 0.01) {
			turnTowardsTarget(time_difference);
		}
	}

	/**
	 * Turns the plane left.
	 * 
	 * @param time_difference The time elapsed since the last frame.
	 */
	public void turnLeft(double time_difference) {
		turnBy(-time_difference * turning_speed);
		manual_bearing_target = Double.NaN;
	}

	/**
	 * Turns the plane right.
	 * 
	 * @param time_difference The time elapsed since the last frame.
	 */
	public void turnRight(double time_difference) {
		turnBy(time_difference * turning_speed);
		manual_bearing_target = Double.NaN;
	}

	/**
	 * Turns the plane by a certain angle (in radians). Positive angles turn the
	 * plane clockwise.
	 * 
	 * @param angle The angle by which to turn.
	 */
	private void turnBy(double angle) {
		current_turning_angle = angle;

		double cosA = Math.cos(angle);
		double sinA = Math.sin(angle);

		double x = velocity.x();
		double y = velocity.y();

		velocity = new Vector(x * cosA - y * sinA, y * cosA + x * sinA, velocity.z());
	}

	/**
	 * Turns the plane towards its current target. How much it turns is
	 * determined by the plane's {@link turnSpeed}.
	 * 
	 * @param time_difference The time elapsed since the last frame.
	 */
	private void turnTowardsTarget(double time_difference) {
		// Get difference in angle
		double angle_difference = (angleToTarget() % (2 * Math.PI)) - (getBearing() % (2 * Math.PI));
		boolean crosses_positive_negative_divide = angle_difference < -Math.PI * 7 / 8;

		// Correct difference
		angle_difference += Math.PI;
		angle_difference %= (2 * Math.PI);
		angle_difference -= Math.PI;

		// Get which way to turn.
		int angleDirection = (int)(angle_difference /= Math.abs(angle_difference));

		if (crosses_positive_negative_divide)
			angleDirection *= -1;

		double angleMagnitude = Math.min(Math.abs((time_difference * turning_speed)), Math.abs(angle_difference));

		turnBy(angleMagnitude * angleDirection);
	}

	/**
	 * Checks whether an aircraft is within a certain distance from this one.
	 * 
	 * @param aircraft The aircraft to check.
	 * @param distance The distance within which to care about.
	 * @return True, if the aircraft is within the distance. False, otherwise.
	 */
	private boolean isWithin(Aircraft aircraft, int distance) {
		double dx = aircraft.getPosition().x() - position.x();
		double dy = aircraft.getPosition().y() - position.y();
		double dz = aircraft.getPosition().z() - position.z();

		return dx * dx + dy * dy + dz * dz < distance * distance;
	}

	/**
	 * Toggles the state of whether this plane is manually controlled.
	 */
	public void toggleManualControl() {
		is_manually_controlled = !is_manually_controlled;

		if (!is_manually_controlled)
			resetBearing();
	}

	/**
	 * Another method to manually change the "manual control". Used so the
	 * player can directly the control instead of click the button or space.
	 * 
	 * @param manual
	 */
	public void setManualControl(boolean manual) {
		is_manually_controlled = manual;
	}

	/**
	 * Triggered when land button is pressed. Causes plane to immediately target
	 * airport, and changes other aircraft properties shown below.
	 * 
	 * The command to land an aircraft cannot be reneged upon.
	 */
	public void toggleLand(Waypoint landing_waypoint) {
		is_landing = !is_landing;

		if (is_landing && (current_target instanceof HoldingWaypoint)) {
			// Advance the route, breaking loose of the holding waypoints
			current_route_stage++;
			current_target = landing_waypoint;

			// Reduce altitude to 100 ft and speed to a lower one
			target_altitude_index = 0;
			velocity = velocity.scaleBy(LANDING_SPEED);

			// Update the text above the aircraft to display "Landing"
			infoText = Texts.LANDING;
		}
	}

	/**
	 * Increases the plane's altitude to altitudeList.get(targetAltitudeIndex).
	 */
	public void climb() {
		if (position.z() < altitude_list.get(target_altitude_index))
			changeAltitude(altitude_change_speed);

		if (position.z() >= altitude_list.get(target_altitude_index)) {
			changeAltitude(0);
			position = new Vector(position.x(), position.y(), altitude_list.get(target_altitude_index));
		}
	}

	/**
	 * Decreases the plane's altitude to altitudeList.get(targetAltitudeIndex).
	 */
	public void fall() {
		if (position.z() > altitude_list.get(target_altitude_index))
			changeAltitude(-altitude_change_speed);

		if (position.z() <= altitude_list.get(target_altitude_index)) {
			position = new Vector(position.x(), position.y(), altitude_list.get(target_altitude_index));
		}
	}
	
	/**
	 * Draws the plane and any warning circles if necessary.
	 */
	public void draw() {
		float scale = 2;

		// Draws the aircraft itself
		graphics.setColour(Main.GREY);
		graphics.draw(image, scale, position.x(), position.y(), getBearing(), 8, 8);

		// Draw the altitude near the aircraft
		// £ is rendered as cursive "ft" from font file
		graphics.print(String.format("%.0f", position.z()) + "£", position.x() - 22, position.y() + 15);

		// Draw the 'land me' message once an aircraft is circling the airport
		if (current_target instanceof HoldingWaypoint) {
			graphics.print(infoText, position.x() - 28, position.y() - 22);
		}

		drawWarningCircles();
	}

	/**
	 * Draws the compass around this plane
	 */
	public void drawCompass() {
		graphics.setColour(Main.GREEN);
		graphics.circle(false, position.x() + 16, position.y() + 16, COMPASS_RADIUS);

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

		if (is_manually_controlled && input.isMouseDown(input.MOUSE_LEFT)) {
			graphics.setColour(0, 128, 0, 128);
			double r = Math.atan2(input.getMouseY() - position.y(), input.getMouseX() - position.x());
			x = 16 + position.x() + (COMPASS_RADIUS * Math.cos(r));
			y = 16 + position.y() + (COMPASS_RADIUS * Math.sin(r));
			graphics.line(position.x() + 16, position.y() + 16, x, y);
			graphics.line(position.x() + 15, position.y() + 16, x, y);
			graphics.line(position.x() + 16, position.y() + 15, x, y);
			graphics.line(position.x() + 17, position.y() + 16, x, y);
			graphics.line(position.x() + 17, position.y() + 17, x, y);
			graphics.setColour(0, 128, 0, 16);
		}

		x = 16 + position.x() + (COMPASS_RADIUS * Math.cos(getBearing()));
		y = 16 + position.y() + (COMPASS_RADIUS * Math.sin(getBearing()));

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
		for (Aircraft plane : planes_too_near) {
			Vector midPoint = position.add(plane.position).scaleBy(0.5);
			double radius = position.sub(midPoint).magnitude() * 2;

			graphics.setColour(Main.RED);
			graphics.circle(false, midPoint.x(), midPoint.y(), radius);
		}
	}

	/**
	 * Draws lines starting from the plane, along its flight path to its
	 * destination.
	 */
	public void drawFlightPath(boolean is_selected) {
		if (is_selected) {
			graphics.setColour(0, 128, 128);
		} else {
			graphics.setColour(0, 128, 128, 128);
		}

		if (current_target != flight_plan.getDestination()) {
			graphics.line(position.x(), position.y(), flight_plan.getRoute()[current_route_stage].position().x(), flight_plan.getRoute()[current_route_stage].position().y());
		}

		for (int i = current_route_stage; i < flight_plan.getRoute().length - 1; i++) {
			graphics.line(flight_plan.getRoute()[i].position().x(), flight_plan.getRoute()[i].position().y(),	flight_plan.getRoute()[i + 1].position().x(), flight_plan.getRoute()[i + 1].position().y());
		}

		if (current_target == flight_plan.getDestination()) {
			graphics.line(position.x(), position.y(), flight_plan.getDestination().position().x(), flight_plan.getDestination().position().y());
		} else {
			graphics.line(flight_plan.getRoute()[flight_plan.getRoute().length - 1].position().x(), flight_plan.getRoute()[flight_plan.getRoute().length - 1].position().y(), flight_plan.getDestination().position().x(), flight_plan.getDestination().position().y());
		}
	}

	/**
	 * Visually represents the pathpoint being moved.
	 * 
	 * @param mouse_x Current x position of mouse
	 * @param mouse_y Current y position of mouse
	 */
	public void drawModifiedPath(int modified, double mouse_x, double mouse_y) {
		graphics.setColour(0, 128, 128, 128);

		if (current_route_stage > modified - 1) {
			graphics.line(getPosition().x(), getPosition().y(), mouse_x, mouse_y);
		} else {
			graphics.line(flight_plan.getRoute()[modified - 1].position().x(), flight_plan.getRoute()[modified - 1].position().y(), mouse_x, mouse_y);
		}

		if (current_target == flight_plan.getDestination()) {
			graphics.line(mouse_x, mouse_y, flight_plan.getDestination().position().x(), flight_plan.getDestination().position().y());
		} else {
			int index = modified + 1;

			if (index == flight_plan.getRoute().length) { // Modifying final waypoint in route
				graphics.line(mouse_x, mouse_y, flight_plan.getDestination().position().x(), flight_plan.getDestination().position().y()); // Line drawn to final waypoint
			} else {
				graphics.line(mouse_x, mouse_y, flight_plan.getRoute()[index].position().x(), flight_plan.getRoute()[index].position().y());
			}
		}
	}

	/**
	 * Updates the amount of planes that are too close, violating the separation
	 * rules, and also checks for crashes.
	 * 
	 * @param time_difference The time elapsed since the last frame.
	 * @param aircraft_list List of aircraft in airspace
	 * @return The index of the aircraft in aircraft_list this aircraft collides with. -1 if no collision.
	 */
	public int updateCollisions(double time_difference, ArrayList<Aircraft> aircraft_list) {
		planes_too_near.clear();

		for (int i = 0; i < aircraft_list.size(); i++) {
			Aircraft plane = aircraft_list.get(i);
			if (!has_finished) {
				if (plane != this && isWithin(plane, RADIUS)) {
					has_finished = true;
					plane.has_finished = true;
					return i;
				} else if (plane != this && isWithin(plane, minimum_separation_distance)) {
					// When separation rules are breached
					planes_too_near.add(plane);

					if (!collision_warning_sound_flag) {
						collision_warning_sound_flag = true;
						WARNING_SOUND.playSound();
					}
					if (!was_breaching_in_last_frame) {
						was_breaching_in_last_frame = true;
						num_points -= 20;
					}
				}
			}
			
		}


		if (planes_too_near.isEmpty()) {
			collision_warning_sound_flag = false;
			was_breaching_in_last_frame = false;
		}

		return -1;
	}
}