package objectdetection;

/**
 * Class for representing the trajectory of an object
 * @author Peter Henderson
 *
 */
public class Trajectory {
	
	public double theta;
	public double distance;
	public boolean suspected;
	
	public Trajectory(double theta, double distance){
		this.theta = theta;
		this.distance = distance;
	}

	public Trajectory(double theta, double distance, boolean suspected){
		this.theta = theta;
		this.suspected = suspected;
		this.distance = distance;
	}
}
