package navigation;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Stack;

import objectdetection.ObstacleDetector;
import odometry.Odometer;
import robotcore.Configuration;
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
	
	public Stack<Coordinate> getAllPointsInFlagZone(){
		Stack<Coordinate> surrounding = new Stack<Coordinate>();
		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		for(int i = (int) bl[1].getX()-15; i>=bl[0].getX()+15; i-=15){
			for(int j = (int) bl[1].getY()-15; j>=bl[0].getY()+15; j-=15){
				surrounding.push(new Coordinate(i, j, 0));
			}
		}
		return surrounding;
	}
	
	public ArrayList<Coordinate> getAllPointsInFlagZoneList(){
		ArrayList<Coordinate> surrounding = new ArrayList<Coordinate>();
		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		for(int i = (int) bl[1].getX()-15; i>=bl[0].getX()+15; i-=15){
			for(int j = (int) bl[1].getY()-15; j>=bl[0].getY()+15; j-=15){
				surrounding.add(new Coordinate(i, j, 0));
			}
		}
		return surrounding;
	}
	
	public Stack<Coordinate> getAllPointsAroundFlagZone(){
		Stack<Coordinate> surrounding = new Stack<Coordinate>();
		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		
		for(int i = (int) bl[1].getX()+15; i>=bl[0].getX()-15; i-=30){
			for(int j = (int) bl[1].getY()+15; j>=bl[0].getY()-15; j-=30){
				if(i > bl[1].getX()){
					surrounding.push(new Coordinate(i, j, 0));
				}
				if(i < bl[0].getX()){
					surrounding.push(new Coordinate(i, j, 0));
				}
				if(j > bl[1].getY()){
					surrounding.push(new Coordinate(i, j, 0));
				}				
				if(j < bl[0].getY()){
					surrounding.push(new Coordinate(i, j, 0));
				}
			}
		}
		
		return surrounding;
	}
	
	

	public Coordinate getDestination(){
//		Coordinate[] flagzone = Configuration.getInstance().getFlagZone();
//		boolean top = Configuration.getInstance().getStartLocation().getY() > 150;
//		boolean left = Configuration.getInstance().getStartLocation().getX() < 150;
		Coordinate current = Odometer.getInstance().getCurrentCoordinate();
		Coordinate best = null;
		double bestDist = Integer.MAX_VALUE;
		Stack<Coordinate> surrounding = getAllPointsAroundFlagZone();
		while(!surrounding.isEmpty()){
			Coordinate possible = surrounding.pop();
			if(!map.isNodeBlocked(possible.getX(), possible.getY())){
				double currentDist = Coordinate.calculateDistance(possible, current);
				if(best==null || (best != null && currentDist < bestDist)){
					best = possible;
					bestDist = currentDist;
				}
			}
		}
		return best;
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
		if(pathIter.hasNext())
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
												Configuration.getInstance().getCurrentLocation(), 
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
		return !pathIter.hasNext();
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
