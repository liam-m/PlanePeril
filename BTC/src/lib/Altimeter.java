package lib;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.input.EventHandler;

public class Altimeter implements EventHandler {
	
	private boolean _visible;
	private cls.Aircraft _aircraft;
	private double _x, _y, _width, _height;
	private double _dragY;

	public Altimeter(double x, double y, double width, double height) {
		_x = x;
		_y = y;
		_width = width;
		_height = height;
		hide();
	}
	
	public void show(cls.Aircraft aircraft) {
		_aircraft = aircraft;
		_visible = true;
	}
	
	public void hide() {
		_aircraft = null;
		_visible = false;
	}
	
	public boolean isMouseOver(int mx, int my) {
		return (mx >= _x && mx <= _x + _width && my >= _y && my <= _y + _height);
	}
	public boolean isMouseOver() { return isMouseOver(input.mouseX(), input.mouseY()); }

	@Override
	public void mousePressed(int key, int x, int y) {
		if (!_visible) return;
		if (key == input.MOUSE_LEFT && isMouseOver(x, y)) {
			_dragY = y;
		}
	}

	@Override
	public void mouseReleased(int key, int x, int y) {
		if (!_visible) return;
		if (key == input.MOUSE_LEFT && isMouseOver(x, y)) {
			if (_dragY - y > 0) {
				_aircraft.climb();
			} else {
				_aircraft.fall();
			}
		}
		if (key == input.MOUSE_WHEEL_DOWN) {
			_aircraft.fall();
		}
		if (key == input.MOUSE_WHEEL_UP) {
			_aircraft.climb();
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
	
	public void draw() {
		if (!_visible) return;
//		graphics.setColour(0, 128, 128);
//		graphics.rectangle(true, _x, _y, _width, _height / 2);
//		graphics.setColour(128, 128, 0);
//		graphics.rectangle(true, _x, _y + _height / 2, _width, _height / 2);
		graphics.setColour(0, 128, 0);
		graphics.rectangle(false, _x, _y, _width, _height);
		double r = 0;
		if (_aircraft.isTurningLeft()) {
			r = -Math.PI / 12;
		} else if (_aircraft.isTurningRight()) {
			r = Math.PI / 12;
		}
		double x = _x + (_width / 2);
		double y = _y + (_height / 2);
		double wingLength = _width / 3;
		double tailLength = _width / 9;
		graphics.line(x, y, x + wingLength * Math.cos(r), y + wingLength * Math.sin(r));
		r -= Math.PI / 2;
		graphics.line(x, y, x + tailLength * Math.cos(r), y + tailLength * Math.sin(r));
		r -= Math.PI / 2;
		graphics.line(x, y, x + wingLength * Math.cos(r), y + wingLength * Math.sin(r));
		graphics.setColour(0, 0, 0);
		graphics.circle(true, _x + (_width / 2), _y + (_height / 2), 4);graphics.setColour(0, 128, 0);
		graphics.circle(false, _x + (_width / 2), _y + (_height / 2), 4);
		
	}

}
