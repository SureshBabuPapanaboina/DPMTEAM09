package robotcore;

/**
 * This is the main state machine class, this will keep track of the current robot states
 * callbacks on this class will change the robot state and thus affect its actions in the core
 *
 * @author Peter Henderson
 */
public class StateMachine extends Thread{
	
	private robotcore.State currentState;
	
	/**
	 * Initializes the state machine, creates instances of the states
	 */
	public StateMachine(){
		currentState = States.SETUP;
	}
	
	/**
	 * Run the state machine, this is the main process!
	 */
	@Override
	public void run(){
		//The first state
		currentState.onEnter();
		
		//Cycle through the states until you reach the fnial state
		while(!(currentState.equals(States.FINAL))){ 
			transition();
		}
		
	}
	
	/**
	 * Gets the current state
	 * @return
	 */
	public robotcore.State getCurrentState(){
		return currentState;
	}
	
	/**
	 * Transitions to the new state calling onExit of the current state
	 * and on enter of the nextState
	 * @param state
	 */
	public void transition(){
		currentState.onExit(); //cleanup
		currentState = currentState.next(); //get the next state
		currentState.onEnter(); //run the next state
	}
	
	/**
	 * Call this to interrupt a current state (this is basically for the travelling/obstacle avoidance
	 */
	public void interrupt(){
		currentState.interrupt();
	}

}
