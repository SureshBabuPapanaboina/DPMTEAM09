package robotcore;

import lejos.nxt.comm.RConsole;
import movement.Driver;
import odometry.Odometer;
import sensors.LineReader;
import sensors.LineReaderListener;
import sensors.UltrasonicPoller;

public class LocalizationIIII{
		//used threads and initialized stuff 
		private static Driver driver = Driver.getInstance();
		private static Odometer odo = Odometer.getInstance();
		private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
		private static Configuration conf = Configuration.getInstance();
		//private static LocalizationIII localization = new LocalizationIII();		//for subscribing to lin readers 
		
		private int distance1,distance2;
		private long currentTime,priviousTime;
		private static boolean flag = true;
		private final static double offsetForBigger = 67;
		private final static double offsetForSmaller = 38;
		
		private final static int LOW_LEFT = 0; 
		private final static int LOW_RIGHT = 1; 
		private final static int UP_LEFT= 2; 
		private final static int UP_RIGHT = 3;
		
		public static void main(String[] args)
		{
			//RConsole.open();
			init();
			scan();
		}
		
		private static void init()
		{
			LCDWriter lcd = LCDWriter.getInstance();
			lcd.start();
			odo.start();
			usp.start();
			//LineReader.getLeftSensor().start();
			//LineReader.getRightSensor().start();
		}
		
		private static void start(int startCorner)
		{
			switch(startCorner)
			{
				case LOW_LEFT:
					conf.getCurrentLocation().setTheta(0).setX(15).setY(15);
					break;
				case LOW_RIGHT:
					conf.getCurrentLocation().setTheta(270).setX(285).setY(15);
					break;
				case UP_LEFT:
					conf.getCurrentLocation().setTheta(90).setX(15).setY(285);
					break;
				case UP_RIGHT :
					conf.getCurrentLocation().setTheta(180).setX(285).setY(285);
					break;
			}
		}
		
		private static void scan()
		{
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
			conf.getCurrentLocation().setTheta(Math.toRadians(90)).setX(0).setY(0);
			//start();
			driver.motorStop();
		}
}
