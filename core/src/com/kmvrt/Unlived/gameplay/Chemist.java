// code by Muhammad Noorghifari

package com.kmvrt.Unlived.gameplay;

import com.kmvrt.Unlived.*;
import java.util.Iterator;

import com.badlogic.gdx.Gdx;

public class Chemist {
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
						(Constants.ROOM_WIDTH + 
						Constants.ROOMS_INTERVAL));
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
				// shoot
//				Gdx.app.log(TAG, "Bursting");
				int dir = c.getDir();

				Magic magic = null;
				if(dir == Constants.DIR_E) {
					// deploy it in the east of the mainChar
					magic = MagicFactory.cast(c.getName(), 
							c.x + (Constants.CHAR_WIDTH / 2), 
							c.y + ((Constants.CHAR_HEIGHT - c.getSpell().getHeight()) / 2),
							Constants.DIR_E, c);
					data.magics.add(magic);
			
				} else if(dir == Constants.DIR_W) {
					// deploy it in the west of the mainChar
					magic = MagicFactory.cast(c.getName(), 
							c.x + (Constants.CHAR_WIDTH / 2) - c.getSpell().getWidth(), 
							c.y + ((Constants.CHAR_HEIGHT - c.getSpell().getHeight()) / 2),
							Constants.DIR_W, c);
					data.magics.add(magic);
				}
				
				c.shoot(false);
			}
			
			// apply the attributes
			if(c.affectingTime <= 0) {
				continue;
			}
/*			if(c.getID() == Constants.CHAR_MAIN) {
				Gdx.app.log(TAG, "affectors");
				Gdx.app.log(TAG, "\tmana = " + c.affectors.getMana());
				Gdx.app.log(TAG, "\taccel = " + c.affectors.getAccel());
				Gdx.app.log(TAG, "\tforce = " + c.affectors.getForce());
				Gdx.app.log(TAG, "atts");
				Gdx.app.log(TAG, "\tmana = " + c.atts.getMana());
				Gdx.app.log(TAG, "\taccel = " + c.atts.getAccel());
				Gdx.app.log(TAG, "\tforce = " + c.atts.getForce() + "\n");
			}*/
			
			float d = 0;
			if(delta > c.affectingTime) {
				d = 1;
				c.affectingTime -= c.affectingTime;
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
			
/*			if(c == data.getMainChar()) {
				Gdx.app.debug(TAG, "Check if bursting");
			}*/
			// bursting shots
			
		}	// c's iterator	
	} // update's end

}	// public class's end
