package com.kmvrt.Unlived;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;

public class Hub implements Screen {

	// logging tag
	private static final String TAG = Hub.class.getName();

	private boolean paused;	// wether the game is paused

	private StateData data; // the game state data

	private Manager manager;
	
	// the game renderer, map and sprite manager
	private Painter painter; 	
	private Navigator navigator;
	private Council council;


// constructor ---------------------------------------------------------------------------------------------
	public Hub(Manager manager) {
	
		Gdx.app.log(TAG, "Creating Hub...");

		this.manager = manager;
		
		paused = false;

		data = new StateData(Constants.STATE_HUB);

		painter = new Painter(data);
		navigator = new Navigator(data);
		council = new Council(data);

		Gdx.app.log(TAG, "Hub created");
	}


// when this selected/unselected as the current screen -----------------------------------------------------
	@Override
	public void show() {
		
		Gdx.app.log(TAG, "Selected as the current screen");
		initNewGame();
		Gdx.app.log(TAG, "New game initialised");
	}

	// initialise new game
	private void initNewGame() {
	

		navigator.initNewGame();
		council.initNewGame();
	}

	@Override
	public void hide() {

		Gdx.app.log(TAG, "Unselected as the current screen");
	}


// update and render the game ------------------------------------------------------------------------------
	@Override
	public void render(float delta) {

		if(!paused) {
			update();
			painter.render();	// render game to the screen
		}
	}

	private void update() {
	
		council.update();
		navigator.update();

		if(data.switchScreen) {
			Gdx.app.log(TAG, "START THE ARENA");
			manager.startArena();
		}
	}


// toggle paused -------------------------------------------------------------------------------------------
	@Override
	public void pause() {
		
		Gdx.app.log(TAG, "Pausing...");
		paused = true;
	}

	@Override
	public void resume() {

		Gdx.app.log(TAG, "Resuming...");
		paused = false;
	}


// unused(yet) ---------------------------------------------------------------------------------------------
	@Override
	public void dispose() {

	}

	@Override
	public void resize(int width, int height) {

	}

}
