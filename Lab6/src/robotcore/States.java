package robotcore;

import lejos.nxt.Sound;

/**
 * This is similar to:
 * http://www.javacodegeeks.com/2012/03/automaton-implementation-in-java.html
 * If you want to look up the inspiration 
 * 
 * The states are:
 * 	ERROR
 * 	SETUP
 * 	LOCALIZING
 * 	TRAVELLING_TO_ZONE
 * 	OBSTACLE_AVOID_TO_ZONE
 *  SEARCHING
 *  CAPTURE
 *  TRAVELLING_TO_END
 *  OBSTACLE_AVOID_TO_END
 *  RELEASE
 *  FINAL
 *  
 * 
 * TODO: I'm still debating whether to keep this in one file or split it up. If someone wants
 * weigh in and/or change it
 *
 */
public enum States implements State{
	ERROR{
		//SHOULD NEVER REALLY GET TO THIS STATE, BUT IS FOR DEBUGGING PURPOSES, MAKE THE ROBOT BEEP A LOT
		@Override
		public void onEnter() {
			for(int i=0;i<10;i++){
				Sound.twoBeeps();
			}
		}

		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
		}

		@Override
		public State next() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
	},
	SETUP{

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public State next() {
			return LOCALIZING;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void interrupt() {
		}	
	},
	LOCALIZING{

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public State next() {
			return TRAVELLING_TO_ZONE;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void interrupt() {
		}	
	},
	TRAVELLING_TO_ZONE{

		boolean interrupted;
		
		@Override
		public void onEnter() {
			interrupted = false;
			
		}

		@Override
		public void interrupt() {
			interrupted = true;
			//should also interrupt main process
		}	

		@Override
		/**
		 * Called online when goal zone is reached
		 */
		public State next() {
			return interrupted ? OBSTACLE_AVOID_TO_ZONE : SEARCHING;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
	},
	OBSTACLE_AVOID_TO_ZONE{

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public State next() {
			return TRAVELLING_TO_ZONE;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
	},
	SEARCHING{

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public State next() {
			return CAPTURE;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
	},
	CAPTURE{

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public State next() {
			return TRAVELLING_TO_END;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
		
	},
	TRAVELLING_TO_END{

		boolean interrupted;

		@Override
		public void onEnter() {
			interrupted = false;
		}

		@Override
		public void interrupt() {
			interrupted = true;
		}

		@Override
		public State next() {
			return interrupted ? OBSTACLE_AVOID_TO_END : RELEASE;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
	},
	OBSTACLE_AVOID_TO_END{

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
		}

		@Override
		public State next() {
			return TRAVELLING_TO_END;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
		
	},
	RELEASE{

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public State next() {
			return FINAL;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
		
	},
	FINAL{

		@Override
		public void onEnter() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void interrupt() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public State next() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void onExit() {
			// TODO Auto-generated method stub
			
		}
		
	}

}
