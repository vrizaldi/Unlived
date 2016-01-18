package com.kmvrt.Unlived;

//import com.kmvrt.Unlived.*;
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

	public Attributes affectors;
		// applied attributes
	public float affectingTime;
	public Attributes atts;
		// store the attribute. e.g. mana, accel, force, etc.
	
	private int dir;  // the direction the character facing
	public boolean wandering;
	
	private int ID;	// type
	
	private Spell spell;
	private boolean bursting;
	private boolean ableToShoot;
	private int costume;

//	public boolean hasSlowMo = false;
	
	public Room cRoom;


// constructor ------------------------------------------------------------------------------------------------
	public GameChar(String spellName, int costume) {
	
		this.ID = Constants.CHAR_CREEP_FOLLOW;

		cRoom = null;
		
		x = 0;
		y = 0;

		sX = x;
		sY = y;

		nX = 0;
		nY = 0;
		
		affectingTime = 0;

		dir = Constants.DIR_E;
		
		spell = MagicFactory.getSpell(spellName);
		bursting = false;
		ableToShoot = true;

		this.costume = costume;

		atts = new Attributes(true);
		affectors = new Attributes(false);
		
		wandering = false;
		
//		hasSlowMo = false;
	}	// new()'s end
	
	public void reset() {
		
		x = 0;
		y = 0;

		sX = x;
		sY = y;

		nX = 0;
		nY = 0;
		
		affectingTime = 0;

		dir = Constants.DIR_E;
		
		bursting = false;
		ableToShoot = true;
		
//		hasSlowMo = false;
		
		// add 50 mana
		atts.applyMana(50);
		affectors = new Attributes(false);
	}



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
		atts.resetAtts();
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
			Gdx.app.error(TAG, "Being set to invalid direction");
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

	public int getCostume() {
		// return the costume id
	
		return costume;
	}
	

	
// interaction with magic --------------------------------------------------------------------------------------------------	
	public void affectedBy(Magic m) {
		// flag that this char is affected by the given spell
		
		if(ID == Constants.CHAR_MAIN) {
			Gdx.app.debug(TAG, "Has just been shot");
		}

		affectingTime = Constants.INIT_AFFECTING_TIME;
		Spell spell = m.getSpell();
		affectors.applyMana(spell.hit.getMana());
		if(m.getDir() == Constants.DIR_E) {
			affectors.applyForce(-spell.hit.getForce());
		} else {	// m.dir == DIR_W
			affectors.applyForce(spell.hit.getForce());
		}
		affectors.applyAccel(spell.hit.getAccel());
	}	// affectedBy(Spell)'s end
	
	public void shoot(boolean firstShot) {
		// called when this char is shooting
		
		if(ID == Constants.CHAR_MAIN
				&& !firstShot) {
			Gdx.app.debug(TAG, "Has just shot bursting");
			Gdx.app.debug(TAG, "burstInterval = " + spell.getBurstInterval());
		}
		
		if(firstShot) {
			// disable the char from shooting for awhile
			ableToShoot = false;
			WorldTimer.schedule(
				new Timer.Task() {
				
					@Override
					public void run() {
						ableToShoot = true;
					}
				}, spell.getInterval());
			
			if(spell.getBurst() > 1) {
				WorldTimer.schedule(
					new Timer.Task() {
						
						@Override
						public void run() {
							bursting = true;
					//		Gdx.app.log(TAG, "burstin = " + String.valueOf(bursting));
						}
					}, spell.getBurstInterval(),
					spell.getBurst() - 2);
			}
			
		} else { // not the first shot
			bursting = false;
		}
		
		affectingTime = Constants.INIT_AFFECTING_TIME;
		if(ID == Constants.CHAR_MAIN
				&& atts.getMana() > spell.cast.getMana()) {
			affectors.applyMana(spell.cast.getMana());
		} 
		if(this.getDir() == Constants.DIR_E) {
			affectors.applyForce(spell.cast.getForce());
		} else {	// m.dir == DIR_W
			affectors.applyForce(-spell.cast.getForce());
		}
		affectors.applyAccel(spell.cast.getAccel());
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
	
	public Spell getSpell() {
		
		return spell;
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
		
		public Attributes(boolean chara) {

			mana = 0;
			if(chara) {
				// mana is inialised as 100 % if it's a character
				mana = 99;
			}
			accel = 0;
			force = 0;
		}


	// apply the attribute ------------------------------------------------------------
		public void applyMana(float mana) {

			// max 99
			this.mana += Math.min(mana, 99 - this.mana);
		}

		public void applyAccel(float accel) {
		
			this.accel += accel;
		}

		public void applyForce(float force) {
		
			this.force += force;
		}
		
		public void resetAtts() {
			
			force = 0;
			accel = 0;
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
