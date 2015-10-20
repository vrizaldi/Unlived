package com.kmvrt.Unlived;

public class Magic {
	// represent a magic in-game
	// have its own properties (specified in the spell)

	// its position 
	public float x;
	public float y;

	// next movement
	// magics only move horizontally
	private float nX;

	private int dir;	// the direction it's moving
	private Spell spell;	// the magic's properties

	private float totalMove; // total distance it has moved


// constructor -----------------------------------------------------------------------------------------------
	public Magic(Spell spell, float x, float y, int dir) {
	
		this.x = x;
		this.y = y;
		this.dir = dir;
		this.spell = spell;
	}	// new(int, int, int)'s end



// getters and setters ---------------------------------------------------------------------------------------
	public void move(float x) {
		// move the magic by the specified distance
		
		nX += x;
	} // move(int, int)'s end
	
	public void moved() {
		// called when the char is moved

		totalMove += Math.abs(nX);
			// increase total move by the nX coordinate
		nX = 0;
	}

	public float getNextX() {
		// return x coordinate
		
		return nX;
	} // getX()'s end
	
	public int getDir() {
		// return the direction of the magic
	
		return dir;
	}

	public Spell getSpell() {
		// return the magic's properties / spell
		
		return spell;
	}
	
	public float totalMove() {
		// return the total distance the magic has moved
	
		return totalMove;
	}

}
