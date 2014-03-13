package reference;

import robotcore.Configuration;
import robotcore.LCDWriter;
import sensors.UltrasonicListener;
import sensors.UltrasonicPoller;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.robotics.Color;
import movement.Driver;
/**
 * lab 5 part 1, this class is a ultrasnicListener and 
 * can subscribe to the ultrasonic poller. see the UltrasonicListener 
 * interface for more information. 
 * 
 * @version 1.1 
 * @date 22 -02 - 14
 * @author yuechuan
 *
 */
public class Lab5Part1 implements UltrasonicListener{
	//following 3 vars are used for UltrasonicListeners
	private int distanceOnInvoke;
	private boolean continuous;
	private boolean called ;
	
	/**
	 * 
	 * @param distanceOnInvoke
	 * @param called
	 */
	public Lab5Part1(int distanceOnInvoke , boolean called ) {
		this.distanceOnInvoke = distanceOnInvoke;
		this.called = called;
	}
	
	/**
	 * main class 
	 * @param args
	 */
	public static void main(String [] args){
		Configuration config = Configuration.getInstance();
		UltrasonicPoller uspoller = UltrasonicPoller.getInstance();
		LCDWriter.getInstance().start();
		LCDWriter lcd = LCDWriter.getInstance();
		uspoller.start();
		
		Lab5Part1 lab5 = new Lab5Part1(7, false);
		lcd.writeToScreen("press start", 0);
		while(Button.waitForAnyPress() != Button.ID_ENTER){}
		Driver.setSpeed(config.getForwardSpeed());
		Driver.getInstance().motorForward();
		uspoller.subscribe(lab5);

		while(Button.waitForAnyPress() != Button.ID_ESCAPE){}
		
		System.exit(0);
	}

	@Override
	public void ultrasonicDistance(int distanceFromObsticle) {
		Driver.getInstance().motorStop();
		LCDWriter.getInstance().writeToScreen("see smt["+ distanceFromObsticle + "]", 3);
		ColorSensor cs = new ColorSensor(Configuration.LIGHT_SENSOR_PORT);
		cs.setFloodlight(true);
		
		while(true){
			Color c = cs.getColor();
			LCDWriter.getInstance().writeToScreen( UltrasonicPoller.getInstance().getDistance() +"" , 2);
			LCDWriter.getInstance().writeToScreen("B" +c.getBlue() , 6);
			LCDWriter.getInstance().writeToScreen("R" +c.getRed() , 5);
			LCDWriter.getInstance().writeToScreen("G" +c.getGreen() , 4);
			if (c.getRed() >= 5 || c.getBlue() >= 5 )
				LCDWriter.getInstance().writeToScreen((((double)c.getRed()/c.getBlue() >1.2) ? "brick" : "foam" ), 2);
			else 
				LCDWriter.getInstance().writeToScreen("nothing found", 2);
			try{Thread.sleep(200);}catch(Exception e){};
		}
	}
	@Override
	public int getDistanceOnInvoke() {
		return distanceOnInvoke;
	}
	@Override
	public UltrasonicListener setDistanceOnInvoke(int distanceOnInvoke) {
		this.distanceOnInvoke = distanceOnInvoke;
		return this;
	}
	@Override
	public boolean isContinuous() {
		return continuous;
	}
	@Override
	public UltrasonicListener setContinuous(boolean continuous) {
		this.continuous = continuous;
		return this;
	}
	@Override
	public boolean isCalled() {
		return called;
	}
	@Override
	public UltrasonicListener setCalled(boolean called) {
		this.called = called;
		return this;
	}
}
	
	
