package Entities;

public class Waypoint {
	
	private int id;
	private double[] position;
	private boolean isAvailable;
	
	public Waypoint(int id, double posX, double posY, double posZ){
		
		this.setId(id);
		this.setPosition(new double[] {posX, posY, posZ});
		this.setAvailable(true);
		
	}

	public int getId() {
		return id;
	}

	public double[] getPosition() {
		return position;
	}

	public boolean isAvailable() {
		return isAvailable;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setPosition(double[] position) {
		this.position = position;
	}

	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

}
