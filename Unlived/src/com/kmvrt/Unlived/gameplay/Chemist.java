// code by Muhammad Noorghifari

package com.kmvrt.Unlived.gameplay;

import com.kmvrt.Unlived.*;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;

class Chemist {
	// update the magics and chars that got hit by magic
	
//	private static final String TAG = Chemist.class.getName();

	private StateData data;

// constructor -----------------------------------------------------------------------------------------------
	public Chemist(StateData data) {
		
		this.data = data;
	}	// new's end


// create or dispose current game ------// decrease the mana if c is mainChar----------------------------------------------------------------------
	public void initNewGame() {

		data.magics.clear();
	}
	
	public void disposeGame() {
		// dispose the current game data
	} // disposeGame()'s end
	
	

// update the magics -----------------------------------------------------------------------------------------
	public void update() {
		// update the magics in the current state of the game
		
		float delta = Gdx.graphics.getDeltaTime();
		if(data.slowMo) {
			delta *= Constants.SLOWMO_RATIO;
				// 10 times slower
		}
		
		for(Iterator<Magic> iter = data.magics.iterator(); iter.hasNext();) {
			Magic m = iter.next();

			// move it based on its direction
			if(m.getDir() == Constants.DIR_E) {
				// move east
				m.move(m.getSpell().getSpeed() * delta);
				
			} else if(m.getDir() == Constants.DIR_W) {
				// move west
				m.move(-m.getSpell().getSpeed() * delta);
			}
			
			// check its current room
			try {
				int roomX = (int)(m.x / 
						(Constants.ins.ROOM_WIDTH + 
						Constants.ins.ROOMS_INTERVAL));
				m.cRoom = data.map.getRoom(roomX, m.cRoom.getY());
			} catch(ArrayIndexOutOfBoundsException e) {
				// the magic is out of the room
				iter.remove();
			} catch(NullPointerException e) {
				iter.remove();
			}
		} // magic collection iteration's end

		// update chars affected by magic 
		for(GameChar c : data.chars) {
			// update the attributes
			
			if(c.isBursting()) {
				updateBurst(c);
			}
			if(c.affectingTime > 0) {
				applyAttributes(c, delta);
			}
		}	// c's iterator	
	} // update's end
	
	private void updateBurst(final GameChar c) {
		
		// shoot
		int dir = c.getDir();

		Magic magic = null;
		if(dir == Constants.DIR_E) {
			// deploy it in the east of the mainChar
			magic = MagicFactory.cast(c.getName(), 
					c.x + (Constants.ins.CHAR_WIDTH / 2), 
					c.y + ((Constants.ins.CHAR_HEIGHT - c.getSpell().getHeight()) / 2),
					Constants.DIR_E, c);
			data.magics.add(magic);
	
		} else if(dir == Constants.DIR_W) {
			// deploy it in the west of the mainChar
			magic = MagicFactory.cast(c.getName(), 
					c.x + (Constants.ins.CHAR_WIDTH / 2) - c.getSpell().getWidth(), 
					c.y + ((Constants.ins.CHAR_HEIGHT - c.getSpell().getHeight()) / 2),
					Constants.DIR_W, c);
			data.magics.add(magic);
		}
		
		c.shoot(false);
	}
	
	private void applyAttributes(final GameChar c, final float delta) {
		
/*		float d = 0;
			// d = delta / affectingTime
		
				
		c.atts.applyMana(c.affectors.getMana() * d);
		c.affectors.applyMana(-c.affectors.getMana() * d);
					
		c.atts.applyForce(c.affectors.getForce() 
				/** Constants.ins.UNIT_CONV ** d);
		c.affectors.applyForce(-c.affectors.getForce()* d);
					
		c.atts.applyAccel(c.affectors.getAccel() 
				/** Constants.ins.UNIT_CONV ** d);
		c.affectors.applyAccel(-c.affectors.getAccel() * d);*/
		
		float d = 0;
			// d = delta / affectingTime
		if(delta > c.affectingTime) {
			d = 1;
			c.affectingTime = 0;
		} else {
			d = delta / c.affectingTime;
			c.affectingTime -= delta;
		}
			
		c.atts.applyMana(c.affectors.getMana() * d);
		c.affectors.applyMana(-c.affectors.getMana() * d);
				
		c.atts.applyForce(c.affectors.getForce() * d);
		c.affectors.applyForce(-c.affectors.getForce() * d);
			
		c.atts.applyAccel(c.affectors.getAccel() * d);
		c.affectors.applyAccel(-c.affectors.getAccel() * d);
	}

}	// public class's end
