package screen;

public abstract class Scene implements lib.jog.input.EventHandler {

	protected bearTrafficControl.Main _main;
	
	protected Scene(bearTrafficControl.Main main) {
		_main = main;
	}
	
	abstract public void start();
	
	abstract public void update(double dt);
	
	abstract public void draw();
	
	abstract public void close();
	
}
