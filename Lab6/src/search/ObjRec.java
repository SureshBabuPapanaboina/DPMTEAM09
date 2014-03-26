

package search;
import java.util.ArrayList;

import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.comm.RConsole;
import lejos.robotics.Color;
import robotcore.Configuration;
import robotcore.LCDWriter;

/**
 * the class provide the search for colored foams 
 * @author yuechuan
 *	
 */
public class ObjRec {
	ColorSensor cs = new ColorSensor(Configuration.COLOR_SENSOR_PORT);
	public static LCDWriter lcd = LCDWriter.getInstance();
	private static final boolean DEBUG = false;
	
	public static enum blockColor{
		BLOCK(0),
		LIGHT_BLUE(1),
		DARK_BLUE(2),
		RED(3),
		YELLOW(4),
		WHITE(5),
		NO_BLOCK(6);
		
		private int blockCode;
		
		public int getCode(){
			return blockCode;
		}
		
		blockColor(int code) {
			blockCode = code;
		}
		public String toString(){
			String result = null ;
			
			switch (blockCode){
				case 0 :
					result = "Block";
					break;
				case 1 : 
					result = "Light blue";
					break ; 
				case 2: 
					result = "Dark blue";
					break ;
				case 3 : 
					result = "Red";
					break ;
				case 4:
					result = "Yellow";
					break ;
				case 5 :
					result = "White";
					break ;
				case 6 :
					result = "No Block Error!"	;		
			}
			
			return result;
		}

		public static String toString(int i) throws Exception {
			for (blockColor bc : blockColor.values()){
				if (bc.getCode() == i){
					return bc.toString();
				}
			}
			throw new Exception("toString error");
			
		}

	};
	
	/**
	 * The mean value of the r/g ,g/b ,r/b ratio for the above 6 colored blocks excluding NO_BLOCK 
	 */
	private static final double [][] MEAN = {
			{2.51,	0.92,	2.26},	//block
			{1.54,	0.46,	0.68},	//light blue 
			{1.53	,0.23	,0.33},	//dark blue 
			{10.15	,0.82	,8.01},	//red	
			{999	,999	,999},	//yellow fake 
//			{2.44	,1.95	,4.65},	//yellow
			{1.88	,0.59	,1.04},	//white 
	};
	/**
	 * the standard div of the r/g ,g/b ,r/b ratio for the above 6 colored blocks excluding NO_BLOCK 
	 */
	private static final double [] [] STANDIV = {
			{0.718267697,0.1373318117,0.4373616683},
			{0.393979162,0.1729484897,0.0807139785},
			{0.630641582,0.1838648267,0.05645944715},
			{4.406467144,0.1868654735,3.259469709},
			{0.552671033,0.2986822523,0.7989758994},
			{0.478164613,0.1866890277,0.08003917469}
	};

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Configuration config = Configuration.getInstance();
		lcd.start();
		ObjRec or = new ObjRec();
		
		while(true){
			while (!(Button.waitForAnyPress() == Button.ID_ENTER)){}
			or.detect();

			or.cs.setFloodlight(false);
			try{Thread.sleep(500);}catch(Exception e){};
			
		}
	}

