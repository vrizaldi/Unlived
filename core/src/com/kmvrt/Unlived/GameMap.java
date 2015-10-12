package com.kmvrt.Unlived;

public class GameMap {
	// represent a map in-game

	private int typeID;

// constructor ----------------------------------------------------------------------------------------------
	public GameMap(int typeID) {
	
		this.typeID = typeID;
	}	// new(int)'s end


// getters and setters ----------------------------------------------------------------------------------------
	public int getTypeID() {
		// return the type id
	
		return typeID;
	} // getTypeID()'s end

	public int getMiddleX() {
		// return the x coordinate of the room's middle
	
		return Constants.ROOM_WIDTH / 2;
	} // getMiddleX()'s end

	public int getMiddleY() {
		// return the y coordinate of the room's middle
	
		return Constants.ROOM_HEIGHT / 2;
	} // getMiddleY()'s end

	public int getPortalX() {
		// return the x coordinate of the portal
		
		return (Constants.ROOM_WIDTH - Constants.PORTAL_WIDTH) / 2;
	}

	public int getPortalY() {
		// return the y coordinate of the portal
	
		return (Constants.ROOM_HEIGHT - Constants.PORTAL_HEIGHT) / 2;
	}

}
