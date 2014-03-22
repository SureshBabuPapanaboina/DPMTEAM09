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
public class LineReaderTest {

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
		try{Thread.sleep(500);}catch(Exception e){}
		Sound.beep();
		driver.setSpeed(250); //300deg rot per sec 

		move();
		try{Thread.sleep(100);}catch(Exception e){}
		move();          
		try{Thread.sleep(100);}catch(Exception e){}
		move();         
		try{Thread.sleep(100);}catch(Exception e){}
	
		
		lcd.writeToScreen("done", 0);
		lcd.writeToScreen(conf.getCurrentLocation().toString(), 0);
		
		conf.setDriveComplete();
		Button.waitForAnyPress();
		System.exit(0);
		
	}
	
	private static void move(){
		NXTRegulatedMotor l = Configuration.LEFT_MOTOR;
		NXTRegulatedMotor r = Configuration.RIGHT_MOTOR;
		l.forward();
		r.forward();
		boolean left = false,right = false;
		while (true){

			lcd.writeToScreen("l:" + left, 1);
			lcd.writeToScreen("r:" + right,2);
			lcd.writeToScreen(lnCounterLeft+"", 3);
			lcd.writeToScreen(lnCounterRight+"", 4);
			if (llr.isPassedLine()){
				lnCounterLeft++;
				left = true ;
				l.stop();
//				lcd.writeToScreen("l:" + left, 1);
//				lcd.writeToScreen(lnCounterLeft+"", 3);
//				lcd.writeToScreen(lnCounterRight+"", 4);
//				Sound.beep();
				if (left && right) break;
				
			}
			if (rlr.isPassedLine()){
				lnCounterRight++;
				right = true ;
				r.stop();
//				Sound.twoBeeps();
//				lcd.writeToScreen("r:" + right,2);
//				lcd.writeToScreen(lnCounterLeft+"", 3);
//				lcd.writeToScreen(lnCounterRight+"", 4);
				if (left && right) break;
			}
		}
		
	}
	private static void startThreads(){
		try{odo.start();      } catch (Exception e){}
		try{driver.start();   } catch (Exception e){}
		try{lcd.start();      } catch (Exception e){}
		try{llr.start();      } catch (Exception e){}
		try{rlr.start();      } catch (Exception e){}
	}

}
