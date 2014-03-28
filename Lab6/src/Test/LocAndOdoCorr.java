package Test;

import lejos.nxt.Button;
import movement.Driver;
import odometry.Odometer;
import odometry.OdometerCorrection;
import robotcore.LCDWriter;
import robotcore.Localization;
import sensors.LineReader;

public class LocAndOdoCorr {
	
	private static Odometer odo = Odometer.getInstance();
	private static Driver driver = Driver.getInstance();
	private static OdometerCorrection odoCorr = OdometerCorrection.getInstance();
	private static LineReader llr = LineReader.getLeftSensor(), rlr = LineReader.getRightSensor();
	private static LCDWriter lcd = LCDWriter.getInstance();
	
	public static void main(String[] args) {
		lcd.writeToScreen("Enter!" , 0);
		while (Button.waitForAnyPress() == Button.ID_ENTER){nap(25);};
		
		Localization.localizeAndMoveToStartLoc();
//		nap (200);
//		init();	//initialize thread if necessary
//		nap(200);
//		LineReader.subscribeToAll(odoCorr);	
//		LineReader.unpauseAll();
//		nap(200);
//		driver.travelTo(45,15);
//		driver.travelTo(75,15);
//		driver.travelTo(75,45);

	}
	
	private static void nap(int i) {
		try{Thread.sleep(i);}catch(Exception e ){};
		
	}

	private static void init(){
		try{odo.start();          }catch(Exception e){};
		try{driver.start();       }catch(Exception e){};
		try{llr.start();          }catch(Exception e){};
		try{rlr.start();          }catch(Exception e){};
		try{lcd.start();          }catch(Exception e){};
		
	}

}
