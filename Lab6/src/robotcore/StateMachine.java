package robotcore;

/**
 * This is the main state machine class, this will keep track of the current robot states
 * callbacks on this class will change the robot state and thus affect its actions in the core
 *
 */
public class StateMachine {
	
	/**
	 * Initializes the state machine, creates instances of the states
	 */
	public StateMachine(){
		
	}
	
	/**
	 * Gets the current state
	 * @return
	 */
	public State getCurrentState(){
		return null;
	}
	
	/**
	 * Transitions to the new state calling onExit of the current state
	 * and on enter of the nextState
	 * @param state
	 */
	public void transitionTo(State state){
		
	}
	
	/**
	 * Transition to Localizing State
	 */
	public void onDoneSetup(){
		
	}
	
	/**
	 * Transition to travelling state
	 */
	public void onDoneLocalizing(){
		
	}
	
	/**
	 * Transition to search state
	 */
	public void onZoneReached(){
		
	}
	
	/**
	 * Transition to the Capture State
	 */
	public void onFound(){
		
	}
	
	/**
	 * Transition to the BlockRemoval state (removes the block from the endzone
	 */
	public void onFoundWrongBlock(){
		
	}
	
	/**
	 * Transition back to the search state
	 */
	public void onWrongBlockRemoved(){
		
	}
	
	/**
	 * Transition to the travelling state to the endzone
	 */
	public void onCapture(){
		
	}
	
	/**
	 * Transition to the obstacle avoidance state
	 */
	public void onObstacleDetected(){
		
	}
	
	/**
	 * Transition back to the travelling state
	 * 
	 */
	public void onPathModified(){
		
	}
	
	/**
	 * Enter final state
	 */
	public void onFlagZoneReached(){
		
	}
}
