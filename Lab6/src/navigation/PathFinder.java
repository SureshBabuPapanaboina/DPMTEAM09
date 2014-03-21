package navigation;

import odometry.Odometer;
import robotcore.Configuration;
import robotcore.Coordinate;
import lejos.robotics.pathfinding.AstarSearchAlgorithm;
import lejos.robotics.pathfinding.Node;
import lejos.robotics.pathfinding.Path;

/**
 * This finds a path based on the Nodes determined in the map
 *
 */
public class PathFinder {
	
	private AstarSearchAlgorithm search;
	private Map map;
	private Configuration config;
	private static PathFinder instance;
	
	/**
	 * Basic Constructor, instantiates variables
	 */
	private PathFinder(){
		this.search = new AstarSearchAlgorithm();
		this.map = Map.getInstance();
	}
	
	public static PathFinder getInstance(){
		if(instance == null) instance = new PathFinder();
		
		return instance;
	}
	
	/**
	 * Uses lejos AstarSearch to find optimal path to the flagZone
	 * @return
	 */
	public Path getOptimalPathToFlagZone(){
		config = Configuration.getInstance();

		//Assuming we want to stop in front of the flag zone, not in it,
		//so 15 cm to the right and 15 cm down from the bottom left coord
		//This is also assuming that the flag zone is in the top right corner
		//as for the demo 
		//TODO: change this hard coded assumption
		Coordinate[] coord = config.getFlagZone();
		
		Node flagNode = map.getClosestNode(coord[0].getX()+15, coord[0].getY()-15);
		
		return getPathBetweenNodes(getCurrentNode(), flagNode);
	}
	
	public Node getCurrentNode(){
		return map.getClosestNode(Odometer.getInstance().getX(), Odometer.getInstance().getY());
	}
	
	/**
	 * Uses lejos AstarSearch to find the optimal path to the flag drop zone
	 * @return
	 */
	public Path getOptimalPathToDropZone(){
		config = Configuration.getInstance();

		Coordinate coord = config.getDropZone();
		
		//Assumes bottom left of the tile, so we want to go the center of the tile
		Node dropNode = map.getClosestNode(coord.getX()+15, coord.getY()+15);
		
		return getPathBetweenNodes(getCurrentNode(), dropNode);
		
	}
	
	public Path getPathBetweenNodes(Node start, Node end){
		Path path = search.findPath(start, end);
		if(path == null || path.isEmpty()){
			map.populateMap(); //clear the map and try again
			path = search.findPath(map.getClosestNode(start.x, start.y), map.getClosestNode(end.x, end.y));
		}
		
		return path;
	}
}
