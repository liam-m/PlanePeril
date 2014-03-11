package lib;

public class PerformanceBar {
	private final int max_value = 100;
	private final int min_value = 0;
	private final int starting_value = 50;
	private int current_value;
	
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
		int current_value = this.current_value;
		current_value += value;
		if (current_value > max_value)
			current_value = max_value;
		else if (current_value < min_value) 
			current_value = min_value; // will be changed to sth else and WILL TAKE A LIFE AWAY
		}
		
	public void setValueTo(int value) {
		if ((value < max_value) && (value > min_value))
			this.current_value = value;
	}
	
	public void drawPerformanceBar() {
		//drawing logic - location and 
	}
	
}

