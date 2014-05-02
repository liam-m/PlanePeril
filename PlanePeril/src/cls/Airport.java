package cls;

import java.io.File;
import java.util.ArrayList;

import pp.Main;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.graphics.Image;
import lib.jog.window;

public class Airport extends Waypoint {

	private final static int MAX_AIRCRAFT_NUMBER = 3;

	private final static int TEXT_POSITION = 55;
	private final static int TEXT_OFFSET = 10;

	public final static int MOUSE_LENIANCY = 32;

	private int time_Left = 0;

	private ArrayList<Aircraft> aircraft_list = new ArrayList<Aircraft>();

	private Image airport;

	private double x_location;

	private double y_location;

	private ArrayList<Aircraft> aircraft_waiting_to_land =  new ArrayList<Aircraft>();
	double timer = 0;
	public java.util.ArrayList<Double> time_entered = new java.util.ArrayList<Double>();

	// All location values are absolute and based on the current version of the airport image.
	private double arrivals_x_location;
	private double arrivals_y_location;
	private double arrivals_width = 105;
	private double arrivals_height = 52;
	
	private double departures_x_location;
	private double departures_y_location;
	private double departures_width = 50;
	private double departures_height = 36;
	
	public boolean is_arrivals_clicked = false;
	public boolean is_departures_clicked = false;
	private boolean not_taking_off = false;
	/**
	 * An airport. In general, it is basically a waypoint (more specifically and
	 * exit point) with few additions: an aircraft and land and takeoff from it. <br>
	 * <br>
	 * It should be noted that the holding waypoints (rendered as X's) are
	 * relative in position to the airport.
	 * 
	 * @param x
	 *            x-coordinate for the airport
	 * @param y
	 *            y-coordinate for the airport
	 * @param name
	 */
	public Airport(double x, double y, String name) {
		super(x, y, WaypointType.ENTRY_EXIT, name);
		x_location = x;
		y_location = y;
		loadImage();
		
		// All location values are absolute and based on the current version of the airport image.
		arrivals_x_location = x_location + 90;
		arrivals_y_location = y_location + 83;

		departures_x_location = x_location + 2;
		departures_y_location = y_location + 50;
	}
	
	public Airport(double x, double y, String name, boolean testing) {
		super(x, y, WaypointType.ENTRY_EXIT, name);
		x_location = x;
		y_location = y;
		if (!testing) {
			loadImage();
		}
		// All location values are absolute and based on the current version of the airport image.
		arrivals_x_location = x_location + 90;
		arrivals_y_location = y_location + 83;

		departures_x_location = x_location + 2;
		departures_y_location = y_location + 50;
	}

	/**
	 * Set how much time left until the next takeoff is allowed. As it counted
	 * in the Demo class, one must pass it here on every iteration. Later on
	 * displayed near the airport as text.
	 * 
	 * @param time_left
	 */
	public void setTimeLeft(int time_left) {
		// avoid the timer becoming lower than 0 i.e. negative
		if (time_left >= 0) {
			this.time_Left = time_left;
		}
	}
	
	/**
	 * Checks if player has a plane or more waiting to take off for particular amount of time
	 * @return if returns true, player should be penalized
	 */
	public boolean isNotTakingOff() {
		return not_taking_off;
	}
	/**
	 * Inserts an aircraft into the airport, done by reference. if the list is already full then nothing happens
	 * 
	 * @param aircraft
	 *            to insert
	 */
	public void insertAircraft(Aircraft aircraft) {
		if (aircraft_list.size() < MAX_AIRCRAFT_NUMBER) {
			aircraft_list.add(aircraft);
			time_entered.add(timer);
		}
	}

	/**
	 * Forces an aircraft to take off. Selects one from the list randomly.
	 * Removes it from the list afterwards.
	 * 
	 * @return Aircraft one of the aircraft that were held in the airport. This
	 *         aircraft can be used instead of generating a new one.
	 * @throws IllegalStateException
	 *             if there are no aircraft in the airport
	 */
	public Aircraft takeoff() throws IllegalStateException {
		if (aircraft_list.size() == 0)
			throw new IllegalStateException("No aircraft in airport");

		time_entered.remove(0);
		return aircraft_list.remove(0);
	}

	/**
	 * Draws all the airport information: <br>
	 * <br>
	 * Name, <br>
	 * Number of <b>aircraft in </b> <br>
	 * if the time to next takeoff is more than 0 seconds (i.e. there has been a
	 * takeoff recently), show how many <b>seconds left until next allowed
	 * takeoff</b>
	 */
	public void drawAirportInfo() {
		graphics.setColour(Main.GREEN);

		// print the name of the airport
		graphics.print("Aero Medved'", position.x() - 40, position.y()
				- TEXT_POSITION);

		// print how many aircraft are currently in the airport
		graphics.print("Aircraft in: " + aircraft_list.size(),
				position.x() - 40, position.y() - (TEXT_POSITION - TEXT_OFFSET));

		// hide the timer if it is 0.
		if (time_Left != 0) {
			graphics.print("Takeoff in: " + this.time_Left, position.x() - 40,
					position.y() - (TEXT_POSITION - TEXT_OFFSET * 2));
		}

	}
	
	public void loadImage() {
		airport = graphics.newImage("gfx" + File.separator + "Airport.png");
	}
	
