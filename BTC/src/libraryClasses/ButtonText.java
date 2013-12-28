package libraryClasses;

import lib.jog.graphics;
import lib.jog.input;

public class ButtonText {
	
	public interface Action {
		public void action();
	}

	private int _x, _y, _width, _height, _ox, _oy;
	private String _text;
	private org.newdawn.slick.Color _colourDefault, _colourHover, _colourUnavailable;
	private Action _action;
	private boolean _available;
	
	public ButtonText(String text, Action action, int x, int y, int w, int h, int ox, int oy) {
		_text = text;
		_action = action;
		_x = x;
		_y = y;
		_width = w;
		_height = h;
		_ox = ox;
		_oy = oy;
		_colourDefault = new org.newdawn.slick.Color(0, 128, 0);
		_colourHover = new org.newdawn.slick.Color(128, 128, 128);
		_colourUnavailable = new org.newdawn.slick.Color(64, 64, 64);
		_available = true;
	}
	
	public ButtonText(String text, Action action, int x, int y, int w, int h) {
		_text = text;
		_action = action;
		_x = x;
		_y = y;
		_width = w;
		_height = h;
		_ox = (w - (text.length() * 8)) / 2;
		_oy = (h - 8) / 2;
		_colourDefault = new org.newdawn.slick.Color(0, 128, 0);
		_colourHover = new org.newdawn.slick.Color(128, 128, 128);
		_colourUnavailable = new org.newdawn.slick.Color(64, 64, 64);
		_available = true;
	}
	
	public boolean isMouseOver(int mx, int my) {
		return (mx >= _x && mx <= _x + _width && my >= _y && my <= _y + _height);
	}
	public boolean isMouseOver() { return isMouseOver(input.mouseX(), input.mouseY()); }
	
	public void setText(String newText) {
		_text = newText;
	}
	
	public void setAvailability(boolean available) {
		_available = available;
	}
	
	public void act() {
		if (!_available) return;
		_action.action();
	}
	
	public void draw() {
		if (!_available) {
			graphics.setColour(_colourUnavailable);
		}
		else if (isMouseOver()) {
			graphics.setColour(_colourHover);
		} else {
			graphics.setColour(_colourDefault);
		}
		graphics.print(_text, _x + _ox, _y + _oy);
	}

}
