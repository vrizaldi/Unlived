package com.kmvrt.Unlived.wardrobe;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

public class Wardrobe implements Screen {

	public static final String TAG = Wardrobe.class.getName();

	private Manager manager;
	private StateData data;
	
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
	
		headClerk.update();
		painter.render();
	}



// when un/selected as the active screen ---------------------------------------------------------------------
	@Override
	public void show() {
	
		Gdx.app.log(TAG, "Selected as the active screen");
		Assets.init();
	}

	@Override
	public void hide() {
	
		Gdx.app.log(TAG, "Unselected from being the active screen");
	}

	@Override
	public void dispose() {
	
		Assets.dispose();
	}



// resize -------------------------------------------------------------------------------------------------------
	@Override
	public void resize(int width, int height) {
	
		painter.resize(width, height);
	} 



// unused ------------------------------------------------------------------------------------------------------
	@Override
	public void pause() {}
	@Override
	public void resume() {}

}
