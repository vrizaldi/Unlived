// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Timer;

import java.util.Collections;
import java.util.Iterator;
import java.util.Comparator;

public class Council {
	// update the characters in-game without moving it
	
	private static final String TAG = Council.class.getName();
	
	private StateData data; // the game state data

	// rectangles for collision detection
	private Rectangle rec1;
	private Rectangle rec2;
	private Rectangle inter;

	// whether the creep ready to attack
	private boolean creepsChange;
	
	private CharsComparator charsComp;
	

// constructor ----------------------------------------------------------------------------------------------
	public Council(StateData data) {
		
		this.data = data;

		rec1 = new Rectangle();
		rec2 = new Rectangle();
		inter = new Rectangle();
	
		charsComp = new CharsComparator();
		
		creepsChange = true;
		Timer.schedule(
			new Timer.Task() {
				
				@Override
				public void run() {
					creepsChange = true;
				}
			}, Constants.CREEPS_CHANGE_INTERVAL,
			Constants.CREEPS_CHANGE_INTERVAL);
	} 



// create or dispose current game --------------------------------------------------------------------------
	public void initNewGame() {
		// create objects for a new game
		
		GameChar mainChar = new GameChar("tiny");
		if(data.getStateID() == Constants.STATE_ARENA) {
			// put it in the middle of the room
			mainChar.x = data.map.getSpawnPosX() - (Constants.CHAR_WIDTH / 2); 
			mainChar.y = data.map.getSpawnPosY() - (Constants.CHAR_HEIGHT / 2);
			mainChar.updateSafePos();
		}
		
		data.chars.add(mainChar); 
		data.setMainChar(mainChar);
		Assets.initChars(data.chars);
	}	// initNewGame()'s end

	public void disposeGame() {
		// dispose current game's object

		// clear char and magic collection
		data.chars.clear();
		data.magics.clear();
	} // disposeGame()'s end



// update the game objects ---------------------------------------------------------------------------------
	public void update() {
		// update the current game state

		float delta = Gdx.graphics.getDeltaTime();
		
		updateMainChar(delta);
		updateCreeps(delta);
		sortChars();
	}	

