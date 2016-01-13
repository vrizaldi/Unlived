package com.kmvrt.Unlived.wardrobe;

class Assets {

	public static Assets ins;

	private static boolean initialised = false;


// constructor --------------------------------------------------------------------------------------------------
	private Assets() {
		// prevent outside init

		// init the assets **************************
	}



// init & dispose -----------------------------------------------------------------------------------------------
	public static void init() {
		// initialise the assets
	
		if(initialised) {
			return;
		}
		initialised = true;
	
		ins = new Assets();
	}

	public static void dispose() {
		// dispose the assets
	
		if(!initialised) {
			return;
		}
		initialised = false;
		
		// dispose the assets ******************************
	}

}
