package scn;

import java.io.File;
import java.util.ArrayList;

import cls.Aircraft;
import cls.Airport;
import cls.Altimeter;
import cls.OrdersBox;
import cls.Waypoint;
import pp.Main;
import lib.jog.audio.Sound;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.graphics.Image;
import lib.jog.window;

public class Multiplayer extends Scene {
	String left_name, right_name;
	Waypoint[] left_waypoints, right_waypoints;
	ArrayList<Aircraft> aircraft = new ArrayList<Aircraft>();
	Aircraft selected_aircraft;
	Airport left_airport, right_airport;
	// PerformanceBar left_performance, right_performance;
	OrdersBox orders_box;
	// AirportControlBox airport_control_box;
	Altimeter altimeter;
	Image background;
	
	public Multiplayer(Main main, String left_name, String right_name) {
		super(main);
		this.left_name = left_name;
		this.right_name = right_name;
		this.background = graphics.newImage("gfx" + File.separator + "map.png");
		left_waypoints = new Waypoint[]{
			new Waypoint(10, 10),
		};
		
		right_waypoints = new Waypoint[]{
				new Waypoint(1300, 10),
		};
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
	}

	@Override
	public void close() {
	}

	@Override
	public void playSound(Sound sound) {
	}
}