/**
*
*return an array of blocks corresponding to the blocks that has passed the test 
*if there are more than 1 element in the array then there is a porblem 
*if there are none in the array then that means it has not detect any block so it should move and then redo test 
*/
	public blockColor[] detect(){
		
		int [] rgb = getRGB();
		double [] ratios = {
				(double) rgb[0] / rgb[1],
				(double) rgb[1] / rgb[2],
				(double) rgb[0] / rgb[2]
		};
		
		ArrayList<blockColor> blkList = new ArrayList<blockColor>();
		
		if (testDarkBlue(rgb, ratios)) blkList.add(blockColor.DARK_BLUE);
		if (testLightBlue(rgb, ratios)) blkList.add(blockColor.LIGHT_BLUE);
		if (testRed(rgb, ratios)) blkList.add(blockColor.RED);
		if (testWhite(rgb, ratios)) blkList.add(blockColor.WHITE);
		if (testYellow(rgb, ratios)) blkList.add(blockColor.YELLOW);

		return (blockColor[]) blkList.toArray();
	}
	
	/**
	 * check if n2 is within eps of n 
	 * @param n first number 
	 * @param n2 2nd number 
	 * @param eps small rage of aprox 
	 * @return
	 */
	private static boolean doubleApprox(double n,double n2 , double eps){
		return (Math.abs(n-n2) <= eps);
	}
	/**
	 * 
	 * @param rgb rgb raw values
	 * @param ratios rg gb rb ratios
	 * @return true if it it has passed the block test 
	 */
	private boolean testBlock(int [] rgb , double[] ratios ){
		boolean c1 = ratios[2] > ratios[0]; //rb > rg 
		boolean c2 = ratios[2] > ratios[1]; //rb > gb 
		boolean c3 = ratios[2] > 1 && ratios[1] > 1 && ratios[0] > 1 ;// rg & gb & rb all > 1
//		lcd.writeToScreen("c1" + c1 , 4);
//		lcd.writeToScreen("c2" + c2 , 5);
//		lcd.writeToScreen("c3" + c3 , 6);
		return (c1&&c2&&c3);
				
	}
	
	/**
	 * 
	 * @param rgb rgb raw values
	 * @param ratios rg gb rb ratios
	 * @return true if it it has passed the block test 
	 */
	private boolean testLightBlue(int [] rgb , double[] ratios ){
		//gb ratio is about .83 +-0.1 when the distance is around 5 cm if the dist 
		boolean c1 = doubleApprox(ratios[1], 0.83, 0.17);
		boolean c2 = ratios[0] > ratios[2]; //rg always > rb 
		boolean c3 = rgb[0] < rgb[2]; //r< b  
//		lcd.writeToScreen("c1" + c1 , 4);
//		lcd.writeToScreen("c2" + c2 , 5);
//		lcd.writeToScreen("c3" + c3 , 6);
		return (c1 && c2 && c3);
	}
	
	/**
	 * 
	 * @param rgb rgb raw values
	 * @param ratios rg gb rb ratios
	 * @return true if it it has passed the block test 
	 */
	private boolean testDarkBlue(int [] rgb , double[] ratios ){
		boolean c1 ,c2 , c3 ;
		c1 = (ratios[0] > .5)? true : false ;
		c2 =  doubleApprox(ratios[2], .343, 0.15);
		c3 = (ratios[2] < 1)? true : false ;
		
		lcd.writeToScreen("c1" + c1 , 4);
		lcd.writeToScreen("c2" + c2 , 5);
		lcd.writeToScreen("c3" + c3 , 6);
		
		return (c1&&c2&&c3);
				
	}
	
	/**
	 * 
	 * @param rgb rgb raw values
	 * @param ratios rg gb rb ratios
	 * @return true if it it has passed the block test 
	 */
	private boolean testRed(int [] rgb , double[] ratios ){
		boolean c1 = ratios[0] > 3 && ratios[2] > 3; //rg > 3 and rb > 3
		boolean c2 = ratios[1] < 1.5;
		boolean c3 = rgb[0] > rgb[1] && rgb[0] > rgb[2];
//		lcd.writeToScreen("c1" + c1 , 4);
//		lcd.writeToScreen("c2" + c2 , 5);
//		lcd.writeToScreen("c3" + c3 , 6);
		return (c1 && c2 && c3);
	}
	
	/**
	 * 
	 * @param rgb rgb raw values
	 * @param ratios rg gb rb ratios
	 * @return true if it it has passed the block test 
	 */
	private boolean testYellow(int [] rgb , double [] ratios ){
		boolean c1 = ratios[0] > 0.9 && ratios[0] < 2.1;
		boolean c2 = ratios[1] > 2 ;
		boolean c3 = ratios[2] > 2.4;
		boolean c4 = rgb[0] > rgb[1] && rgb[1] > rgb[2] ;
//		lcd.writeToScreen("c1" + c1 , 4);
//		lcd.writeToScreen("c2" + c2 , 5);
//		lcd.writeToScreen("c3" + c3 , 6);
//		lcd.writeToScreen("c4" + c4 , 7);
		return (c1 && c2 && c3 && c4);
	}
	
	/**
	 * 
	 * @param rgb rgb raw values
	 * @param ratios rg gb rb ratios
	 * @return true if it it has passed the block test 
	 */
	private boolean testWhite(int [] rgb , double[] ratios ){
		boolean c1 = ratios[0] > 0.95 && ratios[0] < 2;
		boolean c2 =doubleApprox(ratios[1], 1, 0.2) ;
		boolean c3 = ratios[2] > 0.95 && ratios[2] < 2;
//		lcd.writeToScreen("c1" + c1 , 4);
//		lcd.writeToScreen("c2" + c2 , 5);
//		lcd.writeToScreen("c3" + c3 , 6);
		return (c1 && c2 && c3);
	}
	
	

	/**
	 * center of the algorithm 
	 * @param rgb
	 * @return
	 */
	private static blockColor findBestMatch(int[] rgb) {
		// calculate the rgb ratio
		
		double rgRatio = (double) rgb[0] / rgb[1];
		RConsole.println("rgRatio: "+rgRatio);
		double gbRatio = (double) rgb[1] / rgb[2];
		RConsole.println("gbRatio: "+gbRatio);
		double rbRatio = (double) rgb[0] / rgb[2];
		RConsole.println("rbRatio: "+rbRatio);
		
		double [] scores = new double [MEAN.length];


		double alpha = 1 , beta = 1, gama = 1;
		double a = 0 , b = 0  , c =0 ;
		//calculate the score for 
		for (int i = 0 ; i < scores.length ; i ++ ){
			a = Math.abs(alpha * (MEAN[i][0] - rgRatio / STANDIV[i][0]));	//alpha * abs(mean-value)/stdiv
			b = Math.abs(beta * (MEAN[i][1] - gbRatio / STANDIV[i][1])) ;
			c = Math.abs(gama * (MEAN[i][2] - rbRatio / STANDIV[i][2]));
			scores[i] = a + b + c;

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
//			c = new Color(1, 1, 1);
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
