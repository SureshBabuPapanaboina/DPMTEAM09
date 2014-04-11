package objectdetection;

import communication.RemoteConnection;
import lejos.nxt.ColorSensor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.remote.RemoteNXT;
import robotcore.Configuration;
import robotcore.LCDWriter;
import sensors.UltrasonicPoller;

/**
 * This is a class used to perform the pass scanning of the next tile of the path
 * 
 * @author Peter Henderson
 *
 */
public class ObstacleDetector {

	private static ObstacleDetector instance;
	private UltrasonicPoller up;
	private NXTRegulatedMotor sensorMotor;
	ColorSensor cs = new ColorSensor(Configuration.COLOR_SENSOR_PORT);
	public final boolean TILE_INCREMENTS = true; 

	private ObstacleDetector(){
		up = UltrasonicPoller.getInstance();
		sensorMotor = Configuration.SENSOR_MOTOR;
		sensorMotor.setSpeed(50);
	}
	
	public static ObstacleDetector getInstance(){
		if(instance == null) instance  = new ObstacleDetector();
		
		return instance;
	}
	
	/**
	 * scans only 70 degrees directly in front of the robot
	 * @return
	 */
	public boolean limitedSafeScan(){
		
		sensorMotor.rotateTo(35);
		
		sensorMotor.rotateTo(-35);
		
		while(sensorMotor.getPosition() > -35){
			nap(15);
			if(up.getFilteredDistance() < 20){
				Sound.beepSequenceUp();
				sensorMotor.stop();
				sensorMotor.rotateTo(0);
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * This method scans the tile immediately in front of it. It assumes that the robot
	 * is align straight facing the tile and does not scan the surrounding tiles. This method 
	 * also checks two side ultrasonic sensors attached to the remote brick
	 * @return
	 */
	public boolean scanTileWithSideSensors(){

		boolean obstacle = false;
		
		sensorMotor.rotateTo(-30, true);
		
		while(sensorMotor.getPosition() > -30){
			if(up.getFilteredDistance() < (TILE_INCREMENTS ? 30 : 17)){
				Sound.beep();
				obstacle = true;
			}
		}

		if(obstacle){
			sensorMotor.rotateTo(0, true);
			return true;
		}
		
		//NOTE: Once the sensor goes back to the center of the arm this need to change back
		sensorMotor.rotateTo(35, true);
	
		while(sensorMotor.getPosition() < 35){
			if(up.getFilteredDistance() < (TILE_INCREMENTS ? 32 : 17)){
				Sound.beep();
				obstacle = true;
			}
		}	
		
		sensorMotor.rotateTo(0, true);
		
		
		RemoteNXT rm = RemoteConnection.getInstance().getRemoteNXT();
		
		//Check the two side sensors
		UltrasonicSensor leftSide = new UltrasonicSensor(rm.S1);
		UltrasonicSensor rightSide = new UltrasonicSensor(rm.S2);
		int ld = leftSide.getDistance();
		int rd = rightSide.getDistance();
		if( ld < (TILE_INCREMENTS ? 30 : 17) || rd < (TILE_INCREMENTS ? 30 : 17)){
			LCDWriter.getInstance().writeToScreen("LD: " + ld, 6);
			LCDWriter.getInstance().writeToScreen("rD: " + rd, 7);
			obstacle = true;
		}
		
		return obstacle;
		
	}

	
	/**
	 * This method scans the tile immediately in front of it. It assumes that the robot
	 * is align straight facing the tile and does not scan the surrounding tiles
	 * @return
	 */
	public boolean scanTile(){
		boolean obstacle = false;
		
		sensorMotor.rotateTo(-50, true);
		//top part of square
		while(sensorMotor.getPosition() > -17){
			nap(10);
						
			if(up.getFilteredDistance() < 45/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15){
				Sound.beep();
				obstacle = true;
			}
		}
		

		//left section of square
		while(sensorMotor.getPosition() > -50){
			nap(10);
			LCDWriter.getInstance().writeToScreen("P: " +  Math.abs(20/Math.cos(Math.toRadians(90-Math.abs(sensorMotor.getPosition())))-15), 5);
			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 6);

			if(up.getFilteredDistance() < Math.abs(15/Math.cos(Math.toRadians(90-Math.abs(sensorMotor.getPosition())))-15)){
				Sound.beep();
				obstacle = true;
			}
		}
		
		if(obstacle){
			sensorMotor.rotateTo(0, true);
			return true;
		}
		
		//NOTE: Once the sensor goes back to the center of the arm this need to change back
		sensorMotor.rotateTo(50, true);
		
		while(sensorMotor.getPosition() < 19){
			nap(10);
					
			if(up.getFilteredDistance() < 45/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				LCDWriter.getInstance().writeToScreen("P: " + 30/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition()))) , 2);

				Sound.beep();
				obstacle = true;
			}
		}
//		
		while(sensorMotor.getPosition() < 50){
			nap(10);

			if(up.getFilteredDistance() < Math.abs(15/Math.cos(Math.toRadians(90-Math.abs(sensorMotor.getPosition())))-15)){
				Sound.beep();
				obstacle = true;
			}
		}	
		
		sensorMotor.rotateTo(0, true);
		
		return obstacle;
	}
	
	private void nap(int m){
		try {
			Thread.sleep(m);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
