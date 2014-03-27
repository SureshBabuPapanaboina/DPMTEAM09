package robotcore;

import odometry.Odometer;
import communication.RemoteConnection;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.RConsole;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteSensorPort;
import lejos.robotics.Color;

/**
 * This class contains the fundation of a configuration class. 
 * @author yuechuan
 *@version 1.54
 */
public class Configuration {
	private Coordinate startLocation;// starting location
	private Coordinate currentLocation ; //current location ;
	
	private boolean driveComplete  = false ;
	private int ROTATE_SPEED = 160 ;
	private int FORWARD_SPEED = 200;
	private int USB_TIMEOUT = 3000;
	//constants about the plying field
	public static final int GRID_SIZE = 8; //set to demo size
	
	 //remote 
//	private static RemoteConnection rc = RemoteConnection.getInstance();
	
	//robot hardware

	
	//motors 
	public static final NXTRegulatedMotor LEFT_MOTOR = Motor.A;
	public static final NXTRegulatedMotor RIGHT_MOTOR = Motor.B;
	public static final NXTRegulatedMotor SENSOR_MOTOR= Motor.C;	
	
	//remote motors 
	//these should only be initiated when a connection CAN be made, it's causing
	//	null pointers in tests currently
	
//	public static final RemoteMotor LEFT_ARM_MOTOR = rc.getLeftRemote();
//	public static final RemoteMotor RIGHT_ARM_MOTOR= rc.getRightRemote();
//	
	//sensors
	public static final SensorPort ULTRASONIC_SENSOR_PORT = SensorPort.S1;
	public static final SensorPort LINE_READER_LEFT = SensorPort.S2;
	public static final SensorPort LINE_READER_RIGHT = SensorPort.S3;
	public static final SensorPort COLOR_SENSOR_PORT = SensorPort.S4;
	
	public static final double LEFT_RADIUS = 2.090 ;
	public static final double RIGHT_RADIUS =2.090;
	public static final double WIDTH = 22.5 ;
	/**
	 * the distance between the lineReaders and the center of rotation 
	 */
	public static final double distFromLineReaderToCenterOfRot = 0 ; //TODO change to measured values

	//temporarily changing to use smaller robot
//	public static final double LEFT_RADIUS = 2.16 ;
//	public static final double RIGHT_RADIUS = 2.16;
//	public static final double WIDTH = 15.7 ;
//	
	
	public static final CommunicationType INTERBRICK_COMM_METHOD = CommunicationType.Bluetooth;
	
	/**
	 * enable /disable debugging 
	 */
	public static final boolean DEBUG = true;
	
	
	
	//instance
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
		
		currentLocation = new Coordinate(15, 15, 0);
		setStartLocation(new Coordinate(15, 15, 0));
		RConsole.openUSB(USB_TIMEOUT);
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
	 * @return
	 */
	public Coordinate getOpponentDropZone(){
		//TODO
		return null;
	}
	
	/**
	 * Get the coordinates of the drop zone where to bring the flag
	 * @return
	 */
	public Coordinate getDropZone(){
		//TODO
		return null;
	}
	
	/**
	 * Returns the flag color the robot should be looking for
	 * @return
	 */
	public Color getFlagColor(){
		//TODO
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
	
	static Object lock = new Object();
	
	/** 
	 * @return the current location set by the odometer 
	 * {@code getX() getY() getTheata()}
	 */
	public Coordinate getCurrentLocation() {
		Coordinate loc;
		synchronized(lock){
			loc = currentLocation;
		}
		return loc;
	}
	

}
