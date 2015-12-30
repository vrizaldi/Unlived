package com.kmvrt.Unlived.gameplay;

//import java.util.HashMap;
import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.math.Rectangle;
//import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Timer;

public class Navigator {
	// update the map in-game

	private static final String TAG = Navigator.class.getName();
	
	private StateData data;		// the current game state data

	// for collision detection
//	private Rectangle rec1;
//	private Rectangle rec2;
//	private Rectangle inter;
//	private int roomPassed;

	private boolean checkRoom;


// constructor ----------------------------------------------------------------------------------------------
	public Navigator(StateData data) {
	
		this.data = data;
		
//		rec1 = new Rectangle();
//		rec2 = new Rectangle();
//		inter = new Rectangle();
		checkRoom = true;
	}


// create and dispose current game ---------------------------------------------------------------------------
	public void initNewGame() {
		// create objects for new game
	
		data.map = deployMap();
		data.newMap = true;

		checkRoom = true;
		Timer.schedule(
				new Timer.Task() {

					@Override
					public void run() {
						
						checkRoom = true;
					}
				}, 0.2f, 0.2f);
			// check room every 0.2 sec
//		roomPassed = 0;
	} // initNewGame()'s end



// update the game state -------------------------------------------------------------------------------------
	public void update() {
		// update the current state

		for(GameChar c : data.chars) {
			// check if need to go to the next map 

			// room check 
			if(checkRoom) {
				int x = (int)(c.x / 
					(Constants.ROOM_WIDTH + 
					Constants.ROOMS_INTERVAL));	// the gaps between rooms
				int y = (int)(c.y / 
					(Constants.ROOM_HEIGHT + 
					Constants.ROOMS_INTERVAL));
				c.cRoom = data.map.visit(c, x, y);
			}

			// collision with portal
			if(c.getID() == Constants.CHAR_MAIN) {
				// if the char is the main char
				if(data.isLevelFinished()) {
					// if the level is finished
					// move to the next level
					data.switchLevel = true;
					
					data.newMap = true;
				} 
			} 
		}	// char iter's
	}	// update()'s end

	private GameMap deployMap() {
		// return a room object for the game
	
		GameMap nRoom = null;

		Room[][] rooms;
		do {
			rooms = 
				new Room[Constants.ROOMS_NUM_X][Constants.ROOMS_NUM_Y];
			int rx = (int)(Math.random() * Constants.ROOMS_NUM_X);
			int ry = (int)(Math.random() * Constants.ROOMS_NUM_Y);
			Gdx.app.debug(TAG, rx + ", " + ry);
			deployRooms(rooms, rx, ry, true);
		} while(!reachMinimum(rooms));
		
		nRoom = new GameMap(Constants.MAP_NORMAL, rooms);

		return nRoom;
	} // deployMap(bool)'s end

