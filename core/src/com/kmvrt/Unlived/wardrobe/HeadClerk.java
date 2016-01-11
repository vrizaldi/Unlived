package com.kmvrt.Unlived.wardrobe;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

class HeadClerk {

	private Wardrobe wardrobe;
	private StateData data;


// constructor ------------------------------------------------------------------------------------------------
	public HeadClerk(Wardrobe wardrobe, StateData data) {
	
		this.wardrobe = wardrobe;
		this.data = data;
	}



// update the game ---------------------------------------------------------------------------------------------
	public void update() {
	
		procInput();
	}

	public void procInput() {
	
		int p = data.cOptions[data.pointer];
			// selected pointer
		if(Gdx.input.isKeyJustPressed(Keys.UP)) {
		// if up is just pressed
		
		} else if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
		// if down is just pressed
		
		} else if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
		data.pointer = (data.pointer + 1) % data.cOptions.length - 1;
		// if right is just pressed

		} else if(Gdx.input.isKeyJustPressed(Keys.LEFT)) {
		  data.pointer -= 1;
		  if(data.pointer < 0) {
			data.pointer += data.cOptions.length - 1;
		  }
		}
	}

}
