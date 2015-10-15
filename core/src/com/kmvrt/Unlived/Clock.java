// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.Gdx;

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
		
		float delta = Gdx.graphics.deltaTime();

		// move the chars
		for(GameChar c : data.chars) {
			c.x += c.getNextX() + c.atts.getAccel() * delta;			
			c.y += c.getNextY() + c.atts.getAccel() * delta;
			c.moved();
		}	// chars iterator's

		// move the magics
		for(Iterator iter = data.magics.iterator(); iter.hasNext();) {
			Magic m = iter.next();

			m.x += m.getNextX() + m.atts.getAccel() * delta;
			m.y += m.getNextY() + m.atts.getAccel() * delta;
			m.moved();

			if(m.totalMove() > Constants.MAGIC_MAX_DISTANCE) {
				// if the magic has moved to far
				iter.remove();
			}
		}	// magic iterator's 

		// collision detections
		// char to char
		for(GameChar c1 : data.chars) {
			// set rec1 as c1's rectangle
			rec1.setPosition(c1.x, c1.y);
			rec1.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
			for(GameChar c2 : data.chars) {
				if(c2 != c1 && areClose(c1, c2)) {
					// if c1 isn't c2 and they are close to each other
					// set rec2 as c2's rectangle
					rec2.setPosition(c2.x, c2.y);
					rec2.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
					if(Intersector.intersectRectangles(rec1, rec2, inter)) {
						// if they intersect
						fixCharsXCollision(c1, c2);
							// horizontal collision
					}	// if(collide)'s
				}	// if(areClose)'s
			}	// c2 iterator's
		}	// c1 iterator's
	}	// update()'s

	private void fixCharsXCollisions(GameChar c1, GameChar c2) {
	
		if(c1.x - c1.getSafeX() > 0) {
			// c1 was moving to the east
			if(c2.x - c2.getSafeX() < 0) {
				// c1 and c2 were moving towards each other 

			} else if(c2.x - c2.getSafeX() > 0) {
				// c1 and c2 were moving toward the same direction
				if(c1.getSafeX() > c2.getSafeX()) {
					// c1 was ahead
					// push move c2 to the west
					c2.x -= inter.width;

				} else {
					// c2 was ahead
					// push c1 to the west 
					c1.x -= inter.width;
				}
			// if(c1 and c2 same dir)'s

			} else if(c2.x == c2.getSafeX()) {
				// c2 wasn't moving
				// push c1 to the west
				c1.x -= inter.width;
			}
		// if(c1's moving east)'s

		} else if(c1.x - c1.getSafeX() < 0) {
			// c1 was moving to the west
			if(c2.x - c2.getSafeX() > 0) {
				// c1 and c2 were moving toward each other

			} else if(c2.x - c2.getSafeX() < 0) {
				// c1 and c2 were moving toward the same direction
				if(c1.getSafeX() > c2.getSafeX()) {
					// c1 was ahead
					// push c2 to the east
					c2.x += inter.width;

				} else {
					// c2 was ahead
					// push c1 to the east 
					c1.x += inter.width;
				}
			// if(c1 and c2 same dir)'s

			} else if(c2.x == c2.getSafeX()) {
				// c2 wasn't moving
				// push c1 to the east
				c1.x += inter.width;
			}
		// if(c1's moving west)'s

		} else if(c1.x == c1.getSafeX()) {
			// c1 wasn't moving
			if(c2.x - c2.getSafeX() > 0) {
				// c2 was moving to the east
				// push c2 to the west
				c2.x -= inter.width;

			} else {
				// c2 was moving to the west
				// push c2 to the east
				c2.x += inter.width;
			}
		}
	} // fixCharsXCollisions

	private boolean areClose(GameChar c1, GameChar c2) {
		// return whether the chars are close to each other

		if(Math.abs(c1.x - c2.x) < Constants.CHAR_WIDTH * 2
				&& Math.abs(c1.y - c2.y) < Constants.CHAR_HEIGHT * 2) {
			return true;

		} else {
			return false;
		}
	}	// areClose(GameChar, GameChar)'s

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
