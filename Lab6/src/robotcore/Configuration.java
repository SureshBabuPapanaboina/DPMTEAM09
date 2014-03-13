package robotcore;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;
import lejos.robotics.Color;

/**
 * This class contains the fundation of a configuration class. 
 * @author yuechuan
 *@version 1.54
 */
public class Configuration {
	private Coordinate startLocation;// starting location
	
	//TODO: Should current location really be kept in the config file?
	private Coordinate currentLocation ; //current location ;
	private boolean driveComplete  = false ;
	
	private int ROTATE_SPEED = 60 ;
	private int FORWARD_SPEED = 200;
	//robot hardware
	public static final NXTRegulatedMotor LEFT_MOTOR = Motor.A;
	public static final NXTRegulatedMotor RIGHT_MOTOR = Motor.B;
	public static final NXTRegulatedMotor SENSOR_MOTOR= Motor.C;
	public static final SensorPort ULTRASONIC_SENSOR_PORT = SensorPort.S1;
	public static final SensorPort LIGHT_SENSOR_PORT = SensorPort.S2;
	public static final double LEFT_RADIUS = 2.090 ;
	public static final double RIGHT_RADIUS =2.090;
	public static final double WIDTH = 24 ;
	/**
	 * enable /disable debugging 
	 */
	public static final boolean DEBUG = false;
	
	private static Configuration config = null ;
	
	
	/**
	 * @return a instance of config
	 */
	public static Configuration getInstance(){
		if (config == null){
			config = new Configuration() ;
		}
		return config;
	}
	
	private Configuration(){
		setCurrentLocation(new Coordinate(0, 0, 0));
		setStartLocation(new Coordinate(0, 0, 0));
		RConsole.openUSB(3000);
	}
	
	/**
	 * Sets the starting position based on a corner value given
	 * As per project specifications of Bluetooth Message
	 * @param corner
	 */
	public void setStartLocation(int corner){
		//TODO: Complete
	}
	
	/**
	 * Sets the flag zone, this is the area where the robot searches for the flag
	 * @param lowerLeft
	 * @param upperRight
	 */
	public void setFlagZone(Coordinate lowerLeft, Coordinate upperRight){
		//TODO: Complete
	}
	
	/**
	 * Sets the opponents flag zone, not specified if should avoid or not in initial doc
	 * @param lowerLeft
	 * @param upperRight
	 */
	public void setOpponentFlagZone(Coordinate lowerLeft, Coordinate upperRight){
		//TODO: complete
	}
	
	/**
	 * Sets the drop zone that the robot should avoid of the opponent
	 * @param lowerLeft
	 */
	public void setOpponentDropZone(Coordinate lowerLeft){
		
	}
	
	/**
	 * Sets the drop zone of where it should drop off the flag
	 * @param lowerLeft
	 */
	public void setDropZone(Coordinate lowerLeft){
		
	}
	
	/**
	 * Sets the flag color that the robot should look for
	 * @param color
	 */
	public void setFlagColor(int color){
		
	}
	
	/**
	 * Get the end flag zone
	 * @return size-2 array containing bottom left coord, and top right coord
	 */
	public Coordinate[] getFlagZone(){
		//TODO: Complete
		return null;
	}
	
	/**
	 * Get the opponent end flag zone
	 * @return size-2 array containing bottom left coord, and top right coord
	 */
	public Coordinate[] getOpponentFlagZone(){
		//TODO: complete
		return null;
	}
	
	/**
	 * Get the opponent drop zone to avoid
	 * @param lowerLeft coordinate
	 */
	public Coordinate getOpponentDropZone(){
		return null;
	}
	
	/**
	 * Get the coordinates of the drop zone where to bring the flag
	 * @param lowerLeft coordinate
	 */
	public Coordinate getDropZone(){
		return null;
	}
	
	/**
	 * Returns the flag color the robot should be looking for
	 * @return
	 */
	public Color getFlagColor(){
		return null;
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
	 * set the current location to currentLocation this is 
	 * @param currentLocation
	 */
	public void setCurrentLocation(Coordinate currentLocation) {
		this.currentLocation = currentLocation;
	}
	
}