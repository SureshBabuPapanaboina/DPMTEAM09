/**
 * 
 */
package searcher;

import java.util.ArrayList;

import odometry.Odometer;
import robotcore.Configuration;
import robotcore.LCDWriter;
import search.ObjRec;
import search.ObjRec.blockColor;
import sensors.UltrasonicPoller;
import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import movement.Driver;

/**
 * improved version of the original object detector using similar approach
 * @author ychen225
 *
 */
public class ObjectDetector {
	
	public static final int CUTOFF = 40;
	/**
	 * saves the location parameters of potential foam
	 * @author ychen225
	 *
	 */
	public static class ItemLocation{
		/**
		 * indicate foam detected
		 */
		boolean hasItem = false ;
		/**
		 * left edge angle 
		 */
		int leftEdgeAngle = -1 ;
		/**
		 * right edge angle
		 */
		int rightEdgeAngle = -1;
		/**
		 * distance from the center of the block
		 */
		int distance = 225 ;

	}
	private static NXTRegulatedMotor sm = Configuration.SENSOR_MOTOR;
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	private static Driver driver = Driver.getInstance();
	private static LCDWriter lcd = LCDWriter.getInstance();
	private static ColorSensor cs = new ColorSensor(Configuration.COLOR_SENSOR_PORT);

	static int [] dist = new int [4];	//stores the past 5 distance mesaures	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		init();
		//take initial readings 
		for (int i = 0 ; i < dist.length ; i++){
			dist[i] = usp.getChippedDistance(CUTOFF);
			nap(50);
		}

		ItemLocation item = null ;

