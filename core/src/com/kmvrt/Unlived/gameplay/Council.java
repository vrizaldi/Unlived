// code by Muhammad Noorghifari

package com.kmvrt.Unlived.gameplay;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Timer;

import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;

class Council {
	// update the characters in-game without moving it
	
	private static final String TAG = Council.class.getName();
	
	private StateData data; // the game state data

	// rectangles for collision detection
	private Rectangle rec1;
	private Rectangle rec2;
	private Rectangle inter;

	// whether the creep ready to attack
	private boolean creepsChange;
	private boolean creepsDirChange;
	
	private CharsComparator charsComp;
	

// constructor ----------------------------------------------------------------------------------------------
	public Council(StateData data) {
		
		this.data = data;

		rec1 = new Rectangle();
		rec2 = new Rectangle();
		inter = new Rectangle();
	
		charsComp = new CharsComparator();
		
		creepsChange = true;
		creepsDirChange = false;
	} 



// create or dispose current game --------------------------------------------------------------------------
	public void initNewGame() {

		data.chars.clear();
		data.magics.clear();

		GameChar mainChar = new GameChar(data.selChar, data.selCostume);
		init(mainChar);
	}	// initNewGame(String, int)'s

	public void initNewLevel() {

		data.chars.clear();
		data.magics.clear();

		GameChar mainChar = data.getMainChar();
		mainChar.reset();
		init(mainChar);
	}	// initNewLevel()'s end

	private void init(GameChar mainChar) {

		data.chars.add(mainChar); 
		data.setMainChar(mainChar);

		// put mainChar in the middle of the room
		mainChar.x = data.map.getSpawnPosX() 
				- (Constants.ins.CHAR_WIDTH / 2); 
		mainChar.y = data.map.getSpawnPosY() 
				- (Constants.ins.CHAR_HEIGHT / 2);
		mainChar.cRoom = data.map.visit(mainChar, 
				(int)(mainChar.x / (Constants.ins.ROOM_WIDTH 
						+ Constants.ins.ROOMS_INTERVAL)), 
				(int)(mainChar.y / (Constants.ins.ROOM_HEIGHT + 
						Constants.ins.ROOMS_INTERVAL)));
			// give it its first room
		mainChar.updateSafePos();		
		
		Gdx.app.log(TAG, "Deploying new creeps...");
		deployCreeps((int)(data.map.getRoomsNum() / 2));
		Assets.initChars(data.chars);
		
		creepsChange = true;
		Timer.schedule(
			new Timer.Task() {
				
				@Override
				public void run() {

					creepsChange = true;
				}
			}, Constants.CREEPS_CHANGE_INTERVAL,
			Constants.CREEPS_CHANGE_INTERVAL);
		
		creepsDirChange = true;
		Timer.schedule(
			new Timer.Task() {
					
				@Override
				public void run() {

					creepsDirChange = true;
				}
			}, 0.8f, 0.8f);
	}	// init(GameChar)'s



// update the game objects ---------------------------------------------------------------------------------
	public void update() {
		// update the current game state

		float delta = Gdx.graphics.getDeltaTime();
		if(data.getMainChar().atts.getMana() < 11
				&& !data.slowMo
				&& data.slowMoable) {
//			data.getMainChar().hasSlowMo = true;
			data.slowMo = true;
			data.slowMoable = false;
			// time the slowmo for x secs
			Timer.schedule(
				new Timer.Task() {
					
					@Override
					public void run() {
						// disable the slowmo
						data.slowMo = false;
						Timer.schedule(new Timer.Task() {
							
							@Override
							public void run() {
								// enable the slowmoable
								data.slowMoable = true;
							}
						}, 1.5f);
					}
				}, 1);
		}
		
		if(data.slowMo) {
			if(data.getMainChar().atts.getMana() >= 11) {
				data.slowMo = false;
				data.slowMoable = true;
			} else {
				delta *= Constants.SLOWMO_RATIO;
			}
		}
		
		updateMainChar(delta);
		updateCreeps(delta);
		sortChars();
	}	

