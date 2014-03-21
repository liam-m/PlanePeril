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
	private boolean available, border;

	public ButtonText(String text, Action action, int x_coordinate, int y_coordinate, int width, int height, int x_offset, int y_offset, boolean border) {
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
		available = true;
		this.border = border;
	}
	
	public ButtonText(String text, Action action, int x_coordinate, int y_coordinate, int width, int height, boolean border) {
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
		available = true;
		this.border = border;
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
		this.available = available;
	}
	
	public void act() {
		if (!available)
			return;
		action.action();
	}
	
	public void draw() {
		if (!available) {
			graphics.setColour(colour_unavailable);
		}
		else if (isMouseOver()) {
			graphics.setColour(colour_hover);
		} else {
			graphics.setColour(colour_default);
		}
		graphics.print(text, x_coordinate + x_offset, y_coordinate + y_offset);
		if (border)
			graphics.rectangle(false, x_coordinate + x_offset, y_coordinate + y_offset, width, height);
	}

}
