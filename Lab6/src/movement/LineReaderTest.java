package movement;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import movement.Driver;
import odometry.Odometer;
import robotcore.Configuration;
import robotcore.LCDWriter;
import sensors.LineReader;


public class LineReaderTest {

	private static Odometer odo = Odometer.getInstance();
	private static Driver driver = Driver.getInstance();
	private static LCDWriter lcd = LCDWriter.getInstance();
	private static Configuration conf = Configuration.getInstance();
	private static LineReader llr = LineReader.getLeftSensor();	//left + right line reader
	private static LineReader rlr = LineReader.getRightSensor();
	
	public static void main(String[] args) {
		startThreads();
		try{Thread.sleep(500);}catch(Exception e){}
		driver.setSpeed(300); //300deg rot per sec 
		move();
		
	}
	
	private static void move(){
		NXTRegulatedMotor l = Configuration.LEFT_MOTOR;
		NXTRegulatedMotor r = Configuration.RIGHT_MOTOR;
		
		conf.getCurrentLocation().setTheta(0).setX(0).setY(0);
		driver.travelTo(30, 0);
		boolean left = false,right = false;
		while (true){
			if (llr.isPassedLine()){
				left = true ;
				l.stop();
				Sound.beep();
				if (left && right) break;
				
			}
			if (rlr.isPassedLine()){
				right = true ;
				r.stop();
				Sound.twoBeeps();
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
