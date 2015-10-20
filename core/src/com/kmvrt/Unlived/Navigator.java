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
	
		data.cRoom = deployRoom(false);
//		roomPassed = 0;
	} // initNewGame()'s end

	public void disposeGame() {
		// dispose the current game objects
	
		data.cRoom = null;
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
						data.cRoom.getPortalX(), data.cRoom.getPortalY());
					rec2.setSize(Constants.PORTAL_WIDTH, Constants.PORTAL_HEIGHT);
					if(Intersector.intersectRectangles(rec1, rec2, inter)) {
						// if the char is in the portal
						// move to the next level
						data.switchLevel = true;
						Gdx.app.log(TAG, "Deploying new room...");
						data.cRoom = deployRoom(false);
					}
				}
			}
		}	// for's end
	}	// update()'s end

	private GameMap deployRoom(boolean boss) {
		// return a room object for the game
	
		GameMap nRoom = null;

		if(!boss) {
			// normal room
			nRoom = new GameMap(Constants.ROOM_NORMAL);

		} else {
			// boss room
			nRoom = new GameMap(Constants.ROOM_BOSS);
		}

		return nRoom;
	} // deployRoom(bool)'s end

}	// class' end
