package search;

import java.util.ArrayList;
import java.util.Stack;

import odometry.Odometer;

import com.sun.tools.internal.jxc.gen.config.Config;

import navigation.PathFinder;
import navigation.PathTraveller;
import robotcore.Configuration;
import robotcore.Coordinate;
import lejos.robotics.pathfinding.Path;

/**
 * This generates a search path along the endzone and begins to search for the flag
 *
 */
public class Searcher {
	
	/**
	 * Am I in the search zone?
	 * @return
	 */
	public static boolean inSearchZone(){
		Coordinate now = Odometer.getInstance().getCurrentCoordinate();
		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		return (now.getX() > bl[0].getX() && now.getX() < bl[1].getX() 
				&& now.getY() > bl[0].getY() && now.getY() < bl[1].getY());

	}
	
	/**
	 * Generates a search path based on the config
	 */
	public static Stack<Coordinate> generateSearchPath(){
//		Coordinate[] bl = Configuration.getInstance().getFlagZone();
//		Stack<Coordinate> pathstack = new Stack<Coordinate>();
//		
//		if(!fromBottom){
//			for(int i = (int) bl[0].getX()+15; i<=bl[1].getX()-15; i+=30){
//				for(int j = (int) bl[0].getY()+15; j<=bl[1].getY()-15; j+=30){
//					pathstack.push(new Coordinate(i, j, 0));
//				}
//			}
//		}
//		else{
//			for(int i = (int) bl[1].getX()-15; i>=bl[0].getX()+15; i-=30){
//				for(int j = (int) bl[1].getY()-15; j>=bl[0].getY()+15; j-=30){
//					pathstack.push(new Coordinate(i, j, 0));
//				}
//			}
//		}
		
		//todo fix this
		
		ArrayList<Coordinate> points = PathTraveller.getInstance().getAllPointsInFlagZoneList();
		
		Stack<Coordinate> searchpath = new Stack<Coordinate>();
		Coordinate latest = getClosestPoint(Odometer.getInstance().getCurrentCoordinate(), points);
		searchpath.push(latest);
		
		while(points.size() > 0){
			latest = getClosestPoint(latest, points);
			searchpath.push(latest);
		}
		
		Stack<Coordinate> temp = new Stack<Coordinate>();
		while(!searchpath.isEmpty()){
			temp.push(searchpath.pop());
		}
		
		return temp;
	}
	
	public static Coordinate getClosestPoint(Coordinate a, ArrayList<Coordinate> b){
		double bestDist = Integer.MAX_VALUE;
		Coordinate best = null;
		for(Coordinate coord : b){
			double current = Coordinate.calculateDistance(coord, a);
			if(current < bestDist){
				bestDist = current;
				best = coord;
			}
		}
		
		if(best != null) b.remove(best);
		return best;
	}
	
	/**
	 * Begins searching the path
	 */
	public void search(){
		
	}
	
}
