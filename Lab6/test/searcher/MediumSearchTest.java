package searcher;

import java.util.ArrayList;
import java.util.Stack;

import movement.Driver;
import navigation.Map;
import navigation.PathTraveller;
import lejos.geom.Point;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Waypoint;
import objectdetection.ObjectDetector;
import objectdetection.ObstacleDetector;
import odometry.Odometer;
import odometry.OdometerCorrection;
import robotcore.Configuration;
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
public class MediumSearchTest {

	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();
		
		Configuration config = Configuration.getInstance();

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

		Stack<Point> path = new Stack<Point>();
		path.push(new Point(15, 45));
		path.push(new Point(45, 45));
		path.push(new Point(45, 15));
		//need to iterate through the search points, looking for the block
		boolean blockFound  = false;
		
		dr.travelTo(15, 30);
		detector.start();

		while(!blockFound && odo.getY() < 29){
			lcd.writeToScreen("blockf:" + blockFound, 1);
			blockFound = detector.objectFound();
		}
		
		lcd.writeToScreen("here", 1);
		if(blockFound){
			dr.turnTo(odo.getTheta() + detector.getTrajectory().theta);
			int dist = up.getDistance();
			dr.forward(dist);
			try{
//				if(or == null) lcd.writeToScreen("NO!", 1);
				
				ArrayList<ObjRec.blockColor> color = or.detectA();
//		
			if(color == null || color.size() == 0)
				lcd.writeToScreen("EMPTY", 1);
			else{
				int count = 1;
				for(ObjRec.blockColor c : color)
					if(c != null)
						lcd.writeToScreen( c.name(), count++);
			}
			}
			catch(Exception e){
				lcd.writeToScreen("EXCEPTION", 1);
			}

		}
		
//		lcd.writeToScreen("fin", 1);
		//indicate finish
		Sound.beepSequenceUp();		
		
	}
	
}
