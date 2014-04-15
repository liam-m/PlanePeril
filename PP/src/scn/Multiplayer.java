package scn;

import java.io.File;
import java.util.ArrayList;

import cls.Aircraft;
import cls.Airport;
import cls.AirportControlBox;
import cls.Altimeter;
import cls.Lives;
import cls.OrdersBox;
import cls.HoldingWaypoint;
import cls.PerformanceBar;
import cls.Waypoint;
import cls.Waypoint.WaypointType;
import pp.Main;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.graphics.Image;
import lib.jog.window;

public class Multiplayer extends Scene {
	String left_name, right_name;
	boolean is_left;
	Waypoint[] left_waypoints, right_waypoints;
	HoldingWaypoint[] left_airport_waypoints, right_airport_waypoints;
	ArrayList<Aircraft> aircraft = new ArrayList<Aircraft>();
	Aircraft selected_aircraft;
	Airport left_airport = new Airport(449, 390, "Aerodromio Leftved'"),
	right_airport = new Airport(949, 390, "Aerodromio Rightved'");
	PerformanceBar left_performance, right_performance;
	Lives left_lives, right_lives;
	OrdersBox orders_box;
	AirportControlBox airport_control_box;
	Altimeter altimeter;
	Image background;
	
	// Position of things drawn to window
		private final int PLANE_INFO_X = 16;
		private final int PLANE_INFO_Y = window.getHeight() - 120;
		private final int PLANE_INFO_W = window.getWidth() / 4 - 16;
		private final int PLANE_INFO_H = 112;

		private final int ALTIMETER_X = PLANE_INFO_X + PLANE_INFO_W + 8;
		private final int ALTIMETER_Y = window.getHeight() - 120;
		private final int ALTIMETER_W = 244;
		private final int ALTIMETER_H = 112;
		
		private final int AIRPORT_CONTROL_BOX_X = ALTIMETER_X + ALTIMETER_W + 8;
		private final int AIRPORT_CONTROL_BOX_Y = window.getHeight() - 120;
		private final int AIRPORT_CONTROL_BOX_W = 244;
		private final int AIRPORT_CONTROL_BOX_H = 112;

		private final int ORDERSBOX_X = AIRPORT_CONTROL_BOX_X + AIRPORT_CONTROL_BOX_W + 8;
		private final static int ORDERSBOX_Y = window.getHeight() - 120;
		private final int ORDERSBOX_W = window.getWidth() - (ORDERSBOX_X + 16);
		private final static int ORDERSBOX_H = 112;
	
		private final int LEFT_PERFOMANCE_X = 100;
		private final int LEFT_PERFOMANCE_Y = 8;
		
		private final int RIGHT_PERFOMANCE_X = window.getWidth()/2 + 200;
		private final int RIGHT_PERFOMANCE_Y = 8;
		
		private final int LEFT_LIVES_X = window.getWidth()/2 - 200;
		private final int LEFT_LIVES_Y = 8;
		
		private final int RIGHT_LIVES_X = window.getWidth()/2 + 20;
		private final int RIGHT_LIVES_Y = 8;
		
	public final Waypoint[] left_entryexit_waypoints = new Waypoint[] {
			/* A set of Waypoints which are origin / destination points */

			// top 3 entry/exit points
			new Waypoint(8, 8, WaypointType.ENTRY_EXIT),
			new Waypoint((window.getWidth()/4), 8, WaypointType.ENTRY_EXIT),
			new Waypoint((window.getWidth()/2 - 20), 8, WaypointType.ENTRY_EXIT),

			// bottom 3 entry/exit points
			new Waypoint(8, window.getHeight() - 190, WaypointType.ENTRY_EXIT),
			new Waypoint((window.getWidth()/4), window.getHeight() - 190, WaypointType.ENTRY_EXIT),
			new Waypoint((window.getWidth()/2 - 20), window.getHeight() - 190, WaypointType.ENTRY_EXIT),
	};
	
	public final Waypoint[] right_entryexit_waypoints = new Waypoint[] {
			/* A set of Waypoints which are origin / destination points */

			// top 3 entry/exit points
			new Waypoint((window.getWidth()/2 + 20), 8, WaypointType.ENTRY_EXIT),
			new Waypoint(((window.getWidth()* 3)/4), 8, WaypointType.ENTRY_EXIT),
			new Waypoint(window.getWidth() - 40, 8, WaypointType.ENTRY_EXIT),

			// bottom 3 entry/exit points
			new Waypoint((window.getWidth()/2 + 20), window.getHeight() - 190, WaypointType.ENTRY_EXIT),
			new Waypoint(((window.getWidth()* 3)/4), window.getHeight() - 190, WaypointType.ENTRY_EXIT),
			new Waypoint(window.getWidth() - 40, window.getHeight() - 190, WaypointType.ENTRY_EXIT),
	 };
	
