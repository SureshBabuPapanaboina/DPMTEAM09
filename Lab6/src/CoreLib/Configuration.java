/**
 * 
 */
package CoreLib;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.Pose;

/**
 * This class contains the foundation of a configuration class. 
 * this class is a helper class and hence no instance can be instantiated 
 * @author Mike
 * @version 1.0
 */
public final class Configuration {
	private static int ROTATE_SPEED = 60 ;
	private static int FORWARD_SPEED = 200;
	/**
	 * current position of the robot 
	 * initialize to (0,0,0)
	 */
	private static Pose currentPosition = new Pose();
	
	/**
	 * starting position of the robot 
	 * initialize to (0,0,0)
	 */
	private static Pose startPosition = new Pose();
	
	
	
	//robot hardware
	public static final NXTRegulatedMotor LEFT_MOTOR = Motor.A;
	public static final NXTRegulatedMotor RIGHT_MOTOR = Motor.B;
	public static final NXTRegulatedMotor SENSOR_MOTOR= Motor.C;
	public static final SensorPort ULTRASONIC_SENSOR_PORT = SensorPort.S1;
	public static final SensorPort LIGHT_SENSOR_PORT = SensorPort.S2;
	
	//TODO redefine the following
	public static final double LEFT_RADIUS = 2.090 ;
	public static final double RIGHT_RADIUS =2.090;
	public static final double WIDTH = 15.24 ;

	/**
	 * mark this to be true when the whole program life cycle is 
	 * finished.
	 */
	private static boolean driveComplete ;
	
	/**
	* made private so no class can inherit and instantiate 
	*/
	private Configuration(){}


	/**
	 * 
	 * @return average radius of the wheels of robot 
	 * in cm 
	 */
	public static double getAvgRadius(){
		return (LEFT_RADIUS + RIGHT_RADIUS)/2;
	}
	
	/**
	 * set the speed of the motor when the robot is rotating in deg/sec
	 * @param speed
	 */
	public static void setRotationSpeed(int speed){
		ROTATE_SPEED = speed;
	}

	/**
	 * set the ForwardSpeed in deg/sec
	 * @param speed
	 */
	public static void setForwardSpeed(int speed){
		FORWARD_SPEED = speed;
	}
		/**
	 * 
	 * @return current rotation speed in deg/sec
	 */
	public static int getRotationSpeed(){
		return ROTATE_SPEED;
	}
	/**
	 * get the forward speed in deg/sec
	 */
	public static int getForwardSpeed(){
		return FORWARD_SPEED;
	}
	/**
	 * must be called when all goals are met and before the robot terminates..
	 * This will clean up the robot and shuts off what's necessary. 
	 * this is a finalize method  
	 */
	public static void setDriveComplete(){
		driveComplete = true ;
	}
	/**
	 * return if drive is complete and robot system is cleaned and finalized 
	 * @return
	 */
	public static boolean isDriveComplete(){
		return driveComplete;
	}

	/**
	 * 
	 * @return THE STARTING coordinate 
	 */
	public static Pose getStartLocation() {
		return startPosition;
	}

	/**
	 * set the starting coordinate to startLocation 
	 * @param startLocation
	 */
	public static void setStartLocation(Pose startLocation) {
		startPosition = startLocation;
	}
	
	/** 
	 * @return the current location set by the odometer 
	 * @deprecated use the odometer 
	 * {@code getX() getY() getTheata()}
	 */
	public static Pose getCurrentLocation() {
		return currentPosition;
	}
	/**
	 * set the current location to currentLocation
	 * @param currentLocation
	 */
	public static void setCurrentLocation(Pose currentLocation) {
		currentPosition = currentLocation;
	}
}
