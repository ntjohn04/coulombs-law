public class UVector {

	double x;
	double y;
	double z;
	
	double magn;
	
	public UVector(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.magn = Math.sqrt((x*x)+(y*y)+(z*z));
	}
	
	public void unitize() {
		x = x/magn;
		y = y/magn;
		z = z/magn;
	}
	
	public double magnitude() {
		return Math.sqrt((x*x)+(y*y)+(z*z));
	}
	
}
