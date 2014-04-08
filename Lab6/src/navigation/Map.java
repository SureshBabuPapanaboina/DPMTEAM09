package navigation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

import robotcore.Configuration;
import robotcore.Coordinate;
import lejos.robotics.pathfinding.Node;

/**
 * A map representation of the grid area where the robot is going to path find
 * 
 * Note: parts of the following implementation were taken from: 
 * https://github.com/mvertescher/DPM-Winter-2013/blob/master/Final%20Software/Final%20Code/Final/src/pathfinding/GridMap.java
 *  
 *  @author Peter Henderson
 */
public class Map {
	
	private static Map instance;
	private int gridSize;
	private Node[][] nodes;
	
	//Determines if the nodes are in increments of 15 or 30
	public final boolean TILE_INCREMENTS = true; 
	
	
	private HashSet<Node> blocked;
	
	/**
	 * Basic constructor, private to prevent more than one instance of the map
	 */
	private Map() {
		this.gridSize = Configuration.GRID_SIZE;
		this.populateMap();
	}
	
	/**
	 * Gets the singleton instance
	 * @return
	 */
	public static Map getInstance(){
		if(instance == null) instance = new Map();
		return instance;
	}

	/**
	 * Populates the map with Nodes and their respective coordinates
	 */
	public void populateMap() {	
		this.blocked = new HashSet<Node>();
		if(TILE_INCREMENTS){
		nodes = new Node[gridSize][gridSize]; 

			for (int i = 0; i < gridSize; i++) {
				for (int j = 0; j < gridSize; j++) {
						nodes[i][j] = new Node((30*i-15), (30*j-15));
				}
			}
		}
		else{
			nodes = new Node[gridSize*2][gridSize*2]; 

			for (int i = 0; i < gridSize*2; i++) {
				for (int j = 0; j < gridSize*2; j++) {
						nodes[i][j] = new Node((15*i-15), (15*j-15));
				}
			}
		}

		this.generateEdges();
		
		Coordinate opponent = Configuration.getInstance().getOpponentDropZone();
		Stack<Coordinate> flagCoords = PathTraveller.getInstance().getAllTilesInFlagZone();
		
		while(flagCoords != null && !flagCoords.isEmpty()){
			Coordinate toBlock = flagCoords.pop();
			this.blockNodeAt(toBlock.getX(), toBlock.getY());
		}
		
		if(opponent != null)
			this.blockNodeAt(opponent.getX(), opponent.getY());
	}

	/**
	 * Adds the neighbors to the proper nodes
	 */
	private void generateEdges() {
		Node n = null;
		if(TILE_INCREMENTS){
			for (int i = 0; i < gridSize; i++) { 
				for (int j = 0; j < gridSize; j++) { 
					n = nodes[i][j];
	
					if (i != 0)
						n.addNeighbor(nodes[i-1][j]);
					if (j != 0)
						n.addNeighbor(nodes[i][j-1]);
					if (i != gridSize-1)
						n.addNeighbor(nodes[i+1][j]);
					if (j != gridSize-1)
						n.addNeighbor(nodes[i][j+1]);			
				}
			}
		}
		else{
			for (int i = 0; i < gridSize*2; i++) { 
				for (int j = 0; j < gridSize*2; j++) { 
					n = nodes[i][j];
	
					if (i != 0)
						n.addNeighbor(nodes[i-1][j]);
					if (j != 0)
						n.addNeighbor(nodes[i][j-1]);
					if (i != gridSize-1)
						n.addNeighbor(nodes[i+1][j]);
					if (j != gridSize-1)
						n.addNeighbor(nodes[i][j+1]);			
				}
			}
		}
	}

	/**
	 * Returns the closest node to a point
	 * @param x
	 * @param y
	 * @return Node
	 */
	public Node getClosestNode(double x, double y) {
		//TODO: does this really give the closest node?
		int xInt = (int) x;
		int yInt = (int) y;
		int xDiv ;
		int yDiv ;
		if(TILE_INCREMENTS){
			xDiv = ((xInt + 30) / 30);
			yDiv = ((yInt + 30) / 30);
		}
		else{
			xDiv = ((xInt + 15) / 15);
			yDiv = ((yInt + 15) / 15);
		}
		if(xDiv > nodes.length-1) return null;
		if(yDiv > nodes[0].length-1) return null;
		

		return nodes[xDiv][yDiv];
	}

	/**
	 * Get node based on indexes
	 * @param i
	 * @param js
	 * @return Node (i,j)
	 */
	public Node getNode(int i, int j) {
		return nodes[i][j];
	}

	/**
	 * Maps an obstacle by removing a node
	 * @param x
	 * @param y
	 */
	public void blockNodeAt(double x, double y) {
		if (x > -25 && x < gridSize*30-5 && y > -25 && y < gridSize*30-5 ) {
			Node n = this.getClosestNode(x,y);
			if (!n.equals(null)) { 
				this.removeNode(n);
				if (!this.blocked.contains(n))
					this.blocked.add(n);
			}	
		}	
	}   
	
	/**
	 * Maps an obstacle by removing a node
	 * @param x
	 * @param y
	 */
	public boolean isNodeBlocked(double x, double y) {
		if (x > -25 && x < gridSize*30-5 && y > -25 && y < gridSize*30-5 ) {
			Node n = this.getClosestNode(x,y);
			if (n != null) { 
				if (!this.blocked.contains(n))
					return false;
			}
		}
		
		return true;
	}                                         

	/**
	 * Removes a node from the grid by emptying its neighbors 
	 * @param i
	 * @param j
	 */
	public void removeNode(int i, int j) {
		Node n = nodes[i][j];
		if (!n.equals(null))
			this.removeNode(nodes[i][j]);
	}

	/**
	 * Removes a node from the grid by emptying its neighbors
	 * @param n
	 */
	public void removeNode(Node n) {
		ArrayList<Node> c = (ArrayList<Node>) n.getNeighbors();
		
		for(Node neighbor :  c){
			neighbor.removeNeighbor(n);
		}
		
		c.clear();
		
	}

	/**
	 * Remove the edge between two nodes
	 * @param Node n
	 * @param Node m
	 */
	public void removeEdge(Node n, Node m) {
		n.removeNeighbor(m);
		m.removeNeighbor(n);
	}

}
