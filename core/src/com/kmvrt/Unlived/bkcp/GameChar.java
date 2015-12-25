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

	private Spell spellAffecting;
		// spell that affects the char
	public Attributes atts;
		// store the attribute. e.g. mana, accel, force, etc.
	
	private int dir;  // the direction the character facing
	public boolean wandering;
	
	private int ID;	// type
	
	private Spell spell;
	private boolean bursting;
	private boolean ableToShoot;

	public GameMap.Room cRoom;


// constructor ------------------------------------------------------------------------------------------------
	public GameChar(String spellName) {
	
		this.ID = Constants.CHAR_CREEP_FOLLOW;

		x = 0;
		y = 0;

		sX = x;
		sY = y;

		nX = 0;
		nY = 0;
		
//		fainted = false;

		dir = Constants.DIR_E;
		
		spell = MagicFactory.getSpell(spellName);
		bursting = false;
		ableToShoot = true;

		atts = new Attributes(this);
		spellAffecting = null;
		
		wandering = false;
	}	// new()'s end



// setter and getter ------------------------------------------------------------------------------------------
	// next move
	public void move(float x, float y) {
		// move the char by the specified distance
		
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

		if(spellAffecting == null) {
			return false;
		} else {
			return true;
		}
	} // isAffected()'s end

	public void affectedBy(Magic m) {
		// flag that this char is affected by the given spell

		spellAffecting = m.getSpell();
		Timer.schedule(
			new Timer.Task() {
				
				@Override
				public void run() {
					spellAffecting = null;
				}
			}, 0.1f);
	}	// affectedBy(Spell)'s end

	public Spell getSpellAffecting() {
		// return the spell affecting this char

		return spellAffecting;
	}	// getSpellAffecting()'s end

	public void shoot(boolean firstShot) {
		// called when this char is shooting
		
		if(firstShot) {
			// disable the char from shooting for awhile
			ableToShoot = false;
			Timer.schedule(
				new Timer.Task() {
				
					@Override
					public void run() {
						ableToShoot = true;
					}
				}, spell.getInterval());
			
			if(spell.getBurst() > 1) {
				Timer.schedule(
					new Timer.Task() {
						
						@Override
						public void run() {
							bursting = true;
						}
					}, spell.getBurstInterval(),
					spell.getBurstInterval(), spell.getBurst() - 2);
			}
			
		} else { // not the first shot
			bursting = false;
		}
	}	// shoot(bool)'s end
	
	public boolean isBursting() {
		
		return bursting;
	}
	
	public boolean isAbleToShoot() {
		
		return ableToShoot;
	}

	public String getName() {
		
		return spell.getName();
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