	private void updateMainChar(float delta) {
		// update the main character

		GameChar mainChar = data.getMainChar();

		// acts
		if(Gdx.input.isKeyJustPressed(Keys.D)) {
			// cast in the east
			mainChar.setDir(Constants.DIR_E);
			shoot(mainChar, Constants.DIR_E);
		
		} else if(Gdx.input.isKeyJustPressed(Keys.S)) {
			// cast in the west
			mainChar.setDir(Constants.DIR_W);
			shoot(mainChar, Constants.DIR_W);

		} else if(Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			// body switching 
			GameChar creep = closestCreep();
			if(creep != null) {
				data.setMainChar(creep);
			}
		}

		// horizontal movements
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
				// move west
				mainChar.move(-Constants.NORMAL_SPEED * delta, 0);

		} else if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
				// move east
				mainChar.move(Constants.NORMAL_SPEED * delta, 0);
		}

		// vertical movement
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			// move north
			mainChar.move(0, Constants.NORMAL_SPEED * delta);

		} else if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			// move south
			mainChar.move(0, -Constants.NORMAL_SPEED * delta);
		}
	} // updateMainChar()'s end

	private void shoot(GameChar c, int dir) {
		// deploy a magic in front of the deployer
		
		if(!c.isAbleToShoot()) { // still cocking
			return;
		}
		
		float charX = c.x;
		float charY = c.y;

		Magic magic = null;
		if(dir == Constants.DIR_E) {
			// deploy it in the east of the mainChar
			magic = MagicFactory.cast(c.getName(), charX, 
				charY, Constants.DIR_E, c);
			data.magics.add(magic);
	
		} else if(dir == Constants.DIR_W) {
			// deploy it in the west of the mainChar
			magic = MagicFactory.cast(c.getName(), charX, 
				charY, Constants.DIR_W, c);
			data.magics.add(magic);
		}
		
		// decrease the mana if c is mainChar
		if(c == data.getMainChar()) {
			GameChar.Attributes cast = magic.getSpell().cast;
			c.atts.applyMana(cast.getMana());
			c.atts.applyAccel(cast.getAccel());
			c.atts.applyForce(cast.getForce());
		}
		
		c.shoot(true);
	}

	private GameChar closestCreep() {
		// return the inactive creep closest to the mainChar

		GameChar mainChar = data.getMainChar();
		GameChar closest = null;
		float closestX = 100;

		// set rec1 as mainChar's y rectangle
		rec1.setPosition(0, mainChar.y);
		rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
		for(GameChar c : data.chars) {
			float distX = Math.abs(mainChar.x - c.x);
			float distY = Math.abs(mainChar.y - c.y);
			if(c.cRoom == mainChar.cRoom
					&& c != mainChar
					&& distX < Constants.SAFE_DIST_X
					&& distY < Constants.SAFE_DIST_Y
					&& distX < closestX) {
				// if they're the closest found
				closestX = distX;
				closest = c;
			}	// if they're close's
		}	// chars iterator's

		return closest;
	}

	private void updateCreeps(float delta) {
		// update the other characters

		// new level initiation
		// just create new creeps
		if(data.switchLevel) {
			data.switchLevel = false;
			
			Gdx.app.log(TAG, "Deploying new creeps...");
			deployCreeps((int)(Math.random() * Constants.CHARS_MAX));
			return;

		} else if(!data.switchLevel
			&& data.chars.size() == 1) {
			// there's no creep
			return;
		}
		
		if(creepsChange) {
			changeCreeps();
			creepsChange = false;
		}
		moveCreeps(delta);
		creepsAttack();
	} // updateCreeps()'s end

	private void deployCreeps(int num) {
		// deploy creeps to the room

		num = Math.max(num, Constants.CHARS_MIN - 1);
			// creeps deployed can't be less than <CHAR_MIN> - 1
		for(int i = 0; i < num; i++) {
			// create <num> creeps

			int r = (int)(Math.random() * MagicFactory.totalSpells());
			GameChar creep = new GameChar(MagicFactory.getSpellName(r));
			GameMap.Room room = data.map.getRandRoom();
			creep.x = room.getX() * Constants.ROOM_WIDTH + 1;
			creep.y = room.getY() * Constants.ROOM_HEIGHT + 1;
				// put it anywhere in the room;
			creep.updateSafePos();
			data.chars.add(creep);
				// add it to the collection
		}

		Assets.initChars(data.chars);
	}
	
	private void changeCreeps() {
		// change the creeps type
		// 7/8 of their movement is following the mainChar
		// 1/8 is avoiding it
		
		for(GameChar creep : data.chars) {
			if(creep.getID() != Constants.CHAR_MAIN) {
				int mood = (int)(Math.random() * 8) + 1;
					// random number 1 - 8
				switch(mood) {

				case 12:
				case 11:
				case 10:
					creep.changeCreep(Constants.CHAR_CREEP_FOLLOW);
					break;
				case 9:
				case 8:
					creep.changeCreep(Constants.CHAR_CREEP_FOLLOW_W);
					break;
				case 7:
				case 6:
					creep.changeCreep(Constants.CHAR_CREEP_FOLLOW_E);
					break;

				case 5:
				case 4:
					creep.changeCreep(Constants.CHAR_CREEP_FOLLOW_N);
					break;

				case 3:
				case 2:
					creep.changeCreep(Constants.CHAR_CREEP_FOLLOW_S);
					break;

				case 1:
					creep.changeCreep(Constants.CHAR_CREEP_AVOID);
					break;
				}
			}
		}
	}

	private void moveCreeps(float delta) {
		// move the creep
		// do some AI programming
		
		GameChar mainChar = data.getMainChar();
		
		for(Iterator<GameChar> i = data.chars.iterator(); i.hasNext();) {
			GameChar creep = i.next();
			
			if(creep.getID() != Constants.CHAR_MAIN) {
				// follow the mainChar
				float distX = mainChar.x - creep.x;
				float distY = mainChar.y - creep.y;

				// change the direction the creep facing
				if(distX < 0) {
					creep.setDir(Constants.DIR_W);

				} else if(distX > 0) {
					creep.setDir(Constants.DIR_E);
				}
				
				// keep it in safe distance
				if(Math.abs(distX) < Constants.SAFE_DIST_X
						&& Math.abs(distY) < 1) {
					if(distX > 0) {
						distX += Constants.SAFE_DIST_X;
					} else if(distX < 0) {
						distX -= Constants.SAFE_DIST_X;
					}
					distY = 0;
					
				} else if(Math.abs(distX) < 0.2) {
					distX = 0;
					
				} else if(Math.abs(distY) < 0.2) {
					distY = 0;
				} 
	//			distY = Math.abs(distY) < 1 ? 0 : distY; 
				
				// move the creep based on its type
				if(creep.getID() == Constants.CHAR_CREEP_FOLLOW) {
					followMainChar(creep, distX, distY, delta);
				
				} else if(creep.getID() == Constants.CHAR_CREEP_FOLLOW_N) {
					followMainCharX(creep, distX, Constants.DIR_N, delta);

				} else if(creep.getID() == Constants.CHAR_CREEP_FOLLOW_S) {
					followMainCharX(creep, distX, Constants.DIR_S, delta); 
					
				} else if(creep.getID() == Constants.CHAR_CREEP_FOLLOW_E) {
					followMainCharY(creep, distY, Constants.DIR_E, delta);
					
				} else if(creep.getID() == Constants.CHAR_CREEP_FOLLOW_W) {
					followMainCharY(creep, distY, Constants.DIR_W, delta);

				} else if(creep.getID() == Constants.CHAR_CREEP_AVOID) {
					avoidMainChar(creep, distX, distY, delta);
				}
			}
		} // for loop's end
	}	// moveCreeps()'s end

	private void followMainChar(GameChar creep, float distX, float distY, float delta) {
		// move the creep toward the mainChar

		if(Math.abs(Math.abs(distX) - Math.abs(distY)) < 10) {
			// move diagonally by moving horizontally then vertically
			if(distX > 0) {
				// move east
				creep.move(Constants.NORMAL_SPEED * delta, 0);

			} else if(distX < 0) {
				// move west
				creep.move(-Constants.NORMAL_SPEED * delta, 0);
			}
					
			if(distY > 0) {
				// move north
				creep.move(0, Constants.NORMAL_SPEED * delta);
						
			} else if(distY < 0) {
				// move south
				creep.move(0, -Constants.NORMAL_SPEED * delta);
			}
					
		} else if(Math.abs(distX) > Math.abs(distY)) {
			// move horizontally
			if(distX > 0) {
				// move east
				creep.move(Constants.NORMAL_SPEED * delta, 0);
					
			} else if(distX < 0) {
				// move west
				creep.move(-Constants.NORMAL_SPEED * delta, 0);
			}
				
		} else if(Math.abs(distX) < Math.abs(distY)) {
			// move vertically
			if(distY > 0) {
				// move north
				creep.move(0, Constants.NORMAL_SPEED * delta);
					
			} else if(distY < 0) {
				// move south
				creep.move(0, -Constants.NORMAL_SPEED * delta);
			}
		}
	}	// followMainChar(GameChar, float, float)'s end

	private void followMainCharX(GameChar creep, float distX, int dirY, float delta) {
		// follow the mainChar horizontally
		// and keep moving towards dirY(either N or S)
	
		if(dirY != Constants.DIR_N
			&& dirY != Constants.DIR_S) {
			Gdx.app.log(TAG, "ERROR: invalid argument in method followMainCharX()");
			Gdx.app.exit();
		}

		// move horizontally towards the mainChar
		// and also keep moving north/south
		if(distX > 0) {
			// move east
			creep.move(Constants.NORMAL_SPEED * delta, 0);

		} else if(distX < 0) {
			// move west
			creep.move(-Constants.NORMAL_SPEED * delta, 0);
		}

		if(dirY == Constants.DIR_N) {
			creep.move(0, Constants.NORMAL_SPEED * delta);
		} else {
			creep.move(0, -Constants.NORMAL_SPEED * delta);			
		}
	}	// followMainCharX(GameChar, float, int)'s end
	
	private void followMainCharY(GameChar creep, float distY, int dirX, float delta) {
		// follow the mainChar horizontally
		// and keep moving towards dirY(either N or S)
	
		if(dirX != Constants.DIR_E
			&& dirX != Constants.DIR_W) {
			Gdx.app.log(TAG, "ERROR: invalid argument in method followMainCharY()");
			Gdx.app.exit();
		}

		// move vertically towards the mainChar
		// and also keep moving east/west
		if(distY > 0) {
			// move north
			creep.move(0, Constants.NORMAL_SPEED * delta);

		} else if(distY < 0) {
			// move south
			creep.move(0, -Constants.NORMAL_SPEED * delta);
		}

		if(dirX == Constants.DIR_E) {
			creep.move(Constants.NORMAL_SPEED * delta, 0);
		} else {
			creep.move(-Constants.NORMAL_SPEED * delta, 0);
		}
	}	// followMainCharY(GameChar, float, int)'s end

	private void avoidMainChar(GameChar creep, float distX, float distY, float delta) {
		// move the creep away from mainChar

		// move horizontally
		if(distX > 0) {
			// move west
			creep.move(-Constants.NORMAL_SPEED * delta, 0);

		} else if(distX < 0) {
			// move east
			creep.move(Constants.NORMAL_SPEED * delta, 0);
		}

		// move vertically
		if(distY > 0) {
			// move south 
			creep.move(0, -Constants.NORMAL_SPEED * delta);

		} else if(distY < 0) {
			// move north
			creep.move(0, Constants.NORMAL_SPEED * delta);
		}
	}	// avoidMainChar(GameChar, float, float)'s end

		
	private void creepsAttack() {
		// use the creeps to attack mainChar if it sees it
		
		GameChar mainChar = data.getMainChar();
		
		// set rec1 to be the mainChar's y related rectangle
		rec1.setPosition(0, mainChar.y);
		rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

		for(GameChar creep : data.chars) {
			if(creep.getID() != Constants.CHAR_MAIN) {
				// set rec2 to be current creep's y related rectangle
				rec2.setPosition(0, creep.y);
				rec2.setSize(Constants.CHAR_WIDTH, Constants.CAM_HEIGHT);

				if(Intersector.intersectRectangles(rec1, rec2, inter)
					&& Math.abs(
					mainChar.x - creep.x) <= Constants.MAGIC_MAX_DISTANCE + 10) {
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
