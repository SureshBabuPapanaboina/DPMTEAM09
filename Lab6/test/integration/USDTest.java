package integration;

import robotcore.LCDWriter;
import sensors.UltrasonicPoller;
/**
 * test the distance measured by the us sensor and see if it's good 
 * @author yuechuan
 *
 */
public class USDTest {
	
	LCDWriter lcd = LCDWriter.getInstance();
	UltrasonicPoller up = UltrasonicPoller.getInstance();
	public static void main(String[] args) {
		USDTest usdt = new USDTest();
		usdt.lcd.start();
		usdt.up.start();
		while (true){
			usdt.lcd.writeToScreen(usdt.up.getDistance()+"",0);
			try{Thread.sleep(100);}catch(Exception e ){}
		}
		
	}

}
