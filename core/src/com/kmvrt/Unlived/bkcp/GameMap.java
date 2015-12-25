package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;

public class GameMap {
	// represent a map in-game
	
	private static final String TAG = GameMap.class.getName();

	private int typeID;

	private Room[][] rooms;
	
	// spawn position
	private float spawnPosX;
	private float spawnPosY;
	
	// portal position
	private float portalPosX;
	private float portalPosY;
	
	public boolean justChanged;
	

// constructor ----------------------------------------------------------------------------------------------
	public GameMap(int typeID, Room[][] rooms) {
	
		this.typeID = typeID;
		this.rooms = rooms;

		// flag spawn and portal point
		for(int y = 0; y < Constants.ROOMS_NUM_Y; y++) {
			for(int x = 0; x < Constants.ROOMS_NUM_X; x++) {
				if(rooms[x][y] != null 
						&& rooms[x][y].getTypeID() == Constants.ROOM_SPAWN) {
					Gdx.app.debug("GameMap", "Spawn in " + x + ", " + y);
					spawnPosX = getMiddle(x, Constants.ROOM_WIDTH,
							Constants.ROOMS_INTERVAL);
					spawnPosY = getMiddle(y, Constants.ROOM_HEIGHT,
							Constants.ROOMS_INTERVAL);

					portalPosX = getMiddle(x, Constants.ROOM_WIDTH,
							Constants.ROOMS_INTERVAL) 
						- (Constants.PORTAL_WIDTH / 2);
					portalPosY = getMiddle(y, Constants.ROOM_HEIGHT,
							Constants.ROOMS_INTERVAL) 
						- (Constants.PORTAL_HEIGHT / 2);
				} 
			}
		}
		justChanged = false;
	}	// new(int)'s end

	public static float getMiddle(int coor, float length, float add) {
		// return the middle of a line 

		return (coor * length + (coor * add)) + (length / 2);
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

	public static float getDoorPosX(float roomX, int dir) {
		
		if(dir == Constants.DIR_N || dir == Constants.DIR_S) {
			return (roomX + ((Constants.ROOM_WIDTH - Assets.doorHSprite.getWidth()) / 2));
			
		} else if(dir == Constants.DIR_E) {
			return roomX + Constants.ROOM_WIDTH - (float)1/16;
	
		} else if(dir == Constants.DIR_W) {
			return roomX - Assets.doorVSprite.getWidth() + (float)1/16;
			
		} else {	// invalid
			return 0;
		}
	}
	
	public static float getDoorPosY(float roomY, int dir) {
				
		if(dir == Constants.DIR_N) {
			return roomY + Constants.ROOM_HEIGHT - (float)1/16;
			
		} else if(dir == Constants.DIR_S) {
			return roomY - Assets.doorHSprite.getHeight() + (float)1/16;
			
		} else if(dir == Constants.DIR_E || dir == Constants.DIR_W) {
			return roomY + ((Constants.ROOM_HEIGHT 
					- Assets.doorVSprite.getHeight()) / 2);
			
		} else {
			return 0;
		}
	}
	
	public Room getRandRoom() {
		// return a random room
		
		do {
			int x = (int)(Math.random() * Constants.ROOMS_NUM_X);
			int y = (int)(Math.random() * Constants.ROOMS_NUM_Y);
			if(rooms[x][y] != null) {
				return rooms[x][y];
			}
		} while(true);
	}
	
	public Room visit(GameChar c, int roomX, int roomY) {
		// return the specified room
		
		justChanged = true;
		try {
			rooms[roomX][roomY].visit(c);
		} catch(NullPointerException e) {
			Gdx.app.debug(TAG, "Trying to visit undefined room");
		}
/*		if(c.getID() == Constants.CHAR_MAIN) {
			Gdx.app.debug(TAG, "c = mainchar (" + c.x + ", " + c.y + ") : " +
					"(" + roomX + ", " + roomY + ")");
		} else {
			Gdx.app.debug(TAG, "c = creep (" + c.x + ", " + c.y + ") : " +
					"(" + roomX + ", " + roomY + ")");
		}*/
		
		return rooms[roomX][roomY];
	}
	
	public Room getRoom(int x, int y) {
		
		return rooms[x][y];
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

		protected void visit(GameChar c) {
			
			if(!visited && c.getID() == Constants.CHAR_MAIN) {
				visited = true;
			}
		}

		public boolean isVisited() {
			
			return visited;
		}
	}

}
