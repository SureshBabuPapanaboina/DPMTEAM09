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
		sensorMotor.setSpeed(38);
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
		
		sensorMotor.rotateTo(-50, true);
		//top part of square
		while(sensorMotor.getPosition() > -19){
			nap(10);
						
			if(up.getDistance() < 45/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				LCDWriter.getInstance().writeToScreen("P: " + 30/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition()))) , 2);
				Sound.beep();
				obstacle = true;
			}
		}
		//left section of square
		while(sensorMotor.getPosition() > -40){
			nap(10);
			LCDWriter.getInstance().writeToScreen("P: " +  Math.abs(15/Math.sin(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15), 2);
			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

			if(up.getDistance() < Math.abs(15/Math.sin(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15)){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				Sound.beep();
				obstacle = true;
			}
		}	
		
		while(sensorMotor.getPosition() > -50){
			nap(10);
			LCDWriter.getInstance().writeToScreen("P: " +  Math.abs(15/Math.sin(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15), 2);
			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

			if(up.getDistance() < 15){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				Sound.beep();
				obstacle = true;
			}
		}
//		while(sensorMotor.getPosition() > -36){
//			nap(25);
//			LCDWriter.getInstance().writeToScreen("P: " +  Math.abs(15/Math.sin(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15), 2);
//			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
//
//			if(up.getDistance() < Math.abs(15/Math.sin(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15)){
////				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
////				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
//				Sound.beep();
//				obstacle = true;
//			}
//		}	
		
		//This is because at close ranges the up messes up the distance...
		//TODO: Improve this hack
//		while(sensorMotor.getPosition() > -45){
////			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
////			LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
//
//			nap(25);
//			if(up.getDistance() < 15){
//				Sound.beep();
//				obstacle = true;
//			}
//		}
		
		if(obstacle){
			sensorMotor.rotateTo(0, true);
			return true;
		}
		
		//NOTE: Once the sensor goes back to the center of the arm this need to change back
		sensorMotor.rotateTo(50, true);
		
		while(sensorMotor.getPosition() < 0); //wait
		
		while(sensorMotor.getPosition() < 19){
			nap(10);
					
			if(up.getDistance() < 45/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				LCDWriter.getInstance().writeToScreen("P: " + 30/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition()))) , 2);

				Sound.beep();
				obstacle = true;
			}
		}
		
		while(sensorMotor.getPosition() < 45){
			nap(10);
//			LCDWriter.getInstance().writeToScreen("P: " +  Math.abs(15/Math.sin(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15), 2);
//			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

			if(up.getDistance() < Math.abs(15/Math.sin(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15)){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				Sound.beep();
				obstacle = true;
			}
		}	
		
		while(sensorMotor.getPosition() < 50){
			nap(10);
//			LCDWriter.getInstance().writeToScreen("P: " +  Math.abs(15/Math.sin(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15), 2);
//			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

			if(up.getDistance() < 15){
				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
				Sound.beep();
				obstacle = true;
			}
		}	
		
//		while(sensorMotor.getPosition() < 36){
//			nap(25);
//			
//			if(up.getDistance() < Math.abs(15/Math.sin(Math.toRadians(sensorMotor.getPosition()))-15)){
////				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
////				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
////				LCDWriter.getInstance().writeToScreen("P: " +  15/Math.cos(Math.toRadians(90-sensorMotor.getPosition())), 2);
//
//				Sound.beep();
//				obstacle = true;
//			}
//		}
//		
//		while(sensorMotor.getPosition() > 45){
//			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
//			LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
//
//			nap(25);
//			if(up.getDistance() < 15){
//				Sound.beep();
//				obstacle = true;
//			}
//		}
		
		
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
