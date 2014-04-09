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

		if(t.role.getId() == 2){
			config.setFlagZone(new Coordinate(t.greenZoneLL_X*30,t.greenZoneLL_Y*30,0),new Coordinate(t.greenZoneUR_X*30,t.greenZoneUR_Y*30,0));
			config.setFlagColor(t.redFlag);
			config.setStartCorner(t.startingCorner.getId());
			config.setDropZone(new Coordinate(t.redDZone_X*30, t.redDZone_Y*30, 0));		
			config.setOpponentDropZone(new Coordinate(t.greenDZone_X*30, t.greenDZone_Y*30, 0));
		}
		else{
			config.setFlagZone(new Coordinate(t.redZoneLL_X*30,t.redZoneLL_Y*30,0),new Coordinate(t.redZoneUR_X*30,t.redZoneUR_Y*30,0));
			config.setFlagColor(t.greenFlag);
			config.setStartCorner(t.startingCorner.getId());
			config.setDropZone(new Coordinate(t.greenDZone_X*30, t.greenDZone_Y*30, 0));		
			config.setOpponentDropZone(new Coordinate(t.redDZone_X*30, t.redDZone_Y*30, 0));		
		}
		//TODO more for the final 
	}

}
