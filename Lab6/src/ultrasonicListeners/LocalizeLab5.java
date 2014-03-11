package ultrasonicListeners;

import coreLib.Configuration;
import coreLib.Driver;
import coreLib.LCDWriter;
import coreLib.UltrasonicListener;
import lejos.nxt.comm.RConsole;

/**
 * This class hosts a public method that does the localization. 
 * an instance of this class is used in the lab5 part 2.
 * @author yuechuan
 * @version 1.2
 *
 */
public class LocalizeLab5 implements UltrasonicListener{
	//the following 3 is used for the UltrasonicListener
	private int distanceOnInvoke;
	private boolean continuous;
	private boolean called ;
	double angle1 , angle2 , middle ;
	boolean secondAngle = false ;	// if this is called a second time then turn back 

	Driver driver = Driver.getInstance();
	
	public LocalizeLab5(int distanceOnInvoke , boolean continuous){
		this.distanceOnInvoke = distanceOnInvoke;
		this.continuous = continuous;
	}
	int i = 0 ;
	@Override
	public void ultrasonicDistance(int distanceFromObsticle) {
		driver.motorStop();
		RConsole.println("localization entered " + ++i + "times");
		if (!secondAngle){
			angle1 = Configuration.getInstance().getCurrentLocation().getTheta();
			LCDWriter.getInstance().writeToScreen("ang1 " + Math.toDegrees(angle1) , 6);
			//rotate back 
			driver.rotateToRelatively(-360, true);
			try {Thread.sleep(400);} catch ( Exception e ){};
			secondAngle = true;
			called = false;
		}
		else {
			angle2 = Configuration.getInstance().getCurrentLocation().getTheta();
			LCDWriter.getInstance().writeToScreen("ang2 " + Math.toDegrees(angle2) , 5);
			//rotate to middle 
			double angleFromOrigin = ((angle1 + angle2)/2)- angle2 ;
			LCDWriter.getInstance().writeToScreen("AFO " +Math.toDegrees(angleFromOrigin) , 0);
			driver.rotateToRelatively(Math.toDegrees(angleFromOrigin));
			
			//rotate to face the y axis 
			driver.rotateToRelatively(-135);
			continuous = false;
		}
	}
	
	public int getDistanceOnInvoke() {
		return distanceOnInvoke;
	}

	public UltrasonicListener setDistanceOnInvoke(int distanceOnInvoke) {
		this.distanceOnInvoke = distanceOnInvoke;
		return this ;
	}

	public boolean isContinuous() {
		return continuous;
	}

	public UltrasonicListener setContinuous(boolean continuous) {
		this.continuous = continuous;
		return this;
	}

	public boolean isCalled() {
		return called;
	}

	public UltrasonicListener setCalled(boolean called) {
		this.called = called;
		return this ;
	}
	
	public boolean isDone(){
		return (!continuous && secondAngle);
	}
}
