package cls;

import java.io.File;

import lib.jog.graphics;
import lib.jog.graphics.Image;

public class Lives {
	private final int NUMBER_OF_LIVES = 3;
	private int current_lives = NUMBER_OF_LIVES;
	
	// For drawing
	private int position_x;
	private int position_y;
	private Image life_img;
	private Image dead_life_img;
	
	private final int X_MARGIN = 20;
	
	public Lives(int position_x, int position_y){
		this.position_x = position_x;
		this.position_y = position_y;
		this.life_img = graphics.newImage("gfx" + File.separator + "life.png");
		this.dead_life_img = graphics.newImage("gfx" + File.separator + "dead_plane.png"); // Essentially an empty heart container
	}
	
	/**
	 * Purely for unit testing
	 */
	@Deprecated
	public Lives(){	
	}
	
	public void decrement() {
		if (this.current_lives > 0) {
			this.current_lives -= 1;
		}
	}
	
	public int getLives() {
		return this.current_lives;
	}
	
	public void draw() {
		for (int i=0; i < NUMBER_OF_LIVES; i++) {
			Image img = i < current_lives ? life_img : dead_life_img;
			graphics.draw(img, position_x + i*X_MARGIN, position_y);
		}
	}
}