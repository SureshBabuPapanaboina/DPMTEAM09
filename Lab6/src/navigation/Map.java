package navigation;

import java.util.HashSet;

import lejos.robotics.pathfinding.Node;

public class Map {

	Node[][] nodes;
	
	HashSet<Node> blocked;
	
	/**
	 * Creates a basic map object
	 */
	public Map(){
		populateMap();
		blockNodes();
	}
	
	/**
	 * Populates the Map with a basic grid
	 */
	public void populateMap(){
		
	}
	
	/**
	 * This blocks nodes based on the config file and the ended zones
	 */
	public void blockNodes(){
		
	}
	
	/**
	 * Blocks a particular grid if an obstacle is there, takes in x and y coords in cm
	 * and determines internally the Node
	 * @param x
	 * @param y
	 */
	public void blockNode(int x, int y){
		
	}
	
}
