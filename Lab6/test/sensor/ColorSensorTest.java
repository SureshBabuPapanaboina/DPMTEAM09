package sensor;

import robotcore.Configuration;
import robotcore.LCDWriter;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;

/**
 * A test bed to check the capabilities of our color sensors to determine which is the
 * best for block color and/or what is the range and what is the best flood light.
 */
public class ColorSensorTest extends Thread {
	final ColorSensor ls = new ColorSensor(Configuration.COLOR_SENSOR_PORT);    
	final static LCDWriter lcd = LCDWriter.getInstance();
	
	public static void main(String[] args){
		Configuration config = Configuration.getInstance();
		
		// LCD need to be cleared by the user 
		try{lcd.start();} catch (Exception e){};
		
		int option = 0;
		
		lcd.writeToScreen("Press any button to start. ", 0);
		lcd.writeToScreen("Left to change floodlight color. ", 1);
		lcd.writeToScreen("Right to capture value. ", 2);
		
		new ColorSensorTest().start();
		
		while (option == 0) option = Button.waitForAnyPress();
		
			
		while(Button.waitForAnyPress() != Button.ID_ESCAPE);
		//Wait for another button press to exit
		System.exit(0);
	}

	public void run(){
		int floodlight = Color.RED;
		ls.setFloodlight(floodlight);
		ls.setFloodlight(true);
		Color current = ls.getColor();
		while(true){
			//SLEEP TO ALLOW EXIT
			try {	Thread.sleep(15); } catch (InterruptedException e) {}
            
            floodlight++;
            floodlight %= 3;
            ls.setFloodlight(floodlight);
                        
            while(true){
                current = ls.getColor();
                
				lcd.writeToScreen("R" + current.getRed(),1);
				lcd.writeToScreen("G" + current.getGreen(), 2);
				lcd.writeToScreen("B"+ current.getBlue(),3);
                lcd.writeToScreen("Right to change",4);
                try {    Thread.sleep(15); } catch (InterruptedException e) {}
//                if (Button.waitForAnyPress() == Button.ID_RIGHT) break;
            }
		}
			
	}
		

}



