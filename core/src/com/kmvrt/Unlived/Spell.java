// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

public class Spell {
	// represents a spell in a game
	// store properties of magic to be used in magic factories

	public GameChar.Attributes hit;
		// attributes applied to characters affected
	public GameChar.Attributes cast;
		// attributes applied to mainchar casting
	private boolean attsInitialised;
	

// constructor ------------------------------------------------------------------------------------------------
	public Spell() {

		hit = null;
		cast = null;
		attsInitialised = false;
	} // new's end
	
	public void initAtts(GameChar.Attributes hit, GameChar.Attributes cast) {
		if(!attsInitialised) {
			this.hit = hit;
			this.cast = cast;
		}
	}
}
