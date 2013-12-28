package classes;

public class Vector {
	
	private double x, y, z;
	
	public double x() {
		return x;
	}
	public double y() {
		return y;
	}
	public double z() {
		return z;
	}
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != Vector.class) { 
			return false;
		} else {
			Vector v = (Vector) o;
			return (x == v.x()) && (y == v.y()) && (z == v.z());
		}
	}
	
	public double magnitude() {
		return Math.sqrt((x*x) + (y*y) + (z*z));
	}
	
	public double magnitudeSquared() {
		return (x*x) + (y*y) + (z*z);
	}
	
	public Vector normalise() {
		return this.scaleBy(1/magnitude());
	}
	
	public Vector scaleBy(double n) {
		return new Vector(x * n, y * n, z * n);
	}
	
	public Vector add(Vector v) {
		return new Vector(x + v.x(), y + v.y(), z + v.z());
	}
	
	public Vector sub(Vector v) {
		return new Vector(x - v.x(), y - v.y(), z - v.z());
	}
	
	public double angleBetween(Vector v) {
		double a = Math.acos( (x*v.x + y*v.y + z*v.z) / (magnitude() * v.magnitude()));
		if (v.y < y) a *= -1;
		return a;
	}

}