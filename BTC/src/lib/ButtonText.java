package lib;

import lib.jog.graphics;
import lib.jog.input;

public class ButtonText {
	
	public interface Action {
		public void action();
	}

	private int _x, _y, _width, _height, _ox, _oy;
	private String _text;
	private org.newdawn.slick.Color _colourDefault, _colourHover;
	private Action _action;
	
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
	}
	
	public boolean isMouseOver(int mx, int my) {
		return (mx >= _x && mx <= _x + _width && my >= _y && my <= _y + _height);
	}
	public boolean isMouseOver() { return isMouseOver(input.mouseX(), input.mouseY()); }
	
	public void act() {
		_action.action();
	}
	
	public void draw() {
		if (isMouseOver()) {
			graphics.setColor(_colourHover);
		} else {
			graphics.setColor(_colourDefault);
		}
		graphics.print(_text, _x + _ox, _y + _oy);
	}

}
