package interbrick;

import lejos.nxt.Sound;
import communication.RemoteConnection;

/**
 * A basic test for interbrick communication, wiggles the motors on the other brick
 * back and forth to see that it works
 * 
 * 
 * NOTE: NXTLCPRespond must be running on the other brick
 * 
 * @author Peter
 *
 */
public class BasicCommTest {
	
	/**
	 * Run test
	 * @param args
	 */
	public static void main(String[] args) {
		RemoteConnection rc = RemoteConnection.getInstance();
		
		rc.setupConnection();

		if(rc.getRemoteNXT() == null){
			Sound.beepSequence();
			return;
		}
		
		
		rc.getRemoteNXT().A.rotateTo(90);
		rc.getRemoteNXT().B.rotateTo(-90);
		
		
		rc.closeConnection();

	}

}
