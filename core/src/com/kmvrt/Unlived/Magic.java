package com.kmvrt.Unlived;

public class Magic {
	// represent a magic in-game
	// have its own properties (specified in the spell)

	// its position 
	private float x;
	private float y;

	private int dir;	// the direction it's moving

	private float totalMove; // total distance it has moved


// constructor -----------------------------------------------------------------------------------------------
	public Magic(float x, float y, int dir) {
	
		this.x = x;
		this.y = y;
		this.dir = dir;
	}	// new(int, int, int)'s end



// getters and setters ---------------------------------------------------------------------------------------
	public void move(float x, float y) {
		// move the magic by the specified distance
		
		this.x += x;
		this.y += y;

		totalMove += Math.abs(x) + Math.abs(y);
	} // move(int, int)'s end
	
	public void setPos(float x, float y) {
		
		this.x = x;
		this.y = y;
	}
	
	public float getX() {
		// return x coordinate
		
		return x;
	} // getX()'s end

	public float getY() {
		// return x coordinate
		
		return y;
	} // getX()'s end
	
	public int getDir() {
		// return the direction of the magic
	
		return dir;
	}

	public float totalMove() {
		// return the total distance the magic has moved
	
		return totalMove;
	}

}
