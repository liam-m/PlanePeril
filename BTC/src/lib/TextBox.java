package lib;

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
	protected final char DELAY_START = '{';
	protected final char DELAY_END = '}';
	protected final double _typeWait = 0.01;
	
	protected int _x, _y, _width, _height;
	protected String[] _orders;
	protected int _currentOrder;
	protected double _timer;
	protected double _delayTimer;
	protected boolean _isDelaying;
	protected boolean _typing;
	protected String _buffer;

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
		_x = x;
		_y = y;
		_width = width;
		_height = height;
		_orders = new String[LINES];
		_currentOrder = 0;
		for (int i = 0; i < LINES; i ++) {
			_orders[i] = "";
		}
		_timer = 0;
		_delayTimer = 0;
		_isDelaying = false;
		_typing = false;
		_buffer = "";
	}
	
	/**
	 * Adds a line of text to be displayed.
	 * @param the text to be written.
	 */
	public void addText(String order) {
		// Word Wrap
		if (order.length()*8 > _width) {
			String wrappedOrder = order.substring(0, (_width/8)-1);
			while (wrappedOrder.charAt(wrappedOrder.length()-1) != ' ') {
				wrappedOrder = wrappedOrder.substring(0, wrappedOrder.length()-1);
			}
			_buffer += wrappedOrder + SEPARATOR;
			addText(order.substring(wrappedOrder.length()));
		} else {
			_buffer += order + SEPARATOR;
		}
		_typing = true;
	}
	
	protected int linesBeingUsed() {
		for (int i = 0; i < LINES; i ++) {
			if (_orders[i] == null || _orders[i] == "") {
				return i;
			}
		}
		return LINES;
	}
	
	/**
	 * Accessed whether we have stopped typing and have no orders queued up.
	 * @return whether the TextBox is up to date.
	 */
	public boolean isUpToDate() {
		return !_typing;
	}
	
	/**
	 * Updates the timer of the TextBox.
	 * @param dt time since the last update call.
	 */
	public void update(double dt) {
		if (_isDelaying) {
			if (_delayTimer <= 0) {
				_isDelaying = false;
			} else {
				_delayTimer = Math.max(0, _delayTimer - dt);
				return;
			}
		}
		_timer += dt;
		if (_timer >= _typeWait) {
			_timer -= _typeWait;
			if (_buffer.isEmpty()) {
				_typing = false;
			} else if (_buffer.charAt(0) == DELAY_START) {
				_buffer = _buffer.substring(1);
				_isDelaying = true;
				String delay = "";
				while (_buffer.charAt(0) != DELAY_END) {
					delay += _buffer.charAt(0);
					_buffer = _buffer.substring(1);
				}
				_buffer = _buffer.substring(1);
				_delayTimer = Double.parseDouble(delay);
			} else if (_buffer.charAt(0) == SEPARATOR) {
				_currentOrder += 1;
				_buffer = _buffer.substring(1);
			} else {
				if (_currentOrder >= LINES) {
					ripple();
				}
				_orders[_currentOrder] += _buffer.substring(0, 1);
				_buffer = _buffer.substring(1);
			}
		}
	}
	
	public void delay(double duration) {
		_buffer += DELAY_START + String.valueOf(duration) + DELAY_END;
	}
	
	protected void ripple() {
		for (int i = 0; i < LINES-1; i ++) {
			_orders[i] = _orders[i+1];
		}
		_orders[LINES-1] = "";
		_currentOrder = Math.max(0,  _currentOrder - 1);
	}
	
	/**
	 * Prints the currently available characters of the TextBox.
	 */
	public void draw() {
		graphics.setColour(0, 128, 0);
		for (int i = 0; i < linesBeingUsed(); i ++) {
			graphics.print(_orders[i], _x + 4, _y + 4 + (i * (_height-8) / LINES));
		}
	}

}