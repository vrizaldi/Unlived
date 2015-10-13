// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;

public class Chemist {
	// update the magics and chars that got hit by magic

	private StateData data;

	// rectangles for collision detection
	private Rectangle rec1;
	private Rectangle rec2;
	private Rectangle inter;


// constructor -----------------------------------------------------------------------------------------------
	public Chemist(StateData data) {
		
		this.data = data;
	}	// new's end



// update the magics -----------------------------------------------------------------------------------------
	public void update() {
		// update the magics in the current state of the game

		for(Magic m : data.magics) {
			// move it based on its direction
			if(m.getDir() == Constants.DIR_E) {
				// move east
				m.move(Constants.NORMAL_SPEED, 0);
				
			} else if(m.getDir() == Constants.DIR_W) {
				// move west
				m.move(-Constants.NORMAL_SPEED, 0);
			}

			// check collision
			// set rec1 as m's rectangle
			rec1.setPosition(m.getX(), m.getY());
			rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
			for(GameChar c : data.chars) {
				if(areClose(m, c)) {
					// set rec2 as c's rectangle
					rec2.setPosition(c.getX(), c.getY());
					rec2.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

					if(Intersector.intersectRectangles(rec1, rec2, inter)) {
						// if they intersect
						c.affectedBy(m);
					}
				}
			}	// char collection iteration's end
		} // magic collection iteration's end

		// update chars affected by magic 
		for(GameChar c : data.chars) {
			if(c.affected()) {
				// apply the spell to c ********************************************
				Spell spell = c.getSpellAffecting();
				c.applyMana(spell.getMana());
				c.applyAccel(spell.getAccel());
				c.applyForce(spell.getForce());
			}
		}	
	} // update's end

	private boolean areClose(Magic m, GameChar c) {
		// return whether they're close to each other

		if(Math.abs(m.getX() - c.getX()) < Constants.CHAR_WIDTH * 2
				&& Math.abs(m.getY() - c.getY()) < Constants.CHAR_HEIGHT * 2) {
			return true;

		} else {
			return false;
		}
	}	// areClose(Magic, GameChar)'s end

}	// public class's end
