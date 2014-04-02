package movement;

import lejos.nxt.Button;
import movement.Driver;
import odometry.Odometer;
import robotcore.Configuration;
import robotcore.LCDWriter;


public class MoveWithOdo {
	
	private static Odometer odo = Odometer.getInstance();
	private static Driver driver = Driver.getInstance();
	private static LCDWriter lcd = LCDWriter.getInstance();
	private static Configuration conf = Configuration.getInstance();
	
	public static void main(String[] args) {
		startThreads();
		move();
		
		while(Button.waitForAnyPress() != Button.ID_ENTER){
			lcd.writeToScreen("x"+ conf.getCurrentLocation().getX(), 0);
			lcd.writeToScreen("y"+ conf.getCurrentLocation().getY(), 1);
			lcd.writeToScreen("t"+ conf.getCurrentLocation().getTheta(), 2);
		}
	}
	/**
	 * move 
	 */
	private static void move() {
		driver.rotateToRelatively(-30);
		
	}

	public static void startThreads() {
		odo.start();
		lcd.start();
		
		
	}
}
