// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.Gdx;
import java.util.Iterator;

public class Clock {
	// update the time of the game
	// every actual movements are updated here
		
	private static final String TAG = Clock.class.getName();
	
	private StateData data;

	// rectangles for collision detection
	private Rectangle rec1;
	private Rectangle rec2;
	private Rectangle inter;


// constructor ----------------------------------------------------------------------------------------------
	public Clock(StateData data) {

		this.data = data;

		rec1 = new Rectangle();
		rec2 = new Rectangle();
		inter = new Rectangle();
	}	// new's



// update the game ----------------------------------------------------------------------------------------------
	public void update() {
		// update the current state of the game		float delta = Gdx.graphics.getDeltaTime();

		moveParticles();
		collisionFix();
	}	// update()'s

	private void moveParticles() {

		float delta = Gdx.graphics.getDeltaTime();
		for(Iterator<GameChar> iter = data.chars.iterator(); iter.hasNext();) {
			GameChar c = iter.next();
			
			if(c.atts.getMana() < 0) {
				if(c.getID() == Constants.CHAR_MAIN) {
					// if the mainChar's mana is 0
					data.die();
					data.gameOver = true;
					return;

				} else {
					data.kill();
					iter.remove();
				}
			}

			// move the chars
			c.x += c.getNextX() + c.atts.getAccel() * delta;			
			c.y += c.getNextY() + c.atts.getAccel() * delta;
			c.moved();
		}	// chars iterator's

		// move the magics
		for(Iterator<Magic> iter = data.magics.iterator(); iter.hasNext();) {
			Magic m = iter.next();

			m.x += m.getNextX();
			m.moved();

			if(m.totalMove() > Constants.MAGIC_MAX_DISTANCE) {
				// if the magic has moved to far
				iter.remove();
			}
		}	// magic iterator's 
	}	// moveParticles()'s

	private void collisionFix() {
		// collision detections

		// char to char and map
		float roomX = data.map.cRoom.getX() * Constants.ROOM_WIDTH;
		float roomY = data.map.cRoom.getY() * Constants.ROOM_HEIGHT;
		for(GameChar c1 : data.chars) {
			// check if the char is out of the room
			if(c1.x < roomX) {
				// over to the west
				if(throughDoor(c1, Constants.DIR_W) && !data.justMoveRoom) {
					data.justMoveRoom = true;
					Timer.schedule(
							new Timer.Task() {
								@Override
								public void run() {
									data.justMoveRoom = false;
								}
							}, 0.3f);
					
					data.map.cRoom = data.map.getRoom(
							data.map.cRoom.getX() - 1,
							data.map.cRoom.getY());
					
				} else {
					c1.x = roomX;
				}
				
			} else if(c1.x + Constants.CHAR_WIDTH > roomX + Constants.ROOM_WIDTH) {
				// over to the east
				if(throughDoor(c1, Constants.DIR_E) && !data.justMoveRoom) {
					data.justMoveRoom = true;
					Timer.schedule(
							new Timer.Task() {
								@Override
								public void run() {
									data.justMoveRoom = false;
								}
							}, 0.3f);
					data.map.cRoom = data.map.getRoom(
							data.map.cRoom.getX() + 1,
							data.map.cRoom.getY());
					
				} else {
					c1.x = roomX + Constants.ROOM_WIDTH - Constants.CHAR_WIDTH;
					
				}
			}
						
			if(c1.y + Constants.SHADOW_OFFSET_Y < roomY) {
				// over to the south
				if(throughDoor(c1, Constants.DIR_S) && !data.justMoveRoom) {
					data.justMoveRoom = true;
					Timer.schedule(
							new Timer.Task() {
								@Override
								public void run() {
									data.justMoveRoom = false;
								}
							}, 0.3f);
					data.map.cRoom = data.map.getRoom(
							data.map.cRoom.getX(),
							data.map.cRoom.getY() - 1);
					
				} else {
					c1.y = roomY - Constants.SHADOW_OFFSET_Y;
				} 
				
			} else if(c1.y + Constants.CHAR_HEIGHT > roomY + Constants.ROOM_HEIGHT) {
				// over to the north
				if(throughDoor(c1, Constants.DIR_N) && !data.justMoveRoom) {
					data.justMoveRoom = true;
					Timer.schedule(
							new Timer.Task() {
								@Override
								public void run() {
									data.justMoveRoom = false;
								}
							}, 0.3f);
					data.map.cRoom = data.map.getRoom(
							data.map.cRoom.getX(),
							data.map.cRoom.getY() + 1);
					
				} else {
					c1.y = roomY + Constants.ROOM_HEIGHT - Constants.CHAR_HEIGHT;
				} 
				
			}
			
			c1.updateSafePos();
		}	// c1 iterator's
		
		// magic to char
		for(Magic m : data.magics) {
			// set rec1 as m's rectangle
			rec1.setPosition(m.x, m.y);
			rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
			for(GameChar c : data.chars) {
				if(areClose(m, c)) {
					if(m.getSrc() != c) {
						// can't hit its own caster
						rec2.setPosition(c.x, c.y);
						rec2.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
						if(Intersector.intersectRectangles(rec1, rec2, inter)) {
							c.affectedBy(m);
						}
					}
				}	// if m & c are close
			}	// char iter's
		}	// magic iter's
	}
	