	private void updateMainChar(float delta) {
		// update the main character

		GameChar mainChar = data.getMainChar();

		// acts
		if(Gdx.input.isKeyPressed(Keys.D)) {
			// cast in the east
			mainChar.setDir(Constants.DIR_E);
			shoot(mainChar, Constants.DIR_E);
		
		} else if(Gdx.input.isKeyPressed(Keys.S)) {
			// cast in the west
			mainChar.setDir(Constants.DIR_W);
			shoot(mainChar, Constants.DIR_W);

		} else if(Gdx.input.isKeyJustPressed(Keys.A)) {
			// body switch
			GameChar creep = closestCreep();
			if(creep != null) {
				data.setMainChar(creep);
			}
		}

		// horizontal movements
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
				// move west
				mainChar.move(-Constants.ins.NORMAL_SPEED * delta, 0);

		} else if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
				// move east
				mainChar.move(Constants.ins.NORMAL_SPEED * delta, 0);
		}

		// vertical movement
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			// move north
			mainChar.move(0, Constants.ins.NORMAL_SPEED * delta);

		} else if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			// move south
			mainChar.move(0, -Constants.ins.NORMAL_SPEED * delta);
		}
	} // updateMainChar()'s end

	private void shoot(GameChar c, int dir) {
		// deploy a magic in front of the deployer
		
		if(!c.isAbleToShoot()) { // still cocking
			return;
		}

		Magic magic = null;
		if(dir == Constants.DIR_E) {
			// deploy it in the east of the mainChar
			magic = MagicFactory.cast(c.getName(),
				c.x + (Constants.ins.CHAR_WIDTH / 2), 
				c.y + ((Constants.ins.CHAR_HEIGHT - c.getSpell().getHeight()) / 2),
				Constants.DIR_E, c);
			data.magics.add(magic);
	
		} else if(dir == Constants.DIR_W) {
			// deploy it in the west of the mainChar
			magic = MagicFactory.cast(c.getName(), 
					c.x + (Constants.ins.CHAR_WIDTH / 2) - c.getSpell().getWidth() , 
					c.y + ((Constants.ins.CHAR_HEIGHT - c.getSpell().getHeight()) / 2),
					Constants.DIR_W, c);
			data.magics.add(magic);
		}
		
		c.shoot(true);
	}

	private GameChar closestCreep() {
		// return the creep closest to the mainChar 
		// that has all the requirements
		// return null if can't find any

		GameChar mainChar = data.getMainChar();
		GameChar closest = null;
		float closestX = Constants.ins.SAFE_DIST_X;
		float closestY = Constants.ins.SAFE_DIST_Y;
		boolean isClosestSameRoom = false;

		// set rec1 as mainChar's y rectangle
		rec1.setPosition(0, mainChar.y);
		rec1.setSize(Constants.ins.CHAR_WIDTH, Constants.ins.CHAR_HEIGHT);
		for(GameChar c : data.chars) {
			float distX = Math.abs(mainChar.x - c.x);
			float distY = Math.abs(mainChar.y - c.y);
			// check the ones in the same room first
			if(c.cRoom == mainChar.cRoom
					&& c != mainChar) {
				if(!isClosestSameRoom
						&& distY < Constants.ins.SAFE_DIST_Y
						&& distX < Constants.ins.SAFE_DIST_X) {
					// take this one as the closest
					closestX = distX;
					closestY = distY;
					closest = c;
					isClosestSameRoom = true;
				
				} else if(distY < closestY
					|| ((distY - closestY < 0.5f * Constants.ins.UNIT_CONV)
						&& distX < closestX)) {
					closestX = distX;
					closestY = distY;
					closest = c;
				}
			// if in the same room as mainchar
				
			} else if(!isClosestSameRoom
					&& areConnected(c.cRoom, mainChar.cRoom)
					&& c != mainChar) {
				// if they're the closest found
				if((distY < closestY
					|| (distY - closestY < 0.5f * Constants.ins.UNIT_CONV))
						&& distX < closestX) {
					closestX = distX;
					closestY = distY;
					closest = c;
				}
				
			}	// if they're close's
		}	// chars iterator's

		return closest;
	}
	
	public boolean areConnected(Room r1, Room r2) {
		
		// if they're the same room
		// just return true
		if(r1 == r2) {
			return true;
		}
		
		// check if they're next to each other
		// it can only be EITHER vertically or horizontally
		boolean horizontally = Math.abs(r1.getX() - r2.getX()) == 1 ? true : false;
		boolean vertically = Math.abs(r1.getY() - r2.getY()) == 1 ? true : false;
		if(horizontally ^ vertically) {
			if(horizontally) {
				// horizontal bond
				if((r1.east && r2.west)
						|| (r1.west && r2.east)) {
					return true;
				}
				
			} else {	// vertically
				if((r1.north && r2.south)
						|| (r1.south && r2.north)) {
					return true;
				}
			}
		}
		
		return false;
	}	// areConnected(Room, Room)'s

	
	
