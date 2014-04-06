package communication;

import java.io.IOException;

import robotcore.CommunicationType;
import robotcore.Configuration;
import lejos.nxt.LCD;
import lejos.nxt.Sound;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTCommConnector;
import lejos.nxt.comm.RS485;
import lejos.nxt.remote.RemoteMotor;
import lejos.nxt.remote.RemoteNXT;

/**
 * Sets up a remote connection to a second nxt brick with the lejos.nxt.remote class,
 * this allows the main brick to have access to the sensors on the slave brick
 * 
 * See Example for how to implement: 
 * http://fedora.cis.cau.edu/~pmolnar/CIS687F12/Programming-LEGO-Robots/samples/src/org/lejos/sample/remotenxttest/RemoteNXTTest.java
 *
 * @author Peter Henderson
 */
public class RemoteConnection {
	
	private static RemoteConnection instance = null;
	private RemoteNXT remoteNXT;

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
	 * Gets remote brick reference
	 * @return
	 */
	public RemoteNXT getRemoteNXT(){
		if(remoteNXT == null)
			setupConnection();
		
		return remoteNXT;
	}
	
	/**
	 * Returns the left remote motor, or null otherwise
	 * @return
	 */
	public RemoteMotor getLeftRemote(){
		if(getRemoteNXT() == null) return null;
		
		return remoteNXT.A;
	}
	
	/**
	 * Returns the right remote motor, or null otherwise
	 * @return
	 */
	public RemoteMotor getRightRemote(){
		if(getRemoteNXT() == null) return null;
		
		return remoteNXT.B;
	}
	
	/**
	 * Closes the remote brick connection
	 */
	public void closeConnection(){
		remoteNXT.close();
		remoteNXT = null;
	}

	/**
	 * Sets up connection using RS485 connection over the two bricks
	 */
	public void setupConnection(){
		NXTCommConnector conn; 
		Configuration.getInstance();
		if(Configuration.INTERBRICK_COMM_METHOD == CommunicationType.RS485)
			conn = RS485.getConnector();
		else
			conn = Bluetooth.getConnector();

		try {
			//TODO: need to change name of the two bricks to match the standards
			remoteNXT = new RemoteNXT("NXT", conn);
		} catch (IOException e) {
			LCD.drawString("Conn failed", 0, 2);
			LCD.drawString(e.getMessage(), 0, 3);
		}
		
		
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


}
