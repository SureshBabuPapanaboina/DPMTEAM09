package obstacle;

import lejos.nxt.Button;
import objectdetection.ObstacleDetector;
import robotcore.LCDWriter;
import sensors.UltrasonicPoller;

/**
 * Basic test to scan a tile in front of the robot
 * Place obstacle at different points in the tile and see if the scanner picks up
 * the obstacle correctly
 * 
 * Perfectly finds any incursion into next tile as of March 23, 2014
 * May need to tweak to allow some leaway in terms of incursions in the tiles
 * 
 * @author Peter Henderson
 *
 */
public class TileScanTest {

	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();
		
		UltrasonicPoller up = UltrasonicPoller.getInstance();
		ObstacleDetector detector = ObstacleDetector.getInstance();

		up.start();
		
		while(Button.waitForAnyPress() != Button.ID_ESCAPE){
			boolean object = detector.scanTileWithSideSensors();
			
			lcd.writeToScreen("Obstacle: " + object, 4);
		}
		
	}
	
}
