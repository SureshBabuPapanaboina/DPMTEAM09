package robotcore;

import lejos.robotics.Color;

public class MapInfo {
	Configuration conf = Configuration.getInstance();
	/**
	 * Sets the starting position based on a corner value given
	 * As per project specifications of Bluetooth Message
	 * @param corner
	 */
	public void setStartCorner(int corner){
		//TODO 
	}
	/**
	 * Sets the flag zone, this is the area where the robot searches for the flag
	 * @param lowerLeft
	 * @param upperRight
	 */
	public void setFlagZone(Coordinate lowerLeft, Coordinate upperRight){
		//this.endLowerLeft = lowerLeft;
		
	}
	
	/**
	 * Sets the opponents flag zone, not specified if should avoid or not in initial doc
	 * @param lowerLeft
	 * @param upperRight
	 */
	public void setOpponentFlagZone(Coordinate lowerLeft, Coordinate upperRight){
		//TODO: complete
	}
	
	/**
	 * Sets the drop zone that the robot should avoid of the opponent
	 * @param lowerLeft
	 */
	public void setOpponentDropZone(Coordinate lowerLeft){
		
	}
	
	/**
	 * Sets the drop zone of where it should drop off the flag
	 * @param lowerLeft
	 */
	public void setDropZone(Coordinate lowerLeft){
		
	}
	
	/**
	 * Sets the flag color that the robot should look for
	 * @param color
	 */
	public void setFlagColor(int color){
		
	}
	
	/**
	 * Get the end flag zone
	 * @return size-2 array containing bottom left coord, and top right coord
	 */
	public Coordinate[] getFlagZone(){
		//TODO: Complete
		return null;
	}
	
	/**
	 * Get the opponent end flag zone
	 * @return size-2 array containing bottom left coord, and top right coord
	 */
	public Coordinate[] getOpponentFlagZone(){
		//TODO: complete
		return null;
	}
	
	/**
	 * Get the opponent drop zone to avoid
	 * @return
	 */
	public Coordinate getOpponentDropZone(){
		//TODO
		return null;
	}
	
	/**
	 * Get the coordinates of the drop zone where to bring the flag
	 * @return
	 */
	public Coordinate getDropZone(){
		//TODO
		return null;
	}
	
	/**
	 * Returns the flag color the robot should be looking for
	 * @return
	 */
	public Color getFlagColor(){
		//TODO
		return null;
	}

}
