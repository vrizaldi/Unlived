package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.math.Vector3;

public class GameChar {

	private static final String TAG = GameChar.class.getName();
	
	// char position
	private float x;
	private float y;

	private int dir;  // the direction the character facing
	public float forceX;	// the force pushing the character
	public float forceY;
	
	private int ID;	// type


// constructor ------------------------------------------------------------------------------------------------
	public GameChar(int ID) {
	
		this.ID = ID;

		x = 0;
		y = 0;

		dir = Constants.DIR_E;
		forceX = 0;
		forceY = 0;
	}	// new()'s end



// setter and getter ------------------------------------------------------------------------------------------
	public void setPos(float x, float y) {
		// move the char to the specified position
	
		this.x = x;
		this.y = y;
	}	// setPos(int, int)'s end

	public void move(float x, float y) {
		// move the char by the specified distance
	
		this.x += x;
		this.y += y;
	} // move(int, int)'s end

	public float getX() {
		// return the x coordinate

		return this.x;
	}	// getX()'s end

	public float getY() {
		// return the y coordinate
	
		return this.y;
	}	// getY()'s end
	
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

	public void changeCreep(int type) {
		if(ID != Constants.CHAR_MAIN) {
			if(type == Constants.CHAR_CREEP_AVOID
				|| type == Constants.CHAR_CREEP_FOLLOW
				|| type == Constants.CHAR_CREEP_FOLLOW_N
				|| type == Constants.CHAR_CREEP_FOLLOW_S) {
				ID = type;
			}
		}
	}
	
	public int getID() {
		// return the type id
		
		return ID;
	} // getID()'s end

}
