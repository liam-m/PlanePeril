package lib.jog;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

/**
 * <h1>jog.input</h1>
 * <p>Provides a layer for the LWJGL libraries' Mouse and Keyboard classes.
 * jog.input gives all necessary methods, abstracting away, but also allowing for extension.</p>
 * @author IMP1
 */
public abstract class input {
	
	public final static int MOUSE_LEFT = 0;
	public final static int MOUSE_RIGHT = 1;
	public final static int MOUSE_MIDDLE = 2;
	public final static int MOUSE_WHEEL_UP = 3;
	public final static int MOUSE_WHEEL_DOWN = 4;
	
	public final static int KEY_ESCAPE = Keyboard.KEY_ESCAPE;
	public final static int KEY_UP = Keyboard.KEY_UP;
	public final static int KEY_DOWN = Keyboard.KEY_DOWN;
	public final static int KEY_LEFT = Keyboard.KEY_LEFT;
	public final static int KEY_RIGHT = Keyboard.KEY_RIGHT;
	public final static int KEY_SPACE = Keyboard.KEY_SPACE;
	public final static int KEY_LCRTL = Keyboard.KEY_LCONTROL;
	public final static int KEY_W = Keyboard.KEY_W;
	
	/**
	 * Interface for a class that is to receive keyboard and mouse events.
	 * @author IMP1
	 */
	public interface EventHandler {
		
		/**
		 * Called when a mouse button has been depressed.
		 * @param key the button being pressed.
		 * @param x the horizontal position of the mouse.
		 * @param y the vertical position of the mouse.
		 */
		public void mousePressed(int key, int x, int y);
		
		/**
		 * Called when a mouse button has been released.
		 * @param key the button being released.
		 * @param x the horizontal position of the mouse.
		 * @param y the vertical position of the mouse.
		 */
		public void mouseReleased(int key, int x, int y);
		
		/**
		 * Called when a keyboard key has been depressed.
		 * @param key the key being pressed.
		 */
		public void keyPressed(int key);
		
		/**
		 * Called when a keyboard key has been released. 
		 * @param key the key being released.
		 */
		public void keyReleased(int key);

	}

	/**
	 * Updates the input buffer, collecting keyboard and mouse events
	 * @param handler
	 */
	public static void update(EventHandler handler) {
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() >= 0) {
					handler.mousePressed(Mouse.getEventButton(), mouseX(), mouseY());
				}
			} else {
				if (Mouse.getEventButton() >= 0) {
					handler.mouseReleased(Mouse.getEventButton(), mouseX(), mouseY());
				}
			}
		}
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				handler.keyPressed(Keyboard.getEventKey());
			} else {
				handler.keyReleased(Keyboard.getEventKey());
			}
		}
		int mouseScroll = Mouse.getDWheel();
		if (mouseScroll < 0) {
			handler.mouseReleased(MOUSE_WHEEL_DOWN, mouseX(), mouseY());
		} else if (mouseScroll > 0) {
			handler.mouseReleased(MOUSE_WHEEL_UP, mouseX(), mouseY());
		}
	}
	
	/**
	 * Accesses whether the state of the specified key on the keyboard.
	 * @param key the integer representation of the keyboard key being pressed
	 * @return whether the key is currently depressed.
	 */
	public static boolean isKeyDown(int key) {
		return Keyboard.isKeyDown(key);
	}
	
	/**
	 * Accesses whether the state of the specified button on the mouse.
	 * @param button the integer representation of the mouse button being pressed
	 * @return whether the button is currently depressed.
	 */
	public static boolean isMouseDown(int button) {
		return Mouse.isButtonDown(button);
	}
	
	/**
	 * Accesses the coordinates of the mouse relative to a rectangle on the window
	 * @param x the rectangle's window x coordinate 
	 * @param y the rectangle's window y coordinate
	 * @param width the rectangle's width
	 * @param height the rectangle's height
	 * @return whether the mouse's coordinates are in the rectangle
	 */
	public static boolean isMouseInRect(int x, int y, int width, int height) {
		int mx = mouseX();
		int my = mouseY();
		return (mx >= x && mx <= x + width && my >= y && my <= y + height);
	}
	
	/**
	 * Access the horizontal coordinate of the mouse's position, with the upper left being the origin.
	 * @return the current x coordinate of the mouse
	 */
	public static int mouseX() {
		return Mouse.getX();
	}
	
	/**
	 * Access the vertical coordinate of the mouse's position, with the upper left being the origin.
	 * @return the current y coordinate of the mouse
	 */
	public static int mouseY() {
		return window.height() - Mouse.getY();
	}
	
}