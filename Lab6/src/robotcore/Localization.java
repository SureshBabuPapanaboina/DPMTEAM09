package robotcore;

import lejos.robotics.pathfinding.Path;

/**
 * Does the localization, assuming all used threads are started.
 * @author yuechuan
 *
 */
public class Localization {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Path p = new Path();
	}
	/**
	 * localization : first 
	 */
	public static void localize() {
		prepareForFallingEdge();	
		performFallingEdge();
	}
	
	/**
	 * do falling edge and turn face the x axis 
	 */
	private static void performFallingEdge() {
		
	}
	/**
	 * make sure the robot is facing outward 
	 */
	private static void prepareForFallingEdge() {
		// TODO Auto-generated method stub
		
	}
	
	

}
