// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.kmvrt.Unlived.gameplay.Arena;
import com.kmvrt.Unlived.mainmenu.Menu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;

public class Manager extends Game {
	// manage everything in the game (game loop, etc.)

	// logging tag
	private static final String TAG = Manager.class.getName();

	// game screens
	private Arena arena;
	private Menu menu;
	

// constructor ---------------------------------------------------------------------------------------------
	@Override
	public void create() {

		MagicFactory.init();
		
		Gdx.app.log(TAG, "Initialising the game...");
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		arena = new Arena(this);
		menu = new Menu(this);

/*		DisplayMode[] displayMode = Gdx.graphics.getDisplayModes();
		Gdx.app.log(TAG, "Display Modes available:");
		for(int i = 0; i < displayMode.length; i++) {
			Gdx.app.log(TAG, "" + i + ": " + displayMode[i].width 
				+ "x" + displayMode[i].height + ", " + displayMode[i].bitsPerPixel
				+ " bit/p, " + displayMode[i].refreshRate + "Hz"); 
		} */

		this.setScreen(menu);
		Gdx.app.log(TAG, "Game initialised");
	}


// switch between screens ----------------------------------------------------------------------------------
	// called when the player go to arena
	public void startArena() {
	
		// switch screen to arena
		Gdx.app.log(TAG, "Switching to Arena...");
		this.setScreen(arena);
	}

	// called when gameover
	// save the important data
	public void gameOver(StateData data) {
	
		// switch screen to hub
//		Gdx.app.log(TAG, "Switching to Hub...");
//		this.setScreen(hub);
	}

	public void backToMainMenu() {
		
	}


	// dispose all the resources after the game
	@Override
	public void dispose() {
		
		this.getScreen().dispose();
		Gdx.app.log(TAG, "Quitting game...");
	}

}
