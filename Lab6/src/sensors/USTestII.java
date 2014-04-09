package sensors;

import robotcore.Configuration;
import lejos.nxt.Motor;
import lejos.nxt.NXTMotor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;

/**
 * pick up peek of the block algo
 * @author yuechuan
 *
 */
public class USTestII {
	
	private static NXTRegulatedMotor motor = Motor.A;
	private static Configuration conf = Configuration.getInstance();
	private static UltrasonicPoller usp = UltrasonicPoller.getInstance();
	
	public static void main(String [] args){
		usp.start();
		motor.setSpeed(30);
		motor.rotate(-45);
		
		
		motor.rotate(90, true);
		
		
		int cDist=usp.getDistance(),pDist =cDist ;
		
		//right negative left positive
		int i =0; 
		while (i++ < 100){
			
			RConsole.println("" +motor.getTachoCount() + '\t' + cDist);
			cDist=usp.getDistance();
			
			if (cDist < 30 ){
				motor.stop();
				motor.rotate(25);
				break;
			}
			pDist = cDist;	

			try{Thread.sleep(25);}catch(Exception e){}
		}
//			if (cDist - pDist < -5){
//				ang1 = motor.getTachoCount();
//				dis1 = cDist;
//			}
//		//			
//	else if (pDist- cDist > 5){
//				ang2 = motor.getTachoCount();
//				dis2 = cDist;
//			}
//			pDist =cDist ;
//			try{Thread.sleep(25);}catch(Exception e){}
//		}
//		
//		RConsole.println("a1 " + ang1);
//		RConsole.println("a2 " + ang2);
//		RConsole.println("d1 " + dis1);
//		RConsole.println("d2 " + dis2);
//		motor.rotate(-(ang2-ang1)/2 );
		
	}
	/**
	 * return true when there is a positive or negative jump in the data 
	 * positive d1 > d2 by treshold
	 * negative d1 < d2 by treshold
	 */
	private static boolean treshold (int d1 , int d2 , int treshold, boolean positive){
		if (positive){
			if ((d1 - d2) > treshold ) return true;
		}
		else if (!positive){
			if ((d2-d1) > treshold ) return true;
		}
		return false ;
	}
	
}
