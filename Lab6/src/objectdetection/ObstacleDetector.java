package objectdetection;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
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
	private Configuration config;
	private NXTRegulatedMotor sensorMotor;
	
	private ObstacleDetector(){
		up = UltrasonicPoller.getInstance();
		config = Configuration.getInstance();
		sensorMotor = config.SENSOR_MOTOR;
		sensorMotor.setSpeed(45);
	}
	
	public static ObstacleDetector getInstance(){
		if(instance == null) instance  = new ObstacleDetector();
		
		return instance;
	}
	
	/**
	 * This method scans the tile immediately in front of it. It assumes that the robot
	 * is align straight facing the tile and does not scan the surrounding tiles
	 * @return
	 */
	public boolean scanTile(){
		//TODO: might need to make this event driven
		//TODO: CLEAN THIS UP, BUT THIS WORKS
		//check left side
		boolean obstacle = false;
		
		sensorMotor.rotateTo(-45, true);
		//top part of square
		while(sensorMotor.getPosition() > -18){
			nap(25);
						
			if(up.getDistance() < 45/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				LCDWriter.getInstance().writeToScreen("P: " + 30/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition()))) , 2);
				Sound.beep();
				obstacle = true;
			}
		}
		//left section of square
		while(sensorMotor.getPosition() > -45){
			nap(25);

			if(up.getDistance() < 15/Math.cos(Math.toRadians(90-sensorMotor.getPosition()))-15){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				LCDWriter.getInstance().writeToScreen("P: " +  15/Math.cos(Math.toRadians(90-sensorMotor.getPosition())), 2);
				Sound.beep();
				obstacle = true;
			}
		}	
		
		if(obstacle){
			sensorMotor.rotateTo(0);
			return true;
		}
		
		sensorMotor.rotateTo(45, true);
		
		while(sensorMotor.getPosition() < 0); //wait
		
		while(sensorMotor.getPosition() < 18){
			nap(25);
					
			if(up.getDistance() < 45/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				LCDWriter.getInstance().writeToScreen("P: " + 30/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition()))) , 2);

				Sound.beep();
				obstacle = true;
			}
		}
		
		while(sensorMotor.getPosition() < 45){
			nap(25);
			
			if(sensorMotor.getPosition() < 33) continue;

			
			if(up.getDistance() < 15/Math.cos(Math.toRadians(90-sensorMotor.getPosition()))-15){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				LCDWriter.getInstance().writeToScreen("P: " +  15/Math.cos(Math.toRadians(90-sensorMotor.getPosition())), 2);

				Sound.beep();
				obstacle = true;
			}
		}
		
		sensorMotor.rotateTo(0);
		
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
