package robotcore;

import movement.Driver;
import odometry.OdometerCorrection;
import robotcore.Configuration;
import sensors.LineReader;
import sensors.UltrasonicPoller;

/**
 * Class for localizing the robot at the start
 * 
 * @author Bonan
 *
 */
public class Localization {
	//used threads and initialized stuff 
			private Driver driver = Driver.getInstance();
			private UltrasonicPoller usp = UltrasonicPoller.getInstance();
			private Configuration conf = Configuration.getInstance();
			private OdometerCorrection odoC = OdometerCorrection.getInstance();
			
			private boolean flag = true;
			private final double offsetForBigger = 67;
			private final static double offsetForSmaller = 38;
			
			private final int LOW_LEFT = 1; 
			private final int LOW_RIGHT = 2; 
			private final int UP_LEFT= 4; 
			private final int UP_RIGHT = 3;
			
			/**
			 * Runs the localization
			 */
			public void callback()
			{
				scan();
				move();
			}
			
			/**
			 * Sets the starting corner based on the config
			 * @param startCorner
			 */
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
			
			/**
			 * scans for the initial 
			 */
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
				start(conf.getStartCorner());
				//driver.motorStop();
			}
			
			/**
			 * Move to the 1x1 tile, pass the lines and correct the odometry
			 */
			public void move()
			{
				LineReader.subscribeToAll(odoC);

				switch(conf.getStartCorner())
				{
					case LOW_LEFT:
						driver.travelTo(15,-15);
						driver.travelTo(15,15);
						break;
					case LOW_RIGHT:
						driver.travelTo(315,15);
						driver.travelTo(285,15);
						break;
					case UP_LEFT:
						driver.travelTo(-15,285);
						driver.travelTo(15,285);
						break;
					case UP_RIGHT :
						driver.travelTo(285,315);
						driver.travelTo(285,285);
						break;
				}
			}
}
