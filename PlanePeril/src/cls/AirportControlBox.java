package cls;

import lib.jog.graphics;
import lib.jog.input;
import lib.jog.input.EventHandler;
import lib.jog.window;
import pp.Main;

public class AirportControlBox implements EventHandler{
	private Airport airport;	
	private int number_of_divisions;	
	private double x_position, y_position, width, height;	
	private boolean clicked = false;
	public boolean signal_take_off = false;
	
	/**
	 * Constructor for the control box
	 * @param x the x coordinate to draw at
	 * @param y the y coordinate to draw at
	 * @param w the width of the box
	 * @param h the height of the box
	 * @param airport The airport the box controls
	 */
	public AirportControlBox(double x, double y, double w, double h, Airport airport) {
		x_position = x;
		y_position = y;
		width = w;
		height = h;
		this.airport = airport;
		number_of_divisions = airport.getMaxAircraft() + 1;
	}
	
	/**
	 * Draws the box to the screen
	 */
	public void draw() {
		drawBoxOutline();
		drawLabels();
		if (clicked) {
			graphics.setColour(Main.GREEN);
			graphics.rectangle(true, x_position, (y_position + height) - (height /number_of_divisions), width, height/number_of_divisions);
		}
	}
	
	/**
	 * Draws the rectangle and the aircraft slots of the the box 
	 * (number of divisions is the hangar size of the airport + 1 for the button to signal take off)
	 */
	private void drawBoxOutline() {
		// Outline
		graphics.setColour(Main.GREEN);
		graphics.rectangle(false, x_position, y_position, width, height);
		
		// Inner lines
		double y =  (window.getHeight() - height / number_of_divisions) - (window.getHeight() - (y_position + height)); 
		for (int i = 0; i < number_of_divisions; i++) {
			graphics.line(x_position, y, x_position + width, y);
			y -= height / number_of_divisions;
		}
	}
	
	/**
	 * Draws the flight names and time bars, as well as the text on the button either "TAKE OFF" or "AIRPORT BUSY" 
	 */
	private void drawLabels() {
		// Draw take off button
		int opacity = (airport.getNumAircraft() == 0) ? 128 : 256; // Grey out if not clickable
		graphics.setColour(0, 128, 0, opacity);
		double y = (window.getHeight() - height / number_of_divisions) - (window.getHeight() - (y_position + height));
		graphics.print("TAKE OFF", x_position + ((width - 70)/2), y + 9);
		
		graphics.setColour(Main.GREEN);
		
		// Draw aircraft in hangar
		double y_position = y + 12;
		for (int i = 0; i < airport.getNumAircraft(); i++) {
			y_position -= (height / number_of_divisions);
			
			graphics.setColour(Main.GREEN);
			graphics.print(airport.getAircraft().get(i).getName(), x_position + ((width - 70)/2), y_position - 3);		
		}
		
	}
	
	/**
	 * Returns True if the mouse is over the take off button
	 * @param x Cursor's x Coordinate
	 * @param y Cursor's y Coordinate
	 * @return
	 */
	private boolean isMouseOverTakeOffButton(int x, int y) {
		if (x < x_position || x > x_position + width) return false; 
		if (y < (y_position + height) - (height/number_of_divisions) || y > (y_position + height)) return false;
		return true;		
	}
			
	@Override
	public void mousePressed(int key, int x, int y) {
		if (key == input.MOUSE_LEFT && isMouseOverTakeOffButton(x, y) && airport.getNumAircraft() > 0) {
			clicked = true;
		}
	}


	@Override
	public void mouseReleased(int key, int x, int y) {
		clicked = false;
		if (key == input.MOUSE_LEFT) {
			signal_take_off  = true;
		}
	}


	@Override
	public void keyPressed(int key) {
		
	}


	@Override
	public void keyReleased(int key) {
		
	}
}
