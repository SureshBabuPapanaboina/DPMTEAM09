package Test;

import bluetoothclient.BluetoothReceiver;
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
import robotcore.Coordinate;
import robotcore.LCDWriter;
import sensors.LineReader;

public class blueTTst {
	private static Configuration conf= Configuration.getInstance();
	private static Coordinate[] flagZone;
	private static BluetoothReceiver receiver= new BluetoothReceiver();
	private static Driver driver = Driver.getInstance();
	
	public static void main(String[] args)
	{	
		
//		init();
//		try {
//		Thread.sleep(500);
//	} catch (InterruptedException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}
//	
		receiver.listenForStartCommand();
//		flagZone=conf.getFlagZone();
//		driver .travelTo(flagZone[0]);
		
	
	while (Button.waitForAnyPress() != Button.ID_ESCAPE);
	System.exit(0);
	}

	/**
	 * 
	 */
	private static void init() {
		LCDWriter.getInstance().start();
	}
}
