package navigation;

import java.util.ListIterator;

import javax.security.auth.login.Configuration;

import odometry.Direction;
import odometry.Odometer;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import sensors.UltrasonicPoller;
import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Node;
import lejos.robotics.pathfinding.Path;
import movement.Driver;

/**
 * 
 * A practical test for a basic pathfinding scenario with obstacles
 * 
 * @author Peter Henderson
 *
 */
public class PathBlocked {

	/**
	 * Main method of the test, runs a basic pathfinding scenario
	 * visually check that the robot arrives at the destination
	 * @param args
	 */
	public static void main(String[] args){

		LCDWriter.getInstance().start();
		LCDWriter.getInstance().writeToScreen("Here", 6);

		PathFinder finder = PathFinder.getInstance();
		Map map = Map.getInstance();
		Odometer odo = Odometer.getInstance();
		UltrasonicPoller poller = UltrasonicPoller.getInstance();
		poller.start();
		odo.start();

		Driver dr = Driver.getInstance();
		dr.start();
		Node start = map.getClosestNode(odo.getX(), odo.getY());
		Node end = map.getClosestNode(75, 75);

		boolean goalReached =false;
		boolean obstacleDetected = false;
		
		while(!goalReached){
			obstacleDetected =false;
			LCDWriter.getInstance().writeToScreen("Main Loop", 6);

			start = map.getClosestNode(odo.getX(), odo.getY());
			Path path = finder.getPathBetweenNodes(start, end);
			ListIterator<Waypoint> pathiter = path.listIterator();
			
			while(pathiter.hasNext()){

				Waypoint p = pathiter.next();
				LCDWriter.getInstance().writeToScreen("Next:" + p.x + "," + p.y, 3);

				Coordinate current = new Coordinate(odo.getX(), odo.getY(), odo.getTheta());
				double delta = Coordinate.calculateRotationAngle(current, new Coordinate(p.x, p.y, 0));
				
				//Preview the next tile
				dr.turnTo(delta);
				
				//check the next tile
				if(poller.getDistance() < 40){
					//obstacle detected 
					obstacleDetected = true;
					//this is a bit hard coded, change this in the obstacle detection test
					if(odo.getDirection() == Direction.NORTH){
						map.blockNodeAt(odo.getX(), odo.getY()+poller.getDistance());
						LCDWriter.getInstance().writeToScreen("NORTH", 1);
					}else{
						map.blockNodeAt(odo.getX() + poller.getDistance(), odo.getY());
						LCDWriter.getInstance().writeToScreen("EAST", 1);
					}
					break;
				}
				
//				LCDWriter.getInstance().writeToScreen("Next Point", 6);
				dr.travelTo(new Coordinate(p.x, p.y, 0));

			}
			if(!obstacleDetected) goalReached = true;

		}


		//indicate finish
		Sound.beepSequenceUp();


	}

}
