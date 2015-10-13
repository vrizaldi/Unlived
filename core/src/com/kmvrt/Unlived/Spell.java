// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import java.util.ArrayList;

public class Spell {
	// represents a spell in a game
	// store properties of magic to be used in magic factories

	// attributes
	private float mana;
	private float accel;
	private float force;
	

// constructor ------------------------------------------------------------------------------------------------
	public Spell(ArrayList<int> atts, ArrayList<float> attSetters) {

		for(int att : atts) {
			switch(att) {

			case Constants.ATT_MANA:
				mana = attSetters.get(atts.indexOf(att));
				break;

			case Constants.ATT_ACCEL:
				mana = attSetters.get(atts.indexOf(att));
				break;

			case Constants.ATT_FORCE:
				mana = attSetters.get(atts.indexOf(att));
				break;

			}
		}
	} // new's end


// getters ----------------------------------------------------------------------------------------------------
	public float getMana() {
		// return the mana boost
	
		return mana;
	} // getMana()'s end

	public float getAccel() {
		// return the accel boost
		
		return accel;
	}	// getAccel()'s end

	public float getForce() {
		// return the force boost
	
		return force;
	}	// getForce()'s end
}
