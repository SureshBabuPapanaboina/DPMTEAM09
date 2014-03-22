package movement;
import lejos.nxt.Button;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import movement.Driver;
import odometry.Odometer;
import robotcore.Configuration;
import robotcore.LCDWriter;
import sensors.LineReader;

/**
 * This test is to make sure both the line reading color sensors works 
 * correctly and returns true when a line is crosses. 
 * 
 * <b> Details of implementation </b>
 * the robot moves forward 30 cm and stop the corresponding motor
 * (left / right) when the corresponding line reader sees the line (left / right) 
 * @author yuechuan
 *
 */
public class SquareDriver {

	private static Odometer odo = Odometer.getInstance();
	private static Driver driver = Driver.getInstance();
	private static LCDWriter lcd = LCDWriter.getInstance();
	private static Configuration conf = Configuration.getInstance();
	private static LineReader llr = LineReader.getLeftSensor();	//left + right line reader
	private static LineReader rlr = LineReader.getRightSensor();
	private static int lnCounterLeft = 0 ;
	private static int lnCounterRight = 0 ;
	
	public static void main(String[] args) {
		startThreads();
		conf.getCurrentLocation().setTheta(0).setX(0).setY(0);
		
		move();
	

		conf.setDriveComplete();
		Button.waitForAnyPress();
		System.exit(0);
		
	}
	
	private static void move(){
		int k = 3*30;
		driver.travelTo(k, 0);
		driver.travelTo(k, k);
		driver.travelTo(0, k);
		driver.travelTo(0, 0);
		
	}
	private static void startThreads(){
		try{odo.start();      } catch (Exception e){}
		try{driver.start();   } catch (Exception e){}
		try{lcd.start();      } catch (Exception e){}
//		try{llr.start();      } catch (Exception e){}
//		try{rlr.start();      } catch (Exception e){}
	}

}
