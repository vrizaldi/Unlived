package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

public class StateData {
	// data of the current state of the game
	// store chars, magics, current room, and other game variables
	// this is the one that updated by the updater and referred by the Painter

	private static final String TAG = StateData.class.getName();

	public boolean switchScreen;
	public boolean gameOver;
	public boolean switchLevel;
	
	private int stateID;

	private int mainCharAmmo;

	public ArrayList<GameChar> chars;
	public GameMap cRoom;
	public ArrayList<Magic> magics;

	// kill and death count
	public int killCt;
	public int deathCt;

// constructor ----------------------------------------------------------------------------------------------
	public StateData(int stateID) {
		
		if(stateID != Constants.STATE_HUB
			&& stateID != Constants.STATE_ARENA) {
			Gdx.app.log(TAG, "Wrong input on StateData class: " + stateID);

		} else {
			this.stateID = stateID;
		}
		
		chars = new ArrayList<GameChar>();
		magics = new ArrayList<Magic>();
		switchLevel = false;
		
		killCt = 0;
		deathCt = 0;
	} // new(int)'s end


// getter and setter -----------------------------------------------------------------------------------------
	public int getStateID() {
	
		return stateID;
	}
	
	public boolean isLevelFinished() {
		// return whether there's only mainChar left in the level
	
		if(chars.size() == 1
			&& chars.get(0).getID() == Constants.CHAR_MAIN) {
			// if there's onlyindex mainChar left
			return true;

		} else {
			return false;	
		}
	}

	public void shoot() {
		// decrease the ammo
	
		// ammo can't be less than 0 
		mainCharAmmo = mainCharAmmo - 1 < 0 ? 0 : mainCharAmmo - 1; 
	}

	public void initAmmo() {
		// initialise the ammo
		
		mainCharAmmo = Constants.INIT_AMMO;
	}

	public boolean isThereAmmo() {
		// return whether there is ammo left
		
		return mainCharAmmo == 0 ? false : true;
	}

	public int getAmmoLeft() {
		// return the ammo left
		
		return mainCharAmmo;
	}

	public void initDeath() {
		// reinitialise death count

		deathCt = 0;
	}

	public void die() {
		// increment the death count

		deathCt++;
	}

	public void initKill() {
		// reinitialise kill count

		killCt = 0;
	}

	public void kill() {
		// increment the kill count
		
		killCt++;		
	}

	public float getKillPerDeath() {
		// return the average number of kills every death 
	
		// if death == 0, just return killCt
		return deathCt == 0 ? killCt : (float)killCt / (float)deathCt;
	}

}
