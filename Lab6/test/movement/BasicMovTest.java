package movement;

import odometry.Odometer;
import robotcore.Configuration;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

/**
 * Tests the basic movement and config parameters (wheel width/radius)
 * Try different values using this test for WHEEL_RADIUS and WIDTH
 * Can also use this as a base for odometry/correction
 * 
 * The robot should be able to complete two laps with less than 3 cm difference 
 * from the origin and diverge less than 15 deg from the starting angle
 * (without the odometery correction). 
 * 
 * With odo correction : 
 * robot should maintain less than 3 cm difference from origin and less than 15 deg 
 * divergence after <b> 5 runs </b> 
 * 
 */
public class BasicMovTest {
	private static final double WHEEL_RADIUS = Configuration.LEFT_RADIUS;
	private static final double WIDTH = 22.4;	//Also width between wheels
		
	/** 
	 * Move in a square and try to fix the config params until they're just right
	 * @param args
	 */
	public static void main(String[] args){
		
		LCD.drawString("Waiting for button press.", 0, 0);
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);

		// spawn a new Thread to avoid SquareDriver.drive() from blocking
		(new Thread() {
			public void run() {
				SquareDriver.drive(Motor.A, Motor.B, WHEEL_RADIUS, WHEEL_RADIUS, WIDTH);
			}
		}).start();
		
		
		while (Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
}
