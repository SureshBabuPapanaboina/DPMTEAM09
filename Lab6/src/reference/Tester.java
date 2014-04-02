package reference;

import java.util.Stack;

import odometry.Odometer;
import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import sensors.UltrasonicPoller;
import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;
import movement.Driver;

/**
 * test class used to calibrate the odometer and the wheels.
 * Sample code to demonstrate navigation.
 * @author yuechuan
 *	@version 1.3
 */
public class Tester {
	
	public static void main(String[] args) {
		
		LCDWriter lcd = LCDWriter.getInstance();
		Odometer odo = Odometer.getInstance();
		Driver driver = Driver.getInstance();
		UltrasonicPoller usp = UltrasonicPoller.getInstance();
		RConsole.openAny(5000);
				
		lcd.start();
		odo.start();
		usp.start();
		
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		lcd.writeToScreen("started", 0);

		//square drive 
		driver.travelTo(90,0);
		driver.travelTo(90,90);
		driver.travelTo(0,90);
		driver.travelTo(0,0);

		lcd.writeToScreen("done", 6);
		
	}


}
