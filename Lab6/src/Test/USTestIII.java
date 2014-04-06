/**
 * 
 */
package Test;

import robotcore.LCDWriter;
import sensors.UltrasonicPoller;

/**
 * this is the test file for the ultrasonic sensor and the rotational arm. 
 * goal : find a good way for the robot to find and go to the location of the foam
 * @author yuechuan
 *
 */
public class USTestIII {

	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	private static LCDWriter lcd = LCDWriter.getInstance();
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	static void init(){
		usp.start();
		lcd.start();
	}

}
