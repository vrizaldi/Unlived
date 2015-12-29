package com.kmvrt.Unlived.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.kmvrt.Unlived.Constants;

public class Painter {

	private Courier courier;
	private Menu menu;

	public OrthographicCamera cam;
	private SpriteBatch batch;
	private GlyphLayout texts;


// constructor -----------------------------------------------------------------------------------------------
	public Painter(Menu menu, Courier courier) {
	
		this.courier = courier;
		this.menu = menu;

		cam = new OrthographicCamera(
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		batch = new SpriteBatch();
		texts = new GlyphLayout();
	}

	

// render the menu -------------------------------------------------------------------------------------------
	public void render() {
	
		// clear the canvas
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		for(int i = 0; i < courier.cOptions.length; i++) {
			// draw the button labels
			BitmapFont font;
			if(i == courier.pointer) {
				font = Assets.fontSel;
			} else {
				font = Assets.fontNorm;
			}
			int p = courier.cOptions[i];
			texts.setText(font, 
					Assets.labels.get(p));
			
			int gapY = 20;
			int offsetY = -15;
			float OPT_ORIGIN_Y = texts.height * courier.cOptions.length
					+ gapY * (courier.cOptions.length - 1) - offsetY * 2;
			float extra = 4;
			
			float textX = 5;
			float textY = OPT_ORIGIN_Y - (texts.height * (i + 1))
					- (gapY * i);
			float width = texts.width + extra;
			float height = texts.height + extra;
			
			if(i == courier.pointer) {
				// draw the pointer
				
				Assets.pntrSprite.setSize(width, height);
				Assets.pntrSprite.setPosition(
						textX - extra / 2, 
						textY + offsetY - extra / 2);
				Assets.pntrSprite.draw(batch);
			}

			
			font.draw(batch, texts, textX, textY);
			
			if(courier.cOptions == Assets.optionsSetting) {
				// draw the chosen settings
				if(p == Constants.OPT_RES) {
					texts.setText(Assets.fontNorm, 
							Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
					Assets.fontNorm.draw(batch, texts, cam.viewportWidth / 2, textY);
				} else if(p == Constants.OPT_VOL) {
					Assets.pntrSprite.setPosition(
							cam.viewportWidth / 2, textY + offsetY);
					Assets.pntrSprite.setSize(
							(cam.viewportWidth / 4) * (Assets.volMusic / 1f), 
							height);
					Assets.pntrSprite.draw(batch);
				} else if(p == Constants.OPT_SND) {
					Assets.pntrSprite.setPosition(
							cam.viewportWidth / 2, textY + offsetY);
					Assets.pntrSprite.setSize(
							(cam.viewportWidth / 4) * (Assets.volSound / 1f), 
							height);
					Assets.pntrSprite.draw(batch);
				}
				
			}
		}
		batch.end();
	}

} // public class'
