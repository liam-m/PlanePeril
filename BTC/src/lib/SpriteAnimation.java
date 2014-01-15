package lib;

import lib.jog.graphics;
import lib.jog.graphics.Image;
import lib.jog.graphics.Quad;

public class SpriteAnimation {

	private Image image;		// the animation sequence
	private Quad quad;	// the rectangle to be drawn from the animation bitmap
	private int frameNr;		// number of frames in animation
	private int currentFrame;	// the current frame
	private double frameTicker;	// the time of the last frame update
	private double framePeriod;	// milliseconds between each frame (1000/fps)

	private double spriteWidth;	// the width of the sprite to calculate the cut out rectangle
	private double spriteHeight;	// the height of the sprite

	private int x;				// the X coordinate of the object (top left of the image)
	private int y;				// the Y coordinate of the object (top left of the image)
	private double gameTime;
	
	public SpriteAnimation(Image image, int x, int y, int fps, int frameCount){
		this.image = image;
		this.x = x;
		this.y = y;
		
		currentFrame = 0;
		frameNr = frameCount;
		spriteWidth = image.width() / frameCount;
		spriteHeight = image.height();
		framePeriod = 8/fps;
		gameTime = 0;
		quad = new Quad(currentFrame * spriteWidth, currentFrame * spriteHeight, spriteWidth, spriteHeight, image.width(), image.height());
	}
	
	public void update(double dt) {
		gameTime += dt;
		if (gameTime > framePeriod) {
			gameTime = 0;
			// increment the frame
			currentFrame++;
			quad = new Quad(currentFrame* spriteWidth, currentFrame * spriteHeight, spriteWidth, spriteHeight, image.width(), image.height());
			if (currentFrame >= frameNr) {
				currentFrame = 0;
			}
		}
	}
	
	public void draw() {
		graphics.drawq(image, quad, x, y);
	}

}
