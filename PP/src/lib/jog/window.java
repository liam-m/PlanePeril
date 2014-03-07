package lib.jog;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.util.ResourceLoader;

/**
 * <h1>jog.window</h1>
 * <p>Provides a layer upon LWJGL and Slick. jog.window allows a window to be created and managed.</p>
 * @author IMP1
 */
public abstract class window {
	
	final private static int FPS = 60;
	
	private static int _width;
	private static int _height;
	private static boolean _closed;
	
	/**
	 * Creates a new window.
	 * @param title the title of the window.
	 * @param width the width of the window.
	 * @param height the height of the window.
	 */
	public static void initialise(String title, int width, int height) {
		try {
			setSize(width, height);
			setTitle(title);
			Display.create();
			_closed = false;
		} catch (LWJGLException e) {
			e.printStackTrace();
			_closed = true;
		}
	}
	
	/** 
	 * Allows for changing the size of the window.
	 * <p>It does this by creating a new DisplayMode with a specified
	 * width and height, and sets the Display's DisplayMode to 
	 * that new DisplayMode.</p>
	 * @param width the new width for the window.
	 * @param height the new height for the window.
	 */
	public static void setSize(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setLocation(2,5);
			_width = width;
			_height = height;
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Allows access to the width of the window.
	 * @return the width of the window.
	 */
	public static int width() {
		return _width;
	}
	
	/**
	 * Allows access to the height of the window.
	 * @return the height of the window.
	 */
	public static int height() {
		return _height;
	}
	
	/**
	 * Allows access to the closed status of the window.
	 * @return whether the window is closed.
	 */
	public static boolean isClosed() {
		return _closed;
	}
	
	/**
	 * Allows for changing the title of the window.
	 * @param title the new title for the window.
	 */
	public static void setTitle(String title) {
		Display.setTitle(title);
	}
	
	/**
	 * Allows for changing the icon of the window.
	 * @param filepaths a list of filepaths of the icons. The names of 
	 * the icons must end with the size in pixels, for example 
	 * icon_filename16.png for the 16 by 16 icon.
	 */
	public static void setIcon(String[] filepaths) {
		try {
			ByteBuffer[] icons = new ByteBuffer[filepaths.length];
			for (int i = 0; i < icons.length ; i ++) {
				String name = filepaths[i].split("\\.")[0];
				int size = name.length();
				while (name.substring(size-1).matches("\\d+")) {
					size -= 1;
				}
				int width = Integer.parseInt(name.substring(size));
				icons[i] = loadIcon(filepaths[i], width);
			}
			Display.setIcon(icons);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param filename filename of the icon.
	 * @param size the width (and height) of the icon
	 * @return a ByteBuffer containing the icon's data
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	private static ByteBuffer loadIcon(String filename, int size) throws IOException {
		InputStream path = ResourceLoader.getResourceAsStream(filename);
		BufferedImage img = ImageIO.read(path);
		byte[] imageBytes = new byte[size * size * 4];
	    for (int y = 0; y < size; y++) {
	        for (int x = 0; x < size; x++) {
	            int pixel = img.getRGB(y, x);
	            for (int k = 0; k < 3; k++) {
	                imageBytes[(x*size+y)*4 + k] = (byte)(((pixel >> (2-k) * 8)) & 255); // red, green, blue
	            	imageBytes[(x*size+y)*4 + 3] = (byte)(((pixel >> (3) * 8)) & 255); // alpha
	            }
	        }
	    }
		return ByteBuffer.wrap(imageBytes);
	}
	
	/**
	 * Updates the window and syncs it with the specified FPS. 
	 * It also updates whether the window has been closed or not.
	 */
	public static void update() {
		_closed = _closed || Display.isCloseRequested();
		if (_closed) return;
		Display.update();
		Display.sync(FPS);
	}
	
	/**
	 * Disposes of the resources used by the window.
	 */
	public static void dispose() {
		Display.destroy();
	}

}
