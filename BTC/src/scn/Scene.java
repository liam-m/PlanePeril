package scn;

import lib.jog.audio;

public abstract class Scene implements lib.jog.input.EventHandler {

	protected btc.Main main;
	
	protected Scene(btc.Main main) {
		this.main = main;
	}
	
	abstract public void start();
	
	abstract public void update(double dt);
	
	abstract public void draw();
	
	abstract public void close();
	
	abstract public void playSound(audio.Sound sound);
	
}
