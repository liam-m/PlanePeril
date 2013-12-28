package classes;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.input.EventHandler;

public class Altimeter implements EventHandler {
	
	private boolean isVisible;
	private classes.Aircraft currentAircraft;
	private double positionX, positionY, width, height;

	public Altimeter(double x, double y, double w, double h) {
		positionX = x;
		positionY = y;
		width = w;
		height = h;
		hide();
	}
	
	public void show(classes.Aircraft aircraft) {
		if (aircraft == null) return;
		currentAircraft = aircraft;
		isVisible = true;
	}
	
	public void hide() {
		currentAircraft = null;
		isVisible = false;
	}
	
	public boolean isMouseOver(int mx, int my) {
		return (mx >= positionX && mx <= positionX + width && my >= positionY && my <= positionY + height);
	}
	public boolean isMouseOver() { return isMouseOver(input.mouseX(), input.mouseY()); }

	@Override
	public void mousePressed(int key, int x, int y) {
		if (!isVisible) return;
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		if (!isVisible) return;
		if (key == input.MOUSE_WHEEL_DOWN) {
			currentAircraft.fall();
		}
		if (key == input.MOUSE_WHEEL_UP) {
			currentAircraft.climb();
		}
	}

	@Override
	public void keyPressed(int key) {}

	@Override
	public void keyReleased(int key) {}
	
	public void update(double dt) {}
	
	public void draw() {
		drawRectangle();
		if (!isVisible) return;
		drawPlaneIcon();
	}
	
	private void drawRectangle() {
		graphics.setColour(0, 128, 0);
		graphics.rectangle(false, positionX, positionY, width, height);
	}
	
	private void drawPlaneIcon() {
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
		graphics.printCentred(String.valueOf(currentAircraft.position().z()), positionX, y+32, 1, width);
	}

}
