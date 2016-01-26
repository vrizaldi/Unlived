package com.kmvrt.Unlived;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.utils.GdxRuntimeException;

public class Identities {

	public static Identities ins;
	private HashMap<String, Integer> costumeNum;

	private static boolean initialised = false;

// constructor ----------------------------------------------------------------------------------------------
	private Identities() {
		
		costumeNum = new HashMap<String, Integer>();
	}

	public static void init() {
	
		if(initialised) {
			return;
		}
		initialised = true;

		ins = new Identities();
		for(String name : MagicFactory.getSpellNames()) {
			int count = 0;
			while(true) {
				++count;
				// try to find the costume folder
				// if can't be found it means that the previous folder
				// was the last one
				if(Gdx.files.internal(
						"res/chars/" + name + '/' + count + '/')
						.exists()) {
					continue;
				} else {
					break;
				}
			}

			ins.costumeNum.put(name, count);
		}
	}



// getters --------------------------------------------------------------------------------------------------
	public int getCostumeNum(String name) {
	
		return (int)costumeNum.get(name);
	}

}
