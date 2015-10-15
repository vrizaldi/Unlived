// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

public class Chemist {
	// update the magics and chars that got hit by magic

	private StateData data;

// constructor -----------------------------------------------------------------------------------------------
	public Chemist(StateData data) {
		
		this.data = data;
	}	// new's end


// create or dispose current game ----------------------------------------------------------------------------
	public void initNewGame() {}
	
	public void disposeGame() {
		// dispose the current game data
		
		data.magics.clear();
	} // disposeGame()'s end
	
	

// update the magics -----------------------------------------------------------------------------------------
	public void update() {
		// update the magics in the current state of the game

		for(Iterator iter = data.magics.iterator(); iter.hasNext();) {
			Magic m = iter.next();

			// move it based on its direction
			if(m.getDir() == Constants.DIR_E) {
				// move east
				m.move(Constants.NORMAL_SPEED, 0);
				
			} else if(m.getDir() == Constants.DIR_W) {
				// move west
				m.move(-Constants.NORMAL_SPEED, 0);
			}
		} // magic collection iteration's end

		// update chars affected by magic 
		for(GameChar c : data.chars) {
			if(c.isAffected()) {
				// apply the spell to c ********************************************
				Spell spell = c.getSpellAffecting();
				c.atts.applyMana(spell.atts.getMana());
				c.atts.applyAccel(spell.atts.getAccel());
				c.atts.applyForce(spell.atts.getForce());
			}
		}	
	} // update's end

	private boolean areClose(Magic m, GameChar c) {
		// return whether they're close to each other

		if(Math.abs(m.x - c.x) < Constants.CHAR_WIDTH * 2
				&& Math.abs(m.y - c.y) < Constants.CHAR_HEIGHT * 2) {
			return true;

		} else {
			return false;
		}
	}	// areClose(Magic, GameChar)'s end

}	// public class's end
