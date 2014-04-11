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
	private Path path;
	private ListIterator<Waypoint> pathIter;
	
	/**
	 * Get singleton instance
	 * @return
	 */
	public static PathTraveller getInstance(){
		if(instance == null) instance = new PathTraveller();
		
		return instance;
	}

	/**
	 * Private constructor
	 */
	private PathTraveller(){
		finder = PathFinder.getInstance();
		map = Map.getInstance();
	}
	
	/**
	 * Get all the coordinates in the flag zone by 15 cm increments
	 * @return
	 */
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
	
	/**
	 * Get all the coordinates in teh flag zone by 30 cm increments
	 * @return
	 */
	public Stack<Coordinate> getAllTilesInFlagZone(){
		Stack<Coordinate> surrounding = new Stack<Coordinate>();
		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		for(int i = (int) bl[1].getX()-15; i>=bl[0].getX()+15; i-=30){
			for(int j = (int) bl[1].getY()-15; j>=bl[0].getY()+15; j-=30){
				surrounding.push(new Coordinate(i, j, 0));
			}
		}
		return surrounding;
	}
	
	/**
	 * Get list of coords in flag zone by 30 cm
	 * @return
	 */
	public ArrayList<Coordinate> getAllPointsInFlagZoneList(){
		ArrayList<Coordinate> surrounding = new ArrayList<Coordinate>();
		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		for(int i = (int) bl[1].getX()-15; i>=bl[0].getX()+15; i-=30){
			for(int j = (int) bl[1].getY()-15; j>=bl[0].getY()+15; j-=30){
				surrounding.add(new Coordinate(i, j, 0));
			}
		}
		return surrounding;
	}
	
	/**
	 * Get all points surrounding the flag zone except for diagonal for pathfinding as list
	 * @return
	 */
	public ArrayList<Coordinate> getAllPointsAroundFlagZoneList(){
		ArrayList<Coordinate> surrounding = new ArrayList<Coordinate>();
		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		
		for(int i = (int) bl[1].getX()+15; i>=bl[0].getX()-15; i-=30){
			for(int j = (int) bl[1].getY()+15; j>=bl[0].getY()-15; j-=30){
				if(i < bl[0].getX() && j < bl[0].getY()) continue; //bottom left corner
				if(i < bl[0].getX() && j > bl[1].getY()) continue; //top left corner
				if(i > bl[1].getX() && j < bl[0].getY()) continue; //bottom left corner
				if(i > bl[1].getX() && j > bl[1].getY()) continue; //bottom left corner

				if(i > bl[1].getX()){
					surrounding.add(new Coordinate(i, j, 0));
				}
				if(i < bl[0].getX()){
					surrounding.add(new Coordinate(i, j, 0));
				}
				if(j > bl[1].getY()){
					surrounding.add(new Coordinate(i, j, 0));
				}				
				if(j < bl[0].getY()){
					surrounding.add(new Coordinate(i, j, 0));
				}
			}
		}
		
		return surrounding;
	}
	
	/**
	 * Get all points surrounding the flag zone except for diagonal for pathfinding as stack
	 * @return
	 */
	public Stack<Coordinate> getAllPointsAroundFlagZone(){
		Stack<Coordinate> surrounding = new Stack<Coordinate>();
		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		
		for(int i = (int) bl[1].getX()+15; i>=bl[0].getX()-15; i-=30){
			for(int j = (int) bl[1].getY()+15; j>=bl[0].getY()-15; j-=30){
				if(i < bl[0].getX() && j < bl[0].getY()) continue; //bottom left corner
				if(i < bl[0].getX() && j > bl[1].getY()) continue; //top left corner
				if(i > bl[1].getX() && j < bl[0].getY()) continue; //bottom left corner
				if(i > bl[1].getX() && j > bl[1].getY()) continue; //bottom left corner

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
	
	

	/**
	 * Get the best destination that is not blocked from the points surrounding the flag zone
	 * @return
	 */
	public Coordinate getDestination(){
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
	}
	
	/**
	 * Follow a path checking to see if we have entered the area surrounding the flag zone if
	 * passed null, then assumes that don't stop for flag zone
	 * @param nodesAroundFlag
	 * @return
	 */
	public static boolean followPath(ArrayList<Coordinate> nodesAroundFlag){
		PathTraveller t = PathTraveller.getInstance();
		Driver driver = Driver.getInstance();
		ObstacleDetector detector = ObstacleDetector.getInstance();
		Map map = Map.getInstance();
		
		while(!t.pathIsEmpty()){
			Waypoint next = t.popNextWaypoint();
			//Turn to the next tile
			driver.turnTo(Coordinate.calculateRotationAngle(
												Odometer.getInstance().getCurrentCoordinate(), 
												new Coordinate(next)));
			
			
			//scan the next area
			if(detector.scanTileWithSideSensors()){
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
			
			if(nodesAroundFlag != null){
				Node m = Map.getInstance().getClosestNode(n.getX(), n.getY());
				
				if(nodesAroundFlag.contains(m)){
					return true;
				}
			}
		}
		
		return true;
			
	}
	
	/**
	 * Check if the current path is done
	 * @return
	 */
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

	
}
