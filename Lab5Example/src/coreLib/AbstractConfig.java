package coreLib;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;

/**
 * This class contains the fundation of a configuration class. 
 * this class is abstract and must be extended 
 * @author yuechuan
 *@version 1.54
 */
public abstract class AbstractConfig {
	private Object lock ;
	private Coordinate startLocation;// starting location
	private Coordinate currentLocation ; //current location ;
	private Coordinate nextLocation; //current location ;
	private boolean driveComplete  = false ;
	
	private int ROTATE_SPEED = 60 ;
	private int FORWARD_SPEED = 200;
	//robot hardware
	public static final NXTRegulatedMotor LEFT_MOTOR = Motor.A;
	public static final NXTRegulatedMotor RIGHT_MOTOR = Motor.B;
	public static final NXTRegulatedMotor SENSOR_MOTOR= Motor.C;
	public static final SensorPort ULTRASONIC_SENSOR_PORT = SensorPort.S1;
	public static final SensorPort LIGHT_SENSOR_PORT = SensorPort.S2;
	static final double LEFT_RADIUS = 2.090 ;
	static final double RIGHT_RADIUS =2.090;
	static final double WIDTH = 15.24 ;
	
	/**
	 * @return a instance of config
	 */
	public static AbstractConfig getInstance(){
		return Configuration.getInstance();
	}
	
	/**
	 * 
	 * @return average radius of the wheels of robot 
	 * in cm 
	 */
	public double getAvgRadius(){
		return (LEFT_RADIUS + RIGHT_RADIUS)/2;
	}
	/**
	 * set the roation speed of the wheels in deg/sec
	 * @param speed
	 */
	public void setRotationSpeed(int speed){
		ROTATE_SPEED = speed;
	}
	/**
	 * set the ForwardSpeed in deg/sec
	 * @param speed
	 */
	public void setForwardSpeed(int speed){
		FORWARD_SPEED = speed;
	}
	/**
	 * 
	 * @return current rotation speed in deg/sec
	 */
	public int getRotationSpeed(){
		return ROTATE_SPEED;
	}
	/**
	 * get the forward speed in deg/sec
	 */
	public int getForwardSpeed(){
		return FORWARD_SPEED;
	}
	/**
	 * must be called when all goals are met and before the robot terminates..
	 * This will clean up the robot and shuts off what's necessary. 
	 * this is a finalize method  
	 */
	public void setDriveComplete(){
		driveComplete = true ;
	}
	/**
	 * return if drive is complete and robot system is cleaned and finalized 
	 * @return
	 */
	public boolean isDriveComplete(){
		return driveComplete;
	}
	/**
	 * 
	 * @return THE STARTING coordinate 
	 */
	public Coordinate getStartLocation() {
		return startLocation;
	}

	/**
	 * set the starting coordinate to startLocation 
	 * @param startLocation
	 */
	public void setStartLocation(Coordinate startLocation) {
		this.startLocation = startLocation;
	}
	
	/** 
	 * @return the current location set by the odometer 
	 * @deprecated use the odometer 
	 * {@code getX() getY() getTheata()}
	 */
	public Coordinate getCurrentLocation() {
		return currentLocation;
	}
	/**
	 * set the current location to currentLocation
	 * @param currentLocation
	 */
	public void setCurrentLocation(Coordinate currentLocation) {
		this.currentLocation = currentLocation;
	}
	
	/**
	 * returns the next location  
	 * @return
	 * @deprecated since we are using a stack system we should use the stack peek instead 
	 */
	public Coordinate getNextLocation() {
		return nextLocation;
	}
	/**
	 * set the next location 
	 * @param nextLocation
	 * @deprecated we are now using a stack to represent the next locations 
	 */
	public void setNextLocation(Coordinate nextLocation) {
		this.nextLocation = nextLocation;
	}

	/**
	 * reset the motor speed to default forward_speed and motor rotation to forward
	 * and start the motor 
	 */
	public void resetMotorSpeed() {
		LEFT_MOTOR.stop();
		RIGHT_MOTOR.stop();
		
		LEFT_MOTOR.setSpeed(FORWARD_SPEED);
		RIGHT_MOTOR.setSpeed(FORWARD_SPEED);
		
		LEFT_MOTOR.forward();
		RIGHT_MOTOR.forward();
	}
	
	/**
	 * stop the motors right away disregarding the motor's current state 
	 */
	public void stopMotor(){
		LEFT_MOTOR.stop();
		RIGHT_MOTOR.stop();
	}
	/**
	 * this will be called when leftButton is pressed on the brick
	 */
	public abstract void leftButtonPressed();
	/**
	 * this will be called when rightButton is pressed on the brick
	 */
	public abstract void rightButtonPressed();
	
}
