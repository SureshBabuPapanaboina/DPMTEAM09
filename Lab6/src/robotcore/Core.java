package robotcore;

import java.util.ArrayList;

import bluetoothclient.BluetoothReceiver;
import communication.RemoteConnection;
import capture.CaptureMechanism;
import movement.Driver;
import navigation.PathTraveller;
import lejos.nxt.Sound;
import localization.Localization;
import odometry.Odometer;
import odometry.OdometerCorrection;
import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;
import search.Searcher;
import sensors.LineReader;
import sensors.UltrasonicPoller;

/**
 * This runs the main program for the competition. 
 * We removed the state machine because of performance issues temporarily
 * 
 * @author Peter Henderson
 *
 */
public class Core {
	
	
	
	public static void main(String[] args){
		LCDWriter lcd = LCDWriter.getInstance();
		lcd.start();
		Configuration conf = Configuration.getInstance();
	
		
		BluetoothReceiver br = new BluetoothReceiver(); 
		br.listenForStartCommand();// info in Config should be set 
		
		//Initializiations
		Driver driver = Driver.getInstance();
		UltrasonicPoller up = UltrasonicPoller.getInstance();
		PathTraveller traveller = PathTraveller.getInstance();
		LineReader llr = LineReader.getLeftSensor();	//left + right line reader
		LineReader rlr = LineReader.getRightSensor();
		Odometer odo = Odometer.getInstance();
		OdometerCorrection oc = OdometerCorrection.getInstance();

		up.start();
		odo.start();
		llr.start();
		rlr.start();
		//---------------------------------------------------
		// Localization
		//---------------------------------------------------

		Localization localizer = new Localization();
		localizer.callback();
		
		LineReader.subscribeToAll(oc);
		//---------------------------------------------------
		//Travel to destination
		//---------------------------------------------------

		Coordinate destination = traveller.getDestination();

		
		try {Thread.sleep(1000);}catch(Exception e){};
		
		
		traveller.recalculatePathToCoords((int)destination.getX(), (int)destination.getY() );
		RemoteConnection.getInstance().setupConnection();

		ArrayList<Coordinate> nodesAroundFlag = PathTraveller.getInstance().getAllPointsAroundFlagZoneList();
		boolean done  = false;
		while(!done){
			try{
			done = PathTraveller.followPath(nodesAroundFlag);
			if(!done){ 
				if(traveller.pathIsEmpty())
					destination = traveller.getDestination();
				
				traveller.recalculatePathToCoords((int)destination.getX(), (int)destination.getY() );
			}
			else break;
			}
			catch(Exception e){
				lcd.writeToScreen("E: "+ e.toString(), 1);
			}
		}
		
		
		//---------------------------------------------------
		//Search and capture
		//---------------------------------------------------

		Sound.beepSequenceUp();	
		
		CaptureMechanism cm = CaptureMechanism.getInstance();
		Coordinate first = Searcher.generateSearchPath().peek();
		CaptureMechanism.setDropOff(destination);
		
		Searcher.searchForBlock();
		
		driver.travelTo(first);
		driver.travelTo(destination);
		
		destination = conf.getDropZone();
		
		
		Configuration.getInstance().setForwardSpeed(350);
		Configuration.getInstance().setRotationSpeed(300);
		
		//GO TO DROP OFF ZONE
		
		traveller.recalculatePathToCoords((int)destination.getX(), (int)destination.getY() );
		done  = false;
		while(!done){
			try{
			done = PathTraveller.followPath(null);
			if(!done){ 
				if(traveller.pathIsEmpty())
					destination = traveller.getDestination();
				
				traveller.recalculatePathToCoords((int)destination.getX(), (int)destination.getY() );
			}
			else break;
			}
			catch(Exception e){
				lcd.writeToScreen("E: "+ e.toString(), 1);
			}
		}
		
		Sound.beepSequenceUp();		
		
		cm.open();
		driver.backward(20);
		cm.close();
		
		//TODO: play awesome victory music here
		
	}
	
}
