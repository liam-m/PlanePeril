package lib;

import lib.jog.graphics;
import lib.jog.graphics.Image;
import lib.jog.graphics.Quad;

public class SpriteAnimation {

	private Image image; // the animation sequence
	private Quad[] quads; // the rectangle to be drawn from the animation bitmap
	private int frame_count; // number of frames in animation
	private int current_frame; 
	private double frame_period; // milliseconds between each frame (1000/fps)

	private double sprite_width; // used to calculate the cut out rectangle
	private double sprite_height; 
	
	private boolean has_finished; // set to true when all frames have been drawn

	private int x_coordinate; 
	private int y_coordinate; 
	private double game_time; // how long a frame has been shown for
	private double image_width, image_height;
	private boolean is_looping;
	
	/**
	 * <h1>Sprite Animation</h1>
	 * <p>Creates an animation class</p>
	 * @author Stephen Webb
	 * @param image image from which the quads are taken.
	 * @param x_coordinate the x position to draw the animation.
	 * @param y_coordinate the y position to draw the animation.
	 * @param fps how many animation frames to draw per second.
	 * @param frame_count how many frames the animation comprises.
	 * @param frames_wide how many frames wide the image is
	 * @param frames_high how many frames high the image is
	 * @param looping whether to loop the animation
	 */
	public SpriteAnimation(Image image, int x_coordinate, int y_coordinate, int fps, int frame_count, int frames_wide, int frames_high, boolean looping){
		this.image = image;
		image_width = image.getWidth();
		//System.out.println("-----");
		image_height = image.getHeight();
		//System.out.println("-----");
		this.x_coordinate = x_coordinate;
		this.y_coordinate = y_coordinate;
		this.frame_count = frame_count;
		current_frame = 0;
		sprite_width = image_width / frames_wide;
		sprite_height = image_height / frames_high;
		//System.out.println("Image Dimensions: " + image_width + ", " + image_height);
		//System.out.println("Frame Dimensions: " + sprite_width + ", " + sprite_height);
		frame_period = 1.0/fps;
		game_time = 0;
		quads = new Quad[frame_count];
		for (int n = 0; n < frame_count; n ++) {
			int i = n % frames_wide;
			int j = n / frames_wide;
			quads[n] = graphics.newQuad(i * sprite_width, j * sprite_height, sprite_width, sprite_height, image_width, image_height);
		}
		is_looping = looping;
		has_finished = false;
	}
	
	/**
	 * <h1>Sprite Animation</h1>
	 * <p>Creates an animation class</p>
	 * @author Stephen Webb
	 * @param image_filepath the filepath at which the image is.  
	 * @param x_coordinate the x position to draw the animation.
	 * @param y_coordinate the y position to draw the animation.
	 * @param fps how many animation frames to draw per second.
	 * @param frame_count how many frames the animation comprises.
	 * @param frames_wide how many frames wide the image is
	 * @param frames_high how many frames high the image is
	 * @param looping whether to loop the animation
	 */
	public SpriteAnimation(String image_filepath, int x_coordinate, int y_coordinate, int fps, int frame_count, int frames_wide, int frames_high, boolean looping){
		new SpriteAnimation(graphics.newImage(image_filepath), x_coordinate, y_coordinate, fps, frame_count, frames_wide, frames_high, looping);
	
	}
	/**
	 * updates the timer and changes the frame if necessary
	 * @param delta_time time in seconds since last update
	 */
	public void update(double delta_time) {
		if (has_finished) return;
		
		game_time += delta_time;
		if (game_time > frame_period) { // frame period exceeded
			game_time = 0; // reset timer
			current_frame++; // increment frame
			if (current_frame >= frame_count) {
				if (!is_looping) {
					has_finished = true;
				} else {
					current_frame = 0;
				}
			}
		}
	}
	
	/**
	 * draws the animation
	 */
	public void draw() {
		if (has_finished) return;
		graphics.drawQ(image, quads[current_frame], x_coordinate, y_coordinate);
	}
	
	/**
	 * @return whether the animation has finished
	 */
	public boolean hasFinished() {
		return has_finished;
	}

}