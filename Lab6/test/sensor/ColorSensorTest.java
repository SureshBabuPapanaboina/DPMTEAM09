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
		LCDWriter lcd = LCDWriter.getInstance();
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
					
					//Wait for button press to either take value or cycle
					//floodlight
					while (op == 0) op = Button.waitForAnyPress();
					
					switch(op){
					case Button.ID_LEFT:
						//cycle floodlight color
						floodlight++;
						floodlight %= 13;	//13 colors
						ls.setFloodlight(floodlight);
						break;
					case Button.ID_RIGHT:
						//display current value
						current = ls.getColor();
						lcd.writeToScreen("R" + current.getRed(), 1);
						lcd.writeToScreen("G" + current.getGreen(), 2);
						lcd.writeToScreen("B"+ current.getBlue(),3);
						lcd.writeToScreen("Press any button to continue.", 4);
						
						op = 0;
						//Wait for button press to erase from display and continue
						while (op == 0) op = Button.waitForAnyPress();

					}
					
				}
	
			}
		}).start();
			
			while(Button.waitForAnyPress() != Button.ID_ESCAPE);
			//Wait for another button press to exit
			System.exit(0);
	}

}
