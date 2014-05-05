package lib.jog;

import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * <p>Provides a layer upon OpenGL methods. jog.graphics allows drawing basic shapes to the screen,
 * as well as images and limited font capabilities. jog.graphics (unlike OpenGL) has the graphical origin to be the window's
 * upper-left corner.</p>
 * @author IMP1
 */
public abstract class graphics{
	
	/**
	 * <p>Abstract font class with print methods.</p>
	 * @author IMP1
	 */
	public static abstract class Font {
		protected abstract void print(double x, double y, String text, double size);
		protected abstract void printCentred(double x, double y, double width, String text, double size);	
	}
	
	/**
	 * <p>A font generated from an image. Each glyph is as wide as the entire image as high.</p>
	 * @author IMP1
	 */
	private static class BitmapFont extends Font {
		private String glyphs; //string containing the characters in the same order that the image has them
		private Image image;

		private BitmapFont(String filepath, String chars) {
			image = newImage(filepath);
			glyphs = chars;
		}
		
		@Override
		protected void print(double x, double y, String text, double size) {
			y = window.getHeight() - y;
			double w = image.getHeight();
			double h = -image.getHeight();
			double qw = w / image.getWidth();
			double qh = 1;
			
	    	glEnable(GL_TEXTURE_2D);
	    	glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
	    	image.texture.bind();
			glPushMatrix();
			glTranslated(x, y, 0);
			glScaled(size, size, 1);
			glBegin(GL_QUADS);
			for (int i = 0; i < text.length(); i ++) {
				double qx = glyphs.indexOf(text.charAt(i)) * w / image.getWidth();
				glTexCoord2d(qx, 0);
				glVertex2d(w * i, 0);
				glTexCoord2d(qx + qw, 0);
				glVertex2d(w * (i+1), 0);
				glTexCoord2d(qx + qw, qh);
				glVertex2d(w * (i+1), h);
				glTexCoord2d(qx, qh);
				glVertex2d(w * i, h);
			}
			glEnd();
			glPopMatrix();
			glDisable(GL_TEXTURE_2D);
		}
		
		@Override
		protected void printCentred(double x, double y, double width, String text, double size) {
			y = window.getHeight() - y;
			double w = image.getHeight();
			double h = -image.getHeight();
			double qw = w / image.getWidth();
			double qh = 1;
			x += (width - (w * text.length() * size)) / 2;
			
			glEnable(GL_TEXTURE_2D);
	    	image.texture.bind();
			glPushMatrix();
			glTranslated(x, y, 0);
			glScaled(size, size, 1);
			glBegin(GL_QUADS);
			for (int i = 0; i < text.length(); i ++) {
				double qx = glyphs.indexOf(text.charAt(i)) * w / image.getWidth();
				glTexCoord2d(qx, 0);
				glVertex2d(w * i, 0);
				glTexCoord2d(qx + qw, 0);
				glVertex2d(w * (i+1), 0);
				glTexCoord2d(qx + qw, qh);
				glVertex2d(w * (i+1), h);
				glTexCoord2d(qx, qh);
				glVertex2d(w * i, h);
			}
			glEnd();
			glPopMatrix();
			glDisable(GL_TEXTURE_2D);
		}

	}
	
	/**
	 * <p>A font that the exists within the default font folder in the OS. Essentially SystemFont is a wrapper for TrueTypeFont.</p>
	 * @author IMP1
	 * @see TrueTypeFont
	 */
	private static class SystemFont extends Font {

		private TrueTypeFont _font;
		
		/**
		 * Constructor for a system font.
		 * @param name name of the system font.
		 * @param size the size of the created font.
		 */
		private SystemFont(String name, int size) {
			java.awt.Font awtFont = new java.awt.Font("Times New Roman", java.awt.Font.PLAIN, size);
			_font = new TrueTypeFont(awtFont, false);
		}
		
		@Override
		protected void print(double x, double y, String text, double size) {
			y = y - window.getHeight();
			glPushMatrix();
			glScaled(1, -1, 0);
			_font.drawString((int)x, (int)y, text);
			glPopMatrix();
		}

