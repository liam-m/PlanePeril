package lib;

import lib.jog.graphics;

/**
 * Class for a visual representation of orders given
 * to aircraft. It has word wrap enabled and prints out
 * orders character by character in a retro style.
 * @author Huw Taylor
 */
public class OrdersBox {
	
	private final int LINES;
	private final char SEPARATOR = '|';
	private final double _typeWait = 0.01;
	private final double _removalWait = 6;
	
	private int _x, _y, _width, _height;
	private String[] _orders;
	private int _currentOrder;
	private double _timer;
	private double _removalTimer;
	private boolean _typing;
	private String _buffer;

	/**
	 * Constructor of a OrdersBox.
	 * @param x the x coordinate to display the box.
	 * @param y the y coordinate to display the box.
	 * @param width the width the box wrap to.
	 * @param height the height of the box.
	 * @param lines the maximum amount of lines to display at a time.
	 */
	public OrdersBox(int x, int y, int width, int height, int lines) {
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
		_removalTimer = 0;
		_typing = false;
		_buffer = "";
	}
	
	/**
	 * Adds an order to be displayed.
	 * @param order the text to be written.
	 */
	public void addOrder(String order) {
		// Word Wrap
		if (order.length()*8 > _width) {
			String wrappedOrder = order.substring(0, (_width/8)-1);
			while (wrappedOrder.charAt(wrappedOrder.length()-1) != ' ') {
				wrappedOrder = wrappedOrder.substring(0, wrappedOrder.length()-1);
			}
			_buffer += wrappedOrder + SEPARATOR;
			addOrder(order.substring(wrappedOrder.length()));
		} else {
			_buffer += order + SEPARATOR;
		}
		_typing = true;
		_removalTimer = 0;
	}
	
	private int linesBeingUsed() {
		for (int i = 0; i < LINES; i ++) {
			if (_orders[i] == null || _orders[i] == "") {
				return i;
			}
		}
		return LINES;
	}
	
	/**
	 * Accessed whether we have stopped typing and have no orders queued up.
	 * @return whether the OrdersBox is up to date.
	 */
	public boolean isUpToDate() {
		return !_typing;
	}
	
	/**
	 * Updates the timer of the OrdersBox.
	 * @param dt time since the last update call.
	 */
	public void update(double dt) {
		if (!_typing) {
			_removalTimer += dt;
			if (_removalTimer >= _removalWait) {
				_removalTimer -= _removalWait;
				ripple();
			}
			return;
		}
		_timer += dt;
		if (_timer >= _typeWait) {
			_timer -= _typeWait;
			if (_buffer.isEmpty()) {
				_typing = false;
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
	
	private void ripple() {
		for (int i = 0; i < LINES-1; i ++) {
			_orders[i] = _orders[i+1];
		}
		_orders[LINES-1] = "";
		_currentOrder = Math.max(0,  _currentOrder - 1);
	}
	
	/**
	 * Prints the currently available characters of the OrdersBox.
	 */
	public void draw() {
		graphics.setColour(0, 128, 0);
		for (int i = 0; i < linesBeingUsed(); i ++) {
			graphics.print(_orders[i], _x + 4, _y + 4 + (i * (_height-8) / LINES));
		}
	}

}