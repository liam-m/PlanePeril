package cls;

public class HoldingWaypoint extends Waypoint {

	private Waypoint nextWaypoint;

	public HoldingWaypoint(double x, double y) {
		super(x, y);

	}

	public void setNextWaypoint(Waypoint nextWaypoint) {
		this.nextWaypoint = nextWaypoint;
	}

	public Waypoint getNextWaypoint() {
		return this.nextWaypoint;
	}
}
