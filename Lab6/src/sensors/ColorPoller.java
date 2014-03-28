package sensors;
import lejos.nxt.ColorSensor;
import lejos.nxt.SensorPort;
import lejos.robotics.Color;

/**
 * Intended to control the color polling and determine what kind of color
 * is being detected from custom calibration done
 * @deprecated not used 
 * 
 * @author Peter Henderson
 *
 */
public class ColorPoller {
	
	final ColorSensor s;
	
	/**
	 * Constructor
	 */ 
	public ColorPoller(ColorSensor sensor){
		this.s = sensor;	
	}
	
	public Color pollColor(){
		return s.getColor();
	}
	

}
