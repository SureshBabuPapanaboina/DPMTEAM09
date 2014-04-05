package searcher;

import java.util.ArrayList;
import java.util.Stack;

import lejos.nxt.Sound;
import movement.Driver;
import objectdetection.ObjectDetectorII;
import objectdetection.Trajectory;
import objectdetection.ObjectDetectorII.ItemLocation;
import odometry.Odometer;
import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import search.ObjRec;
import search.Searcher;
import search.ObjRec.blockColor;
import sensors.UltrasonicPoller;
import capture.CaptureMechanism;

public class ImpSearchTest {

	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();

		UltrasonicPoller up = UltrasonicPoller.getInstance();
		CaptureMechanism cm = CaptureMechanism.getInstance();

		Odometer odo = Odometer.getInstance();
		Driver dr = Driver.getInstance();
		Driver.setSpeed(30);
		Configuration config = Configuration.getInstance();
		config.setFlagZone(new Coordinate(0, 0,0), new Coordinate(60, 60,0));

		up.start();
		odo.start();
		
		Stack<Coordinate> path = Searcher.generateSearchPath(true);
		int BLOCK_COLOR = 3; //yellows
		boolean blockFound  = false;

		while(!blockFound && !path.isEmpty()){
			Coordinate p = path.pop();
			lcd.writeToScreen("Des:" +p.toString(), 4);
			dr.turnTo(Coordinate.calculateRotationAngle(config.getCurrentLocation(), p));
			int result = ObjectDetectorII.lookForItem(BLOCK_COLOR);
			if(result ==1 ){
				Sound.beep();
				break;
			}
			else if(result == 0) 
				cm.removeBlock();
			
			dr.travelTo(p);
			result = ObjectDetectorII.lookForItem(BLOCK_COLOR);
			if(result ==1 ){
				Sound.beep();
				break;
			}
			else if(result == 0){
				cm.removeBlock();
				dr.backward(10);
			}
			
		}
		Sound.beepSequenceUp();		

	}
}
