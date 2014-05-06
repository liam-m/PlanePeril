package scn;

import java.util.ArrayList;

import cls.Aircraft;
import cls.AirportControlBox;
import cls.Altimeter;
import cls.OrdersBox;
import cls.Waypoint;
import pp.Main;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.window;
import lib.jog.audio.Sound;
import lib.jog.graphics.Image;

public abstract class GameWindow extends Scene {
	protected final int MAX_AIRCRAFT = 20;
	protected final static int TAKEOFF_DELAY = 5;
	
	// Static Final Ints for difficulty settings
	// Difficulty of demo scene determined by difficulty selection scene
	public final static int DIFFICULTY_EASY = 0;
	public final static int DIFFICULTY_MEDIUM = 1;
	public final static int DIFFICULTY_HARD = 2;
	public static int difficulty = DIFFICULTY_EASY;
	
	public ArrayList<Aircraft> aircraft = new ArrayList<Aircraft>();
	public Aircraft selected_aircraft = null;
	
	protected OrdersBox orders_box;
	protected AirportControlBox airport_control_box;
	protected Altimeter altimeter;

	protected double next_take_off = TAKEOFF_DELAY;
	
	protected boolean is_manually_controlling;
	protected boolean compass_dragged = false;
	protected Waypoint selected_waypoint = null;
	protected int selected_pathpoint = -1;
	public Image aircraft_image;
	
	protected double flight_generation_interval = 4;
	protected double flight_generation_time_elapsed = 6;
	
	// Position of things drawn to window
	protected final int PLANE_INFO_X = 16;
	protected final int PLANE_INFO_Y = window.getHeight() - 120;
	protected final int PLANE_INFO_W = window.getWidth() / 4 - 16;
	protected final int PLANE_INFO_H = 112;

	protected final int ALTIMETER_X = PLANE_INFO_X + PLANE_INFO_W + 8;
	protected final int ALTIMETER_Y = window.getHeight() - 120;
	protected final int ALTIMETER_W = 244;
	protected final int ALTIMETER_H = 112;

	protected final int ORDERSBOX_X = ALTIMETER_X + ALTIMETER_W + 8;
	protected final static int ORDERSBOX_Y = window.getHeight() - 120;
	protected final int ORDERSBOX_W = window.getWidth() - (ORDERSBOX_X + 16);
	protected final static int ORDERSBOX_H = 112;
	
	protected GameWindow(Main main) {
		super(main);
	}
	
	protected void drawPlaneInfo() {
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
	public void mousePressed(int key, int mouse_x, int mouse_y) {
	}

	@Override
	public void mouseReleased(int key, int mouse_x, int mouse_y) {
	}

	@Override
	public void keyPressed(int key) {
		if (key == input.KEY_ESCAPE) {
			main.closeScene();
		}
	}

	@Override
	public void keyReleased(int key) {
	}

	@Override
	public void start() {
	}

	@Override
	public void update(double time_difference) {
	}

	@Override
	public void draw() {
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
		sound.stopSound();
		sound.playSound();
	}
}