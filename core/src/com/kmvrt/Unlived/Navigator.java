package com.kmvrt.Unlived;

//import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;

public class Navigator {
	// update the map in-game

	private static final String TAG = Navigator.class.getName();
	
	private StateData data;		// the current game state data

	// for collision detection
	private Rectangle rec1;
	private Rectangle rec2;
	private Rectangle inter;
//	private int roomPassed;


// constructor ----------------------------------------------------------------------------------------------
	public Navigator(StateData data) {
	
		this.data = data;
		
		rec1 = new Rectangle();
		rec2 = new Rectangle();
		inter = new Rectangle();
	}


// create and dispose current game ---------------------------------------------------------------------------
	public void initNewGame() {
		// create objects for new game
	
		data.map = deployMap();
		data.newMap = true;
//		roomPassed = 0;
	} // initNewGame()'s end

	public void disposeGame() {
		// dispose the current game objects
	
		data.map = null;
	} // disposeGame()'s end



// update the game state -------------------------------------------------------------------------------------
	public void update() {
		// update the current state

		for(GameChar c : data.chars) {

			// set rec1 to be the rectangle of current char
			rec1.setPosition(c.x, c.y);
			rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
//			Rectangle inter = new Rectangle();	// the intersection
			
			// do collision detection to other things here *********************

			// check if need to go to the next map 
			if(c.getID() == Constants.CHAR_MAIN) {
				// if the char is the main char
				if(data.isLevelFinished()) {
					// if the level is finished
					rec2.setPosition(
						data.map.getPortalPosX(), data.map.getPortalPosY());
					rec2.setSize(Constants.PORTAL_WIDTH, Constants.PORTAL_HEIGHT);
					if(Intersector.intersectRectangles(rec1, rec2, inter)) {
						// if the char is in the portal
						// move to the next level
//						data.switchLevel = true;
//						Gdx.app.log(TAG, "Deploying new rooms...");
//						data.map = deployMap();
//						data.newMap = true;
					}
				}
			}
		}	// for's end
	}	// update()'s end

	private GameMap deployMap() {
		// return a room object for the game
	
		GameMap nRoom = null;

		GameMap.Room[][] rooms;
		do {
			rooms = 
				new GameMap.Room[Constants.ROOMS_NUM_X][Constants.ROOMS_NUM_Y];
			int rx = (int)(Math.random() * Constants.ROOMS_NUM_X);
			int ry = (int)(Math.random() * Constants.ROOMS_NUM_Y);
			Gdx.app.debug(TAG, rx + ", " + ry);
			deployRooms(rooms, rx, ry, true);
		} while(!reachMinimum(rooms));
		
		for(int y = 0; y < Constants.ROOMS_NUM_Y; y++) {
			for(int x = 0; x < Constants.ROOMS_NUM_X; x++) {
				if(rooms[x][y] == null) {
					rooms[x][y] = new GameMap.Room(Constants.ROOM_BLANK);
				}
			}
		}
		
		nRoom = new GameMap(Constants.MAP_NORMAL, rooms);

		return nRoom;
	} // deployMap(bool)'s end

	private void deployRooms(GameMap.Room[][] map, int x, int y, boolean first) {
		// deploy rooms to the map into the specified x and y by recursing

		if(map[x][y] == null) {	// if haven't inited
			int typeID = first ? Constants.ROOM_SPAWN : Constants.ROOM_NORMAL;
			map[x][y] = new GameMap.Room(typeID);
			
		} else {
			Gdx.app.debug(TAG, "Attempted to reinit a room");
			return;
		}
		GameMap.Room r = map[x][y];

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
		Gdx.app.debug(TAG, "moreDoor = " + moreDoor);
		while(moreDoor > 0) {
			int dir = (int)(Math.random() * 4) + 1;
//			Gdx.app.debug(TAG, "dir = " + dir);
			switch(dir) {
			case 1:
				if(y < Constants.ROOMS_NUM_Y - 1 
						&& map[x][y + 1] == null && !r.north) {
					// init north 
					r.north = true;
//					Gdx.app.debug(TAG, "" + ++doorCt);
					
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
//					Gdx.app.debug(TAG, "" + ++doorCt);
					
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
//					Gdx.app.debug(TAG, "" + ++doorCt);
					
					crWest = true;
					break;
					
				} else if(x > 0 && !r.west) {
					break;
				}

			case 4:
				if(y > 0 && map[x][y - 1] == null && !r.south) {
					r.south = true;
//					Gdx.app.debug(TAG, "" + ++doorCt);
					
					crSouth = true;		// init south
					break;
				
				} else if(y > 0 && !r.south) {
					break;
				}
			}	// switch(dir)'s
			moreDoor--;
		}	// while(times)'s
		Gdx.app.debug(TAG, "Adding doors finished");

		// init other doors
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
	
	private boolean reachMinimum(GameMap.Room[][] rooms) {
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
	
}	// class' end
