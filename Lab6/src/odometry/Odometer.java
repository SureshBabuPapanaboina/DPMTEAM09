package odometry;

import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import lejos.nxt.NXTRegulatedMotor;
/**
 * odometer will obtain a reference to the CurrentCoordinate defined in 
 * Configuration and update this shared variable. using the 
 * 
 * @author yuechuan
 *
 */
public class Odometer extends Thread {
	private static Odometer odo;
	private boolean DEBUG = Configuration.DEBUG;
	
	// odometer update period, in ms
	private static final long UPDATE_PERIOD = 50; //ms
	private Object lock; 	// lock object for mutual exclusion
	private Coordinate cCoord ;  //current location (x,y,heading)
	private NXTRegulatedMotor lMotor; // left motor 
	private NXTRegulatedMotor rMotor;
	private int lTCount, rTCount;	// the previous tacho meter counts of left and right motor

	/**
	*returns an instance of odometer and run it 
	**/
	public static Odometer getInstance(){
		if (odo == null){
			odo = new Odometer(Configuration.getInstance());
		}
		return odo;
	}

	private Odometer(Configuration config){
		lock = new Object();
		lMotor = Configuration.LEFT_MOTOR;
		rMotor = Configuration.RIGHT_MOTOR;
		cCoord = config.getCurrentLocation();
	}
	
	/**
	 * begin the odometer 
	 */
	public void run(){
		long updateStart, updateEnd;
		double leftArcDistance;
		double rightArcDistance;
		double deltaTheta;
		double displacement;
		double currentTheta;
		double deltaX;
		double deltaY;
		
		while (true) {
			if (DEBUG) LCDWriter.getInstance().writeToScreen("OdoT " + getTheta(), 5);
			updateStart = System.currentTimeMillis();
				updateStart = System.currentTimeMillis();
				//get the starting tacho count
				int newLeftCount = lMotor.getTachoCount();
				int newRightCount = rMotor.getTachoCount();
				
				//calc difference in TCount from previous position 
				int deltaLeftCount = newLeftCount - lTCount;
				int deltaRightCount = newRightCount - rTCount;
				
				lTCount = newLeftCount;
				rTCount = newRightCount;
				
				//calculate the distance traveled for each wheel 
				leftArcDistance = getArcLen(deltaLeftCount,Configuration.LEFT_RADIUS);
				rightArcDistance = getArcLen(deltaRightCount,Configuration.RIGHT_RADIUS);
				

				//calculate the degree 
				deltaTheta = (leftArcDistance - rightArcDistance) / Configuration.WIDTH;
				//calculate the displacement 
				displacement = (leftArcDistance + rightArcDistance) / 2.0;
				
				currentTheta = getTheta();
				//computes the dist made on the x and y axes since last check 
				deltaX = displacement * Math.sin(currentTheta + deltaTheta / 2);
				deltaY = displacement * Math.cos(currentTheta + deltaTheta / 2);
		
				synchronized (lock) {			
					//update location (x.y)
					setX(getX() + deltaX);
					setY(getY() + deltaY);
					setTheta(currentTheta + deltaTheta);
				}
				
				if (DEBUG) LCDWriter.getInstance().writeToScreen(cCoord.toString(), 2);
				
			// this ensures that the odometer only runs once every period
			updateEnd = System.currentTimeMillis();
			if (updateEnd - updateStart < UPDATE_PERIOD) {
				try {
					Thread.sleep(UPDATE_PERIOD - (updateEnd - updateStart));
				} catch (InterruptedException e) {	}
			}
		}
	}


	/*
	 * =============================================================================
	 * ACCESSORS  and SETTERS
	 * =============================================================================
	 */

	/**
	 * Calculate the arc length of movement (in degree) taking into the account of the wheel radius 
	 * @param deltaTachometerCount tacho count in degree
	 * @return distance in cm 
	 */
	private double getArcLen(int deltaTachometerCount,double radius) {
		return Math.toRadians(deltaTachometerCount) * radius;
	}

	public double getX() {
		double result;

		synchronized (lock) {
			result = cCoord.getX();
		}

		return result;
	}

	public double getY() {
		double result;
		synchronized (lock) {
			result = cCoord.getY();
		}

		return result;
	}
	
	/**
	 * 
	 * @return theta in Rad
	 */
	public double getTheta() {
		double result;

		synchronized (lock) {
			result = cCoord.getTheta();
		}
		return result;
	}
	
	/**
	 * set x of odometer 
	 * @param x
	 * @return
	 */
	Coordinate setX(double x) {
		synchronized (lock) {
			cCoord.setX(x);
		}
		return cCoord;
	}
/**
 * set y of odometer 
 * @param y
 * @return
 */
	Coordinate setY(double y) {
		synchronized (lock) {
			cCoord.setY(y);
		}
		return cCoord;
	}
	
	/**
	 * set theta in <b> rad <b>
	 * @param theta
	 * @return
	 */
	Coordinate setTheta(double theta) {
		if (Math.abs(theta) >= 2*Math.PI){
			if (theta < 0 ) 
				theta = theta + 2*Math.PI;
			else 
				theta = theta - 2*Math.PI;
		}
		
		synchronized (lock) {
			cCoord.setTheta(theta);
		}
		return cCoord;
	}
	
	/*
	 * =============================================================================
	 * END OF ACCESSORS and SETTERS
	 * =============================================================================
	 */
	
	/**
	 * returns the average tachoCount of both motors 
	 * @return the average tacho count 
	 */
	public int getTachoCount(){
		return ((lMotor.getTachoCount() + rMotor.getTachoCount() )/2) ;
	}
	
}
