package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;

public class GameMap {
	// represent a map in-game
	
//	private static final String TAG = GameMap.class.getName();

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
					spawnPosX = getMiddle(x, Constants.ins.ROOM_WIDTH,
							Constants.ins.ROOMS_INTERVAL);
					spawnPosY = getMiddle(y, Constants.ins.ROOM_HEIGHT,
							Constants.ins.ROOMS_INTERVAL);

					portalPosX = getMiddle(x, Constants.ins.ROOM_WIDTH,
							Constants.ins.ROOMS_INTERVAL) 
						- (Constants.ins.PORTAL_WIDTH / 2);
					portalPosY = getMiddle(y, Constants.ins.ROOM_HEIGHT,
							Constants.ins.ROOMS_INTERVAL) 
						- (Constants.ins.PORTAL_HEIGHT / 2);
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
		rooms[roomX][roomY].visit(c);
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

}
