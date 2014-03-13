package communication;

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

	public static RemoteNXT remoteNXT;
	
	/**
	 * Sets up connection using RS485 connection over the two bricks
	 */
	public static void SetupConnection(){
		
	}
		
	
}
