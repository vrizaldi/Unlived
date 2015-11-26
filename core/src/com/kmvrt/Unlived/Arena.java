// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

public class Arena implements Screen {
	// the main gameplay screen

	// logging tag
	private static final String TAG = Arena.class.getName();

	private boolean paused; // wether the game is paused
	
	private Manager manager;

	// data of current state of the game
	private StateData data;

	// the renderer, map and sprites manager
	private Council council;
	private Painter painter;
	private Navigator navigator;
	private Chemist chemist;
	private Clock clock;


	// constructor -------------------------------------------------------------------------------------------
	public Arena(Manager manager) {
	
		Gdx.app.log(TAG, "Creating Arena...");

		paused = false;

		data = new StateData(Constants.STATE_ARENA);

		this.manager = manager;
		council = new Council(data);
		painter = new Painter(data);
		navigator = new Navigator(data);
		chemist = new Chemist(data);
		clock = new Clock(data);

		Gdx.app.log(TAG, "Arena created");
	}


	// when this selected/unselected as the current screen ---------------------------------------------------
	@Override
	public void show() {

		Gdx.app.log(TAG, "Selected as current screen");
		initNewGame(true);
		Gdx.app.log(TAG, "New game initilised");
	}

	private void initNewGame(boolean initProps) {
		// initialise a new game
		
		// the order is important: navigator before council
		navigator.initNewGame();
		council.initNewGame();
		chemist.initNewGame();
		if(initProps) {
			data.initKill();
			data.initDeath();
		}
	}
	
	@Override
	public void hide() {

		Gdx.app.log(TAG, "Unselected as the current screen");
		disposeGame();
		Gdx.app.log(TAG, "Last game data disposed");
	}

	private void disposeGame() {
		// dispose the resource used
		
		navigator.disposeGame();
		council.disposeGame();
		chemist.disposeGame();
	}


	// updates and renders -----------------------------------------------------------------------------------
	@Override
	public void render(float delta) {

		if(!paused) {
		
			update();
			painter.render();	// render game to the screen
		}
	}

	private void update() {
		// update the game state
		
		// restart
		if(data.gameOver) {
			data.gameOver = false;
			disposeGame();
			initNewGame(false);
		}
		// debug only
		if(Gdx.input.isKeyJustPressed(Keys.R)) {
			disposeGame();
			initNewGame(true);
		}
		
		// the order is important: council before navigator
		council.update();
		navigator.update();
		chemist.update();
		clock.update();

		// if gameover
		if(data.switchScreen) {
			Gdx.app.log(TAG, "GAMEOVER");
			manager.gameOver(data);
		}
	}


	// toggle paused -----------------------------------------------------------------------------------------
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


	// unused(yet) -------------------------------------------------------------------------------------------
	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void dispose() {
	}

}
