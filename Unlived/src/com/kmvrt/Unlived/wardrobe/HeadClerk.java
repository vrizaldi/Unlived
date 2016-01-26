package com.kmvrt.Unlived.wardrobe;

import com.badlogic.gdx.Gdx;
import com.kmvrt.Unlived.*;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input.Keys;

class HeadClerk {

	private static final String TAG = HeadClerk.class.getName();
	
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
	
		String p = MagicFactory.getSpellName(data.pointer);
			// selected pointer
		if(data.receptionist.moveUp()) {
			data.selCostume = ++data.selCostume % Identities.ins.getCostumeNum(p);
		// if up is just pressed's
		
		} else if(data.receptionist.moveDown()) {
			--data.selCostume;
			if(data.selCostume < 0) {
				data.pointer += Identities.ins.getCostumeNum(p);
			}
		// if down is just pressed's
		
		} else if(data.receptionist.moveRight()) {
			data.pointer = ++data.pointer % MagicFactory.totalSpells();
			data.selChar = MagicFactory.getSpellName(data.pointer);
			data.selCostume = 1;
		// if right is just pressed's

		} else if(data.receptionist.moveLeft()) {
			--data.pointer;
			if(data.pointer < 0) {
				data.pointer += MagicFactory.totalSpells();
			}
			data.selChar = MagicFactory.getSpellName(data.pointer);
			data.selCostume = 1;
		// if left is just pressed's

		} else if(data.receptionist.shootRight()) {
			wardrobe.ready();
			
		} else if(data.receptionist.back()) {
			wardrobe.toMainMenu();
		}
	}

}