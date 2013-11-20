package cls;

public class Vector {
	
	private double _x, _y, _z;
	
	public double x() {
		return _x;
	}
	public double y() {
		return _y;
	}
	public double z() {
		return _z;
	}
	
	public Vector(double x, double y, double z) {
		_x = x;
		_y = y;
		_z = z;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != Vector.class) { 
			return false;
		} else {
			Vector v = (Vector) o;
			return (_x == v.x()) && (_y == v.y()) && (_z == v.z());
		}
	}
	
}