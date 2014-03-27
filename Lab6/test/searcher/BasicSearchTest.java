package searcher;

import movement.Driver;
import navigation.Map;
import navigation.PathTraveller;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Waypoint;
import objectdetection.ObjectDetector;
import objectdetection.ObstacleDetector;
import odometry.Odometer;
import odometry.OdometerCorrection;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import search.ObjRec;
import sensors.LineReader;
import sensors.UltrasonicPoller;

/**
 * Basic search test for a 2 x 2 tile
 *  * 
 * @author Peter Henderson
 *
 */
public class BasicSearchTest {

//	private static boolean followPath(){
//		PathTraveller t = PathTraveller.getInstance();
//		Driver driver = Driver.getInstance();
//		ObstacleDetector detector = ObstacleDetector.getInstance();
//		Map map = Map.getInstance();
//		
//		while(!t.pathIsEmpty()){
//			Waypoint next = t.popNextWaypoint();
//			//Turn to the next tile
//			driver.turnTo(Coordinate.calculateRotationAngle(
//												Odometer.getInstance().getCurrentCoordinate(), 
//												new Coordinate(next)));
//			
//			//scan the next area
//			if(detector.scanTile()){
//				//block next tile
//				map.blockNodeAt(next.x, next.y);
//				return false;
//			}
//			
//			driver.travelTo(new Coordinate(next));
//		}
//		
//		return true;
//			
//	}
	
	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();
		
		UltrasonicPoller up = UltrasonicPoller.getInstance();
//		PathTraveller traveller = PathTraveller.getInstance();
//		LineReader llr = LineReader.getLeftSensor();	//left + right line reader
//		LineReader rlr = LineReader.getRightSensor();
		Odometer odo = Odometer.getInstance();
//		OdometerCorrection oc = OdometerCorrection.getInstance();
		Driver.setSpeed(75);
		Driver dr = Driver.getInstance();
		ObjectDetector detector = ObjectDetector.getInstance();
		ObjRec or = new ObjRec();
		
		up.start();
		odo.start();
		dr.start();
//		llr.start();
//		rlr.start();

		
		boolean blockFound  = false;
		detector.start();
//		dr.travelTo(15, 30);

		while(!blockFound && odo.getY() < 29){
			lcd.writeToScreen("blockf:" + blockFound, 1);
			blockFound = detector.objectFound();
		}
		
		lcd.writeToScreen("here", 1);
		if(blockFound){
//			dr.turnTo(odo.getTheta() + detector.getTrajectory().theta);
			try{
			ObjRec.blockColor[] color = or.detect();
			if(color.length == 0)
				lcd.writeToScreen("EMPTY", 1);
			else
				for(ObjRec.blockColor c : color)
					if(c != null)
						lcd.writeToScreen("Color: " + c.toString(), 1);
			}
			catch(Exception e){
				lcd.writeToScreen(e.getMessage(), 1);
			}

		}
		
//		lcd.writeToScreen("fin", 1);
		//indicate finish
		Sound.beepSequenceUp();		
		
	}
	
}
