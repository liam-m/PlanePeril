package lib;

import lib.jog.graphics;
import lib.jog.input;

public class ButtonText {
	
	public interface Action {
		public void action();
	}

	private int x_coordinate, y_coordinate, width, height, x_offset, y_offset;
	private String text;
	private org.newdawn.slick.Color colour_default, colour_hover, colour_unavailable;
	private Action action;
	private boolean is_available, has_border, centred;

	public ButtonText(String text, Action action, int x_coordinate, int y_coordinate, int width, int height, int x_offset, int y_offset, boolean border, boolean centred) {
		this.text = text;
		this.action = action;
		this.x_coordinate = x_coordinate;
		this.y_coordinate = y_coordinate;
		this.width = width;
		this.height = height;
		this.x_offset = x_offset;
		this.y_offset = y_offset;
		colour_default = new org.newdawn.slick.Color(0, 128, 0);
		colour_hover = new org.newdawn.slick.Color(128, 128, 128);
		colour_unavailable = new org.newdawn.slick.Color(64, 64, 64);
		is_available = true;
		this.has_border = border;
		this.centred = centred;
	}
	
	public ButtonText(String text, Action action, int x_coordinate, int y_coordinate, int width, int height, boolean border, boolean centred) {
		this.text = text;
		this.action = action;
		this.x_coordinate = x_coordinate;
		this.y_coordinate = y_coordinate;
		this.width = width;
		this.height = height;
		this.x_offset = (width - (text.length() * 8)) / 2;
		this.y_offset = (height - 8) / 2;
		colour_default = new org.newdawn.slick.Color(0, 128, 0);
		colour_hover = new org.newdawn.slick.Color(128, 128, 128);
		colour_unavailable = new org.newdawn.slick.Color(64, 64, 64);
		is_available = true;
		this.has_border = border;
		this.centred = centred;
	}
	
	public boolean isMouseOver(int mouse_x, int mouse_y) {
		return (mouse_x >= x_coordinate && mouse_x <= x_coordinate + width && mouse_y >= y_coordinate && mouse_y <= y_coordinate + height);
	}
	
	public boolean isMouseOver() {
		return isMouseOver(input.getMouseX(), input.getMouseY()); 
	}
	
	public void setText(String newText) {
		text = newText;
	}
	
	public void setAvailability(boolean available) {
		this.is_available = available;
	}
	
	public void act() {
		if (!is_available)
			return;
		action.action();
	}
	
	public void draw() {
		if (!is_available) {
			graphics.setColour(colour_unavailable);
		}
		else if (isMouseOver()) {
			graphics.setColour(colour_hover);
		} else {
			graphics.setColour(colour_default);
		}
		if (has_border) {
			graphics.rectangle(false, x_coordinate + x_offset, y_coordinate + y_offset, width, height);
			if (centred)
				graphics.printTextCentred(text, x_coordinate + x_offset, y_coordinate + (y_offset + height)/2, 1, width);
			else
				graphics.print(text, x_coordinate + x_offset, y_coordinate + (y_offset + height)/2);
		} else {
			if (centred)
				graphics.printTextCentred(text, x_coordinate + x_offset, y_coordinate + y_offset, 1, width);
			else
				graphics.print(text, x_coordinate + x_offset, y_coordinate + y_offset);
		}
	}

}
