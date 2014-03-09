package coreLib;

import java.util.ArrayList;

import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.RConsole;


/**
 * ultrasonicPoller will poll the ultrasonic sensor periodically and call which ever UltrasonicListener 
 * that should run when reaching certain distance. this is a event driven approach and should be (hopefully) faster 
 * than the while loop setting flag approach 
 * 
 * TODO add public enable() pause()
 * @author yuechuan
 *
 */
public class UltrasonicPoller extends Thread implements UltrasonicPlanner {
	private int SLEEP_INTERVAL = 50 ;
	AbstractConfig config;
	UltrasonicSensor uSensor;

	
	private int distance = 25; //initialize the distance read to 25
	/**
	 * the previous distance 
	 */
	private int prevDist = distance;
	private static UltrasonicPoller instance ;
	private static boolean threadStarted = false ;
	private ArrayList <UltrasonicListener> usListenerList = new ArrayList<UltrasonicListener>();
	private static boolean listenerExecutionDisabled = false ;
	
	private UltrasonicPoller (AbstractConfig config){
		this.config = config;
		uSensor = new UltrasonicSensor(AbstractConfig.ULTRASONIC_SENSOR_PORT);
		//start the sensor 
		uSensor.continuous();
		//filter the distance and get 10 times polling avg
		double mean = 0 ;
		for (int i = 0 ; i < 10 ; i ++ ){
			mean = ((mean*i) + uSensor.getDistance())/i;
		}
		distance = (int) mean ;
	}
	
	public static UltrasonicPoller getInstance(){
		if (instance == null){
			instance = new UltrasonicPoller(AbstractConfig.getInstance());
		}
		return instance;
	}
	
	public void run (){
		threadStarted = true ;
		while (!AbstractConfig.getInstance().isDriveComplete()){
			prevDist = distance;
			distance = uSensor.getDistance();

			LCDWriter.getInstance().writeToScreen("Dist " + distance, 7);
			if (!listenerExecutionDisabled){
				for (final UltrasonicListener usw : usListenerList){
					//if the distance is within range.
					//if it has not been called
					//or if it should be called continuously
					if (usw.getDistanceOnInvoke() >= distance && (!usw.isCalled() || usw.isContinuous())){
						new Thread (){
							public void run(){
								usw.ultrasonicDistance(distance);		
							}
						}.run();
						usw.setCalled(true);
					}
				}
			}
			
			try { Thread.sleep(SLEEP_INTERVAL); } catch(Exception e){};
		} 
		threadStarted = false;
	}
	/**
	 * subscribe the listener iff there is no doublecates in the list.
	 * or else update the values in the list
	 * @param uListener
	 * @param distanceOnInvoke
	 * @param continuous
	 * @return true if subscriber is doublecated 
	 */
	public boolean subscribe(UltrasonicListener uListener){
		boolean subscribe =!containsListener(uListener);
		if (subscribe){
			usListenerList.add(uListener);
		}
		return subscribe;
	}
	public static void enableULinsteners(){
		listenerExecutionDisabled = false;
	}
	public static void disableULinsteners(){
		listenerExecutionDisabled = true;
	}
	
	/**
	 * 
	 * @param ulistener
	 * @return true if the listener exist 
	 */
	public boolean containsListener (UltrasonicListener ulistener ){
		boolean exist = false ;
		for (UltrasonicListener usl : usListenerList){
			if (usl.equals(ulistener)){
				exist = true;
				break;
			}
		}
		return exist ;
	}
	
	/**
	 * 
	 * @param ulistener 
	 * @return true when unsubscribe successful, if the item does not exist in the 
	 * stack then return false 
	 */
	public boolean unsubscribe(UltrasonicListener ulistener){
		boolean removed = false ;
		for (UltrasonicListener usl: usListenerList){
			if (usl.equals(ulistener)){
				usListenerList.remove(usl);
				removed = true ;
			}
		}
		return removed;
	}
	
	/**
	 * distance refresh rate 20hz 
	 * @return
	 */
	public int getDistance() {
		return distance;
	}
	

	/**
	 * check for threashold and return true if there is a possible block 
	 * @return 
	 */
	public boolean objectDetected(){
		boolean result = false;
		if (Math.abs(prevDist - distance) > 5 && distance < 100  ){
			RConsole.println("prevDist" + prevDist + "\t\tcurrentDist" + distance );
			result = true ;
		}
		return result;
		
	}
	
	public static boolean isThreadStarted() {
		return threadStarted;
	}

	public static void setThreadStarted(boolean threadStarted) {
		UltrasonicPoller.threadStarted = threadStarted;
	}

}
