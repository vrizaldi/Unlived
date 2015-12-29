// code by Muhammad Noorghifari

package com.kmvrt.Unlived.mainmenu;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class Menu implements Screen {
	// the main menu screen
	
	private static final String TAG = Menu.class.getName();

	private boolean paused;
	private Manager manager;

	private HeadClerk headClerk;
	private Painter painter;
	private Courier courier;


// constructor ---------------------------------------------------------------------------------------------
	public Menu(Manager manager) {
		
		Assets.init();
	
		this.manager = manager;

		courier = new Courier(Assets.optionsMenu);
		headClerk = new HeadClerk(this, courier);
		painter = new Painter(this, courier);
	}


// when un/selected as the active screen --------------------------------------------------------------------
	@Override
	public void show() {
		// when it's just being selected as the active screen
		
		Assets.init();
	}	// show()'s

	@Override
	public void hide() {
		// when it's just being unselected as the active screen
		
		dispose();
	} // hide()'s
	
	@Override
	public void dispose() {
		// dispose the resources used
		
		Gdx.app.log(TAG, "Disposing resources...");
		Assets.dispose();
	} // dispose()'s



// update & render --------------------------------------------------------------------------------------------
	@Override
	public void render(float delta) {
		
		if(!paused) {
			// update the states
			headClerk.update();
		}
		painter.render();
	}	// render()'s
	
	public void startArena() {
		
		manager.startArena();
	}


// un/pause -----------------------------------------------------------------------------------------------
	@Override
	public void pause() {
		
		paused = true;
	}	// pause()'s


	@Override
	public void resume() {

		paused = false;
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
