package robotcore;

public class Point {
	private double x , y ;
	
	Point(double x , double y){
		this.x = x ;
		this.y = y ;
	}

	public double getX() {
		return x;
	}

	public Point setX(double x) {
		this.x = x;
		return this;
	}

	public double getY() {
		return y;
	}

	public Point setY(double position) {
		this.y = position;
		return this;
	}
	
}
