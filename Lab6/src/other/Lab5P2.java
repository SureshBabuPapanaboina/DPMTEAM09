package other;

import java.util.Stack;
import coreLib.AbstractConfig;
import coreLib.ArmMotor;
import coreLib.Configuration;
import coreLib.Coordinate;
import coreLib.Driver;
import coreLib.LCDWriter;
import coreLib.Odometer;
import coreLib.UltrasonicPoller;
import ultrasonicListeners.BlockInFrontInterrupt;
import ultrasonicListeners.Localize;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.ColorSensor.Color;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;
/**
 * lab 5 part 2 code, arm must be open completely at the beginning to produce better 
 * localization 
 * @author yuechuan
 * @version 1.5 
 * @date 27 - 02 - 14 
 */
public class Lab5P2 {
	
	//declearation of necessary items 
	private static Driver driver = Driver.getInstance();
	private static AbstractConfig config = Configuration.getInstance();
	private static ColorSensor cs = new ColorSensor(AbstractConfig.LIGHT_SENSOR_PORT);
	private static LCDWriter lcd = LCDWriter.getInstance();
	private static Odometer odo = Odometer.getInstance();
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	/**
	 true if we want to stop navigation for a little while 
	 */
	private static boolean navigationInterrupted = false ;
	

	private static Stack <Coordinate> wayPoints = new Stack<Coordinate>();	

	public static void main(String[] args) {
		BlockInFrontInterrupt bifi = new BlockInFrontInterrupt();
		bifi.setCalled(false).setContinuous(true);
		//start the threads NOTE : do not start twice 
		driver.start();
		lcd.start();
		odo.start();
		usp.start();

		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		
		localize(); //facing the y axis 

		//handles the movement when there's a block in front
		//subscribe the brick in front ultrasonic interrupt to the ultrasionic puller
		usp.subscribe(bifi);
		
		//add waypoints to the movement coordinates 
		addWaypointSet2();
		
		while (!wayPoints.empty()){
			if (!navigationInterrupted){
				scanAtWayPoint();
				driver.travelTo(wayPoints.peek());	
			}
			else {try {Thread.sleep(500);} catch (Exception e){}; }
			
			//if current location is near waypoint then consider it on dot and remove it 
			if (wayPoints.peek().isNear(new Coordinate (odo.getX(),odo.getY(),0))){
				wayPoints.pop();
			}
		}
		
		Sound.beep();
		try{Thread.sleep(1000);}catch(Exception e){};
		Sound.beep();
		try{Thread.sleep(1000);}catch(Exception e){};
		Sound.beep();
		try{Thread.sleep(1000);}catch(Exception e){};
		config.setDriveComplete();
		System.exit(0);

	}
	
	/**
	 * second sets of waypoints 
	 */
	private static void addWaypointSet2() {
		wayPoints.push(new Coordinate(80, 180, 0));
		wayPoints.push(new Coordinate(60, 165, 0));
		wayPoints.push(new Coordinate(0, 165, 0));
		wayPoints.push(new Coordinate(15, 120, 0));
		wayPoints.push(new Coordinate(45, 135, 0));
		wayPoints.push(new Coordinate(30, 120, 0));
		wayPoints.push(new Coordinate(30, 60, 0));
	}
	
	/**
	 * Actions to do when at way point :
	 * Scan for block or foam left and right 
	 */
	private static void scanAtWayPoint() {
		if (!navigationInterrupted)	driver.rotateToRelatively(55);
		if (!navigationInterrupted) driver.rotateToRelatively(-110);
	}
	
	/**
	 * moves the robot to the item, checks if the item is 
	 * a block 
	 * 			-(true)->
	 *  			backUp and goto NextWayPoint.
	 * <br> 	-(false)-> a foam 
	 * 				grab and move to destination 				
	 * 
	 * @param usp ultrasonic puller 
	 */
	public static void detection(UltrasonicPoller usp) {
		navigationInterrupted = true ;
		
		driver.motorStop();
		
		if (goToItemAndCheck(usp)){
			cs.setFloodlight(false);
			grab();
			driver.motorStop();
			pushToCorner();	
		}
		else {
			driver.motorStop();
			cs.setFloodlight(false);
			backUp();
			goToNextWayPoint();
		}
		navigationInterrupted = false ;
	}
	
