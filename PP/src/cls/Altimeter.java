package cls;

import pp.Main;
import lib.jog.graphics;
import lib.jog.input;
import lib.jog.input.EventHandler;

/**
 * Shows the planes height in feet (whatever units you want). And the current
 * banking of the plane.
 * 
 * @author Huw Taylor
 */
public class Altimeter implements EventHandler {

	/**
	 * Whether or not the Altimeter should be drawn
	 */
	private boolean isVisible;

	/**
	 * The current aircraft associated with the altimeter
	 */
	private Aircraft currentAircraft;

	private final double positionX, positionY, width, height;

	/**
	 * Constructor for the altimeter
	 * 
	 * @param x
	 *            the x coord to draw at
	 * @param y
	 *            the y coord to draw at
	 * @param w
	 *            the width of the altimeter
	 * @param h
	 *            the height of the altimeter
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
	 * 
	 * @param aircraft
	 *            The aircraft to associate with the altimeter
	 */
	public void show(Aircraft aircraft) {
		if (aircraft == null)
			return;

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
	 * 
	 * @param mx
	 *            the x coord of the mouse location
	 * @param my
	 *            the y coord of the mouse location
	 * @return boolean marking if the mouse is over the altimeter
	 */
	public boolean isMouseOver(int mx, int my) {
		return (mx >= positionX && mx <= positionX + width && my >= positionY && my <= positionY
				+ height);
	}

	public boolean isMouseOver() {
		return isMouseOver(input.mouseX(), input.mouseY());
	}

	@Override
	/**
	 * Handler for mouse clicks
	 */
	public void mousePressed(int key, int x, int y) {
		if (!isVisible)
			return;
	}

	@Override
	/**
	 * Handler for mouse releases
	 */
	public void mouseReleased(int key, int mx, int my) {
		if (!isVisible)
			return;

		if (key == input.MOUSE_LEFT) {
			if (mouseOverTopButton(mx, my)) {
				currentAircraft.increaseTargetAltitude();
			} else if (mouseOverBottomButton(mx, my)) {
				currentAircraft.decreaseTargetAltitude();
			}
		}
	}

	@Override
	public void keyPressed(int key) {
	}

	@Override
	public void keyReleased(int key) {
	}

	public void update(double dt) {
	}

	/**
	 * Draws the altimeter to the screen
	 */
	public void draw() {
		drawRectangle();

		if (isVisible) {
			drawPlaneIcon();
			drawAltitudes();
			drawArrows();
		}
	}

	/**
	 * Draws the box around the altimeter
	 */
	private void drawRectangle() {
		graphics.setColour(Main.GREEN);
		graphics.rectangle(false, positionX, positionY, width, height);
	}

	/**
	 * Draws the icon on the altimeter Icon depicts plane orientation
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
		double y = positionY + (height / 2);
		double wingLength = width / 3 - 8;
		double tailLength = width / 9;

		graphics.line(x, y, x + wingLength * Math.cos(r),
				y + wingLength * Math.sin(r));
		r -= Math.PI / 2;
		graphics.line(x, y, x + tailLength * Math.cos(r),
				y + tailLength * Math.sin(r));
		r -= Math.PI / 2;
		graphics.line(x, y, x + wingLength * Math.cos(r),
				y + wingLength * Math.sin(r));
		graphics.setColour(0, 0, 0);
		graphics.circle(true, x, y, 4);
		graphics.setColour(Main.GREEN);
		graphics.circle(false, x, y, 4);
		graphics.printCentred(
				String.format("%.0f", currentAircraft.position().z()),
				positionX, y + 32, 1, width);
	}

	/**
	 * Draws the altitude lines relative to the aircraft, showing whether the
	 * aircraft is climbing or falling
	 */
	private void drawAltitudes() {
		graphics.setColour(0, 128, 0, 32);
		graphics.setViewport((int) positionX, (int) positionY, (int) width,
				(int) height);

		int midX = (int) (width / 2);
		int midY = (int) (height / 2);

		for (int i = -5; i <= 4; i++) {

			int alt = (int) (currentAircraft.position().z() + (1000 * i));
			int offset = (int) (16.0 * (alt % 1000) / 1000);
			int y = midY - (i * 16) + offset;

			graphics.line(midX - 64, y, midX + 64, y);
			alt -= (alt % 1000);
			graphics.print(String.valueOf(alt), midX + 72, y);
			graphics.print(String.valueOf(alt), midX - 72 - 40, y);

		}

		graphics.setViewport();
		graphics.setColour(Main.GREEN);
	}

	private void drawArrows() {
		int midX = (int) (positionX + (width / 2));
		graphics.setColour(Main.GREEN);

		if (mouseOverTopButton()) {
			graphics.setColour(128, 128, 128);
		}

		graphics.triangle(true, midX - 10, positionY + 10, midX, positionY + 4,
				midX + 10, positionY + 10);
		graphics.setColour(Main.GREEN);

		if (mouseOverBottomButton()) {
			graphics.setColour(128, 128, 128);
		}

		graphics.triangle(true, midX - 10, positionY + height - 10, midX,
				positionY + height - 4, midX + 10, positionY + height - 10);
	}

	private boolean mouseOverTopButton(int mx, int my) {
		if (!isVisible)
			return false;

		if (mx < positionX || mx > positionX + width)
			return false;

		if (my < positionY || my > positionY + height)
			return false;

		return (my <= positionY + 16);
	}

	private boolean mouseOverTopButton() {
		return mouseOverTopButton(input.mouseX(), input.mouseY());
	}

	private boolean mouseOverBottomButton(int mx, int my) {
		if (!isVisible)
			return false;

		if (mx < positionX || mx > positionX + width)
			return false;

		if (my < positionY || my > positionY + height)
			return false;

		return (my >= positionY + height - 16);
	}

	private boolean mouseOverBottomButton() {
		return mouseOverBottomButton(input.mouseX(), input.mouseY());
	}

}
