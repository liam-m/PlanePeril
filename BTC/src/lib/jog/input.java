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
	
	/*
	 * Static Mouse Constants
	 */
	public final static int MOUSE_LEFT = 0;
	public final static int MOUSE_RIGHT = 1;
	public final static int MOUSE_MIDDLE = 2;
	public final static int MOUSE_WHEEL_UP = 3;
	public final static int MOUSE_WHEEL_DOWN = 4;
	
	/*
	 * Static Keyboard Constants
	 */
	public final static int KEY_A = Keyboard.KEY_A;
	public final static int KEY_B = Keyboard.KEY_B;
	public final static int KEY_C = Keyboard.KEY_C;
	public final static int KEY_D = Keyboard.KEY_D;
	public final static int KEY_E = Keyboard.KEY_E;
	public final static int KEY_F = Keyboard.KEY_F;
	public final static int KEY_G = Keyboard.KEY_G;
	public final static int KEY_H = Keyboard.KEY_H;
	public final static int KEY_I = Keyboard.KEY_I;
	public final static int KEY_J = Keyboard.KEY_J;
	public final static int KEY_K = Keyboard.KEY_K;
	public final static int KEY_L = Keyboard.KEY_L;
	public final static int KEY_M = Keyboard.KEY_M;
	public final static int KEY_N = Keyboard.KEY_N;
	public final static int KEY_O = Keyboard.KEY_O;
	public final static int KEY_P = Keyboard.KEY_P;
	public final static int KEY_Q = Keyboard.KEY_Q;
	public final static int KEY_R = Keyboard.KEY_R;
	public final static int KEY_S = Keyboard.KEY_S;
	public final static int KEY_T = Keyboard.KEY_T;
	public final static int KEY_U = Keyboard.KEY_U;
	public final static int KEY_V = Keyboard.KEY_V;
	public final static int KEY_W = Keyboard.KEY_W;
	public final static int KEY_X = Keyboard.KEY_X;
	public final static int KEY_Y = Keyboard.KEY_Y;
	public final static int KEY_Z = Keyboard.KEY_Z;
	public final static int KEY_SPACE = Keyboard.KEY_SPACE;
	public final static int KEY_1 = Keyboard.KEY_1;
	public final static int KEY_2 = Keyboard.KEY_2;
	public final static int KEY_3 = Keyboard.KEY_3;
	public final static int KEY_4 = Keyboard.KEY_4;
	public final static int KEY_5 = Keyboard.KEY_5;
	public final static int KEY_6 = Keyboard.KEY_6;
	public final static int KEY_7 = Keyboard.KEY_7;
	public final static int KEY_8 = Keyboard.KEY_8;
	public final static int KEY_9 = Keyboard.KEY_9;
	public final static int KEY_0 = Keyboard.KEY_0;
	
	public final static int KEY_F1 = Keyboard.KEY_F1;
	public final static int KEY_F2 = Keyboard.KEY_F2;
	public final static int KEY_F3 = Keyboard.KEY_F3;
	public final static int KEY_F4 = Keyboard.KEY_F4;
	public final static int KEY_F5 = Keyboard.KEY_F5;
	public final static int KEY_F6 = Keyboard.KEY_F6;
	public final static int KEY_F7 = Keyboard.KEY_F7;
	public final static int KEY_F8 = Keyboard.KEY_F8;
	public final static int KEY_F9 = Keyboard.KEY_F9;
	public final static int KEY_F10 = Keyboard.KEY_F10;
	public final static int KEY_F11 = Keyboard.KEY_F11;
	public final static int KEY_F12 = Keyboard.KEY_F12;
	public final static int KEY_F13 = Keyboard.KEY_F13;
	public final static int KEY_F14 = Keyboard.KEY_F14;
	public final static int KEY_F15 = Keyboard.KEY_F15;
	public final static int KEY_F16 = Keyboard.KEY_F16;
	public final static int KEY_F17 = Keyboard.KEY_F17;
	public final static int KEY_F18 = Keyboard.KEY_F18;
	public final static int KEY_F19 = Keyboard.KEY_F19;
	
	public final static int KEY_ESCAPE = Keyboard.KEY_ESCAPE;
	public final static int KEY_SCROLL_LOCK = Keyboard.KEY_SCROLL;
	public final static int KEY_PAUSE = Keyboard.KEY_PAUSE;
	
	public final static int KEY_LCRTL = Keyboard.KEY_LCONTROL;
	public final static int KEY_RCRTL = Keyboard.KEY_RCONTROL;
	public final static int KEY_LSHIFT = Keyboard.KEY_LSHIFT;
	public final static int KEY_RSHIFT = Keyboard.KEY_RSHIFT;
	public final static int KEY_CAPS_LOCK = Keyboard.KEY_CAPITAL;
	public final static int KEY_TAB = Keyboard.KEY_TAB;
	public final static int KEY_RETURN = Keyboard.KEY_RETURN;
	public final static int KEY_BACKSPACE = Keyboard.KEY_BACK;
	
	public final static int KEY_UP = Keyboard.KEY_UP;
	public final static int KEY_DOWN = Keyboard.KEY_DOWN;
	public final static int KEY_LEFT = Keyboard.KEY_LEFT;
	public final static int KEY_RIGHT = Keyboard.KEY_RIGHT;
	
}
