/**
 * 
 */
package search;

import lejos.nxt.ColorSensor;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;
import lejos.robotics.Color;
import robotcore.Configuration;

/**
 * @author yuechuan
 *	
 */
public class ObjectRecognization {
	private static final boolean DEBUG = false;
	private static Configuration conf = Configuration.getInstance();
	private static ColorSensor cs = new ColorSensor(Configuration.COLOR_SENSOR_PORT);
	
	public static enum blockColor{
		BLOCK,
		LIGHT_BLUE,
		DARK_BLUE,
		RED,
		YELLOW,
		WHITE,
		NO_BLOCK;
	};
	
	/**
	 * The mean value of the r/g ,g/b ,r/b ratio for the above 6 colored blocks excluding NO_BLOCK 
	 */
	private final double [][] MEAN = {
			{2515,	925,	2265},
			{},
			{},
			{},
			{},
			{},
	};
	/**
	 * the standard div of the r/g ,g/b ,r/b ratio for the above 6 colored blocks excluding NO_BLOCK 
	 */
	private final double [] [] STANDIV = {
			{},
			{},
			{},
			{},
			{},
			{}
	};

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	
	
	public blockColor detect(){
		
		int [] rgb = getRGB();		
		return findBestMatch(rgb);
		
	}
	/**
	 * center of the algorithm 
	 * @param rgb
	 * @return
	 */
	private blockColor findBestMatch(int[] rgb) {
		// calculate the rgb ratio
		
		double rgRatio = (double) rgb[0] / rgb[1];
		double gbRatio = (double) rgb[1] / rgb[2];
		double rbRatio = (double) rgb[0] / rgb[2];
		
		double [] scores = new double [MEAN.length];
		double alpha = 1 , beta = 1, gama = 1 ;
		
		//calculate the score for 
		for (int i = 0 ; i < scores.length ; i ++ ){
			scores[i] = alpha * (MEAN[i][0] - rgRatio / STANDIV[i][0]) +
						beta * (MEAN[i][1] - gbRatio / STANDIV[i][1]) + 
						gama * (MEAN[i][2] - rbRatio / STANDIV[i][2]);
		}
		
		//the block with the lowest score wins
		
		blockColor result = blockColor.NO_BLOCK;
		//initialize minScore
		double minScore = Integer.MAX_VALUE ;
		for (int i = 0 ; i < scores.length ; i ++){
			if (scores[i] <= minScore){
				minScore = scores[i];
				//set to that block
				switch (i){
				case 0 :
					result = blockColor.BLOCK;
					break;
				case 1 : 
					result = blockColor.LIGHT_BLUE;
					break ; 
				case 2: 
					result = blockColor.DARK_BLUE;
					break ;
				case 3 : 
					result = blockColor.RED;
					break ;
				case 4:
					result = blockColor.YELLOW;
					break ;
				case 5 :
					result = blockColor.WHITE;
					break ;
				default :
					result = blockColor.NO_BLOCK;
				}
			}

		}
		
		
		return result;
		
	}


	/**
	 * read the rgb values from the sensor, take the reading 10 times and give the average
	 * @return an 1d arr of 3 elements that represent the rgb value 
	 */
	private int [] getRGB(){
		cs.setFloodlight(true);
		if (DEBUG)	RConsole.println("getting RGB value");
		int [] rgb = new int [3];
		Color c ;
		//obtain 10 reading 
		for (int i = 0 ; i < rgb.length ; i ++ ){
			c = cs.getColor();
			rgb[0] += c.getRed();
			rgb[1] += c.getGreen();
			rgb[2] += c.getBlue();
			//pause 80 ms to get another reading 
			if (DEBUG ) RConsole.println("r " + rgb[0]/i +"\t"+ "g " + rgb[1]/i + "\t" + "b " + rgb[2]/i);
			try {Thread.sleep(80);}catch(Exception e){};
		}
		//calculate the mean
		for (int i = 0 ; i < rgb.length ; i ++){
			rgb[i] /= (rgb.length );
		}
				
		return rgb;
	}

}
