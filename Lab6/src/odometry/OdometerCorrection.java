/*
 * Untested Version
 */
package odometry;

import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.comm.RConsole;
import robotcore.Configuration;
import sensors.LineReaderListener;
/**
 * TODO unimplemented class
 * @author Bonan Zhang
 *	this class corrects the odometer.
 *
 */
public class OdometerCorrection implements LineReaderListener{

	private NXTRegulatedMotor lMotor; 
	private NXTRegulatedMotor rMotor;
	
	private double offset;
	private final double distFromColorSensorToOdo = 12.5; //distance from colorsensors to odometer
	private boolean leftLineSeen = false;	//return true if left colorsensor detects the line
	private boolean rightLineSeen = false;	//return truen if right colorsensor detects the line
	private double[] tachl = new double[2];	//collect tachocounts for left motors
	private double[] tachr = new double[2];	//colleect tachocounts for right motors
	private double angleForXY;	//angle for correcting x,y coordinates
	private final double cSensorWidth = 10.7; //distance between two colorsensors
	private int count;  //determine the switch case for angle correction
	private boolean flagPassLeft;  //decide if correction is needed
	private boolean flagPassRight;

	private double tempAngle;

	private OdometerCorrection(){
		lMotor = Configuration.LEFT_MOTOR;
		rMotor = Configuration.RIGHT_MOTOR;
		this.leftLineSeen = false;
		this.rightLineSeen = false;
		this.count = 0;
		flagPassLeft = false;
		flagPassRight = false;
	}
	
	public void passedLine(boolean isLeft)
	{
		//get angle from odometer in degrees
		double angle = Odometer.getInstance().getTheta()*180/Math.PI;
		tempAngle = convertAngle(angle);
		RConsole.println("angle: "+String.valueOf(angle));
		RConsole.println("tempAngle: "+String.valueOf(tempAngle));
		//face y axis case
		if( 	(tempAngle>0 && tempAngle<20)		//
				||(tempAngle>340 && tempAngle<360)
				||(tempAngle>160 && tempAngle <200))
		{
			//this condition determines which sensor detects the line first, and then set corresponding values
			//for angle correction
			//here, the left sensor detects the line first
			if(isLeft)
			{
				//store the tachocount at this time
				tachl[0] = lMotor.getTachoCount();
				leftLineSeen = true;
				//check if the right sensor also saw the line. If it did, set the count value to prepare for angle correction
				if(leftLineSeen && rightLineSeen)
				{
					tachr[1] = rMotor.getTachoCount();
					count = 1;
					//choose to pass right motor tachocount into angle correction method
					flagPassRight = true;
				}
			}
			//here, the right sensor detects the line first
			else
			{
				tachr[0] = rMotor.getTachoCount();
				rightLineSeen = true;
				if(leftLineSeen && rightLineSeen)
				{
					tachl[1] = lMotor.getTachoCount();
					count = 2;
					flagPassLeft = true;
				}
			}
		}
		//face x axis case
		else if((tempAngle>70 && tempAngle<110)|| (tempAngle>250 && tempAngle<290))
		{
			//similar condition as mentioned above
			if(isLeft)
			{
				tachl[0] = lMotor.getTachoCount();
				leftLineSeen = true;
				if(leftLineSeen && rightLineSeen)
				{
					tachr[1] = lMotor.getTachoCount();
					count = 3;
					flagPassRight = true;
				}
			}
			else
			{
				tachr[0] = rMotor.getTachoCount();
				rightLineSeen = true;
				if(leftLineSeen && rightLineSeen)
				{
					tachl[1] = lMotor.getTachoCount();
					count = 4;
					flagPassLeft = true;
				}
			}
		}
		
		angle = convertBack(angle,tempAngle);
		//this condition determines which case it runs, left or right 
		if(flagPassLeft)
		{
			angleForXY = tCorrect(cSensorWidth,Configuration.LEFT_RADIUS,tachl[1],tachl[0],angle,count);
			coordinateCorrection(angleForXY,angle);
			flagPassLeft = false;
		}
		else if(flagPassRight)
		{
			angleForXY = tCorrect(cSensorWidth,Configuration.LEFT_RADIUS,tachr[1],tachr[0],angle,count);
			coordinateCorrection(angleForXY,angle);
			flagPassRight = false;
		}
	}
	
	//this method fixes the x and y coordinates depending on different cases, including two major cases, facing y case and
	//facing x case, then it will set the proper offset value and the pass to actual correction method
	public void coordinateCorrection(double angleForXY, double angle)
	{
		offset = cSensorWidth/2*Math.sin(angleForXY)+distFromColorSensorToOdo*Math.cos(angleForXY);
		
		if((angle>0 && angle<20)||(angle>340 && angle<360)||(angle>160 && angle <200))
		{
			if((angle>0 && angle<20)||(angle>340 && angle<360))
			{
				offset = Math.abs(offset);
			}
			else 
			{
				offset = -Math.abs(offset);
			}
			yCorrect(offset);
		}
		else if((angle>70 && angle<110)|| (angle>250 && angle<290))
		{
			if(angle>70 && angle<110)
			{
				offset = Math.abs(offset);
			}
			else
			{
				offset = -Math.abs(offset);
			}
			xCorrect(offset);
		}
	}
	
	//correct x
	public void xCorrect(double offset)
	{
		//use reminder (odometer value divided by 30) to determine how much to correct
		double x = (int)(Odometer.getInstance().getX()/30)*30.48;
		Odometer.getInstance().setX(x+offset); 
	}
	
	//correct y
	public void yCorrect(double offset)
	{
		//use reminder (odometer value divided by 30) to determine how much to correct
		double y = (int)(Odometer.getInstance().getY()/30)*30.48;
		Odometer.getInstance().setY(y+offset);
	}
	
	//this method will return a double angle for correcting x,y values. Also, it will correct the angle based on
	//the signal passing by the method above. It will correct in different cases
	public double tCorrect(double width, double radius, double tach1, double tach2, double angle, int count) 
	{	
		//use the difference of tachometer to calculate the error used to calculate the angle to be corrected
		double error = Math.abs(tach1-tach2)/180*Math.PI*radius;
		double correctedAngle = 0;
		//set a reminder to minimize the correction cases
		int reminder = (int)(angle/160);
		//do the correction
		switch(count)
		{
			case 1: correctedAngle = reminder*180-Math.atan(error/width)*180/Math.PI;  //face y, right first
					break;
			case 2: correctedAngle = reminder*180+Math.atan(error/width)*180/Math.PI;	//face y, left first
					break;
			case 3: correctedAngle = reminder*180+Math.atan(width/error)*180/Math.PI;	//face x, right first
					break;
			case 4: correctedAngle = reminder*180-Math.atan(width/error)*180/Math.PI;	//face x, left first
					break;
		}
		
		//set the corrected angle to the odometer
		Odometer.getInstance().setTheta(correctedAngle);
		//return the angle for x,y correction
		return Math.atan(error/width);
	}
	
	public double convertAngle(double angle)
	{
		if (angle < 0.0)
			angle = 360.0 + (angle % 360.0);
		
		return angle % 360.0;
	}
	
	public double convertBack(double angle, double tempAngle)
	{
		if(angle<0.0)
		{
			tempAngle = tempAngle % 360 - 360;
		}
		return tempAngle*Math.PI/180;
	}
}
