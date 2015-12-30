package com.kmvrt.Unlived.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL20;
import com.kmvrt.Unlived.*;

public class Painter {

//	private static final String TAG = Painter.class.getName();
	
	private StateData data;
//	private Menu menu;

	public OrthographicCamera cam;
	private SpriteBatch batch;
	private GlyphLayout texts;
	
	private boolean showPointer;
	private float timerPointer;

	

// constructor -----------------------------------------------------------------------------------------------
	public Painter(StateData data) {
	
		this.data = data;
//		this.menu = menu;

		cam = new OrthographicCamera(
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
		cam.update();

		batch = new SpriteBatch();
		texts = new GlyphLayout();
		
		timerPointer = 0;
		showPointer = true;
	}

	

// render the menu -------------------------------------------------------------------------------------------
	public void render() {

		if(!Assets.isInitialised()) {
			return;
		}
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		// clear the canvas
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	
		for(int i = 0; i < data.cOptions.length; i++) {
			// draw the button labels
			BitmapFont font;
			if(i == data.pointer) {
				font = Assets.ins.fontSel;
			} else {
				font = Assets.ins.fontNorm;
			}
			int p = data.cOptions[i];
			texts.setText(font, 
					Assets.ins.labels.get(p));
			
			int gapY = 20;
			int offsetY = -15;
			float OPT_ORIGIN_Y = texts.height * data.cOptions.length
					+ gapY * (data.cOptions.length - 1) - offsetY * 2;
			float extra = 4;
			
			float textX = 5;
			float textY = OPT_ORIGIN_Y - (texts.height * (i + 1))
					- (gapY * i);
			float width = texts.width + extra;
			float height = texts.height + extra;

			// un/show pointer every 0.3 sec
			timerPointer += Gdx.graphics.getDeltaTime();
			if(timerPointer > 0.5f) {
				showPointer = !showPointer;
				timerPointer -= 0.3f;
			}
			if(i == data.pointer && showPointer) {
				// draw the pointer
				Assets.ins.pntrSprite.setSize(width, height);
				Assets.ins.pntrSprite.setPosition(
						textX - extra / 2, 
						textY + offsetY - extra / 2);
				Assets.ins.pntrSprite.draw(batch);	
			}
			font.draw(batch, texts, textX, textY);
				// draw the text
			
			if(data.cOptions == Assets.ins.optionsSetting) {
				// draw the currently applied settings
				if(p == Constants.OPT_RES) {
					texts.setText(Assets.ins.fontNorm, 
							Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight());
					Assets.ins.fontNorm.draw(batch, texts, cam.viewportWidth / 2, textY);

				} else if(p == Constants.OPT_VOL) {
					Assets.ins.pntrSprite.setPosition(
							cam.viewportWidth / 2, textY + offsetY);
					Assets.ins.pntrSprite.setSize(
							(cam.viewportWidth / 4) * (Assets.ins.volMusic / 1f), 
							height);
					Assets.ins.pntrSprite.draw(batch);

				} else if(p == Constants.OPT_SND) {
					Assets.ins.pntrSprite.setPosition(
							cam.viewportWidth / 2, textY + offsetY);
					Assets.ins.pntrSprite.setSize(
							(cam.viewportWidth / 4) * (Assets.ins.volSound / 1f), 
							height);
					Assets.ins.pntrSprite.draw(batch);

				} else if(p == Constants.OPT_F11) {
					String sw = Gdx.graphics.isFullscreen() ? "ON" : "OFF";
					texts.setText(Assets.ins.fontNorm, sw);
					Assets.ins.fontNorm.draw(batch, texts, cam.viewportWidth / 2, textY);
					
				} else if(p == Constants.OPT_VSNC) {
					String sw = data.vSync ? "ON" : "OFF";
					texts.setText(Assets.ins.fontNorm, sw);
					Assets.ins.fontNorm.draw(batch, texts, cam.viewportWidth / 2, textY);
					
				}
			}
		}
		batch.end();
	}
} // public class'
