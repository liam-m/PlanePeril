package cls;
import pp.Main;
import lib.jog.graphics;

public class PerformanceBar {
	private final int MAX_VALUE = 120;
	private final int MIN_VALUE = 0;
	private final int STARTING_VALUE = 60;
	private int current_value = STARTING_VALUE;
	
	// for drawing
	private int position_x;
	private int position_y;
	private final int BAR_HEIGHT = 50;
	private final int BAR_WIDTH = 300;
	private double drawn_value = current_value;
	
	/**
	 * Constructor for performance bar, takes coordinates of where the performance bar should be drawn
	 * @param location_x  x coordinate where the performance bar starts to be drawn 
	 * @param location_y  y coordinate where the performance bar starts to be drawn
	 */
	public PerformanceBar(int location_x, int location_y) {
		this.position_x = location_x;
		this.position_y = location_y;
	}
	
	public void setMax() {
		current_value = MAX_VALUE;
		drawn_value = MAX_VALUE;
		}
	
	/**
	 * Allows to change the value of performance bar - allows both incrementing and decrementing value
	 * @param value value added to current_value 
	 */
	public void changeValueBy(int value) {
		current_value += value;
		// Can't be greater than max value
		if (current_value > MAX_VALUE)
			current_value = MAX_VALUE;
		// Can't be smaller than min value
		else if (current_value < MIN_VALUE) 
			//current_value = MIN_VALUE; // will be changed to sth different and WILL TAKE A LIFE AWAY
			setMax();
	}
	
	/**
	 * Sets the value of performance bar - only if the value is valid
	 * @param value value that performance current_value is changed to if the value is allowed
	 */
	public void setValueTo(int value) {
		if ((value <= MAX_VALUE) && (value >= MIN_VALUE))
			current_value = value;
	}
	
	public int getCurrentValue() {
		return this.current_value;
	}
	
	/**
	 * Draws the performance bar. The colour of the filling changes when the value decreases and also based on the intermediate value 
	 */
	public void draw() {
		graphics.setColour(Main.GREEN);
		graphics.rectangle(false, position_x, position_y, BAR_WIDTH, BAR_HEIGHT); // draws the borders of the bar
			
		if (current_value < drawn_value) {
			graphics.setColour(Main.RED);
			drawn_value -= 0.5;
		}	

		else { 
			if (this.current_value > drawn_value) 
				drawn_value += 0.5;
			
			if (drawn_value <= (MAX_VALUE / 2)) 
				graphics.setColour(238, 230, 140); // khaki colour
			else if (drawn_value <= (MAX_VALUE / 2 + MAX_VALUE/6)) 
				graphics.setColour(205, 201, 165); // lemonchiffon  colour
			else if (drawn_value <= (MAX_VALUE / 2 + MAX_VALUE/3)) 
				graphics.setColour(205, 205, 0); // yellow  colour
			else
				graphics.setColour(0, 199, 140); // turquoiseblue colour
		}
		
		graphics.rectangle(true, position_x, position_y, BAR_WIDTH * drawn_value / MAX_VALUE, BAR_HEIGHT);
		
	}
			
}

