package robotcore;


import lejos.nxt.LCD;
/**
 * high abstraction to facilate writing to LCD
 * @author yuechuan
 * @version 1.2 
 *
 */
public class LCDWriter extends Thread {
	private static final long DISPLAY_PERIOD = 250;
	private Configuration config;
	public String [] s ;
	private Object lock ;
	private static boolean threadStarted = false ;
	private static LCDWriter lcd ;

	private LCDWriter(Configuration config){
		lock = new Object();;
		s = new String[8];
		this.config = config;
		for(int i = 0 ; i < 8 ; i++){
			s[i] = "";
		}
	}
	/**
	 * return an instance of LCDWriter
	 * @return
	 */
	public static LCDWriter getInstance (){
		if (lcd == null){
			lcd = new LCDWriter(Configuration.getInstance());
		}
		return lcd;
	}
	
	public void run(){
		threadStarted = true;
		long displayStart, displayEnd;
		LCD.clearDisplay();

		while (true) {
			displayStart = System.currentTimeMillis();

			LCD.clear();
			// clear the lines for displaying odometry information
			for (int i = 0; i < 8; i++) {
				LCD.drawString(s[i], 0, i);
			}
			
			// throttle the OdometryDisplay
			displayEnd = System.currentTimeMillis();
			if (displayEnd - displayStart < DISPLAY_PERIOD) {
				try {
					Thread.sleep(DISPLAY_PERIOD - (displayEnd - displayStart));
				} catch (InterruptedException e) {
				}
			}
		}
	}
	/**
	 * write to screen on line lineNumber , first line is 0
	 * @param str
	 * @param lineNumber
	 */
	public void writeToScreen(String str, int lineNumber) {
		synchronized (lock){
			s[lineNumber] = ""+str;
		}

	}
	
	protected static String formattedDoubleToString(double x, int places) {
		String result = "";
		String stack = "";
		long t;
		
		// put in a minus sign as needed
		if (x < 0.0)
			result += "-";
		
		// put in a leading 0
		if (-1.0 < x && x < 1.0)
			result += "0";
		else {
			t = (long)x;
			if (t < 0)
				t = -t;
			
			while (t > 0) {
				stack = Long.toString(t % 10) + stack;
				t /= 10;
			}
			
			result += stack;
		}
		
		// put the decimal, if needed
		if (places > 0) {
			result += ".";
		
			// put the appropriate number of decimals
			for (int i = 0; i < places; i++) {
				x = Math.abs(x);
				x = x - Math.floor(x);
				x *= 10.0;
				result += Long.toString((long)x);
			}
		}
		
		return result;
	}



}
