package com.kmvrt.Unlived.wardrobe;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class Wardrobe implements Screen {

	public static final String TAG = Wardrobe.class.getName();

	private Manager manager;
	private StateData data;
	private boolean sel = false;
	
	private HeadClerk headClerk;
	private Painter painter;


// constructor -----------------------------------------------------------------------------------------------
	public Wardrobe(Manager manager, StateData data) {

		this.manager = manager;
		this.data = data;
	
		headClerk = new HeadClerk(this, data);
		painter = new Painter(data);
	}



// update and render -----------------------------------------------------------------------------------------
	@Override
	public void render(float delta) {
	
		data.receptionist.pollInput();
		headClerk.update();
		if(sel) { 
			painter.render();
		}
	}
	
	public void ready() {
		
		if(data.gameOver) {
			data.gameOver = false;
			manager.revive();
		} else {
			manager.startArena();
		}
	}
	
	public void toMainMenu() {
		
		data.gameOver = false;
		manager.toMainMenu();
	}



// when un/selected as the active screen ---------------------------------------------------------------------
	@Override
	public void show() {
	
		Gdx.app.log(TAG, "Selected as the active screen");
		Assets.init();
		data.pointer = MagicFactory.getIndex("tiny");
		sel = true;
	}

	@Override
	public void hide() {
	
		Gdx.app.log(TAG, "Unselected from being the active screen");
		sel = false;
		dispose();
	}

	@Override
	public void dispose() {
	
		Assets.dispose();
	}



// resize -------------------------------------------------------------------------------------------------------
	@Override
	public void resize(int width, int height) {
	
		manager.resize();
		painter.resize(width, height);
	} 



// unused ------------------------------------------------------------------------------------------------------
	@Override
	public void pause() {}
	@Override
	public void resume() {}

}
