package lib;
import pp.Main;
import lib.jog.graphics;

public class PerformanceBar {
	private final int max_value = 120;
	private final int min_value = 0;
	private final int starting_value = 60;
	private int current_value = starting_value;
	
	// for drawing
	private int position_x;
	private int position_y;
	private double drawn_value = current_value;
	private final int height_of_bar = 50;
	private final int width_of_bar = 300;
	
	/**
	 * Constructor for performance bar, takes coordinates of where the performance bar should be drawn
	 * @param location_x  x coordinate where the performance bar starts to be drawn 
	 * @param location_y  y coordinate where the performance bar starts to be drawn
	 */
	public PerformanceBar(int location_x, int location_y) {
		this.position_x = location_x;
		this.position_y = location_y;
	}
	
	/**
	 * Allows to change the value of performance bar - allows both incrementing and decrementing value
	 * @param value value added to current_value 
	 */
	public void changeValueBy(int value) {
		current_value += value;
		if (current_value > max_value)
			current_value = max_value;
		else if (current_value < min_value) 
			current_value = min_value; // will be changed to sth different and WILL TAKE A LIFE AWAY
		}
	
	/**
	 * Sets the value of performance bar - only if the value is valid
	 * @param value value that performance current_value is changed to if the value is allowed
	 */
	public void setValueTo(int value) {
		if ((value <= max_value) && (value >= min_value))
			current_value = value;
	}
	
	public int getCurrentValue() {
		return this.current_value;
	}
	
	/**
	 * Draws the performance bar. The colour of the filling changes when the value decreases and also based on the intermediate value 
	 */
	public void draw() {
		graphics.rectangle(false, position_x, position_y, width_of_bar, height_of_bar); // draws the borders of the bar
			
		if (current_value < drawn_value) {
			graphics.setColour(Main.RED);
			drawn_value -= 0.5;
		}	

		else { 
			if (this.current_value > drawn_value) 
				drawn_value += 0.5;
			
			if (drawn_value <= (max_value / 2)) 
				graphics.setColour(238, 230, 140); // khaki colour
			else if (drawn_value <= (max_value / 2 + max_value/6)) 
				graphics.setColour(205, 201, 165); // lemonchiffon  colour
			else if (drawn_value <= (max_value / 2 + max_value/3)) 
				graphics.setColour(205, 205, 0); // yellow  colour
			else
				graphics.setColour(0, 199, 140); // turquoiseblue colour
		}
		
		graphics.rectangle(true, position_x, position_y, width_of_bar * drawn_value / max_value, height_of_bar);
		
	}
			
}

