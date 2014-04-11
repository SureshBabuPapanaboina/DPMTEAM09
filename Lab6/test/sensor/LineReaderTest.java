package sensor;

import movement.Driver;
import odometry.Odometer;
import robotcore.Configuration;
import robotcore.LCDWriter;
import sensors.LineReader;
import sensors.LineReaderListener;

public class LineReaderTest implements LineReaderListener {

	Configuration conf = Configuration.getInstance();
	LCDWriter lcd = LCDWriter.getInstance();
	Odometer odo = Odometer.getInstance();
	LineReader llr = LineReader.getLeftSensor(), rlr = LineReader.getRightSensor();
	Driver drive = Driver.getInstance();
	
	
	int left = 0 , right = 0 ;
	
	public static void main(String[] args) {
		LineReaderTest lrt = new LineReaderTest();
		lrt.init (lrt);
		lrt.drive.forward(150);
		
		LineReader.pauseAll();
		
		
		while (true){}
		
	}

	public void init(LineReaderTest lrt)
	{
		lcd.start();
		odo.start();
		llr.start();
		rlr.start();
		
		LineReader.subscribeToAll(lrt);
	}

	@Override
	public void passedLine(boolean isLeft) {
		if (isLeft) left ++ ;
		else right++;
		
		lcd.writeToScreen("l" + left, 0);
		lcd.writeToScreen("r" + right, 1);
		
	}

}
