package com.kmvrt.Unlived.menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.BitmapFont;
//import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.GL10;
//import com.badlogic.gdx.utils.Align;
import com.kmvrt.Unlived.*;

class Painter {

//	private static final String TAG = Painter.class.getName();
	
	private StateData data;
//	private Menu menu;

	private OrthographicCamera cam;
	private SpriteBatch batch;
//	private GlyphLayout texts;
	
	private boolean showPointer;
	private float timerPointer;

	private final int textOffsetY = -15;
	private final int gapY = 20;
	private final float textX = 5;

	

// constructor -----------------------------------------------------------------------------------------------
	public Painter(StateData data) {
	
		this.data = data;
//		this.menu = menu;

		cam = new OrthographicCamera(
				Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(cam.viewportWidth / 2f, 
				cam.viewportHeight / 2f, 0);
		cam.update();

		batch = new SpriteBatch();
//		texts = new GlyphLayout();
		
		timerPointer = 0;
		showPointer = true;
	}

	

// render the menu -------------------------------------------------------------------------------------------
	public void render() {

		if(!Assets.isInitialised()) {
			return;
		}

		// clear the canvas
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		renderLabel();
		for(int i = 0; i < data.cOptions.length - 1; i++) {
			renderOption(i);
		}

		batch.end();
	}

	private void renderLabel() {
		// draw the label

		String label = Assets.ins.labels.get(
				data.cOptions[data.cOptions.length - 1]);
		BitmapFont.TextBounds textBound = Assets.ins.font.getBounds(label);
		Assets.ins.font.drawWrapped(batch, label, 
				5, cam.viewportHeight - textBound.height / 2,
				cam.viewportWidth / 2);
	}

	private void renderOption(final int i) {
		// draw the button labels

		// set the text to the option's text
		if(i == data.pointer) {
			Assets.ins.font.setColor(Color.BLACK);
		} else {
			Assets.ins.font.setColor(Color.WHITE);
		}
		String text = Assets.ins.labels.get(data.cOptions[i]);
		BitmapFont.TextBounds textBound =
				Assets.ins.font.getBounds(text);

		final float OPT_ORIGIN_Y = textBound.height * (data.cOptions.length - 1)
					+ gapY * (data.cOptions.length - 2) - textOffsetY * 2;
		final float textY = OPT_ORIGIN_Y - (textBound.height * (i + 1))
					- (gapY * i);

		updatePointer();
		if(i == data.pointer) {
			renderPointer(textY, textBound.width, textBound.height);
		}
		
		Assets.ins.font.draw(batch, text, textX, textY);
			// draw the text
		Assets.ins.font.setColor(Color.WHITE);
			// reset the colour

		if(data.cOptions == Assets.ins.optionsSetting) {
			renderSetting(i, textY, textBound.height);
		}
	}

	private void updatePointer() {
		// un/show pointer every 0.3 sec

		timerPointer += Gdx.graphics.getDeltaTime();
		if(timerPointer > 0.3f) {
			showPointer = !showPointer;
			timerPointer -= 0.3f;
		}
	}

	private void renderPointer(final float textY, final float textsWidth, 
			final float textsHeight) {
		// render the pointer

		if(!showPointer) {
			return;
		}
		
		final float extra = 4;
		final float width = textsWidth + extra;
		final float height = textsHeight + extra;
		
		// draw the pointer
		Assets.ins.pntrSprite.setSize(width, height);
		Assets.ins.pntrSprite.setPosition(
				textX - extra / 2, 
				textY + textOffsetY - extra / 2);
		Assets.ins.pntrSprite.draw(batch);	
	}

	private void renderSetting(final int i, final float textY, 
			final float height) {
		// draw the currently applied settings

		int p = data.cOptions[i];
		if(p == Constants.OPT_RES) {
			String text = 
					Gdx.graphics.getWidth() + "x" + Gdx.graphics.getHeight();
			Assets.ins.font.draw(batch, text, cam.viewportWidth / 2, textY);

		} else if(p == Constants.OPT_VOL) {
			Assets.ins.pntrSprite.setPosition(
					cam.viewportWidth / 2, textY + textOffsetY);
			Assets.ins.pntrSprite.setSize(
					(cam.viewportWidth / 4) * (Assets.ins.volMusic / 1f), 
					height);
			Assets.ins.pntrSprite.draw(batch);

		} else if(p == Constants.OPT_SND) {
			Assets.ins.pntrSprite.setPosition(
					cam.viewportWidth / 2, textY + textOffsetY);
			Assets.ins.pntrSprite.setSize(
					(cam.viewportWidth / 4) * (Assets.ins.volSound / 1f), 
					height);
			Assets.ins.pntrSprite.draw(batch);

		} else if(p == Constants.OPT_F11) {
			String sw = Gdx.graphics.isFullscreen() ? "ON" : "OFF";
			Assets.ins.font.draw(batch, sw, cam.viewportWidth / 2, textY);
					
		} else if(p == Constants.OPT_VSNC) {
			String sw = data.vSync ? "ON" : "OFF";
			Assets.ins.font.draw(batch, sw, cam.viewportWidth / 2, textY);
					
		}
	}



// resize ---------------------------------------------------------------------------------------------------
	public void resize(int width, int height) {
	
		cam.viewportWidth = width;
		cam.viewportHeight = height;
		cam.position.set(width / 2, height / 2, 0);
		cam.update();
	}

} // public class'
