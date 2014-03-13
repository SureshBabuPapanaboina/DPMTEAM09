/**
 * 
 */
package odometry;

/**
 * TODO unimplemented class
 * @author yuechuan
 *	this class corrects the odometer.
 *
 */
public class OdometerCorrection extends Thread{
	private static OdometerCorrection instance = null ;
	
	//TODO: Bonan needs to finish this, there should be a method that a LineReaderListener triggers
	//(Which should be in a sync block) that updates a line crossing based on which sensor (left or right)
	
	
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
