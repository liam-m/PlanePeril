package lib;

import lib.jog.graphics;

public class OrdersBox {
	
	private final int LINES;
	private final char SEPARATOR = '|';
	
	private int _x, _y, _width, _height;
	private String[] _orders;
	private int _currentOrder;
	private double _timer;
	private double _typeWait;
	private boolean _typing;
	private String _buffer;

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
		_typing = false;
		_typeWait = 0.01;
		_buffer = "";
	}
	
	public void addOrder(String order) {
		_buffer += order + SEPARATOR;
		_typing = true;
	}
	
	private int size() {
		for (int i = 0; i < LINES; i ++) {
			if (_orders[i] == null) {
				return i;
			}
		}
		return LINES;
	}
	
	public void update(double dt) {
		if (!_typing) return;
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
					_currentOrder = LINES - 1;
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
	}
	
	public void draw() {
		graphics.setColour(0, 128, 0);
		graphics.rectangle(false, _x, _y, _width, _height);
		for (int i = 0; i < size(); i ++) {
			if (_orders[i] != null) {
				graphics.print(_orders[i], _x + 4, _y + 4 + (i * (_height-8) / LINES));
			}
		}
	}

}