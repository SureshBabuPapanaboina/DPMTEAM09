package sensors;

import java.util.ArrayList;

import robotcore.Configuration;
import robotcore.LCDWriter;
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
public class UltrasonicPoller extends Thread {
	Configuration config = Configuration.getInstance();
	UltrasonicSensor uSensor;
	private ArrayList <UltrasonicListener> usListenerList = new ArrayList<UltrasonicListener>(); //lst of listeners

	private int pollingInterval = 50 ;	//in ms
	private int currentDist = 25; //initialize the distance read to 25
	/**
	 * the previous distance stored
	 */
	private int prevDist = currentDist;
	private boolean rotate;
	private boolean goingRight= false;
	private boolean rotationStarted;
	private int[] prevFloatDist;
	private boolean distanceUpdated = false ;
	private static UltrasonicPoller instance ;
	private static boolean listenerExecutionDisabled = false ;	//true if we want to disable the ultrasonic listener and only allow passive pulling 
	private final int FLOATING_RANGE = 5;		//number of readings to take before returning the average of the results //reduced from 5 to 3 to save ressources 
	private int count;
	private Object lock = new Object();
	/**
	 * Private constructor
	 */
	private UltrasonicPoller (){
		uSensor = new UltrasonicSensor(Configuration.ULTRASONIC_SENSOR_PORT);
		uSensor.continuous();	//start the sensor 

		//filter the distance and get 10 times polling avg
		double sum = 0;
		count = 0;
		prevFloatDist = new int[FLOATING_RANGE];
		
		for (int i = 0 ; i < FLOATING_RANGE ; i ++ ){
			prevFloatDist[count++%FLOATING_RANGE] = uSensor.getDistance();
			count%=FLOATING_RANGE;
			//what is this.... //take the moving average without using an extra array.
			sum += uSensor.getDistance();
		}
		sum /= FLOATING_RANGE;
		currentDist = (int) sum ;
	}
	
	/**
	 * Since is a singleton, get the instance?

	 * @return
	 */
	public static UltrasonicPoller getInstance(){
		if (instance == null){
			instance = new UltrasonicPoller();
		}
		return instance;
	}
	
	public void setRotating(boolean yes){
		synchronized(lock){
			this.rotate = yes;
			this.rotationStarted = false;
		}
	}
	
	public int[] getFloatingRange(){
		return prevFloatDist;
	}
	
	/**
	 * Runs the poller thread
	 */
	public void run (){
		while (!config.isDriveComplete()){
			
			prevDist = currentDist;
			currentDist = uSensor.getDistance();
			prevFloatDist[count++%5] = currentDist;
			count%=5;
			distanceUpdated = true ;

			LCDWriter.getInstance().writeToScreen("Dist " + currentDist, 3);
			LCDWriter.getInstance().writeToScreen("FDist " + currentDist, 1);
			if (!listenerExecutionDisabled){
				for (final UltrasonicListener usw : usListenerList){
					//if the distance is within range.
					//if it has not been called
					//or if it should be called continuously
					//then start a new Thread to run the code specified in the UltrasonicListener 
					if (usw.getDistanceOnInvoke() >= currentDist && (!usw.isCalled() || usw.isContinuous())){
						new Thread (){
							public void run(){
								usw.ultrasonicDistance(currentDist);		
							}
						}.run();
						usw.setCalled(true);
					}
				}
			}
			try { Thread.sleep(pollingInterval); } catch(Exception e){};
		} 
	}
	
	/**
	 * subscribe the listener to the ultrasonicPoller
	 *  if and only if there are no duplicates in the Arraylist that keeps the UltrasonicListeners.
	 * or else update the values in the list. if the UltrasonicListener already exist in the list
	 * then do nothing and return true.
	 * @param uListener
	 * @param distanceOnInvoke
	 * @param continuous
	 * @return true if subscriber is duplicated 
	 */
	public boolean subscribe(UltrasonicListener uListener){
		boolean subscribe =!containsListener(uListener);
		if (subscribe){
			usListenerList.add(uListener);
		}
		return subscribe;
	}
	/**
	 * enable the execution of UltrasonicLister code
	 */
	public static void enableULinsteners(){
		listenerExecutionDisabled = false;
	}
	/**
	 * disable the execution of UltrasonicLister code
	 */
	public static void disableULinsteners(){
		listenerExecutionDisabled = true;
	}
	
	/**
	 * checks if the {@link UltrasonicListener} is already present in the 
	 * arraylist of listeners.
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
	 *  remove a {@link UltrasonicListener} from the arraylist of executables
	 * @param uListener 
	 * @return true when unsubscribe successful, if the item does not exist in the 
	 * stack then return false 
	 */
	public boolean unsubscribe(UltrasonicListener uListener){
		boolean removed = false ;
		for (UltrasonicListener usl: usListenerList){
			if (usl.equals(uListener)){
				usListenerList.remove(usl);
				removed = true ;
			}
		}
		return removed;
	}
	
	/**
	 * Get the last distance value polled from sensor.<br><b>Note</b><br>it is possible to
	 * call this method many times before the distance value is updated again from the sensor.
	 * the mean calculated from these data will be wrong. a way to avoid polling the value numerous
	 * times is by using the method hasDistanceUpdated(), example code :
	 * 
	 * {@code }<br>if (hasDistanceUpdated()) value = getDistance() <br><br>

	 * distance refresh rate around 20hz 
	 * @return
	 */
	public int getDistance() {
		distanceUpdated  = false ;
		int distance;
		synchronized(lock){
			distance = currentDist;
		}
		return distance;
	}
	
	/**
	 * take more readings and filter out noise 
	 * @return filtered distance 
	 */
	public int getFilteredDistance(){
		int[] distances;
		synchronized(lock){
			distances = getFloatingRange();
		}
		int sum = 0;
		for(int i = 0; i< distances.length;i++){
			sum+=distances[i];
		}
		sum /= distances.length;
		
		return sum;
	}
	
	/**
	 * @return true when the distance is updated since the last time
	 * getDistance is called.
	 */
	public boolean hasDistanceUpdated(){
		return distanceUpdated;
	}
	
	/**
	 * check for threshold and return true if there is a possible block 
	 * @deprecated no reason to use this , but forgot why I put it here 
	 * @return 
	 */
	public boolean objectDetected(){
		boolean result = false;
		if (Math.abs(prevDist - currentDist) > 5 && currentDist < 100  ){
			RConsole.println("prevDist" + prevDist + "\t\tcurrentDist" + currentDist );
			result = true ;
		}
		return result;
		
	}
	/**
	 * return the chipped distance where the range lies between 0 - maxDist 
	 * @param maxDist the maximum distance allowed, any value bigger than this is set to this value
	 * @return
	 */
	public int getChippedDistance(int maxDist){
		int d = getDistance();
		return (d > maxDist) ? maxDist : d;
	}

}
