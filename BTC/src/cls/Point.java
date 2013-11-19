package cls;

public class Point {
	
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
	
	public Point(double x, double y, double z) {
		_x = x;
		_y = y;
		_z = z;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o.getClass() != Point.class) { 
			return false;
		} else {
			Point p = (Point) o;
			return (_x == p.x()) && (_y == p.y()) && (_z == p.z());
		}
	}
	
}