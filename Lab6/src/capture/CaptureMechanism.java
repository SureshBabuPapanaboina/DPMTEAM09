package capture;

import movement.Driver;
import odometry.Direction;
import odometry.Odometer;
import communication.RemoteConnection;
import robotcore.Configuration;

/**
 * Class for the basic capture mechanism, assumes that doors start off as closed
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
		config.SENSOR_MOTOR.rotateTo(90);
		RemoteConnection rc = RemoteConnection.getInstance();
		rc.getRemoteNXT().A.setSpeed(30);
		rc.getRemoteNXT().B.setSpeed(30);
		rc.getRemoteNXT().A.rotateTo(-135, true);
		rc.getRemoteNXT().B.rotateTo(-135);
	}
	
	/**
	 * open the robotic arm / door 
	 */
	public void close(){
		RemoteConnection rc = RemoteConnection.getInstance();
		
		rc.getRemoteNXT().A.rotateTo(15, true);
		rc.getRemoteNXT().B.rotateTo(15);
	}
	
	/**
	 * Uses the arms to align a block in front of it
	 */
	public void align(){
		RemoteConnection rc = RemoteConnection.getInstance();
		
		rc.getRemoteNXT().A.rotateTo(-85, true);
		rc.getRemoteNXT().B.rotateTo(-85);
		
		rc.getRemoteNXT().A.rotateTo(-95,true);
		rc.getRemoteNXT().B.rotateTo(-95);
		
	}
	
	/**
	 * Removes a block from the end zone
	 * TODO: fix this for a search zone greater than 2x2
	 */
	public void removeBlock(){ 
//		switch(Odometer.getInstance().getDirection()){
//		default:
			Configuration.SENSOR_MOTOR.rotateTo(-45);
			Driver.getInstance().forward(10);
			Configuration.SENSOR_MOTOR.setSpeed(250);
			Configuration.SENSOR_MOTOR.rotateTo(55);
			Configuration.SENSOR_MOTOR.setSpeed(45);

			Configuration.SENSOR_MOTOR.rotateTo(0);

//			break;
//		}
	}
	
}
