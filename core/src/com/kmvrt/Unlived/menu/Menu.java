// code by Muhammad Noorghifari

package com.kmvrt.Unlived.menu;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class Menu implements Screen {
	// the main menu screen
	
	private static final String TAG = Menu.class.getName();

	private Manager manager;
	private StateData data;

	private HeadClerk headClerk;
	private Painter painter;


// constructor ---------------------------------------------------------------------------------------------
	public Menu(Manager manager, StateData data) {
		
//		Assets.init();
		this.data = data;
		this.manager = manager;

		headClerk = new HeadClerk(this, data, 
				manager.prefs);
		painter = new Painter(data);
	}


// when un/selected as the active screen --------------------------------------------------------------------
	@Override
	public void show() {
		// when it's just being selected as the active screen
		Gdx.app.log(TAG, "Selected as the active screen");
		
		Assets.init();
	}	// show()'s

	public void initMainMenu() {
		
		data.pointer = 0;
		data.cOptions = Assets.ins.optionsMenu;
	}
	
	public void initPauseMenu() {
		
		data.pointer = 0;
		data.cOptions = Assets.ins.optionsPause;
	}
	
	@Override
	public void hide() {
		// when it's just being unselected as the active screen
		Gdx.app.log(TAG, "Unselected from being the active screen");
		
	} // hide()'s
	
	@Override
	public void dispose() {
		// dispose the resources used
		
		Assets.dispose();
	} // dispose()'s



// update & render --------------------------------------------------------------------------------------------
	@Override
	public void render(float delta) {
		
		// update the states
		headClerk.update();
		painter.render();
	}	// render()'s
	
	public void startArena() {
		
		manager.startArena();
	}
	
	public void resumeArena() {
		
		manager.resumeArena();
	}
	
	public void toMainMenu() {
		
		manager.toMainMenu();
	}


// un/pause -----------------------------------------------------------------------------------------------
	@Override
	public void pause() {
		
//		paused = true;
	}	// pause()'s


	@Override
	public void resume() {

//		paused = false;
	}	// resume()'s



// resize ---------------------------------------------------------------------------------------------------
	@Override
	public void resize(int width, int height) {
		painter.cam.viewportWidth = width;
		painter.cam.viewportHeight = height;
		painter.cam.position.set(width / 2, height / 2, 0);
		painter.cam.update();
	}

}	// public class'