	@Override
	public void draw() {
		graphics.setColour(Main.GREY);
		// Draw the airport image
		graphics.draw(airport, x_location-airport.getWidth()/2, y_location-airport.getHeight()/2);
				
		int green_fine = 128;
		int green_danger = 0;
		int red_fine = 0;
		int red_danger = 128;
				
		// Draw the hangar button if plane is waiting (departing flights)
		if (aircraft_list.size() > 0) {
			// Colour fades from green (fine) to red (danger) over 5 seconds as plane is waiting
			int time_waiting = (int)(timer - time_entered.get(0));
			// Assume it hasn't been waiting
			int green_now = green_fine; 
			int red_now = red_fine;
			if (time_waiting > 0) { // Prevent division by 0
				if (time_waiting >= 5) { // Cap at 5 seconds
					green_now = green_danger;
					red_now = red_danger;
					not_taking_off = true;
				} else {
					// Colour between fine and danger, scaled by time_waiting
					green_now = green_fine - (int)(Math.abs(green_fine-green_danger) * (time_waiting/5.0)); 
					red_now = (int)(Math.abs(red_fine-red_danger) * (time_waiting/5.0));
					not_taking_off = false;
				}
			}
			
			else
				not_taking_off = false;

			// Draw border, draw as filled if clicked
			graphics.setColour(red_now, green_now, 0, 256);
			graphics.rectangle(is_departures_clicked, departures_x_location-airport.getWidth()/2, departures_y_location-airport.getHeight()/2, departures_width, departures_height);

			// Draw box
			graphics.setColour(red_now, green_now, 0, 64);
			graphics.rectangle(true, departures_x_location-airport.getWidth()/2 + 1, departures_y_location-airport.getHeight()/2 + 1, departures_width - 2, departures_height - 2);
					
			// Print number of aircraft waiting
			graphics.setColour(255, 255, 255, 128);
			graphics.print(Integer.toString(aircraft_list.size()), departures_x_location-airport.getWidth()/2 + 23, departures_y_location-airport.getHeight()/2 + 15);
			}
			graphics.setColour(0, 128, 0, 128);
			// Draw the arrivals button if at least one plane is waiting (arriving flights)
			if (aircraft_waiting_to_land.size() > 0) {
				// Draw border, draw as filled if clicked
				graphics.rectangle(is_arrivals_clicked, arrivals_x_location-airport.getWidth()/2, arrivals_y_location-airport.getHeight()/2, arrivals_width, arrivals_height);
				graphics.setColour(128, 128, 0, 64);			
				// Draw box
				graphics.rectangle(true, arrivals_x_location-airport.getWidth()/2 + 1, arrivals_y_location-airport.getHeight()/2 + 1, arrivals_width -2, arrivals_height -2);
					
				// Print number of aircraft waiting
				graphics.setColour(255, 255, 255, 128);
				graphics.print(Integer.toString(aircraft_waiting_to_land.size()), arrivals_x_location-airport.getWidth()/2 + 50, arrivals_y_location-airport.getHeight()/2 + 26);
			}		
		
	}

	public void update(double dt, ArrayList<Aircraft> aircraft, boolean is_left) {
		timer += dt;
		aircraft_waiting_to_land .clear();
		for (Aircraft a : aircraft) {
			if (a.getCurrentTarget() instanceof HoldingWaypoint) {
				if (is_left && a.getPosition().x() < window.getWidth()/2) {
					aircraft_waiting_to_land.add(a);
				} else if (a.getPosition().x() > window.getWidth()/2){
					aircraft_waiting_to_land.add(a);
				}
			}
		}
	
	}
	public int getMaxAircraft() {
		return MAX_AIRCRAFT_NUMBER;
	}
	
	public int getNumAircraft() {
		return this.aircraft_list.size();
	}
	
	public ArrayList<Aircraft> getAircraft() {
		return this.aircraft_list;
	}
	/**
	 *  Arrivals is the portion of the airport image which is used to issue the land command
	 * @param position is the point to be tested
	 * @return true if point is within the rectangle that defines the arrivals portion of the airport
	 */
	public boolean isWithinArrivals(Vector position) {
		return isWithinRect((int)position.x(), (int)position.y(),(int)(arrivals_x_location-airport.getWidth()/2) + Main.VIEWPORT_OFFSET_X, (int)(arrivals_y_location-airport.getHeight()/2) + Main.VIEWPORT_OFFSET_Y, (int)arrivals_width, (int)arrivals_height);
	}
	
	// Used for calculating if an aircraft is within the airspace for landing - offset should not be applied
	public boolean isWithinArrivals(Vector position, boolean apply_offset) {
		return (apply_offset ? isWithinArrivals(position) : isWithinRect((int)position.x(), (int)position.y(),(int)(arrivals_x_location-airport.getWidth()/2), (int)(arrivals_y_location-airport.getHeight()/2), (int)arrivals_width, (int)arrivals_height));
	}
	
	/**
	 * Departures is the portion of the airport image which is used to issue the take off command
	 * @param position is the point to be tested
	 * @return true if point is within the rectangle that defines the departures portion of the airport
	 */
	public boolean isWithinDepartures(Vector position) {
		return isWithinRect((int)position.x(), (int)position.y(), (int)(departures_x_location-airport.getWidth()/2) + Main.VIEWPORT_OFFSET_X, (int)(departures_y_location-airport.getHeight()/2) + Main.VIEWPORT_OFFSET_Y, (int)departures_width, (int)departures_height);
	}
	
	public boolean isWithinRect(int test_x, int test_y, int x, int y, int width, int height) {
		return x <= test_x && test_x <= x + width && y <= test_y && test_y <= y + height;
	}
	
	public void mousePressed(int key, int x, int y) {
		if (key == input.MOUSE_LEFT) { 
			if (isWithinArrivals(new Vector(x, y, 0))) {
				is_arrivals_clicked = true;
			} else if (isWithinDepartures(new Vector(x, y, 0))) {
				is_departures_clicked = true;
			}
		}
	}


	public void mouseReleased(int key, int x, int y) {
		is_arrivals_clicked = false;
		is_departures_clicked = false;
	}
}
