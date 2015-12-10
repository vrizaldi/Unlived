// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
//import com.badlogic.gdx.utils.Timer;
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
		
		for(GameChar c1 : data.chars) {
			// check if the char is out of the room
			float roomX = c1.cRoom.getX() * Constants.ROOM_WIDTH;
			float roomY = c1.cRoom.getY() * Constants.ROOM_HEIGHT;
			
			if(c1.y + Constants.CHAR_HEIGHT > roomY + Constants.ROOM_HEIGHT) {
				// over to the north
				if(hitWall(c1, Constants.DIR_N)) {
					float dX = c1.x;
					c1.x = c1.getSafeX();
					if(hitWall(c1, Constants.DIR_N)) {
						c1.x = dX;
						c1.y = roomY + Constants.ROOM_HEIGHT - Constants.CHAR_HEIGHT;
					}
				} 
			}
			if(c1.y + Constants.SHADOW_OFFSET_Y < roomY) {
				// over to the south
				if(hitWall(c1, Constants.DIR_S)) {
					float dX = c1.x;
					c1.x = c1.getSafeX();
					if(hitWall(c1, Constants.DIR_S)) {
						c1.x = dX;
						c1.y = roomY - Constants.SHADOW_OFFSET_Y;
					}
				} 
			} 
			if(c1.x + Constants.CHAR_WIDTH > roomX + Constants.ROOM_WIDTH) {
				// over to the east
				if(hitWall(c1, Constants.DIR_E)) {
					float dY = c1.y;
					c1.y = c1.getSafeY();
					if(hitWall(c1, Constants.DIR_E)) {
						c1.y = dY;
						c1.x = roomX + Constants.ROOM_WIDTH - Constants.CHAR_WIDTH;
					}
				}
			}	
			if(c1.x < roomX) {
				// over to the west
				if(hitWall(c1, Constants.DIR_W)) {
					c1.x = roomX;
				}
			} 
			
			c1.updateSafePos();
		}	// c1 iterator's
		
		// magic to char/wall
		for(Iterator<Magic> iter = data.magics.iterator(); iter.hasNext();) {
			Magic m = iter.next();
			
			// check if the magic hit the wall
			float roomX = m.cRoom.getX() * Constants.ROOM_WIDTH;
			if(m.x + Constants.CHAR_WIDTH > roomX + Constants.ROOM_WIDTH) {
				// over to the east
				if(hitWall(m, true)) {
					iter.remove();
				}
				
			} else if(m.x < roomX) {
				// over to the west
				if(hitWall(m, false)) {
					iter.remove();
				}
			}
			
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
	
	private boolean hitWall(GameChar c, int dir) {
		
		float roomX = c.cRoom.getX() * Constants.ROOM_WIDTH;
		float roomY = c.cRoom.getY() * Constants.ROOM_HEIGHT;
		
		float doorX = 0; float doorY = 0;
		float doorWidth = 0; float doorHeight = 0;
		switch(dir) {
		case Constants.DIR_N:
			if(c.cRoom.north) {
				doorX = GameMap.getDoorPosX(roomX, Constants.DIR_N);
				doorY = GameMap.getDoorPosY(roomY, Constants.DIR_N);
				
				doorWidth = Assets.doorHSprite.getWidth();
				doorHeight = Assets.doorHSprite.getHeight() * 1.5f;
				break;
				
			} else {
				return true;
			}
		
		case Constants.DIR_S:
			if(c.cRoom.south) {
				doorX = GameMap.getDoorPosX(roomX, Constants.DIR_S);
				doorY = GameMap.getDoorPosY(roomY, Constants.DIR_S)
						- Assets.doorHSprite.getHeight() * 0.5f;
				
				doorWidth = Assets.doorHSprite.getWidth();
				doorHeight = Assets.doorHSprite.getHeight() * 1.5f;
				break;
				
			} else {
				return true;
			}
			
		case Constants.DIR_E:
			if(c.cRoom.east) {
				doorX = GameMap.getDoorPosX(roomX, Constants.DIR_E);
				doorY = GameMap.getDoorPosY(roomY, Constants.DIR_E);
				
				doorWidth = Assets.doorVSprite.getWidth() * 1.5f;
				doorHeight = Assets.doorVSprite.getHeight();
				break;
				
			} else {
				return true;
			}
			
		case Constants.DIR_W:
			if(c.cRoom.west) {
				doorX = GameMap.getDoorPosX(roomX, Constants.DIR_W) 
						- Assets.doorVSprite.getWidth() * 0.5f;
				doorY = GameMap.getDoorPosY(roomY, Constants.DIR_W);
				
				doorWidth = Assets.doorVSprite.getWidth() * 1.5f;
				doorHeight = Assets.doorVSprite.getHeight();
				break;
				
			} else {
				return true;
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
			// if the char can fit into the door
			if((dir == Constants.DIR_N || dir == Constants.DIR_S)
					&& inter.width >= Constants.CHAR_WIDTH) {
				return false;
			} else if((dir == Constants.DIR_E || dir == Constants.DIR_W)
					&& inter.height >= Constants.CHAR_HEIGHT * 0.9f) {
				return false;
			}
		}
		return true;
	}	// hitWall(GameChar, int)'s
	
	private boolean hitWall(Magic m, boolean east) {
		
		float roomX = m.cRoom.getX() * Constants.ROOM_WIDTH;
		float roomY = m.cRoom.getY() * Constants.ROOM_HEIGHT;
		
		float doorX = 0; float doorY = 0;
		float doorWidth = 0; float doorHeight = 0;
			
		if(east) {
			if(m.cRoom.east) {
				doorX = GameMap.getDoorPosX(roomX, Constants.DIR_E);
				doorY = GameMap.getDoorPosY(roomY, Constants.DIR_E);
				
				doorWidth = Assets.doorVSprite.getWidth() * 1.5f;
				doorHeight = Assets.doorVSprite.getHeight();
				
			} else {
				// east door doesn't exist
				return true;
			}
			
		} else {
			if(m.cRoom.west) {
				doorX = GameMap.getDoorPosX(roomX, Constants.DIR_W) 
						- Assets.doorVSprite.getWidth() * 0.5f;
				doorY = GameMap.getDoorPosY(roomY, Constants.DIR_W);
				
				doorWidth = Assets.doorVSprite.getWidth() * 1.5f;
				doorHeight = Assets.doorVSprite.getHeight();
				
			} else {
				// west door doesn't exist
				return true;
			}
		}
		
		// set rec1 as c's rectangle
		rec1.setPosition(m.x, m.y);
		rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
		
		// rec2 as door's rec
		rec2.setPosition(doorX, doorY);
		rec2.setSize(doorWidth, doorHeight);
		
		if(Intersector.intersectRectangles(rec1, rec2, inter)) {
			// if the char can fit in the door
			if(inter.height >= Constants.CHAR_HEIGHT * 0.5f) {
				return false;
			}
		}
		// if doesn't touch the door
		return true;
	}	// hitWall(GameChar, int)'s

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
