package movement;

import odometry.Odometer;
import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import sensors.LineReader;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;

/**
 * Driver class that has the control of motor, not open to extension
 * because we do not want two instance of this running at the same time.
 * @author yuechuan
 * @version 2.0  Apr 01 
 *
 */
public final class Driver{
	
	private static Driver instance;
	private static Configuration config = Configuration.getInstance();
	private static Odometer odo = Odometer.getInstance();
	private static NXTRegulatedMotor leftMotor, rightMotor;
	
	private final static boolean DEBUG = false;	
	
	private static boolean isTurning = false ;		//true if robot is turning 

	
	/**
	 * indicate if the motor is running or not.
	 * set by motorStop and motorForward
	 */
	private static boolean motorStopped = true ;

	/**
	 * do not use this to initialize another instance. only used for extension.
	 * use getInstance to either generate a new instance on the fly or get the previously
	 * generated instance 
	 * @param config
	 */
	private Driver(Configuration config){
		leftMotor= Configuration.LEFT_MOTOR; 
		rightMotor = Configuration.RIGHT_MOTOR;
		config.getStartLocation();
	}
	
	public static Driver getInstance(){
		if(instance == null) instance = new Driver(Configuration.getInstance());
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
	
	private boolean withinRange(double d, double e, double f){
		return Math.abs(e - d) < f;
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
		LCDWriter.getInstance().writeToScreen("Dest:" + nextLocation.toString(), 3);

		Coordinate currentLoc  = odo.getCurrentCoordinate();
		
		config.setStartLocation(currentLoc.copy());
		
		double turningAngle = Coordinate.calculateRotationAngle(currentLoc, nextLocation);
		//make turn
		rotateToRelatively(turningAngle);

		setSpeed(config.getForwardSpeed());
		
		motorForward();
		while(!motorStopped && (!withinRange(odo.getX(), nextLocation.getX(), 3.75) || !withinRange(odo.getY(), nextLocation.getY(), 3.75))){
			double desiredAngle = Coordinate.calculateRotationAngle(odo.getCurrentCoordinate(), nextLocation);
			if(Math.abs(odo.getTheta() - desiredAngle) > 20 ){
				Sound.beepSequence();
				motorStop();
//				LCDWriter.getInstance().writeToScreen("dangle:" + desiredAngle, 3);
				rotateToRelatively(desiredAngle);
				setSpeed(config.getForwardSpeed());
				motorForward();
			}
			try{Thread.sleep(25);} catch (Exception e){};
		}
		
		motorStop();		

		
			
		}
		
	/**
	 * move wheel forward at the same speed it was running at before 
	 * only return once finished moving 
	 * @param dist
	 */
	public void forward(double dist){
		motorStopped = false ;
		
		int currentT = Configuration.LEFT_MOTOR.getTachoCount();
		double rotations = dist/ (2*Math.PI*(+ Configuration.RIGHT_RADIUS)) ;
//		if (DEBUG) RConsole.println("rotations" + rotations );
		
		int finalTachoCount =  currentT+ (int) (rotations * 360 );
//		if (DEBUG) RConsole.println("current Tacho " + Configuration.LEFT_MOTOR.getTachoCount() + "\t\tfinal Tacho"  + finalTachoCount );
		motorForward();
		while(!motorStopped && finalTachoCount- leftMotor.getTachoCount() > 0 ){
//			if (DEBUG) RConsole.println("current Tacho " + Configuration.LEFT_MOTOR.getTachoCount() );
			try{Thread.sleep(25);} catch (Exception e){};
		}
		
		motorStop();
	}
	
	/**
	 * move wheel backward at the same speed it was running at before.
	 * <b> The motor will always move a tinny bit more than the defined distance 
	 * due to the implementation. </b> the extra amount depends on how fast can CPU process 
	 * can run the while loop, the highest pulling interval should be more than 20 ms, which is the
	 * time I have set arbitrarily.
	 * @param dist
	 */
	public void backward(double dist){
		
//		if (DEBUG) RConsole.println("BackWard" );
		int currentT = Configuration.LEFT_MOTOR.getTachoCount();
		double rotations = dist/ (2*Math.PI*(+ Configuration.RIGHT_RADIUS)) ;
//		if (DEBUG) RConsole.println("rotations" + rotations );
		
		int finalTachoCount =  currentT- (int) (rotations * 360 );
//		if (DEBUG) RConsole.println("current Tacho " + Configuration.LEFT_MOTOR.getTachoCount() + "\t\tfinal Tacho"  + finalTachoCount );
		motorBackward();
		//check if the motor has reached the given distance
		int sleepIntv = 20 ;		//how long the loop should wait before checking 
		while(!motorStopped && leftMotor.getTachoCount() - finalTachoCount > 0 ){
//			if (DEBUG) RConsole.println("current Tacho " + Configuration.LEFT_MOTOR.getTachoCount() );
			//if the motor still have lots to rotate then sleep longer 
			sleepIntv = (leftMotor.getTachoCount() - finalTachoCount > 50) ? 50 : 20 ;
			try{Thread.sleep(sleepIntv);} catch (Exception e){};
		}
		motorStop();
	}
	
	/**
	 * rotate to the angle wrt to the current robot angle.
	 * the method will only finish after rotating is over.
	 * @param degree
	 */
	public void rotateToRelatively(double degree){
		setTurning(true);
		rotateToRelatively(degree, false);			
		setTurning(false);
		motorStop();
	}
	/**
	 * <br>rotate to the angle wrt to the current robot angle.
	 * the method will only finish after rotating is over.
	 * this class will not set the motorStop flag !!! 
	 * <b> make sure to call motor Stop after you use this method!!!</b>
	 * <br><br><b> note this method will return right away and will not alter
	 * the return value of {@link #isTurning()} !! </b> <br> 
	 * @param degree 
	 * @param returnRightAway should the function finish before finishing the turn 
	 *  since this method will return right away and will not wait for the rotation to finish
	 * we must manually call the {@link #motorStop()} method to ensure the motorStopped flag is set correctly.
	 * also note that {@link #isTurning()} will never return true when this method is called also due to the 
	 * return right away property. 
	 * 
	 * <b> use the single parameter {@link #rotateToRelatively(double)} if you do not need to return right away </b>
	 * 
	 */
	public void rotateToRelatively(double degree, boolean returnRightAway){
		motorStopped = false;
		rightMotor.setSpeed(Configuration.getInstance().getRotationSpeed());
		leftMotor.setSpeed(Configuration.getInstance().getRotationSpeed());
	        if (degree < 0){		//if degree is negative then rotate back ward
	        	motorBackward();
	        }
	        //set flag to indicate motor is running 
	        motorStopped = false ;
	        
	        leftMotor.rotate(
	        	convertAngle(Configuration.LEFT_RADIUS, Configuration.WIDTH, degree)
	        	, true);
	        rightMotor.rotate(
	        	-convertAngle(Configuration.RIGHT_RADIUS,Configuration.WIDTH , degree)
	        	, returnRightAway);
	}
	
	/**
	 * check if the robot is making a turn 
	 * @return true when the robot is making a turn through the rotateToRelatively() method 
	 */
	public boolean isTurning(){
		return isTurning;
	}
	/**
	 * set if the robot is making a turn, this should be used if you are using the 
	 * 2 parametered version of rotateToRelatively()
	 * @return 
	 */
	public void setTurning(boolean t){
		LineReader.getLeftSensor().setNotPaused(!t);
		LineReader.getRightSensor().setNotPaused(!t);
		isTurning = t;
	}
	
	/**
	 * @param radius radius of the wheel 
	 * @param distance distance we want to cover 
	 * @return angle the wheel need to rotate in degree
	 */
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	/**
	 * convert angle the wheel need to turn (in deg) to the distance driven in cm
	 */
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	/**
	 * turn to angle wrt to the y axies
	 * @param in degrees 
	 * @deprecated same asrotateToRelatively(theata); use that instead, this is kept for 
	 * backward comparability 
	 */
	public void turnTo(double theata) {
		rotateToRelatively(theata);		
	}
	/**
	 * set wheel rotation speed in deg/sec 
	 * @param speed
	 */
	public static void setSpeed(int speed){
		leftMotor.setAcceleration(Configuration.ACCELERATION);
		rightMotor.setAcceleration(Configuration.ACCELERATION);
		leftMotor.setSpeed(speed);
		rightMotor.setSpeed(speed);
//		if (DEBUG) RConsole.println("Motor speed set to :" + speed);
	}
	/**
	 * set motor to go forward till motorStop() or flt() 
	 * All actions involving forwarding the motor must call this method
	 */
	public void motorForward(){
		motorStopped = false ;
		leftMotor.forward();
		rightMotor.forward();		
	}
	/**
	 * set motor to go backward till motorStop()
	 * All actions involving backing of the motor must call this method
	 */
	public void motorBackward(){
		motorStopped = false ;
		leftMotor.backward();
		rightMotor.backward();
	}
	
	/**
	 * check if motor is stopped 
	 * @return true if the motor is in a non action state  
	 */
	public static boolean isMotorStopped() {
		return motorStopped;
	}
	
	//Lock for synch
	Object lock = new Object();
	
	/**
	 * stop motor
	 * All actions involving stopping the motor must call this method
	 */
	public void motorStop(){
		//TODO: might need to put this in a synchronized block

		if (DEBUG) RConsole.println("Motor Stopped");
		leftMotor.stop(true);
		rightMotor.stop();
		
		motorStopped = true ;
		
	}

}
