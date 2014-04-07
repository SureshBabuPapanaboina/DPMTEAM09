package searcher;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Stack;

import odometry.Odometer;

import org.junit.Test;

import robotcore.Configuration;
import robotcore.Coordinate;
import search.Searcher;

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
	
	@Test
	public void test3(){
		ArrayList<Coordinate> surrounding = new ArrayList<Coordinate>();
		Coordinate[] bl = new Coordinate [] {new Coordinate(30, 30,0), new Coordinate(90, 90,0)};

		for(int i = (int) bl[1].getX()-15; i>=bl[0].getX()+15; i-=15){
			for(int j = (int) bl[1].getY()-15; j>=bl[0].getY()+15; j-=15){
				surrounding.add(new Coordinate(i, j, 0));
			}
		}
		

		Stack<Coordinate> searchpath = new Stack<Coordinate>();
		Coordinate latest = Searcher.getClosestPoint(new Coordinate(15,15,0), surrounding);
		searchpath.push(latest);
		
		while(surrounding.size() > 0){
			latest = Searcher.getClosestPoint(latest, surrounding);
			searchpath.push(latest);
		}
		
		while(!searchpath.isEmpty()){
			System.out.println(searchpath.pop().toString());
		}
		
		
	}
	
	@Test
	public void test2() {
//		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		Coordinate[] bl = new Coordinate [] {new Coordinate(120, 120,0), new Coordinate(180, 210,0)};
		Stack<Coordinate> surrounding = new Stack<Coordinate>();
//		Coordinate[] bl = Configuration.getInstance().getFlagZone();
		
		for(int i = (int) bl[1].getX()+15; i>=bl[0].getX()-15; i-=30){
			for(int j = (int) bl[1].getY()+15; j>=bl[0].getY()-15; j-=30){
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
		while(!surrounding.isEmpty()){
			System.out.println(surrounding.pop().toString());
		}
		
	}

}
