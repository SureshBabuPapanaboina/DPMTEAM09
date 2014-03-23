package objectdetection;

import lejos.nxt.NXTRegulatedMotor;
import robotcore.Configuration;
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
	
	public boolean scanTile(){
		//TODO: might need to make this event driven
		
		//check left side
		boolean obstacle = false;
		
		sensorMotor.rotateTo(-45, true);
		while(sensorMotor.getPosition() > -45){
			if(up.getDistance() < 40){
				obstacle = true;
			}
		}
		sensorMotor.rotateTo(45, true);
		while(sensorMotor.getPosition() > 45){
			if(up.getDistance() < 40){
				obstacle = true;
			}
		}
		
		sensorMotor.rotateTo(0);
		
		return obstacle;
	}
	
}