		/**
		 * Prints the text to the display centred within specified boundaries.
		 * @param x the x coordinate for the text to be drawn to.
		 * @param y the y coordinate for the text to be drawn to.
		 * @param width the width the text should be centred around.
		 * @param text the text to be drawn.
		 * @param size the size of the drawn text.
		 */
		@Override
		public void printCentred(double x, double y, double width, String text, double size) {
			y = y - window.getHeight();
			x += (width - _font.getWidth(text)) / 2;
			glPushMatrix();
			glScaled(1, -1, 0);
			_font.drawString((int)x, (int)y, text);
			glPopMatrix();
		}
		
	}
	
	/**
	 * <p>Essentially an object-orientated wrapper for the slick Texture.</p>
	 * @author IMP1
	 * @see Texture
	 */
	public static class Image {
		private Texture texture;

		private Image(String filepath) {
			try {
				String format = filepath.split("\\.")[1].toUpperCase();
				InputStream in = ResourceLoader.getResourceAsStream(filepath);
				texture = TextureLoader.getTexture(format, in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public double getWidth() { 
			return texture.getTextureWidth();
		}
		
		public double getHeight() { 
			return texture.getTextureHeight(); 
		}
		
		/**
		 * Allows access to the colours of the pixels in the image data of the texture.
		 * @param x the x coordinate on the image of the pixel.
		 * @param y the y coordinate on the image of the pixel.
		 * @return the colour at the specified pixel.
		 */
		public Color pixelAt(int x, int y) {
			int r = texture.getTextureData()[y * (int)getWidth() + x ] * -255;
			int g = texture.getTextureData()[y * (int)getWidth() + x + 1] * -255;
			int b = texture.getTextureData()[y * (int)getWidth() + x + 2] * -255;
			int a = texture.getTextureData()[y * (int)getWidth() + x + 3] * -255;
			return new Color(r, g, b, a);
		}
		
	}
	
	/**
	 * Represents a quad for drawing rectangular sections of images.
	 * @author IMP1
	 */
	public static class Quad {
		
		public final double x, y, width, height, quadWidth, quadHeight;
		
		/**
		 * Constructor for Quad.
		 * @param x the beginning horizontal coordinate of the quad in pixels.
		 * @param y the beginning vertical coordinate of the quad in pixels.
		 * @param w the width in pixels of the quad.
		 * @param h the height in pixels of the quad.
		 * @param imgWidth the width of the image the quad will be a part of.
		 * @param imgHeight the height of the image the quad will be a part of.
		 */
		private Quad(double x, double y, double w, double h, double imgWidth, double imgHeight) {
			this.x = x / imgWidth; //Quad coordinates are from 0 to 1, hence division by image size attributes
			this.y = y / imgHeight;
			this.width = w / imgWidth;
			this.height = h / imgHeight;
			this.quadWidth = w;
			this.quadHeight = h;
		}		
		
	}
	
	private static Font currentFont;
	private static Color currentColour;
	private static boolean viewPortEnabled;
	
	/**
	 * Intialises OpenGL with the appropriate matrix modes and orthographic dimensions. 
	 */
	static public void initialise() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, window.getWidth(), 0, window.getHeight(), -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		viewPortEnabled = false;
	}

	/**
	 * Sets the colour of all things drawn until either the colour is changed again
	 * or the end of the draw phase is reached. 
	 * @param colour the colour to draw things.
	 */
	static public void setColour(Color colour) {
		currentColour = colour;
		glColor4f(currentColour.r, currentColour.g, currentColour.b, currentColour.a);
	}

	/**
	 * Sets the colour of all things drawn until either the colour is changed again
	 * or the end of the draw phase is reached. 
	 * @param r the red component of the colour to draw things.
	 * @param g the green component of the colour to draw things.
	 * @param b the blue component of the colour to draw things.
	 * @param a the alpha component of the colour to draw things.
	 */
	static public void setColour(double r, double g, double b, double a) {
		currentColour = new Color((int)r, (int)g, (int)b, (int)a);
		double red = (Math.max(0, Math.min(255, r)) / 255);
		double green = Math.max(0, Math.min(255, g)) / 255;
		double blue = Math.max(0, Math.min(255, b)) / 255;
		double alpha = Math.max(0, Math.min(255, a)) / 255;
		glColor4d(red, green, blue, alpha);
	}
	
	static public void setColour(int r, int g, int b) { 
		setColour(r, g, b, 255); 
	}
	
	static public Color getColour() {
		return currentColour;
	}
	
	static public void setFont(Font font) {
		currentFont = font;
	}
	
	/**
	 * Sets a new viewport for graphics to be drawn to. Anything graphical that would
	 * affect pixels outside the new viewport will not do affect those pixels.
	 * All graphical operations are also transformed by the x and y coordinates,
	 * essentially setting a new window.
	 * @param x the x coordinate of the new viewport, relative to the window.
	 * @param y the y coordinate of the new viewport, relative to the window.
	 * @param width the width of the new viewport, in pixels.
	 * @param height the height of the new viewport, in pixels.
	 */
	static public void setViewport(int x, int y, int width, int height) {
		glPushMatrix();
		glTranslated(x, -y, 0);
		y = window.getHeight() - y;
		glEnable(GL_SCISSOR_TEST);		
		glScissor(x, y - height, width, height);
		viewPortEnabled = true;
	}
	
	/**
	 * Sets the current viewport back to the default, that is, the window.
	 */
	static public void setViewport() {
		glDisable(GL_SCISSOR_TEST);
		glPopMatrix();
		viewPortEnabled = false;
	}

	/**
	 * Creates and returns a new BitmapFont.
	 * @param filepath the path to the image file.
	 * @param glyphs the String containing the characters the image represents.
	 * @return the created font.
	 */
	static public BitmapFont newBitmapFont(String filepath, String glyphs) {
		return new BitmapFont(filepath, glyphs);
	}
	
	/**
	 * Creates and returns a new SystemFont.
	 * @param fontName the name of the font.
	 * @param size the size of the font to be created.
	 * @return the created font.
	 */
	static public SystemFont newSystemFont(String fontName, int size) {
		return new SystemFont(fontName, size);
	}
	static public SystemFont newSystemFont(String fontName) {
		return newSystemFont(fontName, 24);
	}
		
	/**
	 * Creates and returns a new Image.
	 * @param filepath
	 * @return the created image.
	 */
	static public Image newImage(String filepath) {
		return new Image(filepath);
	}
	
	/**
	 * Creates and returns a new Quad.
	 * @param x the beginning horizontal coordinate of the quad in pixels.
	 * @param y the beginning vertical coordinate of the quad in pixels.
	 * @param quadWidth the width in pixels of the quad.
	 * @param quadHeight the height in pixels of the quad.
	 * @param imageWidth the width of the image the quad will be a part of.
	 * @param imageHeight the height of the image the quad will be a part of.
	 * @return the created quad.
	 */
	static public Quad newQuad(double x, double y, double quadWidth, double quadHeight, double imageWidth, double imageHeight) {
		return new Quad(x, y, quadWidth, quadHeight, imageWidth, imageHeight);
	}
	
	/**
	 * Draws the texture image at the specified coordinates.
	 * @param drawable the image to be drawn.
	 * @param x the horizontal pixel to draw at.
	 * @param y the vertical pixel to draw at.
	 * @param r the angle in radians to draw the image at.
	 * @param ox the x coordinate of the origin of the image around which it is rotated.
	 * @param oy the y coordinate of the origin of the image around which it is rotated.
	 */
	static public void draw(Image drawable, double x, double y, double r, double ox, double oy) {
		y = window.getHeight() - y;
		r = -Math.toDegrees(r);
		
    	glEnable(GL_TEXTURE_2D);
    	drawable.texture.bind();
		glPushMatrix();
	    glTranslated(x, y, 0);
	    glRotated(r, 0, 0, 1);
	    glScaled(2, 2, 1);
		glBegin(GL_QUADS);
			glTexCoord2d(0, 0);
			glVertex2d(-ox/2, -oy/2);
			glTexCoord2d(1, 0);
			glVertex2d(ox/2, -oy/2);
			glTexCoord2d(1, 1);
			glVertex2d(ox/2, oy/2);
			glTexCoord2d(0, 1);
			glVertex2d(-ox/2, oy/2);
		glEnd();
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}
	
	static public void draw(Image drawable, double scale, double x, double y, double r, double ox, double oy) {
		y = window.getHeight() - y;
		r = -Math.toDegrees(r);
		
    	glEnable(GL_TEXTURE_2D);
    	drawable.texture.bind();
		glPushMatrix();
	    glTranslated(x, y, 0);
	    glRotated(r, 0, 0, 1);
	    glScaled(scale, scale, 1);
		glBegin(GL_QUADS);
			glTexCoord2d(0, 0);
			glVertex2d(-ox/2, -oy/2);
			glTexCoord2d(1, 0);
			glVertex2d(ox/2, -oy/2);
			glTexCoord2d(1, 1);
			glVertex2d(ox/2, oy/2);
			glTexCoord2d(0, 1);
			glVertex2d(-ox/2, oy/2);
		glEnd();
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}
	
	/**
	 * Draws the texture image at the specified coordinates.
	 * @param drawable the image to be drawn.
	 * @param x the horizontal pixel to draw at.
	 * @param y the vertical pixel to draw at.
	 */
	static public void draw(Image drawable, double x, double y) {
		y = window.getHeight() - y;
		double w = drawable.getWidth();
		double h = -drawable.getHeight();
		
		glEnable(GL_TEXTURE_2D);
    	drawable.texture.bind();
		glPushMatrix();
	    glTranslated(x, y, 0);
		glBegin(GL_QUADS);
			glTexCoord2d(0, 0);
			glVertex2d(0, 0);
			glTexCoord2d(1, 0);
			glVertex2d(w, 0);
			glTexCoord2d(1, 1);
			glVertex2d(w, h);
			glTexCoord2d(0, 1);
			glVertex2d(0, h);
		glEnd();
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}
	
	/**
	 * Draws the texture image at the specified coordinates.
	 * @param drawable the image to be drawn.
	 * @param quad the quad of the image to be drawn.
	 * @param x the horizontal pixel to draw at.
	 * @param y the vertical pixel to draw at.
	 */
	static public void drawQ(Image drawable, Quad quad, double x, double y) {
		y = window.getHeight() - y;
		double w = quad.quadWidth;
		double h = -quad.quadHeight;
		
    	glEnable(GL_TEXTURE_2D);
		drawable.texture.bind();
		glPushMatrix();
		glTranslated(x, y, 0);
		glBegin(GL_QUADS);
			glTexCoord2d(quad.x, quad.y);
			glVertex2d(0, 0);
			glTexCoord2d(quad.x + quad.width, quad.y);
			glVertex2d(w, 0);
			glTexCoord2d(quad.x + quad.width, quad.y + quad.height);
			glVertex2d(w, h);
			glTexCoord2d(quad.x, quad.y + quad.height);
			glVertex2d(0, h);
		glEnd();
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}
	
	static public void line(double x1, double y1, double x2, double y2) {//x1,y1 are coords of the first point etc. 
		y1 = window.getHeight() - y1;
		y2 = window.getHeight() - y2;
		
		glBegin(GL_LINE_STRIP);
		glVertex2d(x1, y1);
		glVertex2d(x2, y2);
		glEnd();
	}
	
	//x1,y1 are coords of the first point etc. 
	public static void triangle(boolean fill, double x1, double y1, double x2, double y2, double x3, double y3) { 
		y1 = window.getHeight() - y1;
		y2 = window.getHeight() - y2;
		y3 = window.getHeight() - y3;
		
	    if (fill) {
		    glBegin(GL_TRIANGLES);
	    } else {
	    	glBegin(GL_LINE_STRIP);
	    }
        glVertex2d(x1, y1);
        glVertex2d(x2, y2);
        glVertex2d(x3, y3);
	    if (!fill) {
	        glVertex2d(x1, y1);
	    }
	    glEnd();
	}
	
	static public void rectangle(boolean fill, double x, double y, double width, double height) {
		y = window.getHeight() - y;
		height = -height;
		
		glPushMatrix();
	    glTranslated(x, y, 0);
	    if (fill) {
		    glBegin(GL_QUADS);
	    } else {
	    	glBegin(GL_LINE_STRIP);
	    }
        glVertex2d(0, 0);
        glVertex2d(width, 0);
        glVertex2d(width, height);
        glVertex2d(0, height);
	    if (!fill) {
	        glVertex2d(0, 0);
	    }
	    glEnd();
	    glPopMatrix();
	}
	
	/**
	 * Draws an arc. That is, a portion of a circle. A curve.
	 * @param fill whether to fill with colour (false just draws a curved line).
	 * @param x the x coordinate of where the centre of circle would be if an arc of 2pi radians were drawn.
	 * @param y the y coordinate of where the centre of circle would be if an arc of 2pi radians were drawn.
	 * @param r the radius of the circle.
	 * @param startAngle the angle the arc begins at.
	 * @param angle the angle of the arc.
	 * @param segments how many lines segments to draw to approximate the curve. 
	 */
	static public void arc(boolean fill, double x, double y, double r, double startAngle, double angle, double segments) {
		y = window.getHeight() - y;
		startAngle = -startAngle;
		angle = -angle;
		
		glPushMatrix();
		glTranslated(x, y, 0);
		glScaled(r, r, 1);
		if (fill) {
			glBegin(GL_TRIANGLE_FAN);
			glVertex2d(0, 0);
	    } else {
	    	glBegin(GL_LINE_STRIP);
	    }
		for (int i = 0; i <= segments; i++) {
		    double theta = startAngle + (angle * i / segments);
		    glVertex2d(Math.cos(theta), Math.sin(theta));
		}
		glEnd();
		glPopMatrix();
	}
	static public void arc(boolean fill, double x, double y, double r, double startAngle, double angle) {
		arc(fill, x, y, r, startAngle, angle, 20);
	}
	

	static public void circle(boolean fill, double x, double y, double radius, double segments) {
		y = window.getHeight() - y;
		
		glPushMatrix();
		glTranslated(x, y, 0);
		glScaled(radius, radius, 1);
		if (fill) {
			glBegin(GL_TRIANGLE_FAN);
			glVertex2d(0, 0);
	    } else {
	    	glBegin(GL_LINE_STRIP);
	    }
		for (int i = 0; i <= segments; i++) {
		    double angle = Math.PI * 2 * i / segments;
		    glVertex2d(Math.cos(angle), Math.sin(angle));
		}
		glEnd();
		glPopMatrix();
	}
	static public void circle(boolean fill, double x, double y, double r) {
		circle(fill, x, y, r, 20);
	}

	static public void printText(String text, double x, double y, double size) {
		if (currentFont == null) currentFont = newSystemFont("Times New Roman");
		currentFont.print(x, y, text, size);
	}
	static public void print(String text, double x, double y) {
		printText(text, x, y, 1);
	}
	

	static public void printTextCentred(String text, double x, double y, double size, double width) {
		if (currentFont == null) currentFont = newSystemFont("Times New Roman");
		currentFont.printCentred(x, y, width, text, size);
	}
	
	/**
	 * Clears the screen ready for another draw process.
	 */
	public static void clear() {
		/*
		try {
			org.lwjgl.opengl.Display.swapBuffers();
		} catch (org.lwjgl.LWJGLException e) {
			e.printStackTrace();
		}
		*/
		if (viewPortEnabled) setViewport();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glColor3d(1, 1, 1);
	}

	public static Font getFont() {
		if (currentFont == null) currentFont = newSystemFont("Times New Roman");
		return currentFont;
	}
	
	public static void push() {
		glPushMatrix();
	}

	public static void translate(double x, double y) {
		glTranslated(x, y, 0);
	}
	
	public static void pop() {
		glPopMatrix();
	}

}
