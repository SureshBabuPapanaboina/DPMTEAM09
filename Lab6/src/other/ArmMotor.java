package other;

import coreLib.Configuration;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;

/**
 * this class is used to move the arm to grab object.
 * this is only used for lab 5
 * @deprecated not used, may require change 
 * @author yuechuan
 *	@version 1.1
 */
public class ArmMotor {
	private static Configuration config ;
	public static final NXTRegulatedMotor ARM_M= Configuration.SENSOR_MOTOR;
	static int movementDeg = 180 ;
	private static boolean isOpen = true ;
	
	private ArmMotor(Configuration conf){
		config = conf;
	}

	/**
	 * open arm 
	 */
	public static void open(){
		Sound.beep();
		if(!isOpen){
			ARM_M.rotate(-movementDeg,false);
			isOpen = true;
		}
	}
	/**
	 * close arm 
	 */
	public static void close (){
		Sound.beep();
		if (isOpen){
			ARM_M.rotate(movementDeg,false);	
			isOpen = false ;
		}
	}
}
