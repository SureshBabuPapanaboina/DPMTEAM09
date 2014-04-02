package capture;

import communication.RemoteConnection;
import odometry.Odometer;
import lejos.nxt.Button;
import movement.Driver;
import robotcore.Configuration;
import robotcore.LCDWriter;
import sensors.UltrasonicPoller;

/**
 * Basic test for a capture mechanism
 *
 */
public class BasicCaptureTest {

	public static void main(String[] args){
		
		while(Button.waitForAnyPress() != Button.ID_LEFT);
		
		CaptureMechanism cm = CaptureMechanism.getInstance();
		UltrasonicPoller up = UltrasonicPoller.getInstance();
		LCDWriter writer = LCDWriter.getInstance();
		writer.start();
		Driver driver = Driver.getInstance();
		up.start();
		Odometer.getInstance().start();
//		Configuration config = Configuration.getInstance();
		
//		cm.close();
		RemoteConnection rc = RemoteConnection.getInstance();

		rc.setupConnection();

		writer.writeToScreen("Connection passed.", 0);
		int d = up.getDistance();
		
		cm.open();

		driver.forward(Math.abs(d - 5));
		
		cm.align();
		
		driver.forward(15);
		
		cm.close();
		
		
		
	}
}
