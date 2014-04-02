package searcher;

import java.util.ArrayList;
import java.util.Queue;
import java.util.Stack;

import communication.RemoteConnection;

import capture.CaptureMechanism;
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
public class PeakSearcher {
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
				int peakTwo = sensorMotor.getPosition(); //5 for error?
				int theta = (peakOne + peakTwo)/2;
				sensorMotor.rotateTo(theta);
				int dist = getFilteredDistance();
				
				if(dist > 20)
					dist = getFilteredDistance();
				
				if(dist > 30){
					dist = 20;
//					sensorMotor.rotateTo(-55, true);
//					continue;
				}
				
				sensorMotor.rotateTo(0);
				return new Trajectory(-theta-7, dist-5);
			}
		}
		sensorMotor.rotateTo(0);
	
//		sensorMotor.rotateTo(55, true);
//		
//		while(sensorMotor.getPosition() < 55){
//			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
//
//			if(getFilteredDistance() < 30){
//				int peakOne = sensorMotor.getPosition();
//				while(getFilteredDistance() < 30); //wait for it to get back to 40
//				int peakTwo = sensorMotor.getPosition();
//				int theta = (peakOne + peakTwo)/2;
//				sensorMotor.rotateTo(theta);
//				int dist = getFilteredDistance();
//				
//				if(dist > 20)
//					dist = getFilteredDistance();
//				
//				if(dist > 30){
//					sensorMotor.rotateTo(55, true);
//				}
//				
//				sensorMotor.rotateTo(0);
//				return new Trajectory(-theta, dist-5);
//			}
//		}
//		
		return null;
	}
	
	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();

		UltrasonicPoller up = UltrasonicPoller.getInstance();
		CaptureMechanism cm = CaptureMechanism.getInstance();
		RemoteConnection rc = RemoteConnection.getInstance();

		Odometer odo = Odometer.getInstance();
		Driver.setSpeed(30);
		Driver dr = Driver.getInstance();
		ObjRec or = new ObjRec();
		Configuration config = Configuration.getInstance();

		up.start();
		odo.start();
		
		Stack<Coordinate> path = new Stack<Coordinate>();
		path.push(new Coordinate(45, 45, 0)); //last point
		path.push(new Coordinate(45, 75, 0));
		path.push(new Coordinate(15, 75, 0)); //move forward
		path.push(new Coordinate(15, 45, 0)); //enter the zone;
		int BLOCK_COLOR = 4; //yellows
		boolean blockFound  = false;

		while(!blockFound && !path.isEmpty()){
			Coordinate p = path.pop();
			dr.turnTo(Coordinate.calculateRotationAngle(config.getCurrentLocation(), p));

			Trajectory block = searchTile();

			if(block != null){
				dr.turnTo(odo.getTheta() + block.theta);

				dr.forward(Math.abs(block.distance-5));

				ArrayList<ObjRec.blockColor> color = or.detect();

				//		
				if(color == null || color.size() == 0)
					lcd.writeToScreen("EMPTY", 1);
				else{
					int count = 1;
					//TODO: extract this into method
					for(ObjRec.blockColor c : color)
						if(c != null){
							lcd.writeToScreen( c.name(), count++);
							if(c.getCode() == BLOCK_COLOR){
								rc.setupConnection();

								Sound.beepSequenceUp();
								blockFound = true;
																
								cm.open();

								dr.forward(18);
								
								cm.align();
								
								dr.forward(15);
								
								cm.close();
								System.exit(0);
								break;
							}
							else{
								cm.removeBlock();
								break;
							}
						}
				}
			}
			
			dr.travelTo(p);
			
			block = searchTile();

			if(block != null){
				dr.turnTo(odo.getTheta() + block.theta);

				dr.forward(Math.abs(block.distance-5));

				ArrayList<ObjRec.blockColor> color = or.detect();

				//		
				if(color == null || color.size() == 0){
					lcd.writeToScreen("EMPTY", 1);
					dr.backward(5);
				}else{
					int count = 1;
					//TODO: extract this into method
					for(ObjRec.blockColor c : color)
						if(c != null){
							lcd.writeToScreen( c.name(), count++);
							if(c.getCode() == BLOCK_COLOR){
								Sound.beepSequenceUp();
								blockFound = true;
								break;
							}
							else{
								cm.removeBlock();
								break;
							}
						}
				}
			}
			
		}
		
		Sound.beepSequenceUp();		

	}
	
}
