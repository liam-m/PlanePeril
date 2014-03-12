package lib;
import pp.Main;
import lib.jog.graphics;

public class PerformanceBar {
	private final int max_value = 100;
	private final int min_value = 0;
	private final int starting_value = 50;
	private int current_value;
	private boolean bar_draining = false;
	
	// for drawing
	private int position_x;
	private int position_y;
	private final int height_of_bar = 50;
	private final int width_of_bar = 300;
	
	public PerformanceBar(int location_x, int location_y) {
		this.current_value = starting_value;
		this.position_x = location_x;
		this.position_y = location_y;
	}
	
	public void changeValueBy(int value) {
		this.current_value += value;
		if (this.current_value > max_value)
			this.current_value = max_value;
		else if (this.current_value < min_value) 
			this.current_value = min_value; // will be changed to sth else and WILL TAKE A LIFE AWAY
		}
		
	public void setValueTo(int value) {
		if ((value < max_value) && (value > min_value))
			this.current_value = value;
	}
	
	public int getValue() {
		return this.current_value;
	}
	
	public void drawPerformanceBar() {
		graphics.setColour(Main.GREEN);
		graphics.rectangle(false, position_x, position_y, width_of_bar, height_of_bar);
		graphics.setColour(Main.GREEN);
		graphics.rectangle(true, position_x, position_y, width_of_bar * this.current_value / 100, height_of_bar);
		//graphics.setColour(128, 0, 0, 64);
		//graphics.rectangle(true, position_x, position_y, width_of_bar - this.current_value / 100, height_of_bar);
		}
			
}

