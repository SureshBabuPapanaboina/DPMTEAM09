package integration;

import lejos.nxt.Button;
import localization.Localization;
import movement.Driver;
import odometry.Odometer;
import odometry.OdometerCorrection;
import robotcore.Configuration;
import robotcore.LCDWriter;
import sensors.LineReader;
import sensors.UltrasonicPoller;

public class LocAndOdoCorr {
	
	private static Odometer odo = Odometer.getInstance();
	private static Driver driver = Driver.getInstance();
	private static OdometerCorrection odoCorr = OdometerCorrection.getInstance();
	private static LineReader llr = LineReader.getLeftSensor(), rlr = LineReader.getRightSensor();
	private static LCDWriter lcd = LCDWriter.getInstance();
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	private static Configuration conf = Configuration.getInstance();
	
	public static void main(String[] args) {
		init();	
		lcd.writeToScreen("Enter!" , 0);
		driver.setSpeed(150);
		Localization.localizeAndMoveToStartLoc();
		nap(200);
		LineReader.subscribeToAll(odoCorr);	
		LineReader.unpauseAll();
		nap(200);
		driver.travelTo(45,15);
		driver.travelTo(75,15);
		driver.travelTo(75,45);

	}
	
	private static void nap(int i) {
		try{Thread.sleep(i);}catch(Exception e ){};
		
	}

	private static void init(){
		try{odo.start();          }catch(Exception e){};
		try{llr.start();          }catch(Exception e){};
		try{rlr.start();          }catch(Exception e){};
		try{lcd.start();          }catch(Exception e){};
		try{usp.start();          }catch(Exception e){};
		
	}

}
