// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Timer;
import java.util.Iterator;

public class Council {
	// update the characters in-game without moving it
	
	private static final String TAG = Council.class.getName();
	
	private StateData data; // the game state data

	// rectangles for collision detection
	private Rectangle rec1;
	private Rectangle rec2;
	private Rectangle inter;

	// whether the creep ready to attack
	private boolean creepsAttack;
	private boolean creepsChange;
	

// constructor ----------------------------------------------------------------------------------------------
	public Council(StateData data) {
		
		this.data = data;

		rec1 = new Rectangle();
		rec2 = new Rectangle();
		inter = new Rectangle();
			
		creepsAttack = true;
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
		
		GameChar mainChar = new GameChar();
		if(data.getStateID() == Constants.STATE_ARENA) {
			// put it in the middle of the room
			mainChar.x = (data.cRoom.getMiddleX() - Constants.CHAR_WIDTH) / 2; 
			mainChar.y = (data.cRoom.getMiddleY() - Constants.CHAR_HEIGHT) / 2;
			mainChar.updateSafePos();
		}
		
		data.chars.add(mainChar); 
		data.setMainChar(mainChar);
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
	}	

	private void updateMainChar(float delta) {
		// update the main character

		GameChar mainChar = data.getMainChar();
		
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

		// attacking
		if(Gdx.input.isKeyJustPressed(Keys.D)) {
			// attack
			shoot(mainChar.x, mainChar.y, mainChar.getDir());
		}
	} // updateMainChar()'s end

	private void shoot(float charX, float charY, int dir) {
		// deploy a magic in front of the deployer

		if(dir == Constants.DIR_E) {
			// deploy it in the east of the mainChar
			Magic magic = MagicFactory.cast("Attack", charX + Constants.CHAR_WIDTH, 
				charY, Constants.DIR_E);
			data.magics.add(magic);
	
		} else if(dir == Constants.DIR_W) {
			// deploy it in the west of the mainChar
			Magic magic = MagicFactory.cast("Attack", charX - Constants.CHAR_WIDTH, 
				charY, Constants.DIR_W);
			data.magics.add(magic);
		}
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

			GameChar creep = new GameChar();
			creep.x = 
				(float)(Math.random() * (Constants.ROOM_WIDTH - Constants.CHAR_WIDTH));
			creep.y =
				(float)(Math.random() * (Constants.ROOM_HEIGHT - Constants.CHAR_HEIGHT));
				// put it anywhere in the room;
			creep.updateSafePos();
			data.chars.add(creep);
				// add it to the collection
		}
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

				case 8:
				case 7:
				case 6:
					creep.changeCreep(Constants.CHAR_CREEP_FOLLOW);
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
				distX = Math.abs(distX) < Constants.SAFE_DIST_X + 1 ?
					0 : distX;
				distY = Math.abs(distY) < Constants.SAFE_DIST_Y + 1?
					0 : distY;
				
				// move the creep based on its type
				if(creep.getID() == Constants.CHAR_CREEP_FOLLOW) {
					followMainChar(creep, distX, distY, delta);
				
				} else if(creep.getID() == Constants.CHAR_CREEP_FOLLOW_N) {
					followMainCharX(creep, distX, Constants.DIR_N, delta);

				} else if(creep.getID() == Constants.CHAR_CREEP_FOLLOW_S) {
					followMainCharX(creep, distX, Constants.DIR_S, delta); 

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

		if(dirY == Constants.DIR_N) {
			// move horizontally towards the mainChar
			// and also keep moving north
			if(distX > 0) {
				// move east
				creep.move(Constants.NORMAL_SPEED * delta, 0);

			} else if(distX < 0) {
				// move west
				creep.move(-Constants.NORMAL_SPEED * delta, 0);
			}

			creep.move(0, Constants.NORMAL_SPEED * delta);

		} else if(dirY == Constants.DIR_S) {
			// move horizontally towards the mainChar
			// and also keep moving south
				
			if(distX > 0) {
				// move east
				creep.move(Constants.NORMAL_SPEED * delta, 0);

			}	else if(distX < 0) {
				// move west
				creep.move(-Constants.NORMAL_SPEED * delta, 0);
			}

			creep.move(0, -Constants.NORMAL_SPEED * delta);
		}
	}	// followMainCharX(GameChar, float, int)'s end

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
		
		if(!creepsAttack) {
			return;
		}
		creepsAttack = false;
		Timer.schedule(
			new Timer.Task() {
				
				@Override
				public void run() {
					
					creepsAttack = true;
				}
			}, Constants.CREEPS_ATK_INTERVAL);
		
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
					shoot(creep.x, creep.y, creep.getDir());
				}
			}
		}	// for's end
	}
	
} // class' end