	private boolean throughDoor(GameChar c, int dir) {
		
		float roomX = data.map.cRoom.getX() * Constants.ROOM_WIDTH;
		float roomY = data.map.cRoom.getY() * Constants.ROOM_HEIGHT;
		
		float doorX = 0; float doorY = 0;
		float doorWidth = 0; float doorHeight = 0;
		switch(dir) {
		case Constants.DIR_N:
			if(data.map.cRoom.north) {
				doorX = GameMap.getDoorPosX(roomX, Constants.DIR_N);
				doorY = GameMap.getDoorPosY(roomY, Constants.DIR_N);
				
				doorWidth = Assets.doorHSprite.getWidth();
				doorHeight = Assets.doorHSprite.getHeight();
				break;
				
			} else {
				return false;
			}
		
		case Constants.DIR_S:
			if(data.map.cRoom.south) {
				doorX = GameMap.getDoorPosX(roomX, Constants.DIR_S);
				doorY = GameMap.getDoorPosY(roomY, Constants.DIR_S);
				
				doorWidth = Assets.doorHSprite.getWidth();
				doorHeight = Assets.doorHSprite.getHeight();
				break;
				
			} else {
				return false;
			}
			
		case Constants.DIR_E:
			if(data.map.cRoom.east) {
				doorX = GameMap.getDoorPosX(roomX, Constants.DIR_E);
				doorY = GameMap.getDoorPosY(roomY, Constants.DIR_E);
				
				doorWidth = Assets.doorVSprite.getWidth();
				doorHeight = Assets.doorVSprite.getHeight();
				break;
				
			} else {
				return false;
			}
			
		case Constants.DIR_W:
			if(data.map.cRoom.west) {
				doorX = GameMap.getDoorPosX(roomX, Constants.DIR_W);
				doorY = GameMap.getDoorPosY(roomY, Constants.DIR_W);
				
				doorWidth = Assets.doorVSprite.getWidth();
				doorHeight = Assets.doorVSprite.getHeight();
				break;
				
			} else {
				return false;
			}
		
		default:
			Gdx.app.log(TAG, "Invalid direction constant passed");
			Gdx.app.exit();
		}
		
		// set rec1 as c's rectangle
		rec1.setPosition(c.x, c.y);
		rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
		
		// rec2 as door's rec
		rec2.setPosition(doorX, doorY);
		rec2.setSize(doorWidth, doorHeight);
		
		if(Intersector.intersectRectangles(rec1, rec2, inter)) {
			if(doorWidth > doorHeight
					&& inter.width == Constants.CHAR_WIDTH) {
				return true;
				
			} else if(inter.height == Constants.CHAR_HEIGHT) {
				return true;
			}
		}
		return false;
	}

	private boolean areClose(Magic m, GameChar c) {
		// return whether the magic and the char are close to each other

		if(Math.abs(m.x - c.x) < Constants.CHAR_WIDTH * 2
				&& Math.abs(m.y - c.y) < Constants.CHAR_HEIGHT * 2) {
			return true;

		} else {
			return false;
		}
	}	// areClose(Magic, GameChar)'s

}	// public class'
