package cls;

public class Point {
	
	private double _x, _y, _z;
	
	public double getX() {
		return _x;
	}
	public double getY() {
		return _y;
	}
	public double getZ() {
		return _z;
	}
	
	public void setX(double x){
		this._x = x;
	}
	
	public void setY(double y){
		this._y = y;
	}
	
	public void setZ(double Z){
		this._z = Z;
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
			return (_x == p.getX()) && (_y == p.getY()) && (_z == p.getZ());
		}
	}
	
}