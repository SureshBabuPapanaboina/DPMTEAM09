/**
 * 
 */
package CoreLib;

import coreLib.Coordinate;
import lejos.nxt.comm.RConsole;
import lejos.robotics.navigation.Pose;

/**
 * @author yuechuan
 *
 */
public final class PoseHelper {
	/**
	 * return a copy of the Pose instance 
	 * @param p
	 * @return
	 */
	static Pose copy(Pose p){
		return new Pose(p.getX(), p.getY() , p.getHeading());
	}
	

	/**
	 * @param currentLocation (x,y,theata wrt y axes)
	 * @param nextLocation (x,y, and some bogus theata)
	 * @return the concave turning angle where the head of the
	 * robot points to the next location. where neg means counter
	 * clockwise turn and positive means clockwise turns <b> in deg</b>
	 */
		public static double calculateRotationAngle( Pose currentLocation
			, Pose nextLocation){
		double	 dX = nextLocation.getX() - currentLocation.getX()
				,dY = nextLocation.getY() - currentLocation.getY(),
				/**
				 * in degree 
				 */
				currentAngle = Math.toDegrees(currentLocation.getHeading());
		
		RConsole.println("dX" + dX);
		RConsole.println("dY" + dY);
		
		double result = -currentAngle ;
		
		
		//double result = 0;
		if (dX >= 0 ){
			if (dY >= 0) 
				result += (Math.atan(dX/dY) * 180 /Math.PI);
			else // (dY <= 0) 
				result += (90 - (Math.atan(dY/dX) * 180 /Math.PI));
		}
		else if (dX < 0){
			if (dY >= 0)
				result +=(Math.atan(dX/dY) * 180 /Math.PI) ;
			else //(dY <=0)
				result +=(-90-(Math.atan(dY/dX) * 180 /Math.PI));
		}
		//should never reach this case, but keep it to make sure nothing goes wrong
		else if (dX==0 && dY == 0){
			result = 0 ;
		}
	
	return normalize(result);
}

	/**
	 * @param start
	 * @param end
	 * @return the distance between c1 and c2 using the formula sqrt(dX^2 + dY^2) 
	 */
	public static double calculateDistance(Coordinate start , Coordinate end){
		return Math.sqrt( sqr(end.getY() - start.getY()) + sqr(end.getX() - start.getX())); 
	}
	
	public static double sqr(double x){
		return x* x;
	}
	/**
	 * normalize angle in degress 
	 * @param angle in deg
	 * @return cancave angle in deg 
	 */
	public static double normalize (double angle ){
		double normalized = angle;
		if (angle > 180){
			normalized = (-360+ angle);
		}
		else if (angle <-180){
			normalized = 360 + angle;
		}
		return normalized;
	}
}
