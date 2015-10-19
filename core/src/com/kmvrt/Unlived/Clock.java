// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import java.util.Iterator;

public class Clock {
	// update the time of the game
	// every actual movements are updated here
		
	private StateData data;

	// rectangles for collision detection
	private Rectangle rec1;
	private Rectangle rec2;
	private Rectangle inter;


// constructor ----------------------------------------------------------------------------------------------
	public Clock(StateData data) {

		this.data = data;

		rec1 = new Rectangle();
		rec2 = new Rectangle();
		inter = new Rectangle();
	}	// new's



// update the game ----------------------------------------------------------------------------------------------
	public void update() {
		// update the current state of the game

		// move the chars
		for(GameChar c : data.chars) {
			c.x += c.getNextX() + c.atts.getAccel();			
			c.y += c.getNextY() + c.atts.getAccel();
			c.moved();
		}	// chars iterator's

		// move the magics
		for(Iterator<Magic> iter = data.magics.iterator(); iter.hasNext();) {
			Magic m = iter.next();

			m.x += m.getNextX();
			m.moved();

			if(m.totalMove() > Constants.MAGIC_MAX_DISTANCE) {
				// if the magic has moved to far
				iter.remove();
			}
		}	// magic iterator's 

		// collision detections
		// char to char
		for(GameChar c1 : data.chars) {
			// check if the char is out of the room
			if(c1.x < 0) {
				// over to the west
				c1.x = 0;
			} else if(c1.x + Constants.CHAR_WIDTH > Constants.ROOM_WIDTH) {
				// over to the east
				c1.x = Constants.ROOM_WIDTH - Constants.CHAR_WIDTH;
			}
						
			if(c1.y < 0) {
				// over to the south
				c1.y = 0;
						
			} else if(c1.y + Constants.CHAR_HEIGHT > Constants.ROOM_HEIGHT) {
				// over to the north
				c1.y = Constants.ROOM_HEIGHT - Constants.CHAR_HEIGHT;
						
			}
			
			c1.updateSafePos();
		}	// c1 iterator's
		
		// magic to char
		for(Magic m : data.magics) {
			// set rec1 as m's rectangle
			rec1.setPosition(m.x, m.y);
			rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
			for(GameChar c : data.chars) {
				if(areClose(m, c)) {
					// set rec2 as c's rectangle
					rec2.setPosition(c.x, c.y);
					rec2.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
					if(Intersector.intersectRectangles(rec1, rec2, inter)) {
						c.affectedBy(m);
					}
				}
			}
		}

	}	// update()'s

	private boolean areClose(Magic m, GameChar c) {
		// return whether the magic and the char are close to each other

		if(Math.abs(m.x - c.x) < Constants.CHAR_WIDTH * 2
				&& Math.abs(m.y - c.y) < Constants.CHAR_HEIGHT * 2) {
			return true;

		} else {
			return false;
		}
	}	// areClose(Magic, GameChar)'s

}	// public class'
