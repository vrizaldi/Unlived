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
				if(rooms[x][y].getTypeID() == Constants.ROOM_SPAWN) {
					spawnPosX = getMiddle(x, Constants.ROOM_WIDTH);
					spawnPosY = getMiddle(y, Constants.ROOM_HEIGHT);

					portalPosX = getMiddle(x, Constants.ROOM_WIDTH) 
						- (Constants.PORTAL_WIDTH / 2);
					portalPosY = getMiddle(y, Constants.ROOM_HEIGHT) 
						- (Constants.PORTAL_HEIGHT / 2);
				} 
			}
		}
	}	// new(int)'s end

	private float getMiddle(int coor, float length) {
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

	public static float getDoorPosX(float roomX, int dir) {
		
		if(dir == Constants.DIR_N || dir == Constants.DIR_S) {
			return roomX + ((Constants.ROOM_WIDTH - Assets.doorHSprite.getWidth()) / 2);
			
		} else if(dir == Constants.DIR_E) {
			return roomX + Constants.ROOM_WIDTH - Assets.doorVSprite.getWidth();
	
		} else if(dir == Constants.DIR_W) {
			return roomX;
			
		} else {
			return 0;
		}
	}
	
	public static float getDoorPosY(float roomY, int dir) {
				
		if(dir == Constants.DIR_N) {
			return roomY + Constants.ROOM_HEIGHT - Assets.doorHSprite.getHeight();
			
		} else if(dir == Constants.DIR_S) {
			return roomY;
			
		} else if(dir == Constants.DIR_E || dir == Constants.DIR_W) {
			return roomY + ((Constants.ROOM_HEIGHT - Assets.doorVSprite.getHeight()) / 2);
			
		} else {
			return 0;
		}
	}

	

// room class ---------------------------------------------------------------------------------------------------
	static class Room {
		
		private int typeID;
		private int x;
		private int y;
		public boolean north, south, east, west;
			// door location
		private boolean visited;

		public Room(int typeID, int x, int y) {

			this.typeID = typeID;
			
			north = south = east = west = false;
			this.x = x;
			this.y = y;
			visited = false;
		}

		public int getTypeID() {
		
			return typeID;
		}
		
		public int getX() {
			
			return x;
		}
		
		public int getY() {
			
			return y;
		} 

		public void visit(GameChar c) {
			
			if(!visited && c.getID() == Constants.CHAR_MAIN) {
				visited = true;
			}
		}

		public boolean isVisited() {
			
			return visited;
		}
	}

}
