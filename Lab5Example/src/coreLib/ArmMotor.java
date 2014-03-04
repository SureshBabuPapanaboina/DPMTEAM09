package coreLib;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;

/**
 * this class is used to move the arm to grab object 
 * @author yuechuan
 *	@version 1.1
 */
public class ArmMotor {
	private static AbstractConfig config ;
	public static final NXTRegulatedMotor ARM_M= AbstractConfig.SENSOR_MOTOR;
	static int movementDeg = 180 ;
	private static boolean isOpen = true ;
	
	private ArmMotor(AbstractConfig conf){
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
