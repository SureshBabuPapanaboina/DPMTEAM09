

package search;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.Sound;
import lejos.nxt.comm.RConsole;
import lejos.robotics.Color;
import robotcore.Configuration;
import robotcore.LCDWriter;

/**
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

		public static String toString(int i) {
			for (blockColor bc : blockColor.values()){
				if (bc.getCode() == i){
					return bc.toString();
				}
			}
			return "error toString in blockColor";
			
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
//		int [][] rgb = {
//				{180	,40	,55   }     ,
//				{125	,20	,25   }     ,
//				{80	,10	,13   }         ,
//				{55	,7	,8    }
//		};
//		
//		for (int [] i : rgb ){
//			RConsole.println("================" + i[0] + "\t"+ i[1] + "\t"+ i[2] );
//			blockColor bc = findBestMatch(i);
//			RConsole.println("<"+bc.toString()+">");
//		}
		
		Configuration config = Configuration.getInstance();
		lcd.start();
		ObjRec or = new ObjRec();
		
		while(true){
			while (!(Button.waitForAnyPress() == Button.ID_ENTER)){}
			blockColor bc ;
			bc = or.detect();
			RConsole.println(bc.toString());
			or.cs.setFloodlight(false);
			try{Thread.sleep(500);}catch(Exception e){};
			
		}
	}

	public blockColor detect(){
		
		int [] rgb = getRGB();
		lcd.writeToScreen("r: " + rgb[0]+"", 0	); 
		lcd.writeToScreen("g: "+ rgb[1]+"", 1	); 
		lcd.writeToScreen("b: "+ rgb[2]+"", 2	); 
		
		lcd.writeToScreen("r/g: " + (double) rgb[0] / rgb[1],3);
		lcd.writeToScreen("g/b: " + (double) rgb[1] / rgb[2],4);
		lcd.writeToScreen("r/b: " + (double) rgb[0] / rgb[2],5);
		lcd.writeToScreen("g/r: " + (double) rgb[1] / rgb[2],5);
		
		RConsole.println("RGB---");
		RConsole.println(rgb[0]+"");
		RConsole.println(rgb[1]+"");
		RConsole.println(rgb[2]+"");
		RConsole.println("");
		
		RConsole.println("r/g: " + (double) rgb[0] / rgb[1]);
		RConsole.println("g/b: " + (double) rgb[1] / rgb[2]);
		RConsole.println("r/b: " + (double) rgb[0] / rgb[2]);
		
		return findBestMatch(rgb);
		
	}

	
	/**
	 * center of the algorithm 
	 * @param rgb
	 * @return
	 */

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
