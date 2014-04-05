package robotcore;

import odometry.Odometer;
import sensors.LineReader;
import sensors.LineReaderListener;
import sensors.UltrasonicListener;
import sensors.UltrasonicPoller;
import lejos.nxt.Motor;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;
import movement.Driver;

/**
 * The third revision of Localization 
 * @author yuechuan
 *
 */
public class LocalizationIII implements LineReaderListener{
	
	//used threads and initialized stuff 
	private static Driver driver = Driver.getInstance();
	private static Odometer odo = Odometer.getInstance();
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	private static Configuration conf = Configuration.getInstance();
	private static final boolean DEBUG = Configuration.DEBUG;
	//falling edge angles 
	private static double angle1 , angle2 ;
	private static LocalizationIII localization = new LocalizationIII();		//for subscribing to lin readers 
	//starting position
	private final static int LOW_LEFT = 0; 
	private final static int LOW_RIGHT = 1; 
	private final static int UP_LEFT= 2; 
	private final static int UP_RIGHT = 3; 
	
	public static void main(String [] args){
		init();
		nap(100);
		localizeAndMoveToStartLoc(LOW_LEFT);
		//signals finish 
		Sound.beep();
		nap(100);
		Sound.beep();
		nap(100);
		Sound.beep();
		nap(100);
		LineReader.unsubscribeToAll(localization);
	}
	
	private static void init(){
		odo.start();
		usp.start();
		LineReader.getLeftSensor().start();
		LineReader.getRightSensor().start();
	}
	
	/**
	 * perform localization and move to start location depending on the given corner 
	 */
	public static void localizeAndMoveToStartLoc(int startingCorner) {
		int rotSpeed = conf.getRotationSpeed();
		conf.setRotationSpeed(120);
		localize();		//this will turn the robot head to the field see [figure 1] in notebook 
		driver.rotateToRelatively(45);	//this will turn parallel to the x axies for case 1 , for other cases see fig 1 in notebook
		conf.getCurrentLocation().setTheta(Math.toRadians(90)).setX(0).setY(0);
		
		LineReader.subscribeToAll(localization);
		
		nap(100);
		driver.travelTo(30, 0);
		driver.travelTo(30, 30);
		nap(100);
		//==========SET X AND Y ================== 
		switch(startingCorner){
		case LOW_LEFT:
			conf.getCurrentLocation().setTheta(0).setX(15).setY(15);
			break;
		case LOW_RIGHT:
			conf.getCurrentLocation().setTheta(270).setX(300).setY(0);
			break;
		case UP_LEFT:
			conf.getCurrentLocation().setTheta(90).setX(0).setY(300);
			break;
		case UP_RIGHT :
			conf.getCurrentLocation().setTheta(180).setX(300).setY(300);
			break;
		}
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
		while (usp.getFilteredDistance() > DistOnInvok){	nap(50);	}
		driver.motorStop();		//stop the motor from rotating
		angle1 = odo.getTheta();
		nap (50);

		driver.rotateToRelatively(-360, true);		//counter clock rotation
		nap(1000);  //avoid miss read
		while (usp.getFilteredDistance() > DistOnInvok){	nap(50);	}
		driver.motorStop();
		angle2 = odo.getTheta();
		double angleFromOrigin = ((angle1 + angle2)/2)- angle2;		//find the angle from origin 
		driver.rotateToRelatively(Math.toDegrees(angleFromOrigin));		//rotate so the robot is parallel to the x/y slop
	}
	
	/**
	 * make sure the robot is facing outward 
	 */
	@SuppressWarnings("deprecation")
	private static void prepareForFallingEdge() {
		driver.rotateToRelatively(360, true);
		while(usp.getFilteredDistance() < 70){	nap(100);	}
		driver.motorStop();
	}
	
	/**
	 * let the thread sleep for t amount of milliseconds 
	 * @param t
	 */
	private static void nap(int t){
		try {Thread.sleep(t);} catch (InterruptedException e) {}
	}
	
	private static boolean rPassed1 = false , rPassed2 = false;	//indicate which of the line is passed and how many times 
	/**
	 * stop the corresponding motor when the corresponding line reader has sensed the line 
	 * @param isLeft
	 */
	@Override
	public void passedLine(boolean isLeft) {
		if(isLeft){
			rPassed2 = rPassed1;
			rPassed1 = true ;
			if (rPassed1 && !rPassed2){ //if the first line is passed
				odo.getCurrentCoordinate().setX(12);
				
			}
			else {	//2nd line passes
				odo.getCurrentCoordinate().setY(12);
				driver.motorStop();
				driver.forward(3);
				LineReader.unsubscribeToAll(localization);
			}
		}
	}

}
