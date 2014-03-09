package other;

import java.util.Stack;

import lejos.nxt.Button;
import lejos.nxt.comm.RConsole;
import coreLib.Configuration;
import coreLib.ArmMotor;
import coreLib.Configuration;
import coreLib.Coordinate;
import coreLib.Driver;
import coreLib.LCDWriter;
import coreLib.Odometer;
import coreLib.UltrasonicPoller;

/**
 * test class used to calibrate the odometer and the wheels 
 * @author yuechuan
 *	@version 1.3
 */
public class Tester {
	private static Configuration config = Configuration.getInstance();
	
	public static void main(String[] args) {
		//has to be placed before driver initialization 
//		config.setCurrentLocation(new 
//		Coordinate(30, 30, 0));
		
		LCDWriter lcd = LCDWriter.getInstance();
		Odometer odo = Odometer.getInstance();
		UltrasonicPoller usp = UltrasonicPoller.getInstance();
		Driver driver = Driver.getInstance();
		RConsole.openAny(5000);
				
		lcd.start();
		odo.start();
		usp.start();
		driver.start();
		
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		lcd.writeToScreen("started", 0);
		
		
		
//		Stack<Coordinate> st = new Stack<Coordinate>();
//		st.push(new Coordinate(0, 0, 0));
//		st.push(new Coordinate(30, 0, 0));
//		st.push(new Coordinate(60, 90, 0));
//		st.push(new Coordinate(0, 60, 0));
		
		driver.travelTo(60,0);
		driver.travelTo(60,60);
		driver.travelTo(0,60);
		driver.travelTo(0,0);
//		driver.travelTo(30,60);
//		driver.travelTo(0,60);
		

//		while (!st.empty()){
//			RConsole.println("current :" + config.getCurrentLocation().toString() + "\t\tdestination : "  + st.peek().toString() );
//			lcd.writeToScreen(odo.getX()+"", 1);
//			lcd.writeToScreen(odo.getY()+"", 2);
//			lcd.writeToScreen(odo.getTheta()+"", 3);
//			driver.travelTo(st.peek());
//			
//			while(usp.getDistance() > 15 ){}
//			
//			RConsole.println("current :" + config.getCurrentLocation().toString() + "\t\tdestination : "  + st.peek().toString() );
//			st.pop();
//		}
		
		lcd.writeToScreen("done", 6);
		
	}


}
