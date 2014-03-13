package objectdetection;

import sensors.UltrasonicListener;

/**
 * This class listens for a possible collision and calls a callback in the object detector 
 * if one is eminent
 *
 */
public class CollisionListener implements UltrasonicListener {
	//TODO: again not sure if we need all these
	
	@Override
	public void ultrasonicDistance(int distanceFromObstacle) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getDistanceOnInvoke() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public UltrasonicListener setDistanceOnInvoke(int distance) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isContinuous() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UltrasonicListener setContinuous(boolean continuous) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isCalled() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public UltrasonicListener setCalled(boolean called) {
		// TODO Auto-generated method stub
		return null;
	}

}
