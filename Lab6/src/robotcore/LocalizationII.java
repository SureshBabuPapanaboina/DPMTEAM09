package robotcore;

import odometry.Odometer;
import sensors.LineReader;
import sensors.LineReaderListener;
import sensors.UltrasonicListener;
import sensors.UltrasonicPoller;
import lejos.nxt.NXTMotor;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import movement.Driver;

/**
 * The second revision of Localization 
 * @author yuechuan
 *
 */
public class LocalizationII implements LineReaderListener{
	
	//used threads and initialized stuff 
	private static Driver driver = Driver.getInstance();
	private static Odometer odo = Odometer.getInstance();
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	private static Configuration conf = Configuration.getInstance();
	private static final boolean DEBUG = Configuration.DEBUG;
	
	private static double angle1 , angle2 ;
	static boolean secondAngle = false ;	// if this is called a second time then turn back 
	
	//starting position
	private final static int LOW_LEFT = 0; 
	private final static int LOW_RIGHT = 1; 
	private final static int UP_LEFT= 2; 
	private final static int UP_RIGHT = 3; 
	
	
	/**
	 * perform localization and move to start location depending on the given corner 
	 */
	public static void localizeAndMoveToStartLoc(int startingCorner) {
		
		
		
		
		
		
		
		
		
		
		int rotSpeed = conf.getRotationSpeed();
		conf.setRotationSpeed(120);
		localize();		//this will turn the robot head to the field see [figure 1] in notebook 
		
		
		switch(startingCorner){
		case LOW_LEFT:
			driver.rotateToRelatively(45);
			odo.setTheta(Math.toRadians(90));
			conf.setRotationSpeed(rotSpeed);
			break;
		case LOW_RIGHT:
			break;
		case UP_LEFT:
			break;
		case UP_RIGHT :
			break;
		}
		
		//TODO fix this 
		Localization loc = new Localization();
		LineReader.getLeftSensor().subscribe(loc);
//		conf.getCurrentLocation().setTheta(Math.toRadians(90)).setX(0).setY(0);
		try{ Thread.sleep(100);} catch(Exception e){};

		driver.forward(25);
		driver.rotateToRelatively(-90);
		driver.forward(28);
		
		LineReader.getLeftSensor().unsubscribe(loc);
		try{ Thread.sleep(100);} catch(Exception e){};
		conf.getCurrentLocation().setTheta(0).setX(15).setY(15);

	}

	/**
	 * localization : do the falling edge to have a general idea where the robot is facing
	 * assuming the current location is at (15,15) , then use odoCorrection to 
	 * correct the actual (x y t )
	 */
	private static void localize() {
		prepareForFallingEdge();	
		performFallingEdge();
	}
	
	/**
	 * do falling edge and turn face the x axis 
	 */
	private static void performFallingEdge() {
		//clockwise  rotation 
		driver.rotateToRelatively(360, true);
		int DistOnInvok = 50;
		while (usp.getFilteredDistance() > DistOnInvok){
			nap(50);
		} 
		driver.motorStop();		//stop the motor from rotating
		
		angle1 = odo.getTheta();
		
		//TODO delete when done testing 
		if (DEBUG) LCDWriter.getInstance().writeToScreen("ang1 " + Math.toDegrees(angle1) , 6);
		nap (50);
		
		//counterclock rotation
		driver.rotateToRelatively(-360, true);
		nap(1000);  //avoid miss read
		while (usp.getFilteredDistance() > DistOnInvok){
			nap(50);
		}
		driver.motorStop();
		
		angle2 = odo.getTheta();
		
		//TODO delete when done testing 
		if (DEBUG) LCDWriter.getInstance().writeToScreen("ang2 " + Math.toDegrees(angle2) , 5);

		//find the angle from origin 
		double angleFromOrigin = ((angle1 + angle2)/2)- angle2;
		
		//TODO delete when done testing 
		LCDWriter.getInstance().writeToScreen("AFO " +Math.toDegrees(angleFromOrigin) , 0);
		//rotate so the robot is parallel to the x/y slop
		driver.rotateToRelatively(Math.toDegrees(angleFromOrigin));
	}
	
	/**
	 * make sure the robot is facing outward 
	 */
	@SuppressWarnings("deprecation")
	private static void prepareForFallingEdge() {
		driver.rotateToRelatively(360, true);
		while(usp.getFilteredDistance() < 70){
			nap(100);
		}
		driver.motorStop();
	}
	
	/**
	 * let the thread sleep for t amount of milliseconds 
	 * @param t
	 */
	private static void nap(int t){
		try {Thread.sleep(t);} catch (InterruptedException e) {}
	}
	
	
	private static int lnNumb = 0 ;
	@Override
	public void passedLine(boolean isLeft) {
		if (!isLeft){
			if (lnNumb++  == 0 ){ //first line : correct X 
				conf.getCurrentLocation().setX(Configuration.DIST_FROM_LINE_READER);
			}
			if (lnNumb == 1 ){
				driver.motorStop();
				conf.getCurrentLocation().setY(Configuration.DIST_FROM_LINE_READER);
				driver.forward(4);
			}
		}
		
	}


}
