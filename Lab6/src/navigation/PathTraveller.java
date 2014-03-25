package navigation;

import java.util.ListIterator;
import java.util.Stack;

import objectdetection.ObstacleDetector;
import odometry.Odometer;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Node;
import lejos.robotics.pathfinding.Path;
import movement.Driver;

/**
 * An abstraction for traveling along a path while scanning the next tile
 * 
 * @author Peter Henderson
 *
 */
public class PathTraveller {
	
	private static PathTraveller instance;
	private PathFinder finder;
	private Map map;
//	private Stack<Waypoint> path;
	private Path path;
	private ListIterator<Waypoint> pathIter;
	private Driver driver;
	private ObstacleDetector detector;
	
	public static PathTraveller getInstance(){
		if(instance == null) instance = new PathTraveller();
		
		return instance;
	}

	/**
	 * Private constructor
	 */
	private PathTraveller(){
		driver = Driver.getInstance();
		finder = PathFinder.getInstance();
		map = Map.getInstance();
//		path = new Stack<Waypoint>();
	}
	
	/**
	 * recalculates the path (for example if an object has been detected)
	 */
	public void recalculatePathToCoords(int x, int y){
		Node n = map.getClosestNode(Odometer.getInstance().getX(),
				Odometer.getInstance().getY());
		LCDWriter.getInstance().writeToScreen("n" + n.x + "," + n.y, 0);
		path = finder.getPathBetweenNodes(n,  map.getClosestNode(x, y));
		pathIter = path.listIterator();
		pathIter.next(); //remove the first node so it doesn't go back to it
	}
	
	/**
	 * recalculates the path (for example if an object has been detected)
	 */
	public void recalculatePathFromCoordsTo(int x, int y, int tox, int toy){
		path = finder.getPathBetweenNodes(map.getClosestNode(x,y), 
								   map.getClosestNode(tox, toy));
		pathIter = path.listIterator();
//		dumpToStack(p); 
	}
	
	/**
	 * Travels along the currently stored path, checking for obstacles at each point
	 * Returns whether arrived at destination or not
	 * @return
	 */
	public boolean followThePath(){
		while(!pathIter.hasNext()){
			Waypoint next = pathIter.next();
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
		}
		
		return true;
	}
	
	public boolean pathIsEmpty(){
		return path.isEmpty();
	}
	
	/**
	 * Pops the next waypoint
	 * @return
	 */
	public Waypoint popNextWaypoint(){
		return pathIter.next();
	}
//	
//	/**
//	 * Pushes all points from lejos.Path to an internal Stack
//	 * This makes it more intuitive, though may be unnecessary 
//	 * @param p
//	 */
//	private void dumpToStack(Path p){
//		path = new Stack<Waypoint>();
//		
//		ListIterator<Waypoint> iter = p.listIterator(p.size());
//		while(iter.hasPrevious()){
//			Waypoint point = iter.previous();
//			path.push(point);
//		}
//	}
	
}