	// All aircraft taking off must go through this waypoint, allows for
	// aircraft to take off in one direction all the time
	private final Waypoint left_takeoff_waypoint = new Waypoint(left_airport.position().x() - 120, left_airport.position().y());

	// All aircraft that land must pass through this waypoint.
	private final HoldingWaypoint[] left_land_waypoints = {
		new HoldingWaypoint(left_airport.position().x() + 200, left_airport.position().y()),
		new HoldingWaypoint(left_airport.position().x() + 150, left_airport.position().y()),
	};
	
	public HoldingWaypoint[] left_holding_waypoints = {
			new HoldingWaypoint(left_airport.position().x() - 150, left_airport.position().y() - 100),
			new HoldingWaypoint(left_airport.position().x() + 150, left_airport.position().y() - 100),
			new HoldingWaypoint(left_airport.position().x() + 150, left_airport.position().y() + 100),
			new HoldingWaypoint(left_airport.position().x() - 150, left_airport.position().y() + 100),
	};
	
	// Same functionality as above for the 2nd player's airport
	private final Waypoint right_takeoff_waypoint = new Waypoint(right_airport.position().x() - 120, right_airport.position().y());

	private final HoldingWaypoint[] right_land_waypoints = {
		new HoldingWaypoint(right_airport.position().x() + 200, right_airport.position().y()),
		new HoldingWaypoint(right_airport.position().x() + 150, right_airport.position().y()),
	};
	
	public HoldingWaypoint[] right_holding_waypoints = {
			new HoldingWaypoint(right_airport.position().x() - 150, right_airport.position().y() - 100),
			new HoldingWaypoint(right_airport.position().x() + 150, right_airport.position().y() - 100),
			new HoldingWaypoint(right_airport.position().x() + 150, right_airport.position().y() + 100),
			new HoldingWaypoint(right_airport.position().x() - 150, right_airport.position().y() + 100),
	};

	
	public Multiplayer(Main main, String left_name, String right_name, boolean is_left) {
		super(main);
		this.left_name = left_name;
		this.right_name = right_name;
		this.background = graphics.newImage("gfx" + File.separator + "map.png");
		this.is_left = is_left;
		
		left_waypoints = new Waypoint[]{
			new Waypoint(100, 150),
			new Waypoint(100, 400),
			new Waypoint(100, 700),
			new Waypoint(400, 100),
			new Waypoint(400, 300),
			new Waypoint(400, 500),
			
			left_entryexit_waypoints[0],
			left_entryexit_waypoints[1],
			left_entryexit_waypoints[2],
			left_entryexit_waypoints[3],
			left_entryexit_waypoints[4],
			left_entryexit_waypoints[5]
		};
		right_waypoints = new Waypoint[]{
			new Waypoint((window.getWidth()/2) + 100, 100),
			new Waypoint((window.getWidth()/2) + 100, 300),
			new Waypoint((window.getWidth()/2) + 100, 500),
			new Waypoint((window.getWidth()/2) + 400, 100),
			new Waypoint((window.getWidth()/2) + 400, 300),
			new Waypoint((window.getWidth()/2) + 400, 500),
				
			right_entryexit_waypoints[0],
			right_entryexit_waypoints[1],
			right_entryexit_waypoints[2],
			right_entryexit_waypoints[3],
			right_entryexit_waypoints[4],
			right_entryexit_waypoints[5]
		};
	}
	