// AI stuff ---------------------------------------------------------------------------------------
	private void updateCreeps(float delta) {
		// update the other characters

		if(data.chars.size() == 1) {
			// there's no creep
			return;
		}
		
		if(creepsChange) {
			changeCreeps();
			creepsChange = false;
		}
		moveCreeps(delta);
		creepsAttack();
		for(GameChar c : data.chars) {
			if(c.cRoom == data.getMainChar().cRoom) {
				Gdx.app.debug(TAG, "c type: " + c.getID());
			}
		}
	} // updateCreeps()'s end

	private void deployCreeps(int num) {
		// deploy creeps to the room

		boolean[][] occupied = new boolean[Constants.ROOMS_NUM_X][Constants.ROOMS_NUM_Y];
			// store the flagged rooms
		// fill it with false
		for(int y = 0; y < Constants.ROOMS_NUM_Y; y++) {
			for(int x = 0; x < Constants.ROOMS_NUM_X; x++) {
				occupied[x][y] = false;
			}	// col iter's
		} // row iter's
		
		occupied[data.getMainChar().cRoom.getX()][data.getMainChar().cRoom.getY()] 
				= true;
			// flag the mainChar's room first
		num = Math.max(num, Constants.CHARS_MIN);
			// set to minimal if it's too small
		
			// creeps deployed can't be less than <CHAR_MIN> - 1
		for(int i = 0; i < num; i++) {
			// create <num> creeps

			int r = (int)(Math.random() * MagicFactory.totalSpells());
			GameChar creep = new GameChar(MagicFactory.getSpellName(r), 1);
			Room room = null;
			do {
				room = data.map.getRandRoom();
			} while(occupied[room.getX()][room.getY()]);
			occupied[room.getX()][room.getY()] = true;
			creep.x = GameMap.getMiddle(room.getX(), 
					Constants.ins.ROOM_WIDTH, Constants.ins.ROOMS_INTERVAL)
					- (Constants.ins.CHAR_WIDTH / 2);
			creep.y = GameMap.getMiddle(room.getY(), 
					Constants.ins.ROOM_HEIGHT, Constants.ins.ROOMS_INTERVAL)
					- (Constants.ins.CHAR_HEIGHT / 2);
				// put it anywhere in the room;
			creep.updateSafePos();
			data.chars.add(creep);
				// add it to the collection
		}

//		Assets.initChars(data.chars);
	}
	
	private void changeCreeps() {
		// change the creeps type
		// 7/8 of their movement is following the mainChar
		// 1/8 is avoiding it
		
		for(GameChar creep : data.chars) {
			if(creep.getID() != Constants.CHAR_MAIN) {
				if(creep.cRoom != data.getMainChar().cRoom
						|| !creep.isAbleToShoot()) {
//					Gdx.app.debug(TAG, "initing wander...");
					creep.wandering = true;

					boolean finish = false;
					int mood = 0;
					while(!finish) {
						mood = (int)(Math.random() * 4);
							// rand number 0 - 3
						if(creep.cRoom == null) {
							Gdx.app.debug(TAG, "cRoom is empty");
						}
						if(mood == 0 && creep.cRoom.north) {
							creep.changeCreep(Constants.DIR_N);
							finish = true;

						} else if(mood == 1 && creep.cRoom.south) {
							creep.changeCreep(Constants.DIR_S);
							finish = true;

						} else if(mood == 2 && creep.cRoom.east) {
							creep.changeCreep(Constants.DIR_E);
							finish = true;

						} else if(mood == 3 && creep.cRoom.west) {	
							creep.changeCreep(Constants.DIR_W);
							finish = true;
						}
					}
//					Gdx.app.debug(TAG, "mood = " + mood);
					
				} else {
					creep.wandering = false;
					int mood = (int)(Math.random() * 9) + 1;
						// random number 1 - 8
					switch(mood) {
					case 9:
					case 8:
					case 7:
					case 6:
					case 5:
					case 4:
					case 3:
					case 2:
						creep.changeCreep(Constants.CHAR_CREEP_FOLLOW);
						break;
	
					case 1:
						creep.changeCreep(Constants.CHAR_CREEP_AVOID);
						break;
					}	// switch-case's
				}	// if !wandering's
			}
		}	// char iter's
	}	// changeCreeps(bool)'s

	private void moveCreeps(float delta) {
		// move the creep
		// do some AI programming
		
		GameChar mainChar = data.getMainChar();
		
		for(Iterator<GameChar> i = data.chars.iterator(); i.hasNext();) {
			GameChar creep = i.next();
			
			if(creep.getID() != Constants.CHAR_MAIN) {
				// locate the mainChar
				float distX = mainChar.x - creep.x;
				float distY = mainChar.y - creep.y;

				// creep should keep facing in the mainChar dir
				if(creepsDirChange) {
					if(distX < 0) {
						creep.setDir(Constants.DIR_W);

					} else if(distX > 0) {
						creep.setDir(Constants.DIR_E);
					}
				}
				
				float shootRangeX = 0;
				if(mainChar.x - creep.x >= 0) {
					// east
					shootRangeX = mainChar.x 
							- (creep.getSpell().getWidth());
				} else {	// west
					shootRangeX = mainChar.x
							+ (creep.getSpell().getWidth());
				}
				distX = shootRangeX - creep.x;
				
				// move the creep based on its type
				if(creep.wandering) {
					wander(creep, delta);
					
				} if(creep.getID() == Constants.CHAR_CREEP_FOLLOW) {
					follow(creep, distX, distY, delta);
					
				} else if(creep.getID() == Constants.CHAR_CREEP_AVOID) {
					avoid(creep, distX, distY, delta);

				}
			}
		} // char iter's

		creepsDirChange = false;
	}	// moveCreeps()'s end
	

	private void wander(GameChar creep, float delta) {
		// wander around the map moving through doors
		
		int dir = creep.getID();
		int roomX = creep.cRoom.getX(); int roomY = creep.cRoom.getY();
		if(dir == Constants.DIR_N) {
			roomY += 1;
		} else if(dir == Constants.DIR_S) {
			roomY -= 1;
		} else if(dir == Constants.DIR_E) {
			roomX += 1;
		} else if(dir == Constants.DIR_W) {
			roomX -= 1;
		}
		float nRoomX = 
				GameMap.getMiddle(roomX, Constants.ins.ROOM_WIDTH, 
						Constants.ins.ROOMS_INTERVAL) 
				- Constants.ins.CHAR_WIDTH / 2;
		float nRoomY = 
				GameMap.getMiddle(roomY, Constants.ins.ROOM_HEIGHT,
						Constants.ins.ROOMS_INTERVAL)
				- (Constants.ins.CHAR_HEIGHT / 2);

		// aim to make the creep go pass through the door
		float distX = nRoomX - creep.x;
		float distY = nRoomY - creep.y;
		follow(creep, distX, distY, delta);
	}	// wander(GameChar, float)'s
	
	private void follow(GameChar creep, float distX, float distY, float delta) {
		// move the creep toward the mainChar/door
		
		if(distX != 0 && distY != 0) {
			/* move diagonally, by moving vertically & horizontally 
			 * use trigonometry to calc how far the creep should go
			 */
			float a = Math.abs(distX);
			float o = Math.abs(distY);
			double angle = Math.atan(o / a);
			float moveX = (float)(Constants.ins.NORMAL_SPEED * Math.cos(angle));
			float moveY = (float)(Constants.ins.NORMAL_SPEED * Math.sin(angle));
			if(distX < 0) {
				moveX = -moveX;
			}
			if(distY < 0) {
				moveY = -moveY;
			}
			creep.move(moveX * delta, moveY * delta);
			
		} else if(distX == 0 && distY == 0) {
			return;
			
		} else if(distX == 0) {
			// just move to vertically
			creep.move(0, Constants.ins.NORMAL_SPEED * delta);
			
		} else if(distY == 0) {
			// just move horizontally
			creep.move(Constants.ins.NORMAL_SPEED * delta, 0);
		}
	}	// followMainChar(GameChar, float, float)'s end

	private void avoid(GameChar creep, float distX, float distY, float delta) {
		// move the creep away from mainChar

		// move horizontally
		if(distX > 0) {
			// move west
			creep.move(-Constants.ins.NORMAL_SPEED * delta, 0);

		} else if(distX < 0) {
			// move east
			creep.move(Constants.ins.NORMAL_SPEED * delta, 0);
		}

		// move vertically
		if(distY > 0) {
			// move south 
			creep.move(0, -Constants.ins.NORMAL_SPEED * delta);

		} else if(distY < 0) {
			// move north
			creep.move(0, Constants.ins.NORMAL_SPEED * delta);
		}
	}	// avoidMainChar(GameChar, float, float)'s end

		
	private void creepsAttack() {
		// use the creeps to attack mainChar if it sees it
		
		GameChar mainChar = data.getMainChar();
		
		// set rec1 to be the mainChar's y related rectangle
		rec1.setPosition(0, mainChar.y);
		rec1.setSize(Constants.ins.CHAR_WIDTH, Constants.ins.CHAR_HEIGHT);

		for(GameChar creep : data.chars) {
			if(creep.getID() != Constants.CHAR_MAIN) {
				// set rec2 to be current creep's y related rectangle
				rec2.setPosition(0, creep.y);
				rec2.setSize(Constants.ins.CHAR_WIDTH, Constants.ins.CHAR_HEIGHT);

				float distX = mainChar.x - creep.x > 0 ?
						mainChar.x - (creep.x + (Constants.ins.CHAR_WIDTH / 2))
							+ creep.getSpell().getWidth() // east
						: 
						(creep.x + (Constants.ins.CHAR_WIDTH / 2))
							- creep.getSpell().getWidth()
							- mainChar.x + Constants.ins.CHAR_WIDTH;	// west
				if(Intersector.intersectRectangles(rec1, rec2, inter)
						&& Math.abs(distX) <= creep.getSpell().getTravelDist()
						&& inter.height > Constants.ins.CHAR_HEIGHT * 0.1f) {
					// if the mainChar is within the range of the creep shoot
					shoot(creep, creep.getDir());
				}
			}
		}	// for's end
	}
	
	private void sortChars() {
		
		Collections.sort(data.chars, charsComp);
	}
	
	
// inner class ------------------------------------------------------------------------
	private class CharsComparator implements Comparator<GameChar> {
		// compare the chars based on their y coordinate
		
		@Override
		public int compare(GameChar o1, GameChar o2) {
			// sort the chars based on their y coor (high -> low)
			if(o1.y > o2.y) {
				// o1 is higher
				return -1;
				
			} else if(o1. y < o2.y) {
				// o2 is higher
				return 1;
				
			} else {
				// they're equal
				return 0;
			}
		}
	}

} // class' end
