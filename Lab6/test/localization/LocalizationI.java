package localization;


import odometry.Odometer;
import robotcore.Configuration;
import robotcore.LCDWriter;
import sensors.LineReader;
import sensors.LineReaderListener;
import sensors.UltrasonicPoller;
import movement.Driver;

/**
 * Does the localization, assuming all used threads are started.
 * @author yuechuan
 *
 */
public class LocalizationI implements LineReaderListener{
	
	//used threads and initialized stuff 
	private static Driver driver = Driver.getInstance();
	private static Odometer odo = Odometer.getInstance();
	private static LCDWriter lcd = LCDWriter.getInstance();
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	private static Configuration conf = Configuration.getInstance();
				
	private static final boolean DEBUG = Configuration.DEBUG;
	private static double angle1 , angle2 ;
	static boolean secondAngle = false ;	// if this is called a second time then turn back 

	
	public static void main(String[] args) {

//		startThread();
		conf = Configuration.getInstance();
		driver = Driver.getInstance();
		 odo = Odometer.getInstance();
		lcd = LCDWriter.getInstance();
		 usp = UltrasonicPoller.getInstance();
		
		try{odo.start();       }catch(Exception e){}
		try{lcd.start();       }catch(Exception e){}
		try{usp.start();       }catch(Exception e){}
		
		localize();
		
	}
	
	/**
	 * 
	 */
	public static void localizeAndMoveToStartLoc() {
		int rotSpeed = conf.getRotationSpeed();
		conf.setRotationSpeed(120);
		localize();
		conf.setRotationSpeed(rotSpeed);
		
		LocalizationI loc = new LocalizationI();
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
	 * thread and things that need to be prepared 
	 * @deprecated thread should be started at the main thread
	 */
	private static void startThread() {
		try{odo.start();       }catch(Exception e){}
		try{lcd.start();       }catch(Exception e){}
		try{usp.start();       }catch(Exception e){}
		
		try {Thread.sleep(300);} catch(Exception e) {}
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
		
		//forward rotation 
		driver.rotateToRelatively(360, true);
		int DistOnInvok = 50;
		while (usp.getFilteredDistance() > DistOnInvok){try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}} 
		driver.motorStop();
		angle1 = conf.getCurrentLocation().getTheta();
		if (DEBUG) LCDWriter.getInstance().writeToScreen("ang1 " + Math.toDegrees(angle1) , 6);
		
		//start back rotation
		driver.rotateToRelatively(-360, true);
		try {Thread.sleep(1300);} catch ( Exception e ){}; 		//make it sleep a little to avoid miss read 
		
		while (usp.getFilteredDistance() > DistOnInvok){try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}} 
		
		driver.motorStop();
		
		angle2 = conf.getCurrentLocation().getTheta();
		if (DEBUG) LCDWriter.getInstance().writeToScreen("ang2 " + Math.toDegrees(angle2) , 5);

		//find the angle from origin 
		double angleFromOrigin = ((angle1 + angle2)/2)- angle2 ;
		LCDWriter.getInstance().writeToScreen("AFO " +Math.toDegrees(angleFromOrigin) , 0);
		driver.rotateToRelatively(Math.toDegrees(angleFromOrigin));
		
		driver.rotateToRelatively(45);
		
		odo.setTheta(Math.toRadians(90));
		
	}
	/**
	 * make sure the robot is facing outward 
	 */
	private static void prepareForFallingEdge() {
		driver.rotateToRelatively(360, true);

		while(usp.getFilteredDistance() < 50){
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		driver.motorStop();

	}

	private static int lnNumb = 0 ;
	@Override
	public void passedLine(boolean isLeft) {
		if (!isLeft){
			if (lnNumb++  == 0 ){ //first line : correct X 
				conf.getCurrentLocation().setX(Configuration.DIST_FROM_LINE_READER);
			}
			if (false){//lnNumb == 1 ){
				driver.motorStop();
				conf.getCurrentLocation().setY(Configuration.DIST_FROM_LINE_READER);
				driver.forward(4);
			}
		}
		
	}


}