	@Override
	public void start() {
		altimeter = new Altimeter(ALTIMETER_X, ALTIMETER_Y, ALTIMETER_W, ALTIMETER_H);
		orders_box = new OrdersBox(ORDERSBOX_X, ORDERSBOX_Y, ORDERSBOX_W, ORDERSBOX_H, 6);
		left_performance = new PerformanceBar(LEFT_PERFOMANCE_X, LEFT_PERFOMANCE_Y);
		right_performance = new PerformanceBar(RIGHT_PERFOMANCE_X,RIGHT_PERFOMANCE_Y);
		left_lives = new Lives(LEFT_LIVES_X, LEFT_LIVES_Y);
		right_lives = new Lives(RIGHT_LIVES_X, RIGHT_LIVES_Y);
		if (is_left) {
			airport_control_box = new AirportControlBox(AIRPORT_CONTROL_BOX_X, AIRPORT_CONTROL_BOX_Y, AIRPORT_CONTROL_BOX_W, AIRPORT_CONTROL_BOX_H, left_airport);
		} else {
			airport_control_box = new AirportControlBox(AIRPORT_CONTROL_BOX_X, AIRPORT_CONTROL_BOX_Y, AIRPORT_CONTROL_BOX_W, AIRPORT_CONTROL_BOX_H, right_airport);
		}
		
		// Initialise values of setNextWaypoint for each holding waypoint.
		left_holding_waypoints[0].setNextWaypoint(left_holding_waypoints[1]);
		left_holding_waypoints[1].setNextWaypoint(left_holding_waypoints[2]);
		left_holding_waypoints[2].setNextWaypoint(left_holding_waypoints[3]);
		left_holding_waypoints[3].setNextWaypoint(left_holding_waypoints[0]);
		
		right_holding_waypoints[0].setNextWaypoint(right_holding_waypoints[1]);
		right_holding_waypoints[1].setNextWaypoint(right_holding_waypoints[2]);
		right_holding_waypoints[2].setNextWaypoint(right_holding_waypoints[3]);
		right_holding_waypoints[3].setNextWaypoint(right_holding_waypoints[0]);
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
	}

	@Override
	public void keyPressed(int key) {
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
		if (key == input.KEY_Q) {
			left_lives.decrementLives();
		}
		if (key == input.KEY_E) {
			left_performance.changeValueBy(10);
		}
		if (key == input.KEY_R) {
			left_performance.changeValueBy(-10);
		}
	}

	@Override
	public void keyReleased(int key) {
	}

	@Override
	public void update(double time_difference) {
	}

	@Override
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.printText(left_name, 10, 10, 2);
		graphics.printText(right_name, window.getWidth()-(right_name.length()*17), 10, 2);
		graphics.rectangle(false, 16, 40, window.getWidth() - 32, window.getHeight() - 180);
		
		graphics.setViewport(16, 40, window.getWidth() - 32, window.getHeight() - 180);
		graphics.setColour(255, 255, 255, 100);
		graphics.draw(background, 0, 0);
		
		for (int i=0; i<left_waypoints.length; i++) { // Should be same length
			left_waypoints[i].draw();
			right_waypoints[i].draw();
		}
		
		for (HoldingWaypoint l : left_holding_waypoints) {
			l.draw();
		}
		
		for (HoldingWaypoint r : right_holding_waypoints) {
			r.draw();
		}
		
		graphics.setColour(256, 256, 256, 128);
		left_airport.draw();
		right_airport.draw();
		
		for (Aircraft a : aircraft) {
			a.draw();
			if (a.isMouseOver()) {
				a.drawFlightPath(false);
			}
		}
		
		if (selected_aircraft != null) {
			selected_aircraft.drawFlightPath(true);
			graphics.setColour(Main.GREEN);
		}
		
		graphics.setViewport();
		
		altimeter.draw();
		airport_control_box.draw();
		orders_box.draw();
		drawPlaneInfo();
		
		left_performance.draw();
		right_performance.draw();
		
		graphics.setColour(Main.GREEN);
		left_lives.draw();
		right_lives.draw();
	}
	
	private void drawPlaneInfo() {
		graphics.setColour(Main.GREEN);
		graphics.rectangle(false, PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);

		if (selected_aircraft != null) {

			graphics.setViewport(PLANE_INFO_X, PLANE_INFO_Y, PLANE_INFO_W, PLANE_INFO_H);
			graphics.printTextCentred(selected_aircraft.getName(), 0, 5, 2, PLANE_INFO_W);

			// Altitude
			String altitude = String.format("%.0f", selected_aircraft.getPosition().z())+ "£";
			graphics.print("Altitude:", 10, 40);
			graphics.print(altitude, PLANE_INFO_W - 10 - altitude.length() * 8, 40);

			// Speed
			String speed = String.format("%.2f", selected_aircraft.getSpeed() * 1.687810) + "$";
			graphics.print("Speed:", 10, 55);
			graphics.print(speed, PLANE_INFO_W - 10 - speed.length() * 8, 55);

			// Origin
			graphics.print("Origin:", 10, 70);
			graphics.print(selected_aircraft.getFlightPlan().getOriginName(), PLANE_INFO_W - 10 - selected_aircraft.getFlightPlan().getOriginName().length() * 8, 70);

			// Destination
			graphics.print("Destination:", 10, 85);
			graphics.print(selected_aircraft.getFlightPlan().getDestinationName(), PLANE_INFO_W - 10 - selected_aircraft.getFlightPlan().getDestinationName().length() * 8, 85);
			graphics.setViewport();
		}
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
	}
}