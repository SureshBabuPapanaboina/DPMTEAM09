/**
 * 
 */
package coreLib;

/**
 * TODO unimplemented class
 * @author yuechuan
 *	this class corrects the odometer.
 *
 */
public class OdometerCorrection extends Thread{
	private static OdometerCorrection instance = null ;
	
	public OdometerCorrection getInstance(){
		//TODO see an example of implementation in Odometer 
		return instance;
	}
	
	private OdometerCorrection(){
		//most important initialization code goes here: the ones that's absolutely necessary
	}
	
	public void run(){
		//TODO stuff...
	}
	
}
