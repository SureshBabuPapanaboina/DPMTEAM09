package sensor;

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
		
		int option = 0;
		LCD.drawString("Press any button to start. ", 0, 1);
		LCD.drawString("Left to change floodlight color. ", 0, 2);
		LCD.drawString("Right to capture value. ", 0, 3);

		while (option == 0) option = Button.waitForAnyPress();
		
		final ColorSensor ls = new ColorSensor(SensorPort.S1);
		
			(new Thread() {
			public void run() {
				int op = 0;
				int floodlight = 0;
				ls.setFloodlight(floodlight);
				ls.setFloodlight(true);
				Color current;
				while(true){
					//SLEEP TO ALLOW EXIT
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					//Wait for button press to either take value or cycle
					//floodlight
					while (op == 0) op = Button.waitForAnyPress();
					
					switch(op){
					case Button.ID_LEFT:
						//cycle floodlight color
						floodlight++;
						floodlight %= 13;
						ls.setFloodlight(floodlight);
						break;
					case Button.ID_RIGHT:
						//display current value
						current = ls.getColor();
						LCD.drawString("R", 0, 1);
						LCD.drawString("G", 0, 2);
						LCD.drawString("B", 0, 3);
						LCD.drawString("" + current.getRed(), 2, 1);
						LCD.drawString("" + current.getGreen(), 2, 2);
						LCD.drawString("" + current.getBlue(), 2, 3);						
						LCD.drawString("Press any button to continue.", 0, 4);
						
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
