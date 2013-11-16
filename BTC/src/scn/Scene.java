package scn;

public abstract class Scene implements lib.jog.input.EventHandler {

	protected btc.Main _main;
	
	protected Scene(btc.Main main) {
		_main = main;
	}
	
	abstract public void start();
	
	abstract public void update(double dt);
	
	abstract public void draw();
	
	abstract public void close();
	
}