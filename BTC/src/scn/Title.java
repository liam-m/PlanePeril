package scn;

import java.util.Calendar;

import lib.jog.graphics;
import lib.jog.window;
import btc.Main;

public class Title extends Scene {
	
	private double _timer;

	public Title(Main main) {
		super(main);
	}

	@Override
	public void start() {
		_timer = 0;
	}

	@Override
	public void update(double dt) {
		_timer += dt;
	}

	@Override
	public void mousePressed(int key, int x, int y) {
		
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		
	}

	@Override
	public void keyPressed(int key) {
		
	}

	@Override
	public void keyReleased(int key) {
		
	}

	@Override
	public void draw() {
		// Radar
		graphics.setColour(0, 128, 0);
		graphics.circle(false, window.height()/2, window.height()/2, window.height()/2 - 32, 100);
		graphics.setColour(0, 128, 0, 32);
		graphics.circle(false, window.height()/2, window.height()/2, window.height()/3, 100);
		graphics.circle(false, window.height()/2, window.height()/2, window.height()/4 - 16, 100);
		graphics.circle(false, window.height()/2, window.height()/2, window.height()/9, 100);
		graphics.circle(false, window.height()/2, window.height()/2, 2, 100);
		graphics.setColour(0, 128, 0);
		double angle = (_timer*4) % (2 * Math.PI);
		int w = (int)( Math.cos(angle) * (window.height()/2 - 32) );
		int h = (int)( Math.sin(angle) * (window.height()/2 - 32) );
		graphics.line(window.height()/2, window.height()/2, window.height()/2 + w, window.height()/2 + h);
		graphics.setColour(0, 128, 0, 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -8 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -7 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -6 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -5 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -4 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -3 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -2 * Math.PI / 8);
		graphics.arc(true, window.height()/2, window.height()/2, window.height()/2 - 32, angle, -1 * Math.PI / 8);
		
		// Title
		double distance = Math.PI / 32;
		int radius = 128;
		String title = "Bear Traffic Controller";

		double a = angle + (Math.PI * 4 / 5);
		for (int i = 0; i < title.length(); i++) {
			a -= Math.PI / 32;
			double opacity = a %= (2 * Math.PI);
			opacity *= 256 / (2 * Math.PI);
			opacity = 256 - opacity;
			opacity %= 256;
			graphics.setColour(0, 128, 0, opacity);
			graphics.print(title.substring(i, i+1), 74 + i * 14, 128, 1.8);
		}
		
		// Random Other Stuff
		graphics.setColour(0, 128, 0);
		graphics.line(window.height(), 16, window.height(), window.height() - 16);
		java.text.DateFormat dateFormat = new java.text.SimpleDateFormat("yyyy/MM/dd");
		java.text.DateFormat timeFormat = new java.text.SimpleDateFormat("HH:mm:ss");
		java.util.Date date = new java.util.Date();
		graphics.print(dateFormat.format(date), window.height() + 8, 20);
		graphics.print(timeFormat.format(date), window.height() + 8, 36);
		graphics.line(window.height(), 48, window.width() - 16, 48);
	}

	@Override
	public void close() {
		
	}

}
