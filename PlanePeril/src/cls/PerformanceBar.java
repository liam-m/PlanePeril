package cls;
import org.newdawn.slick.Color;

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
	private final static int BAR_HEIGHT = 25;
	private final static int BAR_WIDTH = 600;
	private double drawn_value = current_value;
	
	// colours
	final Color KHAKI = new Color(238, 230, 140);
	final Color LEMON = new Color(205, 201, 165);
	final Color YELLOW = new Color(205, 205, 0);
	final Color TURQUOISE = new Color(0, 199, 140);
	private Color last_colour_used = YELLOW; // default value
	
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
	 *  Purely for unit testing
	 */
	@Deprecated
	public PerformanceBar() {	
	}
	
	public int getMax() {
		return MAX_VALUE;
	}
	
	public void setToMax() { // For wrap around (when reaches bottom)		
		current_value = MAX_VALUE;
		drawn_value = MAX_VALUE;
	}
	
	/**
	 * Allows to change the value of performance bar - allows both incrementing and decrementing value
	 * @param value value added to current_value 
	 */
	public void changeValueBy(int value) {
		current_value += value;
		if (current_value > MAX_VALUE) // Can't be greater than max value
			current_value = MAX_VALUE;
		else if (current_value < MIN_VALUE) // Can't be smaller than min value 
			current_value = MIN_VALUE;
	}
	
	/**
	 * Sets the value of performance bar - only if the value is valid
	 * @param value value that performance current_value is changed to if the value is allowed
	 */
	public void setValueTo(int value) {
		if (value <= MAX_VALUE && value >= MIN_VALUE)
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
		graphics.rectangle(false, position_x, position_y, BAR_WIDTH, BAR_HEIGHT); // Bar borders
			
		
		if (current_value != drawn_value) {
			if (current_value < drawn_value)
				drawn_value -= 0.5;
			else
				drawn_value += 0.5;

			if (drawn_value >= (MAX_VALUE / 2 + MAX_VALUE / 4)) {
				graphics.setColour(KHAKI);
				last_colour_used = KHAKI;
			}
			else if (drawn_value >= (MAX_VALUE / 2 + MAX_VALUE / 2)) {
				graphics.setColour(LEMON);
				last_colour_used = LEMON;
			}
			else if (drawn_value >= (MAX_VALUE / 2)) {
				graphics.setColour(YELLOW);
				last_colour_used = YELLOW;
			}
			else if (drawn_value >= (MAX_VALUE / 2 - MAX_VALUE / 4)) {
				graphics.setColour(TURQUOISE);
				last_colour_used = TURQUOISE;
			}
			else {
				graphics.setColour(Main.RED);
				last_colour_used = Main.RED;
			}
		}
		
		graphics.setColour(last_colour_used);	
		graphics.rectangle(true, position_x, position_y, BAR_WIDTH * drawn_value / MAX_VALUE, BAR_HEIGHT -1);//bar fill
	}
	
	public boolean isEmpty() {
		return (current_value <= MIN_VALUE);
	}
	
	public static int getWidth() { 
		return PerformanceBar.BAR_WIDTH;
	}
}