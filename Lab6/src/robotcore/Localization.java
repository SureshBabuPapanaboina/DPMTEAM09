package robotcore;

import odometry.Odometer;
import sensors.LineReader;
import sensors.LineReaderListener;
import sensors.UltrasonicListener;
import sensors.UltrasonicPoller;
import lejos.nxt.NXTMotor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import movement.Driver;

/**
 * Does the localization, assuming all used threads are started.
 * @author yuechuan
 *
 */
public class Localization implements LineReaderListener {
	
	//used threads and initialized stuff 
	private static Driver driver = Driver.getInstance();
	private static Odometer odo = Odometer.getInstance();
	private static LCDWriter lcd = LCDWriter.getInstance();
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	private static Configuration conf = Configuration.getInstance();
	//line readers to run for aligning robot 
	private static LineReader llr = LineReader.getLeftSensor();
	private static LineReader rlr = LineReader.getRightSensor();
	
	//used to subscribe to lineReaders 
	private static Localization localizer = new Localization();
	
	private static final boolean DEBUG = Configuration.DEBUG;
	private static double angle1 , angle2 ;
	static boolean secondAngle = false ;	// if this is called a second time then turn back 

	

	
	public static void main(String[] args) {
		startThread();
		
		localize();
		
		LineReader.subscribeToAll(localizer);
		
		driver.forward(30);
		//right now the robot is stopped with both line readers on the line 
		int distFromCenterOfGrid = 5;
		driver.forward(distFromCenterOfGrid);
		LineReader.pauseAll();
		driver.rotateToRelatively(-90);
		LineReader.unpauseAll();
		driver.forward(30);
		driver.forward(distFromCenterOfGrid);
		cleanUp();
		
	}
	/**
	 * used exclusively in passedLine method 
	 */
	private static int lineNumberPassed = 0 ;
	@Override
	/**
	 * stop corresponding motor once line is seen
	 */
	public void passedLine(boolean isLeft) {
		if (isLeft) conf.LEFT_MOTOR.stop();
		else Configuration.RIGHT_MOTOR.stop();
		driver.motorStop();
		lineNumberPassed++;
		
		//set the odometer 
		if (lineNumberPassed == 1){ 	//first line passed 
			conf.getCurrentLocation().setTheta(0).setY(conf.distFromLineReaderToCenterOfRot);
		}
		else { //second line passed 
			conf.getCurrentLocation().setX(conf.distFromLineReaderToCenterOfRot);
		}
	}

	/**
	 * clean up method
	 */
	private static void cleanUp() {
		//unregister the this class from lineReader 
		LineReader.unsubscribeToAll(localizer);
	}

	/**
	 * thread and things that need to be prepared 
	 */
	private static void startThread() {
		driver.start();
		odo.start();
		lcd.start();
		usp.start();
		llr.start();
		rlr.start();
		
		try {Thread.sleep(300);} catch(Exception e) {}
	}
	/**
	 * localization : do the falling edge to have a general idea where the robot is facing
	 * assuming the current location is at (15,15) , then use odoCorrection to 
	 * correct the actual (x y t )
	 */
	public static void localize() {
		prepareForFallingEdge();	
		performFallingEdge();
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
