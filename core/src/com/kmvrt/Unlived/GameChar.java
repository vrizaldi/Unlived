package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Timer;

public class GameChar {
	// represent a character in-game
	// store coordinate, direction, ID and all the de/buff

	private static final String TAG = GameChar.class.getName();
	
	// char position
	public float x;
	public float y;

	// char last safe position
	private float sX;
	private float sY;

	// char next movement
	private float nX;
	private float nY;

	private Spell spell;
		// spell that affects the char
	public Attributes atts;
		// store the attribute. e.g. mana, accel, force, etc.
	
	private boolean fainted;
		// whether the char has ever fainted

	private int dir;  // the direction the character facing
	
	private int ID;	// type


// constructor ------------------------------------------------------------------------------------------------
	public GameChar() {
	
		this.ID = Constants.CHAR_CREEP_FOLLOW;

		x = 0;
		y = 0;

		sX = x;
		sY = y;

		nX = 0;
		nY = 0;
		
		fainted = false;

		dir = Constants.DIR_E;

		atts = new Attributes(this);
		spell = null;
	}	// new()'s end



// setter and getter ------------------------------------------------------------------------------------------
	// next move
	public void move(float x, float y) {
		// move the char by the specified distance
	
		if(x < 0) {
			dir = Constants.DIR_W;
		} else if(x > 0) {
			dir = Constants.DIR_E;
		}
		
		nX += x;
		nY += y;
	} // move(int, int)'s end

	public void moved() {
		// called when the char is moved

		nX = 0;
		nY = 0;
	}	// moved()'s

	public float getNextX() {
		// return the x coordinate

		return nX;
	}	// getNX()'s end

	public float getNextY() {
		// return the y coordinate
	
		return nY;
	}	// getNY()'s end

	// safe position
	public float getSafeX() {
		// return the last safe x coordinate

		return sX;
	} // getSafeX()'s

	public float getSafeY() {
		// return the last safe y coordinate
	
		return sY;
	}	// getSafeY()'s
	
	public void updateSafePos() {
		// change the safe position to the current position
		
		sX = x;
		sY = y;
	}	// updateSafePos()'s


	// facing direction
	public int getDir() {
		// return the direction the char facing
		
		return dir;
	}
	
	public void setDir(int dir) {
		
		if(dir == Constants.DIR_E
			|| dir == Constants.DIR_W) {
			this.dir = dir;
		} else {
			Gdx.app.log(TAG, "ERROR: being set to invalid direction");
			Gdx.app.exit();
		}
	}


	// type id
	public void changeCreep(int type) {
		// change the creep type

		if(ID != Constants.CHAR_MAIN) {
			ID = type;
		}
	} // changeCreep(int)'s

	public void switchTo(int type) {
		// change from one type to another

		ID = type;
	}
	
	public int getID() {
		// return the type id
		
		return ID;
	} // getID()'s end

	
	
// interaction with magic --------------------------------------------------------------------------------------------------	
	public boolean isAffected() {
		// return whether the char is affected by a spell

		if(spell == null) {
			return false;
		} else {
			return true;
		}
	} // isAffected()'s end

	public void affectedBy(Magic m) {
		// flag that this char is affected by the given spell

		spell = m.getSpell();
		Timer.schedule(
			new Timer.Task() {
				
				@Override
				public void run() {
					spell = null;
				}
			}, 0.1f);
	}	// affectedBy(Spell)'s end

	public Spell getSpellAffecting() {
		// return the spell affecting this char

		return spell;
	}	// getSpellAffecting()'s end

	public boolean hasFainted() {
		
		return fainted;
	}
	
	public void fainted() {
		
		fainted = true;
		changeCreep(Constants.CHAR_CREEP_INACTIVE);
		// wake up after several seconds
		Timer.schedule(
			new Timer.Task() {
				
				@Override
				public void run() {
	
					// if still inactive
					if(ID == Constants.CHAR_CREEP_INACTIVE) {
						ID = Constants.CHAR_CREEP_FOLLOW;
					}
				}
			}, 3.0f);
	}


// nested class ---------------------------------------------------------------------------------------------
	public static class Attributes {
		// store the char's attribute

		private float mana;
		private float accel;
		private float force;
		

	// constructors --------------------------------------------------------------------
		public Attributes() {
			
			mana = 0;
			accel = 0;
			force = 0;
		}
		
		public Attributes(Object holder) {

			mana = 0;
			if(holder instanceof GameChar) {
				// mana is inialised as 100 % if it's a character
				mana = 99;
			}
			accel = 0;
			force = 0;
		}


	// apply the attribute ------------------------------------------------------------
		public void applyMana(float mana) {

			this.mana += mana;
		}

		public void applyAccel(float accel) {
		
			this.accel += accel;
		}

		public void applyForce(float force) {
		
			this.force += force;
		}


	// get the attribute --------------------------------------------------------------
		public float getMana() {
		
			return mana;
		}

		public float getAccel() {
		
			return accel;
		}

		public float getForce() {
		
			return force;
		}
	}	// Attribute's end
	
}	// public class's end
