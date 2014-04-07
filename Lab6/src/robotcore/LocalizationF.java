package robotcore;

import movement.Driver;
import odometry.Odometer;
import odometry.OdometerCorrection;
import sensors.LineReader;
import sensors.UltrasonicPoller;

public class LocalizationF {
	//used threads and initialized stuff 
			private Driver driver = Driver.getInstance();
			private Odometer odo = Odometer.getInstance();
			private UltrasonicPoller usp = UltrasonicPoller.getInstance();
			private Configuration conf = Configuration.getInstance();
			private OdometerCorrection odoC = OdometerCorrection.getInstance();
			//private static LocalizationIII localization = new LocalizationIII();		//for subscribing to lin readers 
			
			private boolean flag = true;
			private final double offsetForBigger = 67;
			private final static double offsetForSmaller = 38;
			
			private final int LOW_LEFT = 0; 
			private final int LOW_RIGHT = 1; 
			private final int UP_LEFT= 2; 
			private final int UP_RIGHT = 3;
			
			/*public static void main(String[] args)
			{
				//RConsole.open();
				init();
				scan();
				move();
			}*/
			
			/*private void init()
			{
				LCDWriter lcd = LCDWriter.getInstance();
				lcd.start();
				odo.start();
				usp.start();
				LineReader.getLeftSensor().start();
				LineReader.getRightSensor().start();
				//LineReader.getLeftSensor().start();
				//LineReader.getRightSensor().start();
			}*/
			
			public void callback()
			{
				scan();
				move();
			}
			
			private void start(int startCorner)
			{
				switch(startCorner)
				{
					case LOW_LEFT:
						conf.getCurrentLocation().setTheta(Math.PI/2).setX(-15).setY(-15);
						break;
					case LOW_RIGHT:
						conf.getCurrentLocation().setTheta(0).setX(315).setY(-15);
						break;
					case UP_LEFT:
						conf.getCurrentLocation().setTheta(Math.PI).setX(-15).setY(315);
						break;
					case UP_RIGHT :
						conf.getCurrentLocation().setTheta(3*Math.PI/2).setX(315).setY(315);
						break;
				}
			}
			
			private void scan()
			{
				//start(0);
				double angle;
				if(usp.getFilteredDistance()>50)
				{
					driver.rotateToRelatively(360, true);
					while(flag)
					{
						if(usp.getFilteredDistance()<50)
						{
							driver.motorStop();
							driver.turnTo(-offsetForBigger);
							flag = false;
						}
					}	
				}
				else
				{
					driver.rotateToRelatively(-360, true);
					while(flag)
					{
						if(usp.getFilteredDistance()>=50)
						{
							driver.motorStop();
							driver.turnTo(-offsetForSmaller);
							flag = false;
						}
					}
				}
				start(3);
				//driver.motorStop();
			}
			
			public void move()
			{
				//LineReader.subscribeToAll(odoC);
				driver.forward(28);
				driver.turnTo(-90);
				driver.forward(28);
				/*while(true)
				{
					driver.motorStop();
				}*/
			}
}
