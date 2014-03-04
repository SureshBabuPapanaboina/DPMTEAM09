/**
 * 
 */
package coreLib;

/**
 * LineReaderListener is the interface that contains the method signiture 
 * that defines what happens when the LineReader detects a line 
 * @author yuechuan
 *
 */
public interface LineReaderListener {
	/**
	 * if a line is passed call this method 
	 */
	public void passedLine();
}