		do {
			//scan 
			do{
				item = scanForItem();
				nap(100);
			} while(!(item.hasItem));
			//print info 
			lcd.writeToScreen("lA " + item.leftEdgeAngle , 1 );
			lcd.writeToScreen("rA " + item.rightEdgeAngle, 0 );
			lcd.writeToScreen("ctr " + (item.leftEdgeAngle + item.rightEdgeAngle )/2, 2 );
			lcd.writeToScreen("dist " + item.distance, 3 );

			if (usp.getDistance() < 9 ) break ;
			//drive towards the foam
			driver.rotateToRelatively(-((item.leftEdgeAngle + item.rightEdgeAngle )/2-5));
			driver.forward(4);

		}while (usp.getDistance() > 9 );
		ObjRec oRec = new ObjRec();
		ArrayList <blockColor> bc = oRec.detect();
		String ans = "";
		for (blockColor b : bc){
			ans += b.toString() ;
		}
		lcd.writeToScreen(ans, 6);		
	}
	
	public static int lookForItemII(int colorID){
		//take initial readings
		ObjRec oRec = new ObjRec();
		sm.setSpeed(40);

		sm.rotateTo(70, false);
		sm.rotateTo(-65, true);
		
		while(sm.getPosition() >-65 ){
			nap(10);
			if(usp.getFilteredDistance() < 7){
				//found object
				sm.stop();
				
				//to align the light sensor
				sm.rotateTo(sm.getPosition() - 20);
				ArrayList <blockColor> bc = oRec.detect();
				String ans = "";
				for (blockColor b : bc){
					ans += b.toString() ;
				}
				lcd.writeToScreen(ans, 6);	
				if(bc.contains(blockColor.getInstance(colorID))){
					//face the robot
					driver.backward(3);
					driver.turnTo(Odometer.getInstance().getTheta() - (sm.getPosition())-10);
					return 1;
//				}
//				else if(bc.size() < 1 || bc.contains(blockColor.BLOCK)){
//					return -1;
				}else{
					driver.backward(3);
					driver.turnTo(Odometer.getInstance().getTheta() - sm.getPosition() - 10);
					return 0;
				}
			}
			if(usp.getFilteredDistance() < 18){
				sm.stop();
				
				driver.turnTo(Odometer.getInstance().getTheta() - sm.getPosition());
				driver.forward(5);
				
				sm.rotateTo(70, false);
				sm.rotateTo(-65, true);
			}
		}
		
		return -1;

	}


	public static int lookForItem(int colorID){
		//take initial readings 
		for (int i = 0 ; i < dist.length ; i++){
			dist[i] = usp.getChippedDistance(CUTOFF);
			nap(50);
		}

		ItemLocation item = null ;

		do {
			//scan 
			
			//Initial scan for anything too close 
			sm.rotateTo(70,false);
			sm.rotateTo(-65, true);
			boolean found = false;
			int smAngle = 0;
			while(sm.getPosition() > -65){
				if(usp.getFilteredDistance() < 9){
					sm.stop();
					found = true;
					smAngle = sm.getPosition();
					break;
				}
			}
			
			if(!found){
				item = scanForItem();
				nap(100);
				if(!item.hasItem) return -1;
				//print info
	
				if (usp.getDistance() < 9 ) break ;
				//drive towards the foam
				driver.rotateToRelatively(-((item.leftEdgeAngle + item.rightEdgeAngle )/2-5));
				driver.forward(4);
			}
			else{
				sm.rotateTo(smAngle - 15);
				break;
			}

		}while (usp.getDistance() > 9 );
		ObjRec oRec = new ObjRec();
		ArrayList <blockColor> bc = oRec.detect();
		String ans = "";
		for (blockColor b : bc){
			ans += b.toString() ;
		}
		lcd.writeToScreen(ans, 6);	
		if(bc.contains(blockColor.getInstance(colorID))){
			//face the robot
			driver.turnTo(Odometer.getInstance().getTheta() - sm.getPosition());
			return 1;
		}
		else if(bc.size() < 1 || bc.contains(blockColor.NO_BLOCK) || bc.contains(blockColor.BLOCK))
			return -1;
		else{
			driver.turnTo(Odometer.getInstance().getTheta() - sm.getPosition());
			return 0;
		}

	}

	private static void init() {
		usp.start();
		lcd.start();
	}

	static void nap(int t){
		try {Thread.sleep(t);} catch (InterruptedException e) {	}
	}

	/**
	 * return an {@link ItemLocation} object, the hasItem in this object is set to true when there has an suspicious foam
	 * @return 
	 */
	private static ItemLocation scanForItem() {
		ItemLocation itm = new ItemLocation();
		sm.setSpeed(45);
		sm.rotateTo(70,false);
		resample(dist);
		nap(50);
//		cs.setFloodlight(true);
		sm.rotateTo(-65, true);
		int detection = 0 ;
		int ang ;
		long enterTime = System.currentTimeMillis();
		while(detection < 2 ){
			if (peekDetection()){
				ang = sm.getTachoCount();
				if (detection == 0){		//first edge 
					itm.rightEdgeAngle = ang;
					lcd.writeToScreen("rA " + itm.rightEdgeAngle, 0 );
					resample(dist);
				}
				else if (detection == 1){ 	//second edge
					itm.leftEdgeAngle = ang;
					lcd.writeToScreen("lA " + itm.rightEdgeAngle, 1 );
					itm.hasItem = true ; 
					sm.rotateTo(0);		//rotate back to center 
				}
				detection ++;
			}
//			
//			if(usp.getFilteredDistance() < 10){
//				sm.stop();
//				itm.hasItem = true;
//				itm.rightEdgeAngle = sm.getPosition()-7;
//				itm.leftEdgeAngle = sm.getPosition()-7;
//				break;
//			}
//			//TODO: test this and remove if not
//			if(cs.getColor().getRed() > 40){
//				//probably a block
//				sm.stop(true);
//				itm.hasItem = true;
//				itm.rightEdgeAngle = sm.getPosition(); //+5 to compensate for later minus 10
//				itm.leftEdgeAngle = sm.getPosition();
//				break;
//			}
//			
			if (System.currentTimeMillis() - enterTime > 3500)	{
				Sound.beep();
				itm.hasItem = false ;
				break;	
			}	//break out loop if havent got a good reading in 15 sec 

		}
		//with the two angles rotate to center 	
		if (itm.hasItem) sm.rotateTo((itm.rightEdgeAngle + itm.leftEdgeAngle)/2 - 10);  //-10 because the Usensor is off to the right 
		else sm.rotateTo(0);
		nap(100);
		itm.distance = usp.getDistance();
//		cs.setFloodlight(false);

		return itm ;
	}
	/**
	 * detect the peek in the edge
	 */
	private static boolean peekDetection() {
		int MAX_ARR_SIZE = dist.length -1;

		shiftArr(dist);
		dist[ MAX_ARR_SIZE] = usp.getChippedDistance(CUTOFF);

		//if the avg of the first 2 average is different than the last 2 average then return true 
		boolean detected = false ;
		if (Math.abs(dist[2] + dist[3] - dist[0] - dist[1]) > 20){ 	//if the 
			detected = true ;
		}
		return detected;
	}
	/**
	 * resample arr with ultrasonic distance
	 * @param arr
	 */
	private static void resample(int[] arr) {
		for  (int i = 0 ; i < arr.length ; i++){

			arr[i] = usp.getChippedDistance(CUTOFF);
		}
	}

	/**
	 * shift array to the left by 1 
	 * @param arr
	 */
	private static void shiftArr(int [] arr ){
		//slightly faster ... maybe...
		for (int i = 0 ; i < arr.length-1 ; i ++){
			arr[i] = arr[i] ^ arr[i+1] ;
			arr[i+1] = arr[i] ^ arr[i+1] ;
			arr[i] = arr[i] ^ arr[i+1] ;
		}
	}
}
