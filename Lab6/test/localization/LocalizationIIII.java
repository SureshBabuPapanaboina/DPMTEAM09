package localization;


import movement.Driver;
import odometry.Odometer;
import odometry.OdometerCorrection;
import robotcore.Configuration;
import robotcore.LCDWriter;
import sensors.LineReader;
import sensors.UltrasonicPoller;

public class LocalizationIIII{
		//used threads and initialized stuff 
		private static Driver driver = Driver.getInstance();
		private static Odometer odo = Odometer.getInstance();
		private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
		private static Configuration conf = Configuration.getInstance();
		private static OdometerCorrection odoC = OdometerCorrection.getInstance();
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
			move();
		}
		
		private static void init()
		{
			LCDWriter lcd = LCDWriter.getInstance();
			lcd.start();
			odo.start();
			usp.start();
			LineReader.getLeftSensor().start();
			LineReader.getRightSensor().start();
			//LineReader.getLeftSensor().start();
			//LineReader.getRightSensor().start();
		}
		
		private static void start(int startCorner)
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
		/*
		private static void startAngle(int startCorner)
		{
			switch(startCorner)
			{
				case LOW_LEFT:
					conf.getCurrentLocation().setTheta(90);
					break;
				case LOW_RIGHT:
					conf.getCurrentLocation().setTheta(0);
					break;
				case UP_LEFT:
					conf.getCurrentLocation().setTheta(180);
					break;
				case UP_RIGHT :
					conf.getCurrentLocation().setTheta(270);
					break;
			}
		}*/
		
		private static void scan()
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
			start(0);
			//driver.motorStop();
		}
		
		public static void move()
		{
			//LineReader.subscribeToAll(odoC);
			driver.forward(28);
			driver.turnTo(-90);
			driver.forward(28);
			while(true)
			{
				driver.motorStop();
			}
		}
}
