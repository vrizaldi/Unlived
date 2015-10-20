// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;

public class Chemist {
	// update the magics and chars that got hit by magic
	
	private static final String TAG = Chemist.class.getName();

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

		float delta = Gdx.graphics.getDeltaTime();
		
		for(Iterator<Magic> iter = data.magics.iterator(); iter.hasNext();) {
			Magic m = iter.next();

			// move it based on its direction
			if(m.getDir() == Constants.DIR_E) {
				// move east
				m.move(Constants.MAGIC_SPEED * delta);
				
			} else if(m.getDir() == Constants.DIR_W) {
				// move west
				m.move(-Constants.MAGIC_SPEED * delta);
			}
		} // magic collection iteration's end

		// update chars affected by magic 
		for(GameChar c : data.chars) {
			if(c.isAffected()) {
				// apply the spell to c ********************************************
				Spell spell = c.getSpellAffecting();
				if(spell == null) Gdx.app.log(TAG, "spell = null");
				c.atts.applyMana(spell.atts.getMana() * 10 * delta);
				c.atts.applyAccel(spell.atts.getAccel() * 10 * delta);
				c.atts.applyForce(spell.atts.getForce() * 10 * delta);
			}
		}	
	} // update's end

}	// public class's end
