package searcher;

import static org.junit.Assert.*;

import java.util.Stack;

import org.junit.Test;

import robotcore.Configuration;
import robotcore.Coordinate;

public class SearchPathGeneratorTest {

	@Test
	public void test() {
//		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		Coordinate[] bl = new Coordinate [] {new Coordinate(30, 30,0), new Coordinate(90, 90,0)};
		Stack<Coordinate> pathstack = new Stack<Coordinate>();
		
			for(int i = (int) bl[1].getX()-15; i>=bl[0].getX()+15; i-=30){
				for(int j = (int) bl[1].getY()-15; j>=bl[0].getY()+15; j-=30){
					pathstack.push(new Coordinate(i, j, 0));
				}
			}
		
		while(!pathstack.isEmpty()){
			System.out.println(pathstack.pop().toString());
		}
		
	}

}
