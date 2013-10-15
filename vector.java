public class Vector {

	double x;
	double y;
	double z;
	
	public Vector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector copy() {
		return new Vector(this.x, this.y, this.z);
	}
	
	public Vector unit() {
		return this.divide(this.size());
	}
	
	public double magnitude() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
	}
	
	public String toString() {
		return "(%f, %f, %f)".format(this.x, this.y, this.z);
	}
	
	public Vector divide(double n) {
		return new Vector(this.x / n, this.y / n, this.z / n);
	}
	
	public static double dotProduct(Vector v1, Vector v2) {
		return (v1.x * v2.x) + (v1.y * v2.y) + (v1.z * v2.z);
	}
	
	public static double angleBetween(Vector v1, Vector v2) {
		return Math.acos( Vector.dotProduct(v1, v2) / (v1.magnitude() * v2.magnitude()) );
	}

}