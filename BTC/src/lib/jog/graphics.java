package lib.jog;

import java.awt.FontFormatException;
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
 * <h1>jog.graphics</h1>
 * <p></p>
 * @author IMP1
 */
public abstract class graphics {
	
	public static abstract class Font {
		
		protected abstract void print(double x, double y, String text, double size);
		
	}
	
	private static class BitmapFont extends Font {

		private String _glyphs;
		private Image _img;
		
		private BitmapFont(String filepath, String glyphs) {
			_img = newImage(filepath);
			_glyphs = glyphs;
		}

		protected void print(double x, double y, String text, double size) {
			y = window.height() - y;
			double w = _img.height();
			double h = -_img.height();
			double qw = w / _img.width();
			double qh = 1;
			
	    	glEnable(GL_TEXTURE_2D);
	    	_img._texture.bind();
			glPushMatrix();
			glTranslated(x, y, 0);
			glScaled(size, size, 1);
			glBegin(GL_QUADS);
			for (int i = 0; i < text.length(); i ++) {
				double qx = _glyphs.indexOf(text.charAt(i)) * w / _img.width();
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
	
	private static class FileFont extends Font {
		
		private TrueTypeFont _font;
		
		private FileFont(String filename, int size) {
			try {
				InputStream inputStream = ResourceLoader.getResourceAsStream(filename);
				java.awt.Font awtFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, inputStream);
				awtFont = awtFont.deriveFont(size);
				_font =  new TrueTypeFont(awtFont, false);
			} catch (FontFormatException | IOException e) {
				e.printStackTrace();
			}
		}

		protected void print(double x, double y, String text, double size) {
			_font.drawString((int)x, (int)y, text);
		}
	}
	
	private static class SystemFont extends Font {

		private TrueTypeFont _font;
		
		private SystemFont(String name, int size) {
			java.awt.Font awtFont = new java.awt.Font("Times New Roman", java.awt.Font.PLAIN, size);
			_font = new TrueTypeFont(awtFont, false);
		}
		
		protected void print(double x, double y, String text, double size) {
			y = y - window.height();
			
			glPushMatrix();
			glScaled(1, -1, 0);
			_font.drawString((int)x, (int)y, text);
			glPopMatrix();
		}
		
	}
	
	public static class Image {
		
		private Texture _texture;
		
		private Image(String filepath) {
			try {
				String format = filepath.split("\\.")[1].toUpperCase();
				InputStream in = ResourceLoader.getResourceAsStream(filepath);
				_texture = TextureLoader.getTexture(format, in);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		public double width() { 
			return _texture.getTextureWidth();
		}
		
		public double height() { 
			return _texture.getTextureHeight(); 
		}
		
		public Color pixelAt(int x, int y) {
			int r = _texture.getTextureData()[y * (int)width() + x ] * -255;
			int g = _texture.getTextureData()[y * (int)width() + x + 1] * -255;
			int b = _texture.getTextureData()[y * (int)width() + x + 2] * -255;
			int a = _texture.getTextureData()[y * (int)width() + x + 3] * -255;
			return new Color(r, g, b, a);
		}
		
	}
	
	public static class Quad {
		
		public final double x, y, w, h, imgW, imgH;
		
		private Quad(double x, double y, double w, double h, double imgW, double imgH) {
			this.x = x / imgW;
			this.y = y / imgH;
			this.w = w / imgW;
			this.h = h / imgH;
			this.imgW = w;
			this.imgH = h;
		}		
		
	}
	
	private static Font _font;
	private static Color _colour;
	private static boolean _viewPortEnabled;
	
	static public void initialise() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, window.width(), 0, window.height(), -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		_viewPortEnabled = false;
	}

	static public void setColour(Color colour) {
		_colour = colour;
		glColor4f(_colour.r, _colour.g, _colour.b, _colour.a);
	}

	static public void setColour(double r, double g, double b, double a) {
		_colour = new Color((int)r, (int)g, (int)b, (int)a);
		double red = (Math.max(0, Math.min(255, r)) / 255);
		double green = Math.max(0, Math.min(255, g)) / 255;
		double blue = Math.max(0, Math.min(255, b)) / 255;
		double alpha = Math.max(0, Math.min(255, a)) / 255;
		glColor4d(red, green, blue, alpha);
	}
	static public void setColour(int r, int g, int b) { setColour(r, g, b, 255); }
	
	static public Color getColour() {
		return _colour;
	}
	
	static public void setFont(Font font) {
		_font = font;
	}
	
	static public void setViewport(int x, int y, int width, int height) {
		glPushMatrix();
		glTranslated(x, -y, 0);
		y = window.height() - y;
		glEnable(GL_SCISSOR_TEST);		
		glScissor(x, y - height, width, height);
		_viewPortEnabled = true;
	}
	
	static public void setViewport() {
		glDisable(GL_SCISSOR_TEST);
		glPopMatrix();
		_viewPortEnabled = false;
	}
	
	static public FileFont newFont(String filepath, int size) {
		return new FileFont(filepath, size);
	}
	static public FileFont newFont(String filepath) { return newFont(filepath, 24); }
	
	static public BitmapFont newBitmapFont(String filepath, String glyphs) {
		return new BitmapFont(filepath, glyphs);
	}
	
	static public SystemFont newSystemFont(String fontName, int size) {
		return new SystemFont(fontName, size);
	}
	static public SystemFont newSystemFont(String fontName) {
		return newSystemFont(fontName, 24);
	}
		
	static public Image newImage(String filepath) {
		return new Image(filepath);
	}
	
	static public Quad newQuad(double x, double y, double quadWidth, double quadHeight, double imageWidth, double imageHeight) {
		return new Quad(x, y, quadWidth, quadHeight, imageWidth, imageHeight);
	}
	
	static public void draw(Image drawable, double x, double y) {
		y = window.height() - y;
		double w = drawable.width();
		double h = -drawable.height();
		
    	glEnable(GL_TEXTURE_2D);
    	drawable._texture.bind();
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
	
	static public void drawq(Image drawable, Quad quad, double x, double y) {
		y = window.height() - y;
		double w = quad.imgW;
		double h = -quad.imgH;
		
    	glEnable(GL_TEXTURE_2D);
		drawable._texture.bind();
		glPushMatrix();
		glTranslated(x, y, 0);
		glBegin(GL_QUADS);
			glTexCoord2d(quad.x, quad.y);
			glVertex2d(0, 0);
			glTexCoord2d(quad.x + quad.w, quad.y);
			glVertex2d(w, 0);
			glTexCoord2d(quad.x + quad.w, quad.y + quad.h);
			glVertex2d(w, h);
			glTexCoord2d(quad.x, quad.y + quad.h);
			glVertex2d(0, h);
		glEnd();
		glPopMatrix();
		glDisable(GL_TEXTURE_2D);
	}
	
	static public void line(double x1, double y1, double x2, double y2) {
		y1 = window.height() - y1;
		y2 = window.height() - y2;
		
		glBegin(GL_LINE_STRIP);
		glVertex2d(x1, y1);
		glVertex2d(x2, y2);
		glEnd();
	}
	
	static public void rectangle(boolean fill, double x, double y, double w, double h) {
		y = window.height() - y;
		h = -h;
		
		glPushMatrix();
	    glTranslated(x, y, 0);
	    if (fill) {
		    glBegin(GL_QUADS);
	    } else {
	    	glBegin(GL_LINE_STRIP);
	    }
        glVertex2d(0, 0);
        glVertex2d(w, 0);
        glVertex2d(w, h);
        glVertex2d(0, h);
	    if (!fill) {
	        glVertex2d(0, 0);
	    }
	    glEnd();
	    glPopMatrix();
	}
	
	static public void arc(boolean fill, double x, double y, double r, double startAngle, double angle, double segments) {
		y = window.height() - y;
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
	
	static public void circle(boolean fill, double x, double y, double r, double segments) {
		y = window.height() - y;
		
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
		    double angle = Math.PI * 2 * i / segments;
		    glVertex2d(Math.cos(angle), Math.sin(angle));
		}
		glEnd();
		glPopMatrix();
	}
	static public void circle(boolean fill, double x, double y, double r) {
		circle(fill, x, y, r, 20);
	}
	
	static public void print(String text, double x, double y, double size) {
		if (_font == null) _font = newSystemFont("Times New Roman");
		_font.print(x, y, text, size);
	}
	static public void print(String text, double x, double y){
		print(text, x, y, 1);
	}
	
	/**
	 * Clears the screen ready for another iteration of a draw process.
	 */
	public static void clear() {
		/*
		try {
			org.lwjgl.opengl.Display.swapBuffers();
		} catch (org.lwjgl.LWJGLException e) {
			e.printStackTrace();
		}
		*/
		if (_viewPortEnabled) setViewport();
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glColor3d(1, 1, 1);
	}

}