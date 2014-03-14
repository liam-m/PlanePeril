package cls;

import java.io.File;

import lib.jog.graphics;
import lib.jog.graphics.Image;

public class Lives {
	private final int INTIAL_VALUE = 3;
	private final int FINAL_VALUE = 0;
	private int current_lives = INTIAL_VALUE;
	
	//for drawing
	private int position_x;
	private int position_y;
	private int img_height;
	private int img_width;
	public Image img = graphics.newImage("gfx" + File.separator + "plane.png");
	private int x_margin;
	
	public Lives(int position_x, int position_y, Image img, int img_height, int img_width, int x_margin){
		this.position_x = position_x;
		this.position_y = position_y;
		this.img = img;
		this.img_height = img_height;
		this.img_width = img_width;
		this.x_margin = x_margin;
	}
	
	public void decrementLives(){
		this.current_lives -= 1;
	}
	
	public int getLives(){
		return this.current_lives;
	}
	
	public void draw(int position_x, int position_y, int x_margin, int current_lives){
		for(int i=0; i<current_lives; i++){
			graphics.draw(img, position_x, position_y);
			position_x = position_x + x_margin;
		}
	}
}