package capture;

import movement.Driver;
import communication.RemoteConnection;
import robotcore.Configuration;
import robotcore.Coordinate;

/**
 * Class for the basic capture mechanism, assumes that doors start off as closed
 * @author yuechuan
 *
 */
public class CaptureMechanism {
	//	private static Configuration config = Configuration.getInstance();
	private static CaptureMechanism instance;
	
	private static Coordinate dropOff;

	/**
	 * do not allow creating instances 
	 */
	private CaptureMechanism(){}
	
	public static void setDropOff(Coordinate drop){
		dropOff = drop;
	}

	public static CaptureMechanism getInstance(){
		if(instance == null) instance = new CaptureMechanism();

		return instance;
	}

	/**
	 * open the robotic arm / door 
	 */
	public void open(){
		Configuration.SENSOR_MOTOR.rotateTo(90);
		RemoteConnection rc = RemoteConnection.getInstance();
		rc.getRemoteNXT().A.setSpeed(55);
		rc.getRemoteNXT().B.setSpeed(55);
		rc.getRemoteNXT().B.rotateTo(-135, true);
		rc.getRemoteNXT().A.rotateTo(-135, false);
	}

	/**
	 * open the robotic arm / door 
	 */
	public void close(){
		RemoteConnection rc = RemoteConnection.getInstance();

		rc.getRemoteNXT().A.rotateTo(15, true);
		rc.getRemoteNXT().B.rotateTo(15);
		Configuration.SENSOR_MOTOR.rotateTo(0);

	}

	/**
	 * Uses the arms to align a block in front of it
	 */
	public void align(){
		RemoteConnection rc = RemoteConnection.getInstance();

		rc.getRemoteNXT().A.rotateTo(-80, true);
		rc.getRemoteNXT().B.rotateTo(-80);

		rc.getRemoteNXT().A.rotateTo(-100,true);
		rc.getRemoteNXT().B.rotateTo(-100);

	}

	/**
	 * Removes a block from the end zone
	 * TODO: fix this for a search zone greater than 2x2
	 */
	public void removeBlock(){ 

		Configuration.SENSOR_MOTOR.rotateTo(-45);
		Driver.getInstance().forward(15);
		Configuration.SENSOR_MOTOR.setSpeed(250);
		Configuration.SENSOR_MOTOR.rotateTo(55);
		Configuration.SENSOR_MOTOR.setSpeed(45);

		Configuration.SENSOR_MOTOR.rotateTo(0);


	}

	/**
	 * Removes a block from the end zone
	 * TODO: fix this for a search zone greater than 2x2
	 */
	public void removeBlockII(){ 
		RemoteConnection rc = RemoteConnection.getInstance();
		
		rc.getRemoteNXT().A.setSpeed(200);
		rc.getRemoteNXT().B.setSpeed(200);
		
		open();

		Driver.getInstance().forward(18);

		//grab the block
		rc.getRemoteNXT().A.rotateTo(-70, true);
		rc.getRemoteNXT().B.rotateTo(-70);
	
		if(dropOff != null)
			Driver.getInstance().travelTo(dropOff);
		else{
			Driver.getInstance().rotateToRelatively(180);
			Driver.getInstance().forward(8);
		}
		
		rc.getRemoteNXT().A.rotateTo(-90, true);
		rc.getRemoteNXT().B.rotateTo(-90);
		
		Driver.getInstance().backward(10);

		close();


	}

}
