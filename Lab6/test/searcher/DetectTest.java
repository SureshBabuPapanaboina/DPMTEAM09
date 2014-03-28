package searcher;

import java.util.ArrayList;

import movement.Driver;
import navigation.Map;
import navigation.PathTraveller;
import lejos.nxt.Button;
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
 * Basic test for the color differentiation mechanism, read when press left button to test values
 *  * 
 * @author Peter Henderson
 *
 */
public class DetectTest {


	
	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();
		

		UltrasonicPoller up = UltrasonicPoller.getInstance();

		ObjRec or = new ObjRec();
		
		up.start();

		
		while(true){
			while(Button.waitForAnyPress() != Button.ID_LEFT);
			
			try{
				
				ArrayList<ObjRec.blockColor> color = or.detect();
			
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
		
	}
	
}
