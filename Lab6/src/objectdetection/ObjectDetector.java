package objectdetection;
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
	
	/**
	 * constructor
	 * @param cp
	 * @param up
	 */
	private ObjectDetector(ColorPoller cp, UltrasonicPoller up){
		//TODO: remove these and add references to singletons
		
	}
	
	/**
	 * Returns the singleton objectDetector
	 * @return
	 */
	public ObjectDetector getInstance(){
		return this;
	}
	
	/**
	 * This sends a callback to the StateMachine that there is a collision coming
	 * @return boolean
	 */
	public void collisionIsEminent(){
	}
	
	/**
	 * Determines if the object is in the target area and 
	 * whether the robot should investigate the object's color
	 * @return
	 */
	public boolean shouldInvestigate(){
		return false;
	}
	
	/**
	 * Determines if the block is the correct color based on the config file
	 * and the value from the colorPoller
	 * @return
	 */
	public boolean isCorrectColor(){
		return false;
	}
	
}
