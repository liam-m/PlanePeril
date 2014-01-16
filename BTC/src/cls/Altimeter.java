package cls;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.input.EventHandler;

/**
 * Shows the planes height in feet (whatever units you want). And the current banking of the plane.
 * @author Huw Taylor
 */
public class Altimeter implements EventHandler {
	
	private boolean isVisible;
	/**
	 * Whether or not the Altimeter should be drawn
	 */
	
	private cls.Aircraft currentAircraft;
	/**
	 * The current aircraft associated with the altimeter
	 */
	
	private double positionX, positionY, width, height;
	
	/**
	 * Constructor for the altimeter
	 * @param x the x coord to draw at
	 * @param y the y coord to draw at
	 * @param w the width of the altimeter
	 * @param h the height of the altimeter
	 */
	public Altimeter(double x, double y, double w, double h) {
		positionX = x;
		positionY = y;
		width = w;
		height = h;
		hide();
	}
	
	/**
	 * Makes the altimeter visible
	 * @param aircraft The aircraft to associate with the altimeter
	 */
	public void show(cls.Aircraft aircraft) {
		if (aircraft == null) return;
		currentAircraft = aircraft;
		isVisible = true;
	}
	
	/**
	 * Makes the altimeter invisible
	 */
	public void hide() {
		currentAircraft = null;
		isVisible = false;
	}
	
	/**
	 * Checks if the mouse is over the altimeter
	 * @param mx the x coord of the mouse location
	 * @param my the y coord of the mouse location
	 * @return boolean marking if the mouse is over the altimeter
	 */
	public boolean isMouseOver(int mx, int my) {
		return (mx >= positionX && mx <= positionX + width && my >= positionY && my <= positionY + height);
	}
	public boolean isMouseOver() { return isMouseOver(input.mouseX(), input.mouseY()); }

	@Override
	/**
	 * Handler for mouse clicks
	 */
	public void mousePressed(int key, int x, int y) {
		if (!isVisible) return;
	}

	@Override
	/**
	 * Handler for mouse releases
	 */
	public void mouseReleased(int key, int x, int y) {
		if (!isVisible) return;
		if (key == input.MOUSE_WHEEL_DOWN) {
			currentAircraft.setAltitudeState(Aircraft.altitudeFall);
		}
		if (key == input.MOUSE_WHEEL_UP) {
			currentAircraft.setAltitudeState(Aircraft.altitudeClimb);
		}
	}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}
	
	public void update(double dt) {}
	
	/**
	 * Draws the altimeter to the screen
	 */
	public void draw() {
		drawRectangle();
		if (isVisible) {
			drawPlaneIcon();
		}
	}
	
	/**
	 * Draws the box around the altimeter
	 */
	private void drawRectangle() {
		graphics.setColour(0, 128, 0);
		graphics.rectangle(false, positionX, positionY, width, height);
	}
	
	/**
	 * Draws the icon on the altimeter
	 * Icon depicts plane orientation
	 */
	private void drawPlaneIcon() {
		// angle to draw plane
		double r = 0;
		if (currentAircraft.isTurningLeft()) {
			r = -Math.PI / 12;
		} else if (currentAircraft.isTurningRight()) {
			r = Math.PI / 12;
		}
		double x = positionX + (width / 2);
		double y = positionY + (height / 2) - 16;
		double wingLength = width / 3;
		double tailLength = width / 9;
		graphics.line(x, y, x + wingLength * Math.cos(r), y + wingLength * Math.sin(r));
		r -= Math.PI / 2;
		graphics.line(x, y, x + tailLength * Math.cos(r), y + tailLength * Math.sin(r));
		r -= Math.PI / 2;
		graphics.line(x, y, x + wingLength * Math.cos(r), y + wingLength * Math.sin(r));
		graphics.setColour(0, 0, 0);
		graphics.circle(true, x, y, 4);
		graphics.setColour(0, 128, 0);
		graphics.circle(false, x, y, 4);
		graphics.printCentred(String.format("%.0f", currentAircraft.position().z()), positionX, y+32, 1, width);
	}

}
