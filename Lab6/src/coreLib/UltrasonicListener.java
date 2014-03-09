package coreLib;

/**
 * these are the interface for classes that listens to the ultrasonic poller
 * it contains a method ultrasonicDistance(int distance); which obtains the
 * distance measured by the ultrasonic poller and make actions accordinly.
 * @author yuechuan
 *
 */
public interface UltrasonicListener {
	
	//TODO make the ultrasonicPoller create a new thread every time it handles the Listener!!!!
	/**
	 * this is the method that ultrasonic poller will execute in a Thread 
	 * when the threshold distance is reached.
	 * @param distanceFromObsticle
	 */
	public void ultrasonicDistance(int distanceFromObsticle);
	
	/**
	 * 
	 * @return the threshold distance 
	 */
	public int getDistanceOnInvoke ();
	/**
	 * set the threshold distance 
	 * @param distance
	 * @return
	 */
	public UltrasonicListener setDistanceOnInvoke (int distance);
	
	
	/**
	 * @return true if this can be called again and again 
	 */
	public boolean isContinuous();
	
	/**
	 * set if the listener can be reexecuted 
	 * @param continuous true if it can be executed over and over in a loop until
	 * the end of time 
	 * @return itself
	 */
	public UltrasonicListener setContinuous(boolean continuous );

	/**
	 * isCalled is set to true when it was called
	 * @return true if it was called.
	 */
	public boolean isCalled();
	/**
	 * set the called states
	 * @param called
	 * @return itself 
	 */
	public UltrasonicListener setCalled(boolean called);
	
	

}
