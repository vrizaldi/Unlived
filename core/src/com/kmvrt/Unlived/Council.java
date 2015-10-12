package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Timer;
import java.util.Iterator;

public class Council {
	// update the characters in-game
	
	private static final String TAG = Council.class.getName();
	
	private StateData data; // the game state data

	private GameChar mainChar;

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
		
		mainChar = new GameChar(Constants.CHAR_MAIN);
		if(data.getStateID() == Constants.STATE_ARENA) {
			// put it in the middle of the room
			mainChar.setPos((data.cRoom.getMiddleX() - Constants.CHAR_WIDTH) / 2, 
				(data.cRoom.getMiddleY() - Constants.CHAR_HEIGHT) / 2);
		}
		
		data.chars.add(mainChar);  
	}	// initNewGame()'s end

	public void disposeGame() {
		// dispose current game's object

		mainChar = null;

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
		
		if(data.magics.size() >= 1) {
			// if there is any magic happening
			updateMagics(delta);
		}
	}	

	private void updateMainChar(float delta) {
		// update the main character
		
		// horizontal movements
		if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			if(mainChar.getDir() == Constants.DIR_E) { 
				// just change direction, don't move
				mainChar.setDir(Constants.DIR_W);
				return;

			} else {
				// move west
				mainChar.move(-Constants.NORMAL_SPEED * delta, 0);
				fixCollision(mainChar, Constants.DIR_W);
			}

		} else if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			if(mainChar.getDir() == Constants.DIR_W) {
				// just change direction, don't move
				mainChar.setDir(Constants.DIR_E);
				return;

			} else {
				// move east
				mainChar.move(Constants.NORMAL_SPEED * delta, 0);
				fixCollision(mainChar, Constants.DIR_E);
			}
		}

		// vertical movement
		if(Gdx.input.isKeyPressed(Keys.UP)) {
			// move north
			mainChar.move(0, Constants.NORMAL_SPEED * delta);
			fixCollision(mainChar, Constants.DIR_N);

		} else if(Gdx.input.isKeyPressed(Keys.DOWN)) {
			// move south
			mainChar.move(0, -Constants.NORMAL_SPEED * delta);
			fixCollision(mainChar, Constants.DIR_S);
		}

		// attacking
		if(Gdx.input.isKeyJustPressed(Keys.D)) {
			// attack
			if(!data.isThereAmmo()) {
				// if there's no ammo
				return;
			}

			if(mainChar.getDir() == Constants.DIR_E) {
				// deploy it in the east of the mainChar
				Magic magic = new Magic(mainChar.getX() + Constants.CHAR_WIDTH, 
					mainChar.getY(), mainChar.getDir());
				data.magics.add(magic);
				data.shoot();
	
			} else if(mainChar.getDir() == Constants.DIR_W) {
				// deploy it in the west of the mainChar
				Magic magic = new Magic(mainChar.getX() - Constants.CHAR_WIDTH, 
					mainChar.getY(), mainChar.getDir());
				data.magics.add(magic);
				data.shoot();
			}
		}
	} // updateMainChar()'s end

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

			GameChar creep = new GameChar(Constants.CHAR_CREEP_FOLLOW);
			creep.setPos(
				(float)(Math.random() * (Constants.ROOM_WIDTH - Constants.CHAR_WIDTH)),
				(float)(Math.random() * (Constants.ROOM_HEIGHT - Constants.CHAR_HEIGHT)));
				// put it anywhere in the room;
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
		
		for(Iterator<GameChar> i = data.chars.iterator(); i.hasNext();) {
			GameChar creep = i.next();
			
			if(creep.getID() != Constants.CHAR_MAIN) {
				// follow the mainChar
				float distX = mainChar.getX() - creep.getX();
				float distY = mainChar.getY() - creep.getY();

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
				fixCollision(creep, Constants.DIR_E);

			} else if(distX < 0) {
				// move west
				creep.move(-Constants.NORMAL_SPEED * delta, 0);
				fixCollision(creep, Constants.DIR_W);
			}
					
			if(distY > 0) {
				// move north
				creep.move(0, Constants.NORMAL_SPEED * delta);
				fixCollision(creep, Constants.DIR_N);
						
			} else if(distY < 0) {
				// move south
				creep.move(0, -Constants.NORMAL_SPEED * delta);
				fixCollision(creep, Constants.DIR_S);
			}
					
		} else if(Math.abs(distX) > Math.abs(distY)) {
			// move horizontally
			if(distX > 0) {
				// move east
				creep.move(Constants.NORMAL_SPEED * delta, 0);
				fixCollision(creep, Constants.DIR_E);
					
			} else if(distX < 0) {
				// move west
				creep.move(-Constants.NORMAL_SPEED * delta, 0);
				fixCollision(creep, Constants.DIR_W);
			}
				
		} else if(Math.abs(distX) < Math.abs(distY)) {
			// move vertically
			if(distY > 0) {
				// move north
				creep.move(0, Constants.NORMAL_SPEED * delta);
				fixCollision(creep, Constants.DIR_N);
					
			} else if(distY < 0) {
				// move south
				creep.move(0, -Constants.NORMAL_SPEED * delta);
				fixCollision(creep, Constants.DIR_S);
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
				fixCollision(creep, Constants.DIR_E);

			} else if(distX < 0) {
				// move west
				creep.move(-Constants.NORMAL_SPEED * delta, 0);
				fixCollision(creep, Constants.DIR_W);
			}

			creep.move(0, Constants.NORMAL_SPEED * delta);

		} else if(dirY == Constants.DIR_S) {
			// move horizontally towards the mainChar
			// and also keep moving south
				
			if(distX > 0) {
				// move east
				creep.move(Constants.NORMAL_SPEED * delta, 0);
				fixCollision(creep, Constants.DIR_E);

			}	else if(distX < 0) {
				// move west
				creep.move(-Constants.NORMAL_SPEED * delta, 0);
				fixCollision(creep, Constants.DIR_W);
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
			fixCollision(creep, Constants.DIR_W);

		} else if(distX < 0) {
			// move east
			creep.move(Constants.NORMAL_SPEED * delta, 0);
			fixCollision(creep, Constants.DIR_E);
		}

		// move vertically
		if(distY > 0) {
			// move south 
			creep.move(0, -Constants.NORMAL_SPEED * delta);
			fixCollision(creep, Constants.DIR_S);

		} else if(distY < 0) {
			// move north
			creep.move(0, Constants.NORMAL_SPEED * delta);
			fixCollision(creep, Constants.DIR_N);
		}
	}	// avoidMainChar(GameChar, float, float)'s end

	private void fixCollision(GameChar ch, int dir) {
		// fix the collision between ch to other chars
	
		// set rec1 to be ch's rectangle
		rec1.setPosition(ch.getX(), ch.getY());
		rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

		for(GameChar c : data.chars) {
			if(c == ch) {
				continue;
			}
				
			if(areClose(ch, c)) {
//				Gdx.app.log(TAG, "ch is close to c");
				// set rec2 to be c's rectangle
				rec2.setPosition(c.getX(), c.getY());
				rec2.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
				if(Intersector.intersectRectangles(rec1, rec2, inter)) {
	//				Gdx.app.log(TAG, "Collision detected");
					// if they intersect
					// move the ch to the opposite direction it's moving before
					switch(dir) {
					
					case Constants.DIR_N:
						// move it to the south
						ch.move(0, -inter.getHeight());					
						break;

					case Constants.DIR_S:
						// move it to the north
						ch.move(0, inter.getHeight());
						break;

					case Constants.DIR_E:
						// move it to the west
						ch.move(-inter.getWidth(), 0);
						break;

					case Constants.DIR_W:
						// move it to the east
						ch.move(inter.getWidth(), 0);
						break;

					}	// switch's end
				}
			}	// if(areClose)'s end
		}
	}	// fixCollision(GameChar, int(dir constant))'s end

	private boolean areClose(GameChar c1, GameChar c2) {
		// return whether c1 and c2 are close to each other
	
		if(Math.abs(c1.getX() - c2.getX()) < Constants.CHAR_WIDTH * 2
			&& Math.abs(c1.getY() - c2.getY()) < Constants.CHAR_HEIGHT * 2) {
			return true;

		} else {
			return false;
		}
	}
	
	private void creepsAttack() {
		// use the creeps to attack mainChar if it sees it
		
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
		rec1.setPosition(0, mainChar.getY());
		rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

		for(GameChar creep : data.chars) {
			if(creep.getID() != Constants.CHAR_MAIN) {
				// set rec2 to be current creep's y related rectangle
				rec2.setPosition(0, creep.getY());
				rec2.setSize(Constants.CHAR_WIDTH, Constants.CAM_HEIGHT);

				if(Intersector.intersectRectangles(rec1, rec2, inter)
					&& Math.abs(
					mainChar.getX() - creep.getX()) <= Constants.MAGIC_MAX_DISTANCE) {
					// if the mainChar is within the range of the creep shoot
					if(creep.getDir() == Constants.DIR_E) {
						// on the right side
						Magic magic = new Magic(creep.getX() + Constants.CHAR_WIDTH,
							creep.getY(), creep.getDir());
						data.magics.add(magic);

					} else if(creep.getDir() == Constants.DIR_W) {
						// on the left side
						Magic magic = new Magic(creep.getX() - Constants.CHAR_WIDTH,
							creep.getY(), creep.getDir());
						data.magics.add(magic);
					}
				}
			}
		}	// for's end
	}
	
	private void updateMagics(float delta) {
		// update the magic movement
		
		for(Iterator<Magic> i = data.magics.iterator(); i.hasNext();) {
			Magic magic = i.next();
			
			if(magic.getDir() == Constants.DIR_E) {
				// move right
				magic.move(Constants.MAGIC_SPEED * delta, 0);

			} else if(magic.getDir() == Constants.DIR_W) {
				// move left
				magic.move(-Constants.MAGIC_SPEED * delta, 0);
			}

			// do collision detection here ***************
			// set the rec1 as current magic's rectangle
			rec1.setPosition(magic.getX(), magic.getY());
			rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

			// collision detection with chars
			for(Iterator<GameChar> it = data.chars.iterator(); it.hasNext();) {
				GameChar c = it.next();
				
				// set rec2 as current char's rectangle				
				rec2.setPosition(c.getX(), c.getY());
				rec2.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
			
				if(Intersector.intersectRectangles(rec1, rec2, inter)) {
					// if they're intersect
					if(c.getID() == Constants.CHAR_MAIN) {
						data.die();
						data.gameOver = true;
					} else {
						data.kill();
						it.remove();
					}
				}
			}

			if(magic.totalMove() >= (float)Constants.MAGIC_MAX_DISTANCE) {
				// if the magic has went far enough
			 	// destroy it
				i.remove();
			}
		}
	}	// updateMagics()'s end

} // class' end
