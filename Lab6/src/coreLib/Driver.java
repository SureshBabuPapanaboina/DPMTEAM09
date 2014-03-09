package coreLib;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;

/**
 * driver class that has the control of motor 
 * @author yuechuan
 * @version 1.8 
 *
 */
public class Driver extends Thread{
	private Configuration config ;
	private NXTRegulatedMotor leftMotor= Configuration.LEFT_MOTOR , 
								rightMotor = Configuration.RIGHT_MOTOR;

	
	private static Driver instance ;
	private Odometer odo = Odometer.getInstance();
	/**
	 * indicate if the motor is running or not.
	 * set by motorStop and motorForward
	 */
	private boolean motorStopped = true ;

	/**
	 * do not use this to initialize another instance. only used for extension.
	 * use getInstance to either generate a new instance on the fly or get the previously
	 * generated instance 
	 * @param config
	 */
	@Deprecated
	public Driver(Configuration config){
		this.config = config ;


		config.getStartLocation();
	}
	
	/**
	 * 
	 * @return an instance of Driver 
	 */
	public static Driver getInstance(){
		if (instance == null){
			instance = new Driver(Configuration.getInstance());
		}
		return instance;
	}

	/**
	 * overloaded version of <br> {@code travelTo(new Coordinate(x, y, 0));			}
	 * @param x
	 * @param y
	 */
	public void travelTo(int x, int y){
		travelTo(new Coordinate(x, y, 0));			
	}
	
	/**
	 * travel to wrt to the global (0,0) coordinate
	 * . Since this method use currentCoordinate which is \
	 * initialized during object initialization. this method is 
	 * made an instance method to avoid undefined behavior.
	 * 
	 * @param nextLocationg
	 */
	public void travelTo(Coordinate nextLocation) {
		Coordinate currentLoc  = new Coordinate(odo.getX(), odo.getY(), odo.getTheta());
		config.setNextLocation(nextLocation);
		config.setStartLocation(currentLoc.copy());
		
		double distance = Coordinate.calculateDistance(currentLoc, nextLocation);
		double turningAngle = Coordinate.calculateRotationAngle(currentLoc, nextLocation);
		
		RConsole.println("Driver:travelTo:CurrentCoord: " + currentLoc.toString2());
		RConsole.println("Driver:travelTo:NxtCoord: " + nextLocation.toString2());
		RConsole.println("Driver:travelTo:traveling dist: " + distance);
		RConsole.println("Driver:travelTo:turning Angle: " + turningAngle);
		//make turn
		rotateToRelatively(turningAngle);

		setSpeed(config.getForwardSpeed());
		
		forward(distance);
		
		RConsole.println("Driver:travelTo:currentCoordinate : x " + config.getCurrentLocation().getX()
			+"\ty " + config.getCurrentLocation().getY() 
			+ "\ttheata " +config.getCurrentLocation().getTheta());
			RConsole.println("=======");
		
			
		}
		
	/**
	 * move wheel forward at the same speed it was running at before 
	 * @param dist
	 */
	public void forward(double dist){
		motorStopped = false ;
		
		int currentT = Configuration.LEFT_MOTOR.getTachoCount();
		double rotations = dist/ (2*Math.PI*(+ Configuration.RIGHT_RADIUS)) ;
		RConsole.println("rotations" + rotations );
		
		int finalTachoCount =  currentT+ (int) (rotations * 360 );
		RConsole.println("current Tacho " + Configuration.LEFT_MOTOR.getTachoCount() + "\t\tfinal Tacho"  + finalTachoCount );
		motorForward();
		while(!motorStopped && finalTachoCount- leftMotor.getTachoCount() > 0 ){
			RConsole.println("current Tacho " + Configuration.LEFT_MOTOR.getTachoCount() );
			try{Thread.sleep(20);} catch (Exception e){};
		}
		
		motorStop();
	}
	
