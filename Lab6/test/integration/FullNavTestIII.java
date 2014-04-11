package integration;

import java.util.ArrayList;
import java.util.Stack;

import capture.CaptureMechanism;
import communication.RemoteConnection;
import bluetoothclient.BluetoothReceiver;
import movement.Driver;
import navigation.Map;
import navigation.PathTraveller;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.robotics.navigation.Waypoint;
import localization.Localization;
import localization.LocalizationI;
import objectdetection.ObstacleDetector;
import objectdetection.Trajectory;
import odometry.Odometer;
import odometry.OdometerCorrection;
import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import search.ObjRec;
import search.ObjRec.blockColor;
import sensors.LineReader;
import sensors.UltrasonicPoller;

/**
 * Basic test to scan a tile in front of the robot
 * Place obstacle at different points in the tile and see if the scanner picks up
 * the obstacle correctly. This is an extension of the fullNaveTest with inteegrated Localization 
 * 
 * NOTE: This is without odometry correction and pretty slow.
 * 
 * @author Peter Henderson
 *
 */
public class FullNavTestIII {

	private static boolean followPath(){
		PathTraveller t = PathTraveller.getInstance();
		Driver driver = Driver.getInstance();
		ObstacleDetector detector = ObstacleDetector.getInstance();
		Map map = Map.getInstance();
		Configuration conf = Configuration.getInstance();
		
		while(!t.pathIsEmpty()){
			Waypoint next = t.popNextWaypoint();
			//Turn to the next tile
			driver.turnTo(Coordinate.calculateRotationAngle(
												Odometer.getInstance().getCurrentCoordinate(), 
												new Coordinate(next)));
			
			//scan the next area
			if(detector.scanTile()){
				//block next tile
				map.blockNodeAt(next.x, next.y);
				return false;
			}
			
			driver.travelTo(new Coordinate(next));
		}
		
		return true;
			
	}
	
	private static int getFilteredDistance(){
		int[] distances = UltrasonicPoller.getInstance().getFloatingRange();
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
//			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
			
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
				return new Trajectory(-theta-3, dist-5);
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
		//===============get info from blue tooth ========\
		
		BluetoothReceiver br = new BluetoothReceiver(); 
		br.listenForStartCommand();// info in Config should be set 
		
		
		
		//=================INIT THREADS==========================
		LCDWriter lcd = LCDWriter.getInstance();
		Configuration conf = Configuration.getInstance();		
		UltrasonicPoller up = UltrasonicPoller.getInstance();
		PathTraveller traveller = PathTraveller.getInstance();
		LineReader llr = LineReader.getLeftSensor();	//left + right line reader
		LineReader rlr = LineReader.getRightSensor();
		Odometer odo = Odometer.getInstance();
		OdometerCorrection oc = OdometerCorrection.getInstance();
		Driver dr = Driver.getInstance();

		up.start();
		odo.start();
		llr.start();
		rlr.start();
		lcd.start();
		
		//=================INIT var ==========================
		Coordinate [] flagZone = conf.getFlagZone();
		//=====================INIT END=========================
		
		//do localization 
		LocalizationI.localizeAndMoveToStartLoc();
		
		
		//===============end of localization==================== 
		
		//start odometer correction
		LineReader.subscribeToAll(oc);
		
		try {Thread.sleep(1000);}catch(Exception e){};

		//set  destination
		//		traveller.recalculatePathToCoords(135,105);
		traveller.recalculatePathToCoords((int)flagZone[0].getX()+15,(int) flagZone[0].getY()-15);
		
		//movement loop
		boolean done  = false;
		while(!done){
			done = followPath();
			try{
			if(!done)
//				traveller.recalculatePathToCoords(135,105);
				traveller.recalculatePathToCoords((int)flagZone[0].getX(),(int) flagZone[0].getY());
			else break;
			}
			catch(Exception e){
				lcd.writeToScreen("E: "+ e.toString(), 1);
			}
		}
		
		//==========Foam looing and grabbing init  ============
		
		//indicate finish
		Sound.beepSequenceUp();	

		RemoteConnection rc = RemoteConnection.getInstance();
		CaptureMechanism cm = CaptureMechanism.getInstance();
		Stack<Coordinate> path = new Stack<Coordinate>();
		
		path.push(new Coordinate((int)flagZone[1].getX()-15, (int)flagZone[0].getY()+15, 0)); //last point
		path.push(new Coordinate((int)flagZone[1].getX()-15, (int)flagZone[1].getY()-15, 0));
		path.push(new Coordinate((int)flagZone[0].getX()+15, (int)flagZone[1].getY()-15, 0)); //move forward
		path.push(new Coordinate((int)flagZone[0].getX()+15, (int)flagZone[0].getY()+15, 0)); //enter the zone;
		path.push(new Coordinate((int)flagZone[1].getX()-15, (int)flagZone[0].getY()+15, 0)); //last point
		path.push(new Coordinate((int)flagZone[1].getX()-15, (int)flagZone[1].getY()-15, 0));
		path.push(new Coordinate((int)flagZone[0].getX()+15, (int)flagZone[1].getY()-15, 0)); //move forward
		path.push(new Coordinate((int)flagZone[0].getX()+15, (int)flagZone[0].getY()+15, 0)); //enter the zone;
		
		ObjRec or = new ObjRec();
		
		//=================block Rec ============
		blockColor BLOCK_COLOR = conf.getBlockColor(); //yellows
		
		boolean blockFound  = false;
		while(!blockFound && !path.isEmpty()){
			Coordinate p = path.pop();
			dr.turnTo(Coordinate.calculateRotationAngle(Configuration.getInstance().getCurrentLocation(), p));

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
							if(c == BLOCK_COLOR){
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
							if(c == BLOCK_COLOR){
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
