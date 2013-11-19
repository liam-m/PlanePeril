package lib;

import lib.jog.graphics;

public class OrdersBox {
	
	private final int LINES;
	
	private int _x, _y, _width, _height;
	private String[] _orders;
	private int _currentOrder;
	private int _currentChar;
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
		_currentChar = 0;
		_timer = 0;
		_typing = false;
		_typeWait = 0.1;
		_buffer = "";
	}
	
	public void addOrder(String order) {
		boolean gap = false;
		for (int i = 0; i < LINES; i ++) {
			if (_orders[i] == null && !gap) {
				_orders[i] = order;
				gap = true;
			}
		}
		if (!gap) {
			for (int i = 0; i < LINES-1; i++) {
				_orders[i] = _orders[i+1];
			}
			_orders[LINES-1] = order;
		}
		_typing = true;
		_buffer.concat("\n" + order);
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
		if (_typing) {
			_timer += dt;
			if (_timer >= _typeWait) {
				_timer -= _typeWait;
				_currentChar += 1;
				if (_currentChar == _orders[_currentOrder].length() - 1) {
					_currentChar = 0;
					_currentOrder += 1;
					if (_currentOrder == size()) {
						_typing = false;
						if (_currentOrder == LINES) _currentOrder -= 1;
					}
				}
			}
		}
	}
	
	public void draw() {
		graphics.setColour(0, 128, 0);
		graphics.rectangle(false, _x, _y, _width, _height);
		for (int i = 0; i < _currentOrder; i ++) {
			if (_orders[i] != null) {
				graphics.print(_orders[i], _x + 4, _y + 4 + (i * (_height-8) / LINES));
			}
		}
		if (_typing) {
			for (int i = 0; i < _currentChar; i ++) {
				graphics.print(_orders[_currentOrder].substring(i, i+1), _x + 4 + (8 * i), _y + 4 + (_currentOrder * (_height-8) / LINES));
			}
		} else if (size() == LINES) {
			graphics.print(_orders[_currentOrder], _x + 4, _y + 4 + ((_currentOrder) * (_height-8) / LINES));
		}
	}

}