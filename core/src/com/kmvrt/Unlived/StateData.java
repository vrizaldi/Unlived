package com.kmvrt.Unlived;

//import com.badlogic.gdx.Gdx;
import java.util.ArrayList;

public class StateData {
	// data of the current state of the game
	// store chars, magics, current room, and other game variables
	// this is the one that updated by the updater and referred by the Painter

//	private static final String TAG = StateData.class.getName();

	public boolean paused = false;
//	public boolean switchScreen;
	public boolean gameOver;
	public boolean switchLevel;
	
	private GameChar mainChar;
	public ArrayList<GameChar> chars;
	public GameMap map;
	public boolean newMap;
	public ArrayList<Magic> magics;

	// kill and death count
	public int killCt;
	public int deathCt;
	
	public int[] cOptions;
	public int pointer = 0;
	public boolean vSync;

	public boolean justResumed = false;
	public boolean beep = false;
	public int beepIter = 0;
	

// constructor ----------------------------------------------------------------------------------------------
	public StateData() {
		
		newMap = false;
		
		mainChar = null;
		chars = new ArrayList<GameChar>();
		magics = new ArrayList<Magic>();
		switchLevel = false;
		
		killCt = 0;
		deathCt = 0;
	} // new(int)'s end


// getter and setter -----------------------------------------------------------------------------------------
	public void setMainChar(GameChar c) {
		// set the current mainChar
		
		if(mainChar != null) {
			mainChar.switchTo(Constants.CHAR_CREEP_FOLLOW);
		}
		c.switchTo(Constants.CHAR_MAIN);
		mainChar = c;
	}

	public GameChar getMainChar() {
		// return the current mainChar

		return mainChar;
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
