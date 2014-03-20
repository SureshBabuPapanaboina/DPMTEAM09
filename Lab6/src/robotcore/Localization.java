package robotcore;

import odometry.Odometer;
import sensors.UltrasonicListener;
import sensors.UltrasonicPoller;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import movement.Driver;

/**
 * Does the localization, assuming all used threads are started.
 * @author yuechuan
 *
 */
public class Localization {
	private static final boolean DEBUG = Configuration.DEBUG;
	private static Driver driver = Driver.getInstance();
	private static Odometer odo = Odometer.getInstance();
	private static LCDWriter lcd = LCDWriter.getInstance();
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	private static Configuration conf = Configuration.getInstance();
	private static double angle1 , angle2 , middle ;
	static boolean secondAngle = false ;	// if this is called a second time then turn back 
	
	public static void main(String[] args) {
		startThread();
//		driver.rotateToRelatively(-100);
		
		localize();
	}
	
	private static void startThread() {
		driver.start();
		odo.start();
		lcd.start();
		usp.start();
		try {Thread.sleep(300);} catch(Exception e) {}
	}
	/**
	 * localization : do the falling edge to have a general idea where the robot is facing
	 * assuming the current location is at (15,15) , then use odoCorrection to 
	 * correct the actual (x y t )
	 */
	public static void localize() {
//		prepareForFallingEdge();	
		performFallingEdge();
//		
		driver.forward(30);
		driver.rotateToRelatively(-90);		//rotate and then correct Y coordinate 
		driver.forward(30); 	//correct the y coordinant 
		
		while (true){ } //pause 
	}
	
	/**
	 * do falling edge and turn face the x axis 
	 */
	private static void performFallingEdge() {
		
		//forward rotation 
		driver.rotateToRelatively(360, true);
		while (usp.getDistance() > 40){} 
		driver.motorStop();
		angle1 = conf.getCurrentLocation().getTheta();
		if (DEBUG) LCDWriter.getInstance().writeToScreen("ang1 " + Math.toDegrees(angle1) , 6);
		
		//start back rotation
		driver.rotateToRelatively(-360, true);
		try {Thread.sleep(400);} catch ( Exception e ){}; 		//make it sleep a little to avoid miss read 
		
		while (usp.getDistance() > 40){} 
		
		angle2 = conf.getCurrentLocation().getTheta();
		if (DEBUG) LCDWriter.getInstance().writeToScreen("ang2 " + Math.toDegrees(angle2) , 5);

		//find the angle from origin 
		double angleFromOrigin = ((angle1 + angle2)/2)- angle2 ;
		LCDWriter.getInstance().writeToScreen("AFO " +Math.toDegrees(angleFromOrigin) , 0);
		driver.rotateToRelatively(Math.toDegrees(angleFromOrigin));
		
		driver.rotateToRelatively(45);
		
		
		
	}
	/**
	 * make sure the robot is facing outward 
	 */
	private static void prepareForFallingEdge() {
		
	}

	

}
