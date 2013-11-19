package Entities;

public class Position {
	
	private double X, Y, Z;
	
	public Position(double posX, double posY, double posZ){
		
		this.setX(posX);
		this.setY(posY);
		this.setZ(posZ);
		
	}

	public double getX() {
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}

	public double getZ() {
		return Z;
	}

	public void setZ(double z) {
		Z = z;
	}
}
