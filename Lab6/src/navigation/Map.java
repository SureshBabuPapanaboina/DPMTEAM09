package navigation;

import java.util.HashSet;

import lejos.robotics.pathfinding.Node;

/**
 * A map representation of the grid area where the robot is going to path find
 * 
 * Note: parts of the following implementation were taken from: 
 * https://github.com/mvertescher/DPM-Winter-2013/blob/master/Final%20Software/Final%20Code/Final/src/pathfinding/GridMap.java
 *  
 */
public class Map {

	Node[][] nodes;
	
	HashSet<Node> blocked;
	
	/**
	 * Basic constructor
	 */
	public Map() {
		this.blocked = new HashSet<Node>();

		this.populateMap();

	}

	/**
	 * Populates the map with Nodes and their respective coordinates
	 */
	public void populateMap() {	
		nodes = new Node[12][12]; 

		for (int i = 0; i < 12; i++) {
			for (int j = 0; j < 12; j++) {
				nodes[i][j] = new Node((30*i-15), (30*j-15));
			}
		}

		this.generateEdges();
	}

	/**
	 * Adds the neighbors to the proper nodes
	 */
	private void generateEdges() {
		Node n = null;
		for (int i = 0; i < 12; i++) { 
			for (int j = 0; j < 12; j++) { 
				n = nodes[i][j];

				if (i != 0)
					n.addNeighbor(nodes[i-1][j]);
				if (j != 0)
					n.addNeighbor(nodes[i][j-1]);
				if (i != 11)
					n.addNeighbor(nodes[i+1][j]);
				if (j != 11)
					n.addNeighbor(nodes[i][j+1]);			
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
		int xInt = (int) x;
		int yInt = (int) y;
		int xDiv = ((xInt + 30) / 30);
		int yDiv = ((yInt + 30) / 30);

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
		if (x > -25 && x < 325 && y > -25 && y < 325) {
			Node n = this.getClosestNode(x,y);
			if (!n.equals(null)) { 
				this.removeNode(n);
				if (!this.blocked.contains(n))
					this.blocked.add(n);
			}	
		}	
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
		HashSet<Node> c = (HashSet<Node>) n.getNeighbors();
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
