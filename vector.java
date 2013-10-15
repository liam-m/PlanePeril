class Vector {

	double x;
	double y;
	double z;
	
	public Vector(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector unit() {
		return this.divide(this.size());
	}
	
	public double size() {
		return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2));
	}
	
	public String toString() {
		return "(%f, %f, %f)".format(this.x, this.y, this.z);
	}
	
	public Vector divide(double n) {
		return new Vector(this.x / n, this.y / n, this.z / n);
	}

}