package searcher;

import java.util.ArrayList;
import java.util.Stack;

import communication.RemoteConnection;
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

public class ImpSearchTestI {

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
		
		CaptureMechanism.setDropOff(new Coordinate(0, 0, 0));
		
		Stack<Coordinate> path = Searcher.generateSearchPath();
		int BLOCK_COLOR = 3; //yellows
		boolean blockFound  = false;
		RemoteConnection.getInstance().setupConnection();

		while(!blockFound && !path.isEmpty()){
			Coordinate p = path.pop();
			lcd.writeToScreen("Des:" +p.toString(), 4);
			dr.turnTo(Coordinate.calculateRotationAngle(config.getCurrentLocation(), p));
			int result = ObjectDetectorII.lookForItemII(BLOCK_COLOR);
			if(result ==1 ){
				Sound.beep();
				blockFound = true;
				cm.open();
				dr.forward(15);
				cm.align();
				dr.forward(15);
				cm.close();
				Sound.beep();
				Sound.beepSequenceUp();		
				break;
			}
			else if(result == 0){ 
				cm.removeBlockII();
				
				ObjRec oRec = new ObjRec();
				//test again
				ArrayList <blockColor> bc = oRec.detect();
				String ans = "";
				for (blockColor b : bc){
					ans += b.toString() ;
				}
				lcd.writeToScreen(ans, 6);	
				if(bc.contains(blockColor.getInstance(BLOCK_COLOR))){
					//face the robot
					blockFound = true;
					cm.open();
					dr.forward(15);
					cm.align();
					dr.forward(15);
					cm.close();
					Sound.beep();
					Sound.beepSequenceUp();		
					break;
				}
			}
			
			
			dr.travelTo(p);
			
			result = ObjectDetectorII.lookForItemII(BLOCK_COLOR);
			if(result ==1 ){
				Sound.beep();
				blockFound = true;
				cm.open();
				dr.forward(15);
				cm.align();
				dr.forward(15);
				cm.close();
				Sound.beep();
				Sound.beepSequenceUp();		
				break;
			}
			else if(result == 0){
				cm.removeBlockII();
				ObjRec oRec = new ObjRec();
				//test again
				ArrayList <blockColor> bc = oRec.detect();
				String ans = "";
				for (blockColor b : bc){
					ans += b.toString() ;
				}
				lcd.writeToScreen(ans, 6);	
				if(bc.contains(blockColor.getInstance(BLOCK_COLOR))){
					//face the robot
					blockFound = true;
					cm.open();
					dr.forward(15);
					cm.align();
					dr.forward(15);
					cm.close();
					Sound.beep();
					Sound.beepSequenceUp();		
					break;
				}
		}
			
			if(path.isEmpty() && !blockFound){
				path = Searcher.generateSearchPath();
			}
			
		}
		
		
		Sound.beepSequenceUp();		

	}
}
