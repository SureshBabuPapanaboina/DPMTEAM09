package communication;

import java.io.IOException;

import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.RS485;
import lejos.nxt.remote.RemoteNXT;

/**
 * Sets up a remote connection to a second nxt brick with the lejos.nxt.remote class,
 * this allows the main brick to have access to the sensors on the slave brick
 * 
 * See Example for how to implement: 
 * http://fedora.cis.cau.edu/~pmolnar/CIS687F12/Programming-LEGO-Robots/samples/src/org/lejos/sample/remotenxttest/RemoteNXTTest.java
 *
 */
public class RemoteConnection {
	
	private static RemoteConnection instance = null;
	public static RemoteNXT remoteNXT;

	/**
	 * Exists to defeat instantiation to ensure singleton
	 */
	protected RemoteConnection() {
		// Exists only to defeat instantiation.
	}
	
	/**
	 * Get instance of singleton
	 * @return
	 */
	public static RemoteConnection getInstance() {
		if(instance == null) {
			instance = new RemoteConnection();
		}
		return instance;
	}

	/**
	 * Sets up connection using RS485 connection over the two bricks
	 */
	public static void SetupConnection(){
		NXTCommConnector rsconn = RS485.getConnector();
		try {
			remoteNXT = new RemoteNXT("NXT", rsconn);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}


}
