package bluetoothclient;

import robotcore.Configuration;
import robotcore.Coordinate;
import robotcore.LCDWriter;

/**
 * This class sets up a bluetooth listener 
 * and listens for the initial commands for the task given
 * @author Peter Henderson
 *
 */
public class BluetoothReceiver {

	Configuration config = Configuration.getInstance();
	BluetoothConnection conn ; 
	/**
	 * Constructor, sets up the receiver
	 */
	public BluetoothReceiver(){
		conn = new BluetoothConnection();
		
	}
	
	/**
	 * This listens for the start command in serial manner (to prevent launching of other systems)
	 * It sets the configuration file
	 */
	public void listenForStartCommand(){
		// example usage of Tranmission class
		Transmission t = conn.getTransmission();

		config.setFlagZone(new Coordinate(t.redZoneLL_X,t.redZoneLL_Y,0),new Coordinate(t.redZoneUR_X,t.redZoneUR_Y,0));
		config.setFlagColor(t.redFlag);
		//TODO more for the final 
	}
	
}
