package bluetoothclient;

import robotcore.Configuration;
import robotcore.Coordinate;

/**
 * This class sets up a bluetooth listener 
 * and listens for the initial commands for the task given
 * @author Peter Henderson
 *
 */
public class BluetoothReceiver {

	Configuration config = Configuration.getInstance();
	/**
	 * Constructor, sets up the receiver
	 */
	public BluetoothReceiver(){
		//TODO: Setup bluetooth
	}
	
	/**
	 * This listens for the start command in serial manner (to prevent launching of other systems)
	 * It sets the configuration file
	 */
	public void listenForStartCommand(){
		//TODO: complete
		BluetoothConnection conn = new BluetoothConnection();
		
		// as of this point the bluetooth connection is closed again, and you can pair to another NXT (or PC) if you wish
		
		// example usage of Tranmission class
		Transmission t = conn.getTransmission();
		
		//TODO: do otherstuff
		config.setFlagZone(new Coordinate(t.redZoneLL_X,t.redZoneLL_Y,0),new Coordinate(t.redZoneUR_X,t.redZoneUR_Y,0));
		config.setFlagColor(t.redFlag);
	}
	
}
