package searcher;

import java.util.ArrayList;
import java.util.Stack;

import capture.CaptureMechanism;
import communication.RemoteConnection;
import movement.Driver;
import navigation.Map;
import navigation.PathTraveller;
import lejos.geom.Point;
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
 * Basic search test for 2x2 tile and a 20 cm radius
 *  * 
 * @author Peter Henderson
 *
 */
public class SCaptureTest {

	private static Trajectory searchTile(){
		UltrasonicPoller up = UltrasonicPoller.getInstance();
		NXTRegulatedMotor sensorMotor = Configuration.SENSOR_MOTOR;
		sensorMotor.setSpeed(45);

		sensorMotor.rotateTo(-55, true);
		//		LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

		//TODO: better algorithm
		while(sensorMotor.getPosition() > -55){
			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

			if(up.getDistance() < 25){
				sensorMotor.rotateTo(0);
				return new Trajectory(-sensorMotor.getPosition()+15, up.getDistance());
			}
		}

		sensorMotor.rotateTo(55, true);

		while(sensorMotor.getPosition() < 55){
			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

			if(up.getDistance() < 25){
				//				sensorMotor.stop();
				sensorMotor.rotateTo(0);
				return new Trajectory(-sensorMotor.getPosition()-15, up.getDistance());
			}
		}

		return null;
	}

	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();

		UltrasonicPoller up = UltrasonicPoller.getInstance();
		Odometer odo = Odometer.getInstance();
		Driver.setSpeed(30);
		Driver dr = Driver.getInstance();
		ObjRec or = new ObjRec();
		Configuration config = Configuration.getInstance();
		CaptureMechanism cm = CaptureMechanism.getInstance();
		RemoteConnection rc = RemoteConnection.getInstance();

		up.start();
		odo.start();
		dr.start();
		
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

				ArrayList<ObjRec.blockColor> color = or.detectA();

				//		
				if(color == null || color.size() == 0)
					lcd.writeToScreen("EMPTY", 1);
				else{
					int count = 1;
					//TODO: extract this into method that returns only one color
					for(ObjRec.blockColor c : color)
						if(c != null){
							lcd.writeToScreen( c.name(), count++);
							if(c.getCode() == BLOCK_COLOR){
								Sound.beepSequenceUp();

								rc.setupConnection();

//								writer.writeToScreen("Connection passed.", 0);
//								int d = up.getDistance();
								
								cm.open();

								dr.forward(5);
								
								cm.align();
								
								dr.forward(15);
								
								cm.close();
								blockFound = true;
								break;
							}
							else{
								cm.removeBlock();
							}
						}
				}
			}
			
			dr.travelTo(p);
		}
		
		Sound.beepSequenceUp();		

	}

}
