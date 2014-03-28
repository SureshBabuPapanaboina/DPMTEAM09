/**
 * 
 */
package robotcore;

/**
 * contains the 4 starting corners.
 * 
 * X1 = lower left 
 * X2 = lower right 
 * X3 = top left 
 * X4 = top right 
 * 
 * @author yuechuan
 *
 */
public enum Corners {
	
	X1(0),X2(1),X3(2),X4(3);
	
	private int ID ;
	private Corners(int i ){
		ID = i ;
	}
	
	public String toString(){
		switch (this){
		case X1:
			return "lower left";
		case X2:
			return "lower right";
		case X3:
			return "top left ";
		case X4 :
			return "top right";
		}
		return "error";
	}

}
