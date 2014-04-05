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
 * The second revision of Localization 
 * @author yuechuan
 *
 */
public class LocalizattionII implements LineReaderListener{
	
	//used threads and initialized stuff 
	private static Driver driver = Driver.getInstance();
	private static Odometer odo = Odometer.getInstance();
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	private static Configuration conf = Configuration.getInstance();
	private static final boolean DEBUG = Configuration.DEBUG;
	//motors 
	private static NXTRegulatedMotor lMotor = Configuration.LEFT_MOTOR;
	private static NXTRegulatedMotor rMotor= Configuration.RIGHT_MOTOR;
	//falling edge angles 
	private static double angle1 , angle2 ;
	private static LocalizattionII localization = new LocalizattionII();		//for subscribing to lin readers 
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
		//assume at this location 
		conf.getCurrentLocation().setTheta(0).setX(15).setY(15);
		int rotSpeed = conf.getRotationSpeed();
		conf.setRotationSpeed(120);
		localize();		//this will turn the robot head to the field see [figure 1] in notebook 
		driver.rotateToRelatively(45);	//this will turn parallel to the x axies for case 1 , for other cases see fig 1 in notebook
		odo.setTheta(Math.toRadians(90));
		conf.setRotationSpeed(rotSpeed);
		driver.motorStop();
		nap(100);
		//==============correct x and y =========================
		LineReader.subscribeToAll(localization );
		driver.motorForward();		
		nap(10000);		
		driver.rotateToRelatively(-90);	//this will turn parallel to the y axies for case 1 , for other cases see fig 1 in notebook
		driver.motorForward();		
		nap(10000);	
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
	
	private static int lnNumb = 0 ; //how many lines was passes since the beginning of the localization
	private static boolean lPassed1 = false, rPassed1 = false , lPassed2 = false, rPassed2 = false;	//indicate which of the line is passed and how many times 
	/**
	 * stop the corresponding motor when the corresponding line reader has sensed the line 
	 * @param isLeft
	 */
	@Override
	public void passedLine(boolean isLeft) {
		if (!isLeft){  //only increment when right motor passes
			rMotor.stop();
			if (lnNumb == 0)	rPassed1 = true ;
			else rPassed2 = true ;
			lnNumb++;
		}
		else {
			lMotor.stop();
			if (lnNumb == 0) lPassed1 = true ;
			else lPassed2 = true ;
		}
		//if first line is crossed by two sensors or 2nd line crossed by 2 sensors 
		if ((lPassed1 && rPassed1) || (lPassed2 && rPassed2)){
			driver.forward(3);
		}
	}

}
