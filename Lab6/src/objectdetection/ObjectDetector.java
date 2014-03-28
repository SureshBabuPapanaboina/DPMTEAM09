package objectdetection;
import odometry.Odometer;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import movement.Driver;
import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;
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
public class ObjectDetector extends Thread{
	
	private static ObjectDetector instance;
	private UltrasonicPoller up;
	private Configuration config;
	private NXTRegulatedMotor sensorMotor;
	private boolean objectFound = false;
	private Trajectory currentObjectT;
	private Object lock;
	
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
		lock = new Object();
	}
	
	/**
	 * Returns the singleton objectDetector
	 * @return
	 */
	public static ObjectDetector getInstance(){
		if(instance == null) instance = new ObjectDetector();
		
		return instance;
	}
	
	public boolean objectFound(){
		boolean t;
//		synchronized(lock){
			t = objectFound;
//		}
		return t;
	}
	
	public Trajectory getTrajectory(){
		Trajectory t;
//		synchronized(lock){
			t = currentObjectT;
//		}
		return t;
	}
	
	@Override
	public void run(){
		Trajectory object = null;
		while(object == null){
			object = findObjectLocation();
		}
		
		Driver.getInstance().motorStop();
		Sound.beep();
		Sound.beep();
		objectFound = true;
		currentObjectT = object;
	
	}
	
	/**
	 * Rotates the sensorMotor and interrupts the driver if there is in fact
	 * an object, raises a flag
	 * 
	 * @return
	 */
	public Trajectory findObjectLocation(){
		sensorMotor.rotateTo(-55, true);
//		LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

		//TODO: better algorithm
		while(sensorMotor.getPosition() > -55){
			nap(15);
			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

			if(up.getDistance() < 25){
				sensorMotor.rotateTo(0);
				return new Trajectory(-sensorMotor.getPosition()+5, up.getDistance());
			}
		}
	
		sensorMotor.rotateTo(55, true);
		
		while(sensorMotor.getPosition() < 55){
			nap(15);
			LCDWriter.getInstance().writeToScreen("D: " + up.getDistance(), 0);

			if(up.getDistance() < 25){
//				sensorMotor.stop();
				sensorMotor.rotateTo(0);
				return new Trajectory(-sensorMotor.getPosition()-5, up.getDistance());
			}
		}
		
		return null;
	}
	
	/**
	 * Checks if the flag is actually in the end zone
	 * 
	 * @param error
	 * @return
	 */
	public boolean isInZoneWithError(int error){
		return true;
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
