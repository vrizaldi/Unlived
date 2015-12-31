// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.kmvrt.Unlived.gameplay.Arena;
import com.kmvrt.Unlived.menu.Menu;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.Timer;

public class Manager extends Game {
	// manage everything in the game (game loop, etc.)

	// logging tag
	private static final String TAG = Manager.class.getName();
	
	// game screens
	private Arena arena;
	private Menu menu;

	private StateData data;
	

// constructor ---------------------------------------------------------------------------------------------
	@Override
	public void create() {
		
		Gdx.app.log(TAG, "Initialising the game...");
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		resize(Gdx.graphics.getHeight());
/*		UNIT_CONV = 16 * Gdx.graphics.getHeight() / 480;
		Constants.init();
		MagicFactory.init();*/
		data = new StateData();
		arena = new Arena(this, data);
		menu = new Menu(this, data);
		toMainMenu();
		Gdx.app.log(TAG, "Game initialised");
	}


// switch between screens ----------------------------------------------------------------------------------
	// called when the player go to arena
	public void startArena() {
	
		// switch screen to arena
		Gdx.app.log(TAG, "Switching to Arena...");
		arena.initNewGame(true, true);
		this.setScreen(arena);
	}

	public void pauseArena() {
		// pause the arena
		// switch screen to the menu (pause)
		data.paused = true;
		menu.initPauseMenu();
		this.setScreen(menu);
	}
	
	public void resumeArena() {
		// resume the arena
		data.paused = false;
		data.justResumed = true;
		// count to 3 before resuming
		Timer.schedule(
				new Timer.Task() {
					
					@Override
					public void run() {
						if(!this.isScheduled()) {
							data.justResumed = false;
						}
//						Gdx.app.debug(TAG, "PEW PEW PEW");
					}
				}, 1, 1, 2);
		this.setScreen(arena);
	}

	// called when gameover
	// save the important data
	public void gameOver(StateData data) {
	
		// switch screen to hub
//		Gdx.app.log(TAG, "Switching to Hub...");
//		this.setScreen(hub);
	}

	public void toMainMenu() {
		
		// switch screen to the mainmenu
		Gdx.app.log(TAG, "Switching to mainmenu");
		data.paused = false;
		arena.dispose();
		menu.initMainMenu();
		this.setScreen(menu);
		this.setScreen(menu);
	}

	// dispose all the resources after the game
	@Override
	public void dispose() {
		
		arena.dispose();
		menu.dispose();
		Gdx.app.log(TAG, "Quitting game...");
	}


	
// resize ------------------------------------------------------------------------------------------------
	public int resize(/*int width,*/ int height) {
		
		int oldUnitConv = 0;
		if(Constants.ins != null) {
			oldUnitConv = Constants.ins.UNIT_CONV;
		}
			// save it to be returned later
		Constants.init();
		if(Constants.ins.UNIT_CONV != oldUnitConv) {
			MagicFactory.init();
		}
		
		return oldUnitConv;
	}
	
}
