package navigation;

import static org.junit.Assert.*;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

import org.junit.Test;

/**
 * Basic unit tests for pathfinding
 *
 */
public class PathUnitTests {

	/**
	 * Tests that an optimal path is found and that the end zone is reached
	 * with no obstacles
	 */
	@Test
	public void basicPath() {
		Map map = Map.getInstance();
		map.populateMap(); // reset
		
		PathFinder find = PathFinder.getInstance();
		
		Path path = find.getPathBetweenNodes(map.getClosestNode(-15, -15), map.getClosestNode(45, 45));
		
		//this was to see the path
		for(Waypoint p : path){
			System.out.println("P: (" + p.x + ", " + p.y + ")");
		}
//		
		assertEquals(path.get(path.size()-1).x, 45, 0);
		assertEquals(path.get(path.size()-1).y, 45, 0);
	}
	
	/**
	 * Tests that an optimal path is found and that the end zone is reached
	 * with one obstacles
	 * 
	 */
	@Test
	public void pathWithOneObstacle(){
		Map map = Map.getInstance();
		map.populateMap(); // reset
		
		map.blockNodeAt(45, 45);
		
		PathFinder find = PathFinder.getInstance();
		
		Path path = find.getPathBetweenNodes(map.getClosestNode(-15, -15), map.getClosestNode(75, 75));
		
		//this was to see the path
		for(Waypoint p : path){
			if(p.x == 45 && p.y == 45) fail();
			
			System.out.println("P: (" + p.x + ", " + p.y + ")");
		}
		
		assertEquals(path.get(path.size()-1).x, 75, 0);
		assertEquals(path.get(path.size()-1).y, 75, 0);
	}
	
	/**
	 * Tests that an optimal path is found and that the end zone is reached
	 * with 6 obstacles
	 */
	@Test
	public void pathWithSeveralObstacles(){
		Map map = Map.getInstance();
		map.populateMap(); // reset
		
		map.blockNodeAt(45, 45);
		map.blockNodeAt(45, 15);
		map.blockNodeAt(45, -15);
		map.blockNodeAt(75, 45);
		map.blockNodeAt(75, 15);
//		map.blockNodeAt(-15, 45);
		map.blockNodeAt(15, 15);

		PathFinder find = PathFinder.getInstance();
		
		Path path = find.getPathBetweenNodes(map.getClosestNode(-15, -15), map.getClosestNode(75, 75));
		
		//this was to see the path
		for(Waypoint p : path){
			if(p.x == 45 && p.y == 45) fail();
			if(p.x == 45 && p.y == 15) fail();
			if(p.x == 45 && p.y == -15) fail();
			if(p.x == 75 && p.y == 45) fail();
			if(p.x == 75 && p.y == 15) fail();
//			if(p.x == -15 && p.y == 45) fail();
			if(p.x == 15 && p.y == 15) fail();

			System.out.println("P: (" + p.x + ", " + p.y + ")");
		}
		
		assertEquals(path.get(path.size()-1).x, 75, 0);
		assertEquals(path.get(path.size()-1).y, 75, 0);
	}
	
	/**
	 * This just assumes that the robot resets and assumes a clean map, maybe later
	 * it can be more robust if enough time
	 */
	@Test
	public void pathWithAllPathsBlocked(){
		Map map = Map.getInstance();
		map.populateMap(); // reset
		
		map.blockNodeAt(45, 45);
		map.blockNodeAt(45, 15);
		map.blockNodeAt(45, -15);
		map.blockNodeAt(75, 45);
		map.blockNodeAt(75, 15);
		map.blockNodeAt(-15, 45);
		map.blockNodeAt(15, 15);

		PathFinder find = PathFinder.getInstance();
		
		Path path = find.getPathBetweenNodes(map.getClosestNode(-15, -15), map.getClosestNode(75, 75));
		
		//this was to see the path
		for(Waypoint p : path){
			System.out.println("P: (" + p.x + ", " + p.y + ")");
		}
		
		assertEquals(path.get(path.size()-1).x, 75, 0);
		assertEquals(path.get(path.size()-1).y, 75, 0);
	}

}
