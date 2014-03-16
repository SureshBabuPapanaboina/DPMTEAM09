package sensor;

import robotcore.LCDWriter;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;

/**
 * A test bed to check the capabilities of our color sensors to determine which is the
 * best for block color and/or what is the range and what is the best floodlight
 *
 */
public class ColorSensorTest {
    
	
	public static void main(String[] args){
		// LCD need to be cleared by the user 
		final LCDWriter lcd = LCDWriter.getInstance();
		try{lcd.start();} catch (Exception e){};
		
		int option = 0;
		
		lcd.writeToScreen("Press any button to start. ", 0);
		lcd.writeToScreen("Left to change floodlight color. ", 1);
		lcd.writeToScreen("Right to capture value. ", 2);

		
		while (option == 0) option = Button.waitForAnyPress();
		
		final ColorSensor ls = new ColorSensor(SensorPort.S1);
		
			(new Thread() {
    			public void run() {
    				int op = 0;
    				int floodlight = Color.RED;
    				ls.setFloodlight(floodlight);
    				ls.setFloodlight(true);
    				Color current;
    				while(true){
    					//SLEEP TO ALLOW EXIT
    					try {	Thread.sleep(15); } catch (InterruptedException e) {}
                        
                        floodlight++;
                        floodlight % = 3;
                        ls.setFloodlight(floodlight);
     
                        while(true){
       		    			lcd.writeToScreen("R" + current.getRed(), 1);
    						lcd.writeToScreen("G" + current.getGreen(), 2);
    						lcd.writeToScreen("B"+ current.getBlue(),3);
                            lcd.writeToScreen("Right to change");
                            try {    Thread.sleep(15); } catch (InterruptedException e) {}
                            if (Button.waitForAnyPress() == Button.ID_RIGHT) break;
                        }
    				}
    					
    			}
	
			}
		}).start();
			
			while(Button.waitForAnyPress() != Button.ID_ESCAPE);
			//Wait for another button press to exit
			System.exit(0);
	}

}
