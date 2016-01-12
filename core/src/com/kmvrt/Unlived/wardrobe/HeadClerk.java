package com.kmvrt.Unlived.wardrobe;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

class HeadClerk {

	private Wardrobe wardrobe;
	private StateData data;

//	private int pointer;


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
	
		String p = MagicFactory.getSpellNames().get(data.pointer);
			// selected pointer
		if(Gdx.input.isKeyJustPressed(Keys.UP)) {
			data.selCostume = ++data.selCostume % Identities.ins.getCostumeNum(p);
		// if up is just pressed's
		
		} else if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			--data.selCostume;
			if(data.selCostume < 0) {
				data.pointer += Identities.ins.getCostumeNum(p);
			}
		// if down is just pressed's
		
		} else if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
			data.pointer = ++data.pointer % data.cOptions.length;
			data.selCostume = 0;
		// if right is just pressed's

		} else if(Gdx.input.isKeyJustPressed(Keys.LEFT)) {
			--data.pointer;
			if(data.pointer < 0) {
				data.pointer += data.cOptions.length;
			}
			data.selCostume = 0;
		// if left is just pressed's

		} else if(Gdx.input.isKeyJustPressed(Keys.D)) {
			data.selChar = MagicFactory.getSpellName(data.pointer);
		}
	}

}
