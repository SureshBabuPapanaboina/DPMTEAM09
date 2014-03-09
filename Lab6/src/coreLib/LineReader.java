package coreLib;


import java.util.LinkedList;

import lejos.nxt.ColorSensor;
/**
 * this is used to read line from the grund with the colorReader
 * @author yuechuan
 *	@version 1.3
 */
public class LineReader extends Thread{
	private ColorSensor colorSensor; 
	private Configuration config;
	private int previousSensedValue , currentSensedValue ;
	private boolean passedLine = false ;
	private long sensorStartTime;
	
	private static LineReader lineReader ;

	/**
	 * contains a list of classes to call when a line is detected. A
	 * linkedList is used in this implementation since there seem to be 
	 * no support for HashSet and the other Set seem to be deprecated 
	 */
	private LinkedList<LineReaderListener> lrlistenerList = new LinkedList<LineReaderListener>();

	private LineReader(Configuration config){
		colorSensor  = new ColorSensor(Configuration.LIGHT_SENSOR_PORT);
		this.config = config;    
	}
	
	public static LineReader getInstance(){
		if (lineReader == null){
			lineReader = new LineReader(Configuration.getInstance());
		}
		return lineReader;
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
				try{Thread.sleep(100);} catch (Exception e){};
			}
			else {
				passedLine = false ;
				try{Thread.sleep(25);} catch (Exception e){};
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
	 *  remove subscriber from list of actions 
	 * @param subscriber
	 * @return true if it contain subscriber
	 */
	public boolean unsubscribe(LineReaderListener subscriber) {
		return lrlistenerList.remove(subscriber);
	}
	
	/**
	 * execute the list of items when line has passed.
	 */
	private void callBack(){
		for (LineReaderListener lr : lrlistenerList){
			lr.passedLine();
		}
	}
	
	public int getLightValue(){
			return currentSensedValue;			
	}
		
	public boolean isPassedLine() {
			return passedLine;			
	}
	
	/**
	 * derivative if robot has passed line 
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