	private void deployRooms(Room[][] map, int x, int y, boolean first) {
		// deploy rooms to the map into the specified x and y by recursing

		if(map[x][y] == null) {	// if haven't inited
			int typeID = first ? Constants.ROOM_SPAWN : Constants.ROOM_NORMAL;
			map[x][y] = new Room(typeID, x, y);
			
		} else {
//			Gdx.app.debug(TAG, "Attempted to reinit a room");
			return;
		}
		Room r = map[x][y];

		int doorCt = 0;
		// check if the close rooms has a door to this room
		if(!first) {
			if(y < Constants.ROOMS_NUM_Y - 1 
					&& map[x][y + 1] != null 
					&& map[x][y + 1].south) {
				// room in the north has a door to this room
				// create a door to the north
				r.north = true;
				doorCt++;
			}
			if(y > 0 && map[x][y - 1] != null 
					&& map[x][y - 1].north) {
				// room in the south
				r.south = true;
				doorCt++;
			}
			if(x < Constants.ROOMS_NUM_X - 1
					&& map[x + 1][y] != null
					&& map[x + 1][y].west) {
				// room in the east
				r.east = true;
				doorCt++;
			}
			if(x > 0 && map[x - 1][y] != null
					&& map[x - 1][y].east) {
				// room int the west
				r.west = true;
				doorCt++;
			}
		}
		
		if(y == Constants.ROOMS_NUM_Y || y == 0) {
			doorCt++;
		}
		if(x == Constants.ROOMS_NUM_X || x == 0) {
			doorCt++;
		}

		// create new doors randomly
		boolean crNorth, crSouth, crEast, crWest;	// rooms to be created later
		crNorth = crSouth = crEast = crWest = false;
		
		int moreDoor = doorCt < 2 ? (int)(Math.random() * (4 - doorCt)) + 1 
				: (int)(Math.random() * (4 - doorCt));
//		Gdx.app.debug(TAG, "moreDoor = " + moreDoor);
		while(moreDoor > 0) {
			int dir = (int)(Math.random() * 4) + 1;
			switch(dir) {
			case 1:
				if(y < Constants.ROOMS_NUM_Y - 1 
						&& map[x][y + 1] == null && !r.north) {
					// init north 
					r.north = true;
					
					crNorth = true;
					break;
					
				} else if(y < Constants.ROOMS_NUM_Y - 1 
						&& !r.north) {
					break;
				}

			case 2:
				if(x < Constants.ROOMS_NUM_X - 1 
						&& map[x + 1][y] == null && !r.east) {
					// init east
					r.east = true;
					
					crEast = true;
					break;
					
				} else if(x < Constants.ROOMS_NUM_X - 1 
						&& !r.east) {
					break;
				}

			case 3:
				if(x > 0 && map[x - 1][y] == null && !r.west) {
					// init west
					r.west = true;
					
					crWest = true;
					break;
					
				} else if(x > 0 && !r.west) {
					break;
				}

			case 4:
				if(y > 0 && map[x][y - 1] == null && !r.south) {
					r.south = true;
					
					crSouth = true;		// init south
					break;
				
				} else if(y > 0 && !r.south) {
					break;
				}
			}	// switch(dir)'s
			moreDoor--;
		}	// while(times)'s
//		Gdx.app.debug(TAG, "Adding doors finished");
		
		if(!r.south && !r.north && !r.east && !r.west
				/*(crNorth || crSouth || crEast || crWest)*/) {
			Gdx.app.error(TAG, "Room with no door inited");
//			Gdx.app.exit();
		}

		// init other rooms
		while(crNorth || crSouth || crEast || crWest) {
//			Gdx.app.debug(TAG, "Adding rooms...");
			int order = (int)(Math.random() * 4); 
			if(order == 0 && crNorth) {
				// deploy new room in the north
				crNorth = false;
				deployRooms(map, x, y + 1, false);

			} else if(order == 1 && crSouth) {
				// south
				crSouth = false;
				deployRooms(map, x, y - 1, false);

			} else if(order == 2 && crEast) {
				// east
				crEast = false;
				deployRooms(map, x + 1, y, false);

			} else if(order == 3 && crWest) {
				// west
				crWest = false;
				deployRooms(map, x - 1, y, false);
			}
		}	// while still creating
	}	// deployRooms(Room, int, int)'s
	
	private boolean reachMinimum(Room[][] rooms) {
		// whether there are enough rooms in the game
		
		int roomCt = 0;
		for(int y = 0; y < Constants.ROOMS_NUM_Y; y++) {
			for(int x = 0; x < Constants.ROOMS_NUM_X; x++) {
				if(rooms[x][y] != null) {
					roomCt++;
				}
			}	// x iter's
		}	// y iter's
		
		if(roomCt >= Constants.ROOMS_NUM_MIN) {
			return true;
		} else {
			return false;
		}
	}
	
public static float getDoorPosX(float roomX, int dir) {
		
		if(dir == Constants.DIR_N || dir == Constants.DIR_S) {
			return (roomX + ((Constants.ROOM_WIDTH - Assets.ins.doorHSprite.getWidth()) / 2));
			
		} else if(dir == Constants.DIR_E) {
			return roomX + Constants.ROOM_WIDTH - (float)1/16;
	
		} else if(dir == Constants.DIR_W) {
			return roomX - Assets.ins.doorVSprite.getWidth() + (float)1/16;
			
		} else {	// invalid
			return 0;
		}
	}
	
	public static float getDoorPosY(float roomY, int dir) {
				
		if(dir == Constants.DIR_N) {
			return roomY + Constants.ROOM_HEIGHT - (float)1/16;
			
		} else if(dir == Constants.DIR_S) {
			return roomY - Assets.ins.doorHSprite.getHeight() + (float)1/16;
			
		} else if(dir == Constants.DIR_E || dir == Constants.DIR_W) {
			return roomY + ((Constants.ROOM_HEIGHT 
					- Assets.ins.doorVSprite.getHeight()) / 2);
			
		} else {
			return 0;
		}
	}
}	// class' end
