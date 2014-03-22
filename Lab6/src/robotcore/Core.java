package robotcore;

import lejos.nxt.Button;

/**
 * This class runs the main functionality of the robot,
 * it contains the main state machine and runs initial
 * setup
 * 
 * @author Peter Henderson
 *
 */
public class Core {

	/**
	 * Runs the main program, instantiates the state machine and has 
	 * the main process loop
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		StateMachine sm = new StateMachine();
		sm.run();
		
		//Allows for quit kill switch, in final competition should 
		//maybe be removed for extra CPU cycles so this thread dies.
		while(Button.waitForAnyPress() != Button.ID_ESCAPE);
		System.exit(0);
	}
}