	/**
	 * move wheel backward at the same speed it was running at before 
	 * @param dist
	 */
	public void backward(double dist){
		
		RConsole.println("BackWard" );
		int currentT = Configuration.LEFT_MOTOR.getTachoCount();
		double rotations = dist/ (2*Math.PI*(+ Configuration.RIGHT_RADIUS)) ;
		RConsole.println("rotations" + rotations );
		
		int finalTachoCount =  currentT- (int) (rotations * 360 );
		RConsole.println("current Tacho " + Configuration.LEFT_MOTOR.getTachoCount() + "\t\tfinal Tacho"  + finalTachoCount );
		motorBackward();
		while(!motorStopped && leftMotor.getTachoCount() - finalTachoCount > 0 ){
			RConsole.println("current Tacho " + Configuration.LEFT_MOTOR.getTachoCount() );
			try{Thread.sleep(20);} catch (Exception e){};
		}
		motorStop();
	}
	
	/**
	 * rotate to the angle wrt to the current robot angle.
	 * the method will only finish after rotating is over.
	 *	<br> this method is here only for comparability.
	 *	<br> use rotateToRelatively (double degree , boolean returnRightAway);
	 *	when ever possible 
	 * @param degree
	 */
	public void rotateToRelatively(double degree){
		rotateToRelatively(degree, false);			
	}
	/**
	 * rotate to the angle wrt to the current robot angle.
	 * the method will only finish after rotating is over.
	 * @param degree 
	 * @param returnRightAway should the function finish before finishing the turn 
	 */
	public void rotateToRelatively(double degree, boolean returnRightAway){
		
		motorStopped = false;
		rightMotor.setSpeed(Configuration.getInstance().getRotationSpeed());
		leftMotor.setSpeed(Configuration.getInstance().getRotationSpeed());
	        if (degree < 0){		//if degree is negative then rotate back ward
	        	motorBackward();
	        }
	        //set flage to indicate motor is running 
	        motorStopped = false ;
	        
	        leftMotor.rotate(
	        	convertAngle(Configuration.LEFT_RADIUS, Configuration.WIDTH, degree)
	        	, true);
	        rightMotor.rotate(
	        	-convertAngle(Configuration.RIGHT_RADIUS,Configuration.WIDTH , degree)
	        	, returnRightAway);
	        //set flage to true 
	        motorStop();
	}
	
	/**
	 * 
	 * @param radius radius of the wheel 
	 * @param distance distance we want to cover 
	 * @return angle the wheel need to rotate in degree
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	/**
	 * convert angle in deg to the distance driven in cm
	 */
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	/**
	 * turn to angle wrt to the y axies
	 * @param in degrees 
	 */
	public void turnTo(double theata) {
		rotateToRelatively(theata);		
	}
	/**
	 * set wheel rotation speed in deg/sec 
	 * @param speed
	 */
	public static void setSpeed(int speed){
		Configuration.LEFT_MOTOR.setSpeed(speed);
		Configuration.RIGHT_MOTOR.setSpeed(speed);
	}
	/**
	 * set motor to go forward till stop()
	 */
	public void motorForward(){
		motorStopped = false ;
		Configuration.LEFT_MOTOR.forward();
		Configuration.RIGHT_MOTOR.forward();		

	}
	/**
	 * set motor to go backward till stop()
	 */
	public void motorBackward(){
		motorStopped = false ;
		Configuration.LEFT_MOTOR.backward();
		Configuration.RIGHT_MOTOR.backward();
	}
	
	/**
	 * check if motor is stopped 
	 * @return
	 */
	public boolean isMotorStopped() {
		return motorStopped;
	}
	/**
	 * stop motor 
	 */
	public void motorStop(){
		motorStopped = true ;
		RConsole.println("Motor Stopped");
		Configuration.LEFT_MOTOR.stop();
		Configuration.RIGHT_MOTOR.stop();
	}

}
