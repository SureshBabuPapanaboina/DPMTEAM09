package odometry;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;
import movement.Driver;
import odometry.Odometer;
import odometry.OdometerCorrection;
import robotcore.Configuration;
import robotcore.LCDWriter;
import sensors.LineReader;

public class odoTest {
	private static Odometer odo = Odometer.getInstance();
	private static LCDWriter lcd = LCDWriter.getInstance();
	private static Configuration conf = Configuration.getInstance();
	private static LineReader llr = LineReader.getLeftSensor();	//left + right line reader
	private static LineReader rlr = LineReader.getRightSensor();
	private static OdometerCorrection odoCorrect = OdometerCorrection.getInstance();
	//pushed by Mike
	private static Driver driver = Driver.getInstance();
	
	public static void main(String[] args)
	{
		RConsole.open();
		int buttonChoice;
		do {
			// clear the display
			LCD.clear();

			// ask the user whether the motors should drive in a square or float
			LCD.drawString("< Left | Right >", 0, 0);
			LCD.drawString("       |        ", 0, 1);
			LCD.drawString(" Float | Drive  ", 0, 2);
			LCD.drawString("motors | in a   ", 0, 3);
			LCD.drawString("       | square ", 0, 4);

			buttonChoice = Button.waitForAnyPress();
		} while (buttonChoice != Button.ID_LEFT	
				&& buttonChoice != Button.ID_RIGHT);
	//turn on the floodlight
	//colorSensor.setFloodlight(true);
	
	if (buttonChoice == Button.ID_LEFT) {
		for (NXTRegulatedMotor motor : new NXTRegulatedMotor[] { Motor.A, Motor.B}) {
			motor.forward();
			motor.flt();
		}

		// start only the odometer and the odometry display
		//odometer.start();
		//odometryDisplay.start();
	} else {
		//odometryCorrection = new OdometryCorrection(odometer,colorSensor);
		// start the odometer, the odometry display and (possibly) the
		// odometry correction
		odo.start();
		lcd.start();
		llr.start();
		rlr.start();
		driver.start();
		//odometryCorrection.start();

		//subscribe the odoCorrect to the line readers 
		LineReader.subscribeToAll(odoCorrect);
		
		// spawn a new Thread to avoid SquareDriver.drive() from blocking
		(new Thread() {
			public void run() {
				int k = 3* 30;	//distance to move 
				conf.getCurrentLocation().setTheta(0).setX(0).setY(0);
				driver.travelTo(k, 0);
				driver.travelTo(k, k);
				driver.travelTo(0, k);
				driver.travelTo(0, 0);
				//set drive to be complete !! this will finalize the machine (shuts off linereaders and odo )
				Configuration.getInstance().setDriveComplete();
				// do not use the below I don't think it uses the drivers correctly 
//				SquareDriver.drive(Motor.A, Motor.B, 2.090, 2.090, 22.5);//2.1,2.101,15.75
			}
		}).start();
		
	}
	
	while (Button.waitForAnyPress() != Button.ID_ESCAPE);
	System.exit(0);
	}
}
