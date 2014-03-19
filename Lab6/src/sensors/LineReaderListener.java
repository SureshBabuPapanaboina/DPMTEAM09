/**
 * 
 */
package sensors;

/**
 * LineReaderListener is the interface that contains the method signiture 
 * that defines what happens when the LineReader detects a line 
 * @author yuechuan
 *
 */
public interface LineReaderListener {
	/**
	 * if a line is passed call this method, 
	 * @param isLeft true if method is called by a left Color sensor else false siginfies right Sensor
	 */
	public void passedLine(boolean isLeft);	
}
