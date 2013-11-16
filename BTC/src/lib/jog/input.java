package lib.jog;

import org.lwjgl.input.Mouse;
import org.lwjgl.input.Keyboard;

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
	
	public interface EventHandler {
		
		public void mousePressed(int key, int x, int y);
		public void mouseReleased(int key, int x, int y);
		public void keyPressed(int key);
		public void keyReleased(int key);

	}

	public static void update(EventHandler handler) {
		while (Mouse.next()) {
			if (Mouse.getEventButtonState()) {
				if (Mouse.getEventButton() >= 0) {
					handler.mousePressed(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());
				}
			} else {
				if (Mouse.getEventButton() >= 0) {
					handler.mouseReleased(Mouse.getEventButton(), Mouse.getX(), Mouse.getY());
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
			handler.mouseReleased(MOUSE_WHEEL_DOWN, Mouse.getX(), Mouse.getY());
		} else if (mouseScroll > 0) {
			handler.mouseReleased(MOUSE_WHEEL_UP, Mouse.getX(), Mouse.getY());
		}
	}
	
	public static boolean isKeyDown(int key) {
		return Keyboard.isKeyDown(key);
	}
	
	public static boolean isMouseDown(int button) {
		return Mouse.isButtonDown(button);
	}
	
	public static int mouseX() {
		return Mouse.getX();
	}
	
	public static int mouseY() {
		return Mouse.getY();
	}
	
}