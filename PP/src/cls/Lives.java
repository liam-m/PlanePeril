package cls;

import java.io.File;

import lib.jog.graphics;
import lib.jog.graphics.Image;

public class Lives {
	private final int INITIAL_VALUE = 3;
	private int current_lives = INITIAL_VALUE;
	
	//for drawing
	private int position_x;
	private int position_y;
	private double img_width;
	private Image life_img;
	private Image dead_life_img;
	
	private final int X_MARGIN = 20;
	
	public Lives(int position_x, int position_y){
		this.position_x = position_x;
		this.position_y = position_y;
		this.life_img = graphics.newImage("gfx" + File.separator + "life.png");
		this.dead_life_img = graphics.newImage("gfx" + File.separator + "dead_plane.png"); //Essentially an empty heart container
		this.img_width = life_img.getWidth();
	}
	
	public void decrementLives() {
		if (this.current_lives > 0) {
			this.current_lives -= 1;
		}
	}
	
	public int getLives(){
		return this.current_lives;
	}
	
	public void draw(){
		int x = position_x;
		for(int i=0; i<current_lives; i++){
			graphics.draw(life_img, x, position_y);
			x += img_width + X_MARGIN;
		}
		for (int i=0; i<(INITIAL_VALUE - current_lives);i++){
			graphics.draw(dead_life_img, x, position_y);
			x += img_width + X_MARGIN;
		}
	}
}