package robotcore;

/**
 * An interface for the states, all states should implement these 3 methods
 * The run method should actually have a check in a sort of while loop to make sure 
 * that there is no interrupt needed
 * 
 * @author Peter Henderson
 *
 */
public interface State {

	/**
	 * This sets up the state
	 */
	public void onEnter();
	
	/**
	 * For extra cases
	 */
	public void interrupt();
	
	/**
	 * This runs the main point of the state
	 */
	public State next();
	
	/**
	 * This runs some cleanup
	 */
	public void onExit();
	
}
