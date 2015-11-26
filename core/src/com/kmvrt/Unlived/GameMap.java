package com.kmvrt.Unlived;

public class GameMap {
	// represent a map in-game

	private int typeID;

	private Room[][] rooms;
	
	// spawn position
	private float spawnPosX;
	private float spawnPosY;
	
	// portal position
	private float portalPosX;
	private float portalPosY;

// constructor ----------------------------------------------------------------------------------------------
	public GameMap(int typeID, Room[][] rooms) {
	
		this.typeID = typeID;
		this.rooms = rooms;

		for(int y = 0; y < Constants.ROOMS_NUM_Y; y++) {
			for(int x = 0; x < Constants.ROOMS_NUM_X; x++) {
/*				if(rooms[x][y].getTypeID() == Constants.ROOM_SPAWN) {
					spawnPosX = getMiddle(x, Constants.ROOM_WIDTH);
					spawnPosY = getMiddle(y, Constants.ROOM_HEIGHT);

				} else if(rooms[x][y].getTypeID() == Constants.ROOM_PORTAL) {
					portalPosX = getMiddle(x, Constants.ROOM_WIDTH) 
						- (Constants.PORTAL_WIDTH / 2);
					portalPosY = getMiddle(y, Constants.ROOM_HEIGHT) 
						- (Constants.PORTAL_HEIGHT / 2);
				} */
			}
		}
	}	// new(int)'s end

	private float getMiddle(int coor, int length) {
		// return the middle of a line 

		return (coor * length) + (length / 2);
	}


// getters and setters ----------------------------------------------------------------------------------------
	public int getTypeID() {
		// return the type id
	
		return typeID;
	} // getTypeID()'s end

	public float getSpawnPosX() {
	
		return spawnPosX;
	}

	public float getSpawnPosY() {
	
		return spawnPosY;
	}

	public float getPortalPosX() {
		
		return portalPosX;
	}
	
	public float getPortalPosY() {
		
		return portalPosY;
	}
	
	public Room getRoom(int x, int y) {
		
		return rooms[x][y];
	}

	

// room class ---------------------------------------------------------------------------------------------------
	static class Room {
		
		private int typeID;
		public boolean north, south, east, west;
			// door location

		public Room(int typeID) {

			this.typeID = typeID;
			
			north = south = east = west = false;
		}

		public int getTypeID() {
		
			return typeID;
		}
	}

}
