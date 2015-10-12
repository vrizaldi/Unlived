package com.kmvrt.Unlived;

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

			case Constants.ATT_ACCEL:
				mana = attSetters.get(atts.indexOf(att));

			case Constants.ATT_FORCE:
				mana = attSetters.get(atts.indexOf(att));

			}
		}
	} // new's end
}
