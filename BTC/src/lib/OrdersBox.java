package lib;

/**
 * Class for a visual representation of orders given
 * to aircraft. It has word wrap enabled and prints out
 * orders character by character in a retro style.
 * @author Huw Taylor
 */
public class OrdersBox extends TextBox {
	
	private final double REMOVAL_WAIT = 6;
	private double _removalTimer;

	/**
	 * Constructor of a OrdersBox.
	 * @param x the x coordinate to display the box.
	 * @param y the y coordinate to display the box.
	 * @param width the width the box wrap to.
	 * @param height the height of the box.
	 * @param lines the maximum amount of lines to display at a time.
	 */
	public OrdersBox(int x, int y, int width, int height, int lines) {
		super(x, y, width, height, lines);
		_removalTimer = 0;
	}
	
	/**
	 * Adds an order to be displayed.
	 * @param order the text to be written.
	 */
	public void addOrder(String order) {
		addText(order);
	}

	/**
	 * Updates the timer of the OrdersBox.
	 * @param dt time since the last update call.
	 */
	public void update(double dt) {
		if (!_typing) {
			_removalTimer += dt;
			if (_removalTimer >= REMOVAL_WAIT) {
				_removalTimer -= REMOVAL_WAIT;
				ripple();
			}
			return;
		}
		super.update(dt);
	}
	
}