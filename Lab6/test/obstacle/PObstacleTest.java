package obstacle;

import movement.Driver;
import navigation.Map;
import navigation.PathTraveller;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Waypoint;
import objectdetection.ObstacleDetector;
import odometry.Odometer;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import sensors.UltrasonicPoller;

/**
 * Basic test to scan a tile in front of the robot
 * Place obstacle at different points in the tile and see if the scanner picks up
 * the obstacle correctly
 * 
 * NOTE: This is without odometry correction and pretty slow.
 * 
 * @author Peter Henderson
 *
 */
public class PObstacleTest {

	private static boolean followPath(){
		PathTraveller t = PathTraveller.getInstance();
		Driver driver = Driver.getInstance();
		ObstacleDetector detector = ObstacleDetector.getInstance();
		Map map = Map.getInstance();
		
		while(!t.pathIsEmpty()){
			Waypoint next = t.popNextWaypoint();
			//Turn to the next tile
			driver.turnTo(Coordinate.calculateRotationAngle(
												Odometer.getInstance().getCurrentCoordinate(), 
												new Coordinate(next)));
			
			//scan the next area
			if(detector.scanTile()){
				//block next tile
				map.blockNodeAt(next.x, next.y);
				return false;
			}
			
			driver.travelTo(new Coordinate(next));
		}
		
		return true;
			
	}
	
	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();
		
		UltrasonicPoller up = UltrasonicPoller.getInstance();
		PathTraveller traveller = PathTraveller.getInstance();
		
		Odometer odo = Odometer.getInstance();
		Driver dr = Driver.getInstance();

		up.start();
		odo.start();

		traveller.recalculatePathToCoords(135, 105);
//		traveller.popNextWaypoint();
//		lcd.writeToScreen(str, lineNumber);
//		while(!traveller.followThePath()){
//			traveller.recalculatePathToCoords(75, 75);
//		}
		boolean done  = false;
		while(!done){
			done = followPath();
			try{
			if(!done) traveller.recalculatePathToCoords(135, 105);
			else break;
			}
			catch(Exception e){
				lcd.writeToScreen("E: "+ e.toString(), 1);
			}
		}
		
		
		//indicate finish
		Sound.beepSequenceUp();		
		
	}
	
}
