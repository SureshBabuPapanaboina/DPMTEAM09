package search;

import java.util.Stack;

import robotcore.Configuration;
import robotcore.Coordinate;
import lejos.robotics.pathfinding.Path;

/**
 * This generates a search path along the endzone and begins to search for the flag
 *
 */
public class Searcher {
	
	/**
	 * Generates a search path based on the config
	 */
	public static Stack<Coordinate> generateSearchPath(boolean fromBottom){
		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		Stack<Coordinate> pathstack = new Stack<Coordinate>();
		
		if(!fromBottom){
			for(int i = (int) bl[0].getX()+15; i<=bl[1].getX()-15; i+=30){
				for(int j = (int) bl[0].getY()+15; j<=bl[1].getY()-15; j+=30){
					pathstack.push(new Coordinate(i, j, 0));
				}
			}
		}
		else{
			for(int i = (int) bl[1].getX()-15; i>=bl[0].getX()+15; i-=30){
				for(int j = (int) bl[1].getY()-15; j>=bl[0].getY()+15; j-=30){
					pathstack.push(new Coordinate(i, j, 0));
				}
			}
		}
		
		return pathstack;
	}
	
	/**
	 * Begins searching the path
	 */
	public void search(){
		
	}
	
}
