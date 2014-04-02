package navigation;

import java.util.ListIterator;

import javax.security.auth.login.Configuration;

import odometry.Odometer;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import lejos.nxt.Button;
import lejos.nxt.Sound;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Node;
import lejos.robotics.pathfinding.Path;
import movement.Driver;

/**
 * 
 * A practical test for a basic pathfinding scenario (i.e. no obstacles)
 * 
 * @author Peter Henderson
 *
 */
public class PracticalPath {
	
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
		odo.start();

		Driver dr = Driver.getInstance();
		Node start = map.getClosestNode(odo.getX(), odo.getY());
		Node end = map.getClosestNode(75, 75);
		Path path = finder.getPathBetweenNodes(start, end);
		ListIterator<Waypoint> pathiter = path.listIterator();
		
		while(pathiter.hasNext()){
			LCDWriter.getInstance().writeToScreen("Next Point", 6);
			Waypoint p = pathiter.next();
			dr.travelTo(new Coordinate(p.x, p.y, 0));
		}
		
		//indicate finish
		Sound.beepSequenceUp();
		
		
	}

}
