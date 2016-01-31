package com.kmvrt.Unlived.wardrobe;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

class Painter {

	private static final String TAG = Painter.class.getName();
	
	private StateData data;

	private OrthographicCamera cam;
	private SpriteBatch batch;

// constructor -----------------------------------------------------------------------------------------------
	public Painter(StateData data) {
	
		this.data = data;

		cam = new OrthographicCamera(
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(cam.viewportWidth / 2, 
				cam.viewportHeight / 2, 0);
		cam.update();

		batch = new SpriteBatch();
	}



// render the game --------------------------------------------------------------------------------------------
	public void render() {
	
		// clear the canvas
		Gdx.gl.glClearColor(0, 0, 0, 1);	// black
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		// draw the box
		Assets.ins.boxSprite.setPosition(
				(cam.viewportWidth - Assets.ins.boxSprite.getWidth()) / 2,
				(cam.viewportHeight - Assets.ins.boxSprite.getHeight()) / 2);
		Assets.ins.boxSprite.draw(batch);

		// draw the selected char
//		Gdx.app.debug(TAG, "selChar = " + data.selChar);
//		Gdx.app.debug(TAG, "selCostume = " + data.selCostume);
//		Gdx.app.debug(TAG, "pointer = " + data.pointer);
		Sprite selC = Assets.ins.charSprites.get(data.selChar + data.selCostume);
		if(selC == null) {
			Gdx.app.error(TAG, "selC is null name:" + data.selChar + data.selCostume);
		}
		selC.setPosition((cam.viewportWidth - selC.getWidth()) / 2,
				(cam.viewportHeight - selC.getHeight()) / 2);
		selC.draw(batch);

		batch.end();
	}



// resize -------------------------------------------------------------------------------------------------------
	public void resize(int width, int height) {
	
		Assets.resize();
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.position.set(width / 2, height / 2, 0);
		cam.update();
	}
}
