package sensors;


import java.util.ArrayList;

import robotcore.Configuration;
import lejos.nxt.ColorSensor;
/**
 * this is used to read line from the grund with the colorReader
 * @author yuechuan
 *	@version 1.3
 */
public class LineReader extends Thread{
	private static final int SLEEP_TIME = 25;
	private static final int IGNORE_INTERVAL = 300;	//how long to ignore after a line has seen to avoid over sensitive
	/**
	 * if this a left sensor then true 
	 * else false 
	 */
	private boolean isLeft ;
	private ColorSensor colorSensor; 
	private Configuration config;
	private int previousSensedValue , currentSensedValue ;
	private boolean passedLine = false ;
	private long sensorStartTime;
	 
	
	private static LineReader leftLineReader ;
	private static LineReader rightLineReader ;

	/**
	 * contains a list of classes to call when a line is detected. A
	 * linkedList is used in this implementation since there seem to be 
	 * no support for HashSet and the other Set seem to be deprecated 
	 * 
	 * <br>
	 * 
	 * <b> note <b> there are two instances that need to be started : left and right Sensor
	 * either of them will occupy one thread. 
	 */
	private ArrayList<LineReaderListener> lrlistenerList = new ArrayList<LineReaderListener>();
	
	/**
	 * create a line reader Sensor
	 * @param config
	 * @param left if true then this is the left sensor else right 
	 */
	private LineReader(Configuration config,boolean left){
		//left or right sensor 
		colorSensor  = left? new ColorSensor(Configuration.LINE_READER_LEFT) : new ColorSensor(Configuration.LINE_READER_RIGHT);
		isLeft = left;
		this.config = config;
	}
	
	public static LineReader getLeftSensor(){
		if (leftLineReader == null){ leftLineReader = new LineReader(Configuration.getInstance(),true); 	}
		return leftLineReader;
	}
	public static LineReader getRightSensor(){
		if (rightLineReader == null){ rightLineReader = new LineReader(Configuration.getInstance(),false); 	}
		return rightLineReader;
	}
	
	
	public void run (){
		colorSensor.setFloodlight(true);
		
		previousSensedValue = currentSensedValue = colorSensor.getLightValue();
		sensorStartTime = System.currentTimeMillis(); //mark the start time 
		
		while(!config.isDriveComplete()){
			
			previousSensedValue = currentSensedValue;
			currentSensedValue = colorSensor.getLightValue();
			
			if (hasPassedLine(currentSensedValue, previousSensedValue)){
				passedLine = true ;
				callBack();
				try{Thread.sleep(SLEEP_TIME);} catch (Exception e){};
				passedLine = false; //set it back to false 
				try{Thread.sleep(IGNORE_INTERVAL);} catch (Exception e){};
			}
			else {
				passedLine = false ;
				try{Thread.sleep(SLEEP_TIME);} catch (Exception e){};
			}
		}
		
		//shuts off light to save the earth
		colorSensor.setFloodlight(false);
	}
	
	
	/**
	 * the subscriber's passedLine method will be called when a line has been crossed.
	 * allowing it to perform what ever it need without having to constantly check
	 * status of the isPassedLine(). subscriber should be instantiated and running.
	 * @param subscriber
	 */
	public void subscribe(LineReaderListener subscriber){
		//if it is not already a subscriber then subscribe 
		if (!lrlistenerList.contains(subscriber) ){
			lrlistenerList.add(subscriber);
		}
	}
	/**
	 * have the subscriber to subscribe the both the left and right Line reader
	 * @param subscriber 
	 */
	public static void subscribeToAll(LineReaderListener subscriber){
		if (rightLineReader == null || rightLineReader == null ) throw new NullPointerException("l/r LRdr uninitialized");
		rightLineReader.subscribe(subscriber);
		leftLineReader.subscribe(subscriber);
	}
	/**
	 *  remove subscriber from list of actions 
	 * @param subscriber
	 * @return true if it contain subscriber
	 */
	public boolean unsubscribe(LineReaderListener subscriber) {
		return lrlistenerList.remove(subscriber);
	}
	/**
	 * remove subscriber from both l and r lineReader's list of actions
	 * , similar to calling unsubscribe() from each of left / right sensor
	 * @param subscriber
	 */
	public static void unsubscribeToAll(LineReaderListener subscriber) {
		if (rightLineReader == null || rightLineReader == null ) throw new NullPointerException("l/r LRdr uninitialized");
		
		rightLineReader.unsubscribe(subscriber);
		leftLineReader.unsubscribe(subscriber);
		
	}
	
	/**
	 * execute the list of items when line has passed.
	 */
	private void callBack(){
		for (LineReaderListener lr : lrlistenerList){
			lr.passedLine(isLeft);
		}
	}
	
	private int getLightValue(){
			return currentSensedValue;			
	}
	/**
	 * return true if the line is passed 
	 * @return
	 */
	public boolean isPassedLine() {
			return passedLine;			
	}
	
	/**
	 * derivative method to determine if the robot has passed a line 
	 * @param currentSensedValue
	 * @param previousSensedValue
	 * @return a determination if one has passed a line or not
	 */
	private boolean hasPassedLine(int currentSensedValue, int previousSensedValue) {
		//TIME TO AVOID false positive at the beginning of the robot movement 
		long waitTimeBeforeStart =50;
		int lightSensorThreshold = 6 ;// how sensitive sensor should be when it detects changes
		int ignorePeriod = 500 ; //time in ms to ignore further input 
		boolean hasDetected = ((previousSensedValue - currentSensedValue ) > lightSensorThreshold) ;
		boolean result;
		long diffInDectectionTime ; // time between positive feedbacks from sensor 
		//avoid detecting a line twice : two valid detection has to be 800 ms appart 
		diffInDectectionTime = (System.currentTimeMillis() - waitTimeBeforeStart - sensorStartTime);
		
		//if this is a new line and should be counted
		if (hasDetected && (diffInDectectionTime > ignorePeriod)){
			waitTimeBeforeStart = System.currentTimeMillis();
			result = hasDetected;
		}
		//if this is a false positive
		else if (hasDetected){
			waitTimeBeforeStart = System.currentTimeMillis();
			result = false;
		}
		//if negative 
		else{
			result = false ;
		}
		//reset the time between detection .
		diffInDectectionTime = 0 ;
		
		
		return result ; 
	}

}
