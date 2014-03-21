package capture;

import lejos.nxt.Button;
import robotcore.Configuration;

/**
 * Basic test for a capture mechanism
 *
 */
public class BasicCaptureTest {

	public static void main(String[] args){
		
		while(Button.waitForAnyPress() != Button.ID_LEFT);
		
		CaptureMechanism cm = CaptureMechanism.getInstance();
		Configuration config = Configuration.getInstance();
		
		cm.close();
		
		cm.open();
		config.LEFT_MOTOR.setSpeed(100);
		config.RIGHT_MOTOR.setSpeed(100);
		
		config.LEFT_MOTOR.forward();
		config.RIGHT_MOTOR.forward();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		config.LEFT_MOTOR.stop();
		config.RIGHT_MOTOR.stop();
		
		cm.align();
		
		config.LEFT_MOTOR.setSpeed(200);
		config.RIGHT_MOTOR.setSpeed(200);
		
		config.LEFT_MOTOR.forward();
		config.RIGHT_MOTOR.forward();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		config.LEFT_MOTOR.stop();
		config.RIGHT_MOTOR.stop();
		
		
		cm.close();
		
		
		
	}
}
