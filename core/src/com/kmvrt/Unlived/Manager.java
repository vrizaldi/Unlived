// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Graphics.DisplayMode;
//import com.badlogic.gdx.Screen;

public class Manager extends Game {
	// manage everything in the game (game loop, etc.)

	// logging tag
	private static final String TAG = Manager.class.getName();

	// game screens
	private Arena arena;
	

// constructor ---------------------------------------------------------------------------------------------
	@Override
	public void create() {

		Gdx.app.log(TAG, "Initiating the game...");
		arena = new Arena(this);

		Gdx.app.log(TAG, "Initiating the assets...");
		Assets.init();
		MagicFactory.init();

		DisplayMode[] displayMode = Gdx.graphics.getDisplayModes();
		Gdx.app.log(TAG, "Display Modes available:");
		for(int i = 0; i < displayMode.length; i++) {
			Gdx.app.log(TAG, "" + i + ": " + displayMode[i].width 
				+ "x" + displayMode[i].height + ", " + displayMode[i].bitsPerPixel
				+ " bit/p, " + displayMode[i].refreshRate + "Hz");
		}

		this.setScreen(arena);
		Gdx.app.log(TAG, "Game initiated");
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


	// dispose all the resources after the game
	@Override
	public void dispose() {
	
		Gdx.app.log(TAG, "Disposing assets...");
		Assets.dispose();
		Gdx.app.log(TAG, "Quitting game...");
	}

}
