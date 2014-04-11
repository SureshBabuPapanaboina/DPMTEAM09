package search;

import java.util.ArrayList;
import java.util.Stack;

import capture.CaptureMechanism;
import lejos.nxt.Sound;
import movement.Driver;
import objectdetection.ObjectDetector;
import odometry.Odometer;
import navigation.PathTraveller;
import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import search.ObjRec.blockColor;

/**
 * This generates a search path along the endzone and begins to search for the flag
 *
 */
public class Searcher {
	
	
	/**
	 * Is the robot near a wall?
	 * @return
	 */
	public static boolean nearWall(){
		Coordinate now = Configuration.getInstance().getCurrentLocation();

		return now.getX() < 25 || now.getY() < 25 || now.getX()>305 || now.getY()>305;
	}
	
	/**
	 * Am I in the search zone?
	 * @return
	 */
	public static boolean inSearchZone(){
		Coordinate now = Configuration.getInstance().getCurrentLocation();
		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		return (now.getX() > bl[0].getX() && now.getX() < bl[1].getX() 
				&& now.getY() > bl[0].getY() && now.getY() < bl[1].getY()) && !nearWall();

	}
	
	/**
	 * Run a search routine
	 */
	public static void searchForBlock(){
		Stack<Coordinate> path = Searcher.generateSearchPath();
		Configuration conf = Configuration.getInstance();
		Driver driver = Driver.getInstance();
		Coordinate first = path.peek();
		LCDWriter lcd = LCDWriter.getInstance();
		CaptureMechanism cm = CaptureMechanism.getInstance();
		
		int BLOCK_COLOR = conf.getBlockColor().getCode();
		
		boolean blockFound  = false;

		Configuration.getInstance().setForwardSpeed(300);
		Configuration.getInstance().setRotationSpeed(250);

		while(!blockFound && !path.isEmpty()){
			Coordinate p = path.pop();
			lcd.writeToScreen("Des:" +p.toString(), 4);
			driver.turnTo(Coordinate.calculateRotationAngle(conf.getCurrentLocation(), p));
			int result = ObjectDetector.lookForItemII(BLOCK_COLOR);
			if(result ==1 ){
				Sound.beep();
				blockFound = true;
				cm.open();
				driver.forward(15);
				cm.align();
				driver.forward(15);
				cm.close();
				Sound.beep();
				Sound.beepSequenceUp();		
				break;
			}
			//If not the right block remove it
			else if(result == 0 && Searcher.inSearchZone()){ 
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
					driver.forward(15);
					cm.align();
					driver.forward(15);
					cm.close();
					Sound.beep();
					Sound.beepSequenceUp();		
					break;
				}
			}
			
			
			driver.travelTo(p);
		
			//Double check for item
			result = ObjectDetector.lookForItemII(BLOCK_COLOR);
			//capture it
			if(result ==1 ){
				Sound.beep();
				blockFound = true;
				cm.open();
				driver.forward(15);
				cm.align();
				driver.forward(15);
				cm.close();
				Sound.beep();
				Sound.beepSequenceUp();		
				break;
			}
			else if(result == 0 && Searcher.inSearchZone()){
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
					driver.forward(15);
					cm.align();
					driver.forward(15);
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
	}
	
	/**
	 * Generates a search path based on the config
	 */
	public static Stack<Coordinate> generateSearchPath(){
		
		ArrayList<Coordinate> points = PathTraveller.getInstance().getAllPointsInFlagZoneList();
		
		Stack<Coordinate> searchpath = new Stack<Coordinate>();
		Coordinate latest = getClosestPoint(Odometer.getInstance().getCurrentCoordinate(), points);
		searchpath.push(latest);
		
		while(points.size() > 0){
			latest = getClosestPoint(latest, points);
			searchpath.push(latest);
		}
		
		Stack<Coordinate> temp = new Stack<Coordinate>();
		while(!searchpath.isEmpty()){
			temp.push(searchpath.pop());
		}
		
		return temp;
	}
	
	/**
	 * Get the point from an arraylist closest to the given coordinate
	 * @param a
	 * @param b
	 * @return
	 */
	public static Coordinate getClosestPoint(Coordinate a, ArrayList<Coordinate> b){
		double bestDist = Integer.MAX_VALUE;
		Coordinate best = null;
		for(Coordinate coord : b){
			double current = Coordinate.calculateDistance(coord, a);
			if(current < bestDist){
				bestDist = current;
				best = coord;
			}
		}
		
		if(best != null) b.remove(best);
		return best;
	}

	
}
