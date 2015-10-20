// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

public class Spell {
	// represents a spell in a game
	// store properties of magic to be used in magic factories

	public GameChar.Attributes atts;
		// attributes to be applied to characters affected
	private boolean attsInitialised;
	

// constructor ------------------------------------------------------------------------------------------------
	public Spell() {

		atts = null;
		attsInitialised = false;
	} // new's end


	
	public void initAtts(GameChar.Attributes atts) {
		if(!attsInitialised) {
			this.atts = atts;
		}
	}
}
