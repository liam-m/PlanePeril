package lib;

import pp.Main;
import lib.jog.graphics;

/**
 * Class for a visual representation text. It has word wrap enabled 
 * and prints out orders character by character in a retro style. It
 * also has support for delays between lines.
 * @author Huw Taylor
 */
public class TextBox {
	
	protected final int LINES;
	protected final char SEPARATOR = '|';
	public static final char DELAY_START = '{';
	public static final char DELAY_END = '}';
	
	protected double typeWait;
	protected int x, y, width, height;
	protected String[] orders;
	protected int currentOrder;
	protected double timer;
	protected double delayTimer;
	protected boolean isDelaying;
	protected boolean is_typing;
	protected String buffer;

	/**
	 * Constructor of a TextBox.
	 * @param x the x coordinate to display the box.
	 * @param y the y coordinate to display the box.
	 * @param width the width the box wrap to.
	 * @param height the height of the box.
	 * @param lines the maximum amount of lines to display at a time.
	 */
	public TextBox(int x, int y, int width, int height, int lines) {
		LINES = lines;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		typeWait = 0.01;
		orders = new String[LINES];
		currentOrder = 0;
		for (int i = 0; i < LINES; i ++) {
			orders[i] = "";
		}
		timer = 0;
		delayTimer = 0;
		isDelaying = false;
		is_typing = false;
		buffer = "";
	}
	
	/**
	 * Changes the speed at which text is 'typed;.
	 * @param delay the new wait in seconds between each character.
	 */
	public void setSpeed(double delay) {
		typeWait = delay;
	}
	
	/**
	 * Adds a line of text to be displayed.
	 * @param the text to be written.
	 */
	public void addText(String order) {
		// Word Wrap
		if (order.length()*8 > width) {
			String wrappedOrder = order.substring(0, (width/8)-1);
			while (wrappedOrder.charAt(wrappedOrder.length()-1) != ' ') {
				wrappedOrder = wrappedOrder.substring(0, wrappedOrder.length()-1);
			}
			buffer += wrappedOrder + SEPARATOR;
			addText(order.substring(wrappedOrder.length()));
		} else {
			buffer += order + SEPARATOR;
		}
		is_typing = true;
	}

	/**
	 * Adds a delay, making the textbox wait before continuing.
	 * @param duration the length of the delay in seconds.
	 */
	public void delay(double duration) {
		buffer += DELAY_START + String.valueOf(duration) + DELAY_END;
	}
	
	/**
	 * Adds a newline to the textbox.
	 */
	public void newline() {
		addText(" ");
	}
	
	public void newlines(int num) {
		for (int i=0; i<num; i++)
			newline();
	}
	
	/**
	 * Accesses how many lines we are currently using.
	 * @return the amount of used lines.
	 */
	protected int linesBeingUsed() {
		for (int i = 0; i < LINES; i ++) {
			if (orders[i] == null || orders[i] == "") {
				return i;
			}
		}
		return LINES;
	}
	
	/**
	 * Accesses whether we have stopped typing and have no orders queued up.
	 * @return whether the TextBox is up to date.
	 */
	public boolean isUpToDate() {
		return !is_typing;
	}
	
	/**
	 * Updates the timer of the TextBox.
	 * @param dt time since the last update call.
	 */
	public void update(double dt) {
		// Update delay
		if (isDelaying) {
			if (delayTimer <= 0) {
				isDelaying = false;
			} else {
				delayTimer = Math.max(0, delayTimer - dt);
				return;
			}
		}
		// Update timer
		timer += dt;
		if (timer >= typeWait) {
			timer -= typeWait;
			// Finished
			if (buffer.isEmpty()) {
				is_typing = false;
			// Delay
			} else if (buffer.charAt(0) == DELAY_START) {
				buffer = buffer.substring(1);
				isDelaying = true;
				String delay = "";
				while (buffer.charAt(0) != DELAY_END) {
					delay += buffer.charAt(0);
					buffer = buffer.substring(1);
				}
				buffer = buffer.substring(1);
				delayTimer = Double.parseDouble(delay);
			// New Line
			} else if (buffer.charAt(0) == SEPARATOR) {
				currentOrder += 1;
				buffer = buffer.substring(1);
			} else {
				// Too many lines
				if (currentOrder >= LINES) {
					ripple();
				}
				orders[currentOrder] += buffer.substring(0, 1);
				buffer = buffer.substring(1);
			}
		}
	}
	
	/**
	 * Cycle through the lines, removing the first, and moving the rest up by one.
	 */
	protected void ripple() {
		for (int i = 0; i < LINES-1; i ++) {
			orders[i] = orders[i+1];
		}
		orders[LINES-1] = "";
		currentOrder = Math.max(0, currentOrder - 1);
	}
	
	/**
	 * Prints the currently available characters of the TextBox.
	 */
	public void draw() {
		graphics.setColour(Main.GREEN);
		for (int i = 0; i < linesBeingUsed(); i ++) {
			graphics.print(orders[i], x + 4, y + 4 + (i * (height-8) / LINES));
		}
	}

}