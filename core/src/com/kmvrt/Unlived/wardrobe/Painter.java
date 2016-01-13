package com.kmvrt.Unlived.wardrobe;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;

class Painter {

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

		

		batch.end();
	}



// resize -------------------------------------------------------------------------------------------------------
	public void resize(int width, int height) {
	
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.position.set(width / 2, height / 2, 0);
		cam.update();
	}
}
