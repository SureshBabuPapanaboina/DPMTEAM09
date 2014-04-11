package searcher;

import java.util.ArrayList;
import java.util.Queue;

import movement.Driver;
import navigation.Map;
import navigation.PathTraveller;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Waypoint;
import objectdetection.ObjectDetector;
import objectdetection.ObstacleDetector;
import objectdetection.Trajectory;
import odometry.Odometer;
import odometry.OdometerCorrection;
import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import search.ObjRec;
import sensors.LineReader;
import sensors.UltrasonicPoller;

/**
 * Basic search test with peak to peak detection
 *  * 
 * @author Peter Henderson
 *
 */
public class BasicPeakTest {
	static int count = 0;
//	static int[] distances = new int[5];
	static UltrasonicPoller up = UltrasonicPoller.getInstance();

	private static int getFilteredDistance(){
		int[] distances = up.getFloatingRange();
		int sum = 0;
		for(int i = 0; i< distances.length;i++){
			sum+=distances[i];
		}
		
		sum /= distances.length;
		return sum;
	}

	private static Trajectory searchTile(){
		NXTRegulatedMotor sensorMotor = Configuration.SENSOR_MOTOR;
		sensorMotor.setSpeed(45);
		sensorMotor.rotateTo(55);
		sensorMotor.rotateTo(-55, true);
//		LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

		//TODO: better algorithm
		while(sensorMotor.getPosition() > -55){
			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
			
			if(getFilteredDistance() < 30){
				int peakOne = sensorMotor.getPosition();
				while(getFilteredDistance() < 30); //wait for it to get back to 40
				int peakTwo = sensorMotor.getPosition();
				int theta = (peakOne + peakTwo)/2;
				sensorMotor.rotateTo(theta);
				int dist = getFilteredDistance();
				
				if(dist > 20)
					dist = getFilteredDistance();
				
				sensorMotor.rotateTo(0);
				return new Trajectory(-theta, dist-5);
			}
		}
	
		sensorMotor.rotateTo(55, true);
		
		while(sensorMotor.getPosition() < 55){
			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

			if(getFilteredDistance() < 30){
				int peakOne = sensorMotor.getPosition();
				while(getFilteredDistance() < 30); //wait for it to get back to 40
				int peakTwo = sensorMotor.getPosition();
				int theta = (peakOne + peakTwo)/2;
				sensorMotor.rotateTo(theta);
				int dist = getFilteredDistance();
				sensorMotor.rotateTo(0);
				return new Trajectory(-theta, dist);
			}
		}
		
		return null;
	}
	
	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();
		
		Configuration config = Configuration.getInstance();

		UltrasonicPoller up = UltrasonicPoller.getInstance();

		Odometer odo = Odometer.getInstance();
		Driver.setSpeed(30);
		Driver dr = Driver.getInstance();
		ObjRec or = new ObjRec();
		
		up.start();
		odo.start();


		Trajectory block = searchTile();

//		
		lcd.writeToScreen("here", 1);
		if(block != null){
			dr.turnTo(odo.getTheta() + block.theta);
			
			dr.forward(Math.abs(block.distance-5));
			try{
//				if(or == null) lcd.writeToScreen("NO!", 1);
				
				ArrayList<ObjRec.blockColor> color = or.detect();
			
//		
			if(color == null || color.size() == 0)
				lcd.writeToScreen("EMPTY", 1);
			else{
				int count = 1;
				for(ObjRec.blockColor c : color)
					if(c != null)
						lcd.writeToScreen( c.name(), count++);
			}
			}
			catch(Exception e){
				lcd.writeToScreen("EXCEPTION", 1);
			}

		}
		
//		lcd.writeToScreen("fin", 1);
		//indicate finish
		Sound.beepSequenceUp();		
		
	}
	
}
