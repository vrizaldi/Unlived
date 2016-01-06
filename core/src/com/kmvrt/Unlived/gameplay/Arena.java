// code by Muhammad Noorghifari

package com.kmvrt.Unlived.gameplay;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Timer;

public class Arena implements Screen {
	// the main gameplay screen

	// logging tag
	private static final String TAG = Arena.class.getName();
	
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
	public Arena(Manager manager, StateData data) {
	
		Gdx.app.log(TAG, "Creating Arena...");

		this.data = data;

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
	}

	public void initNewGame(boolean initProps, boolean initMainChar) {
		// initialise a new game
		
		Assets.init();	// init assets
			
		// the order is important: navigator before council
		navigator.initNewGame();
		council.initNewGame(initMainChar);
		chemist.initNewGame();
		if(initProps) {
			data.initKill();
			data.initDeath();
		}
		
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
						data.beep = true;
							// render the beep
						Timer.schedule(
							new Timer.Task() {
							
								@Override
								public void run() {
									data.beep = false;
									data.beepIter = (data.beepIter + 1) % 3;
								}
							}, 0.2f);
					}
				}, 1, 1, 2);

		// time the slowmo
		data.slowMo = false;
	}
	
	@Override
	public void hide() {
		Gdx.app.log(TAG, "Unselected from being the current screen");
	}

	@Override
	public void dispose() {
		Gdx.app.log(TAG, "Disposing assets...");
		Assets.dispose();
		Timer.instance().clear();
		WorldTimer.clear();
	}


	// updates and renders -----------------------------------------------------------------------------------
	@Override
	public void render(float delta) {

		update();
		painter.render();	// render game to the screen
	}

	private void update() {
		// update the game state
		
		// restart
		if(data.switchLevel) {
			data.switchLevel = false;
			dispose();
			initNewGame(false, false);
			
		} else if(data.gameOver) {
			data.gameOver = false;
			dispose();
			initNewGame(false, true);
			
		} else if(Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			manager.pauseArena();
			
		}/*else if(Gdx.input.isKeyJustPressed(Keys.R)) {
			dispose();
			initNewGame(true, false);
			
		} */
		
		// the order is important: council before navigator
		float delta = Gdx.graphics.getDeltaTime();
		if(data.slowMo) {
			delta *= Constants.SLOWMO_RATIO;
		}
		WorldTimer.update(delta);
		navigator.update();
		if(!data.paused
				&& !data.justResumed)
		council.update();
		chemist.update();
		clock.update();

		// if gameover
/*		if(data.switchScreen) {
			Gdx.app.log(TAG, "GAMEOVER");
			manager.gameOver(data);
		}*/
	}


	// toggle paused -----------------------------------------------------------------------------------------
	@Override
	public void pause() {

		Gdx.app.log(TAG, "Pausing...");
		manager.pauseArena();
	}

	@Override
	public void resume() {

//		Gdx.app.log(TAG, "Resuming...");
//		paused = false;
	}


	// unused(yet) -------------------------------------------------------------------------------------------
	@Override
	public void resize(int width, int height) {
//		Constants.ins.CAM_WIDTH = width;
//		Constants.ins.CAM_HEIGHT = height;
		int oldUnitConv = manager.resize(height);
//		Gdx.app.debug(TAG, "old unit conv = " + oldUnitConv);
		float unitConvRatio = (float)Constants.ins.UNIT_CONV / oldUnitConv;
			// the ratio of the new unit to the old unit
		painter.resize();
		
		// convert the old coor to the new one
		// if they're different
		if(unitConvRatio != 1) {
			for(GameChar c : data.chars) {
				Gdx.app.debug(TAG, "old c.x = " + c.x);
				c.x *= unitConvRatio;
				Gdx.app.debug(TAG, "new c.x = " + c.x);
				c.y *= unitConvRatio;
			}
			for(Magic m : data.magics) {
				m.x *= unitConvRatio;
				m.y *= unitConvRatio;
			}
		}
		
//		data.newMap = true;
	}
	
}
