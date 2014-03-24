package navigation;

import static org.junit.Assert.*;

import java.util.ListIterator;

import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

import org.junit.Test;

/**
 * Tests some functionality of the PathTraveller class
 * 
 * @author Peter Henderson
 *
 */
public class PathTravellerTests {

	/**
	 * Make sure stack works properly, note: have to comment out some
	 * initializiations of the lejos stuff to make sure it works in PathTraveller
	 */
	@Test
	public void test() {
		PathTraveller.getInstance().recalculatePathFromCoordsTo(0, 0, 75, 75);
		Path path = PathFinder.getInstance().getPathBetweenNodes(
				Map.getInstance().getClosestNode(0, 0), 
				Map.getInstance().getClosestNode(75, 75));
		ListIterator<Waypoint> iter = path.listIterator();
		while(iter.hasNext()){
			Waypoint p = iter.next();
			System.out.println(p);
			assertEquals(p, PathTraveller.getInstance().popNextWaypoint());
		}
	}

}