	/**
	 * clears the wayPoint stack 
	 */
	public static void clearStack(){
		while (!wayPoints.empty()){
			wayPoints.pop();
		}
	}
	
	/**
	 * goes to the item and stop infront of it. beep and stop motor 
	 * then execute isStyrofoam()
	 * if it is a foam or brick
	 * @return true if is froam
	 */
	private static boolean goToItemAndCheck(UltrasonicPoller usp) {
		boolean isFoam = false ;
		//go to item 		
		driver.forward(11);
		driver.motorStop();
		RConsole.println("motor is stoped");
		try{Thread.sleep(1000);} catch (Exception e){};
		RConsole.println("check for foam");
		driver.motorStop();
		isFoam = isStyrofoam();
		return isFoam;
	}
	/**
	 * back up when necessary 
	 */
	private static void backUp(){
		driver.backward(20);
	}
	/**
	 * when facing a wall push extra waypoints to avoid the block 
	 */
	private static void goToNextWayPoint() {
		
		//if the next waypoint is on a block (42 cm near) then remove this waypoint and move to the next 
		int tolerance = 42; 
		if (new Coordinate(Odometer.getInstance().getX(),Odometer.getInstance().getY(),0)
							.isNear(Lab5P2.getWayPoints().peek(),tolerance)){
			Lab5P2.getWayPoints().pop();
		}
		
		int shift = 0 ;
		if (odo.getX() > 45 ) {
			shift = -30;
			Sound.beep();
			Sound.beep();
			
		}
		else {
			shift = 30;
			Sound.beep();
		}
		wayPoints.push(new Coordinate(odo.getX() + shift, odo.getY() + Math.abs(shift) , 0 ) );
		wayPoints.push(new Coordinate(odo.getX() + shift, odo.getY(), 0 ) );
		//re-enable the ultrasonic listeners 
		UltrasonicPoller.enableULinsteners();
		navigationInterrupted = false ;
	}
	/**
	 * action  needed to grab the block 
	 */
	private static void grab() {
		driver.motorStop();
		try {Thread.sleep(200);} catch(Exception e){};
		//close light 
		cs.setFloodlight(false);
		ArmMotor.close();
	}

	/**
	 * return true if this is a styrofoam 
	 * @return
	 */
	private static boolean isStyrofoam() {
		boolean isStyrofoam = false ;
		boolean found = false ;
		
		driver.motorStop();
		
		cs.setFloodlight(true);
		try{Thread.sleep(500);}catch(Exception e){};
		Color c = cs.getColor();

		long time = System.currentTimeMillis();
		while (!found){
			int r = c.getRed();
			int g = c.getGreen();
			int b = c.getBlue();
			
			if(r >=5 && b >= 5){
				LCDWriter.getInstance().writeToScreen(r + " " + g + " " + b ,2 );
				if ( r/(double)b > 1.2){
					found = true ;
					isStyrofoam = false ;
					break ;
				} else {
					found = true;
					isStyrofoam = true ;
					break;
				}
			} 
			else { //if can't find something for 4s break 
				LCDWriter.getInstance().writeToScreen("nothing found", 2);
				if (System.currentTimeMillis() - time > 4000 ) {
					isStyrofoam = false;
					Sound.beepSequenceUp();
					break ;
				}
			}
		}

		return isStyrofoam ;
		
	}
	
	/**
	 * push to the corner of the field 
	 */
	private static void pushToCorner() {
			clearStack();
			wayPoints.push(new Coordinate (80,180,0));
	}

	/**
	 * localization code: falling edge 
	 */
	private static void localize(){
		
		Driver.setSpeed(config.getRotationSpeed());
		UltrasonicPoller upoller = UltrasonicPoller.getInstance();
		
		Localize loc = new Localize(30, true);
		upoller.subscribe(loc);
		
		//start rotating 
		driver.rotateToRelatively(360);
		//localization process is called when the uspoller have certain value
		while (!loc.isDone()) {/* wait for localization to be finished */
			try{Thread.sleep(1000);}catch(Exception e){}}		
		//clean up and unsbuscribe from uspoller 
		LCDWriter.getInstance().writeToScreen(upoller.unsubscribe(loc)? "unsubscribed" : "error ", 1);;
		
	}
	/**
	 * returns the waypoint stack 
	 * @return
	 */
	public static Stack<Coordinate> getWayPoints() {
		return wayPoints;
	}
}

