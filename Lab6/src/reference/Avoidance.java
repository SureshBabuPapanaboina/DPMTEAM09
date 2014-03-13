/**
 * 
 */
package reference;

import sensors.UltrasonicListener;

/**
 * @author yuechuan
 * TODO unimplemented class, see an example of ultrasonic listener in the UltrasonicListeners package
 */
public class Avoidance implements UltrasonicListener {

	@Override
	public void ultrasonicDistance(int distanceFromObsticle) {
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
