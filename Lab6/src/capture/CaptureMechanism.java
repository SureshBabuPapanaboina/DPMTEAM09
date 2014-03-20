package capture;

import communication.RemoteConnection;

import robotcore.Configuration;

/**
 *TODO unimplemented class 
 * @author yuechuan
 *
 */
public class CaptureMechanism {
	private static Configuration config = Configuration.getInstance();
	private static CaptureMechanism instance;
	
	/**
	 * do not allow creating instances 
	 */
	private CaptureMechanism(){}
	
	public static CaptureMechanism getInstance(){
		if(instance == null) instance = new CaptureMechanism();
		
		return instance;
	}
	
	/**
	 * open the robotic arm / door 
	 */
	public void open(){
		config.SENSOR_MOTOR.rotateTo(-90);
		RemoteConnection rc = RemoteConnection.getInstance();
		
		rc.getRemoteNXT().A.rotateTo(-45);
		rc.getRemoteNXT().B.rotateTo(-45);
	}
	
	/**
	 * open the robotic arm / door 
	 */
	public void close(){
		RemoteConnection rc = RemoteConnection.getInstance();
		
		rc.getRemoteNXT().A.rotateTo(90);
		rc.getRemoteNXT().B.rotateTo(90);
	}
	
	/**
	 * Uses the arms to align a block in front of it
	 */
	public void align(){
		RemoteConnection rc = RemoteConnection.getInstance();
		
		rc.getRemoteNXT().A.rotateTo(0);
		rc.getRemoteNXT().B.rotateTo(0);
	}
	
}
