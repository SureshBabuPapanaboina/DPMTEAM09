package objectdetection;
import odometry.Odometer;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import robotcore.Configuration;
import robotcore.Coordinate;
import sensors.ColorPoller;
import sensors.UltrasonicPoller;

/**
 * This class uses the ColorPoller and the UltraSonicPoller to determine if an object is 
 * within a collision range, if it should be investigated and whether it is the correct
 * color block or not
 * 
 * @author Peter Henderson
 *
 */
public class ObjectDetector{
	
	private static ObjectDetector instance;
	private UltrasonicPoller up;
	private Configuration config;
	private NXTRegulatedMotor sensorMotor;
	
	/**
	 * constructor
	 * @param cp
	 * @param up
	 */
	private ObjectDetector(){
		up = UltrasonicPoller.getInstance();
		config = Configuration.getInstance();
		sensorMotor = config.SENSOR_MOTOR;
		sensorMotor.setSpeed(45);
	}
	
	/**
	 * Returns the singleton objectDetector
	 * @return
	 */
	public ObjectDetector getInstance(){
		if(instance == null) instance = new ObjectDetector();
		
		return instance;
	}
	
	public Trajectory findObjectLocation(){
		
		sensorMotor.rotateTo(-45, true);
		
		int t = 0;
		int d = 0;
		//top part of square
		while(sensorMotor.getPosition() > -18){
			nap(25);
						
			if(up.getDistance() < 45/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15){
//				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
//				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
//				LCDWriter.getInstance().writeToScreen("P: " + 30/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition()))) , 2);
				Sound.beep();
				t = sensorMotor.getPosition();
				d = up.getDistance();
			}
		}
		//left section of square
		while(sensorMotor.getPosition() > -45){
			nap(25);

			if(up.getDistance() < 15/Math.cos(Math.toRadians(90-sensorMotor.getPosition()))-15){
//				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
//				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
//				LCDWriter.getInstance().writeToScreen("P: " +  15/Math.cos(Math.toRadians(90-sensorMotor.getPosition())), 2);
				Sound.beep();
				t = sensorMotor.getPosition();
				d = up.getDistance();
			}
		}
		
		if(d != 0){
			sensorMotor.rotateTo(0, true);
//			return new Coordinate(Odometer.getInstance().getX() + 
//					Math.sin(Math.toRadians(t))*(15+d), 
//					Odometer.getInstance().getY() + 
//					Math.cos(Math.toRadians(t))*(15+d), 0);
		}
		
		sensorMotor.rotateTo(45, true);
		
		while(sensorMotor.getPosition() < 0); //wait
		
		while(sensorMotor.getPosition() < 18){
			nap(25);
					
			if(up.getDistance() < 45/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition())))-15){
//				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
//				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
//				LCDWriter.getInstance().writeToScreen("P: " + 30/Math.cos(Math.toRadians(Math.abs(sensorMotor.getPosition()))) , 2);

				Sound.beep();
			}
		}
		
		while(sensorMotor.getPosition() < 45){
			nap(25);
			
			if(sensorMotor.getPosition() < 33) continue;

			
			if(up.getDistance() < 15/Math.cos(Math.toRadians(90-sensorMotor.getPosition()))-15){
//				LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);
//				LCDWriter.getInstance().writeToScreen("T: " + sensorMotor.getPosition() , 1);
//				LCDWriter.getInstance().writeToScreen("P: " +  15/Math.cos(Math.toRadians(90-sensorMotor.getPosition())), 2);

				Sound.beep();
			}
		}
		
		sensorMotor.rotateTo(0, true);
		
		return null;
		
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
