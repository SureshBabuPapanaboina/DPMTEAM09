package integration;

import movement.Driver;
import navigation.Map;
import navigation.PathTraveller;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Waypoint;
import objectdetection.ObstacleDetector;
import odometry.Odometer;
import odometry.OdometerCorrection;
import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import sensors.LineReader;
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
public class FullNavTest {

	static Coordinate destination = new Coordinate(165, 165, 0);
	
	private static boolean followPath(){
		PathTraveller t = PathTraveller.getInstance();
		Driver driver = Driver.getInstance();
		ObstacleDetector detector = ObstacleDetector.getInstance();
		Map map = Map.getInstance();
		Configuration conf = Configuration.getInstance();
		
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
			
			Coordinate n = new Coordinate(next);
			switch(Odometer.getInstance().getDirection()){
			case NORTH:
				n.setY(n.getY()+3);
				break;
			case WEST:
				n.setX(n.getX()-3);
				break;
			case EAST:
				n.setX(n.getX()+3);
				break;	
			case SOUTH:
				n.setY(n.getY()-3);
				break;
			}
			driver.travelTo(n);
		}
		
		return true;
			
	}
	
	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();
				
		UltrasonicPoller up = UltrasonicPoller.getInstance();
		PathTraveller traveller = PathTraveller.getInstance();
		LineReader llr = LineReader.getLeftSensor();	//left + right line reader
		LineReader rlr = LineReader.getRightSensor();
		Odometer odo = Odometer.getInstance();
		OdometerCorrection oc = OdometerCorrection.getInstance();
		Configuration conf = Configuration.getInstance();
		
		up.start();
		odo.start();
		llr.start();
		rlr.start();
		
		LineReader.subscribeToAll(oc);
		
		odo.setTheta(0);
		odo.setX(15);
		odo.setY(15);
		
		try {Thread.sleep(1000);}catch(Exception e){};
		
		traveller.recalculatePathToCoords((int)destination.getX(), (int)destination.getY() );

		boolean done  = false;
		while(!done){
			try{
			done = followPath();
			if(!done) 
				traveller.recalculatePathToCoords((int)destination.getX(), (int)destination.getY() );
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
