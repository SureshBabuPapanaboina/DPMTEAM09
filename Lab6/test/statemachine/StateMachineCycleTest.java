package statemachine;

import static org.junit.Assert.*;

import org.junit.Test;

import robotcore.StateMachine;
import robotcore.States;

public class StateMachineCycleTest {

	@Test
	public void regularCycle() {
		StateMachine sm = new StateMachine();
		assertEquals(States.SETUP, sm.getCurrentState());
		sm.transition();
		assertEquals(States.LOCALIZING, sm.getCurrentState());
		sm.transition();
		assertEquals(States.TRAVELLING_TO_ZONE, sm.getCurrentState());
		sm.interrupt();
		sm.transition();
		assertEquals(States.OBSTACLE_AVOID_TO_ZONE, sm.getCurrentState());
		sm.transition();
		assertEquals(States.TRAVELLING_TO_ZONE, sm.getCurrentState());
		sm.transition();
		assertEquals(States.SEARCHING, sm.getCurrentState());
		sm.transition();
		assertEquals(States.CAPTURE, sm.getCurrentState());
		sm.transition();
		assertEquals(States.TRAVELLING_TO_END, sm.getCurrentState());
		sm.interrupt();
		sm.transition();
		assertEquals(States.OBSTACLE_AVOID_TO_END, sm.getCurrentState());
		sm.transition();
		assertEquals(States.TRAVELLING_TO_END, sm.getCurrentState());
		sm.transition();
		assertEquals(States.RELEASE, sm.getCurrentState());
		sm.transition();
		assertEquals(States.FINAL, sm.getCurrentState());
	}

}
