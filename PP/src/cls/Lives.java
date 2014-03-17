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
	public Image life_img = graphics.newImage("gfx" + File.separator + "plane.png");
	public Image dead_life_img = graphics.newImage("gfx" + File.separator + "dead_plane.png");
	private int x_margin;
	
	public Lives(int position_x, int position_y, Image img, Image dead_img, int img_height, int img_width, int x_margin){
		this.position_x = position_x;
		this.position_y = position_y;
		this.life_img = img;
		this.dead_life_img = dead_img;
		this.img_height = img_height;
		this.img_width = img_width;
		this.x_margin = x_margin;
	}
	
	public void decrementLives() throws Exception{
		if (this.current_lives == 0){
			throw new Exception("Value of current_lives cannot be less than 0.");
		}
		else {
			this.current_lives -= 1;
		}
	}
	
	public int getLives(){
		return this.current_lives;
	}
	
	public void draw(int position_x, int position_y, int x_margin, int current_lives, int INITAL_VALUE){
		for(int i=0; i<current_lives; i++){
			graphics.draw(life_img, position_x, position_y);
			position_x = position_x + x_margin;
		}
		for (int i=0; i<(INITAL_VALUE - current_lives);i++){
			System.out.println("AM DRAWING LIVES");
			graphics.draw(dead_life_img, position_x, position_y);
			position_x = position_x + x_margin;
		}
	}
}