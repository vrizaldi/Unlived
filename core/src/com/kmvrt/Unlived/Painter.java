package com.kmvrt.Unlived;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Painter {
	// render in-game sprites on-screen

	private StateData data;

	private SpriteBatch batch;	// rendering batch

	private SpriteCache cacheBatch;
	private int roomCache;

	private OrthographicCamera cam;	// viewport
	private OrthographicCamera ui; // ui

	private GlyphLayout texts;	// text bounds, etc.
	
	private float stateTime;


// constructor ------------------------------------------------------------------------------------------------
	public Painter(StateData data) {
	
		this.data = data;

		batch = new SpriteBatch();

		cacheBatch = new SpriteCache();
		cacheBatch.beginCache();
		cacheBatch.add(Assets.roomSprite);
		roomCache = cacheBatch.endCache();

		cam = new OrthographicCamera(Constants.CAM_WIDTH, 
			Constants.CAM_HEIGHT);
		ui = new OrthographicCamera(Gdx.graphics.getWidth(),
			Gdx.graphics.getHeight());
		ui.position.set(ui.viewportWidth / 2f, ui.viewportHeight / 2f, 0f);
		ui.update();

		texts = new GlyphLayout();
		
		stateTime = 0;
	}



// render to screen -------------------------------------------------------------------------------------------
	public void render() {
		// render the objects to the screen, based on the data
		
		stateTime += Gdx.graphics.getDeltaTime();
		Assets.update(stateTime);
		updateCam();

		// clear the canvas
		Gdx.gl.glClearColor(1, 1, 1, 1); // black
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cacheBatch.setProjectionMatrix(cam.combined);
		cacheBatch.begin();
		// draw map ****************
		cacheBatch.draw(roomCache);
		cacheBatch.end();
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		if(data.isLevelFinished()) {
			// draw the portal
			Assets.portalSprite.setPosition(
				data.cRoom.getPortalX(), data.cRoom.getPortalY());
			Assets.portalSprite.draw(batch);
		}

		// draw chars **************
		for(GameChar c : data.chars) {
			// draw the shadow
			Assets.shadowSprite.setPosition(c.x, c.y + Constants.SHADOW_OFFSET_Y);
			Assets.shadowSprite.draw(batch);
			
			char dir = c.getDir() == Constants.DIR_E ? 'E' : 'W';
			Sprite cSprite = Assets.charSprites.get(c.getName() + dir);
			cSprite.setPosition(c.x, c.y);
			cSprite.draw(batch);
		}

		// draw magics *************
		if(data.magics.size() != 0) {
			for(Magic  m : data.magics) {
			
				Assets.magicSprite.setPosition(m.x, m.y);
				Assets.magicSprite.draw(batch);
			}
		}

		// draw UIs
		batch.setProjectionMatrix(ui.combined);
		
		// kill/death count on top-right screen
		// set texts to be kill/death
		texts.setText(Assets.font, String.format("%.2f", data.getKillPerDeath()) 
			+ " kills/death");
		Assets.font.draw(batch, texts, ui.viewportWidth - texts.width,
			ui.viewportHeight - texts.height);

		// mainChar mana on top left screen
		texts.setText(Assets.font, "Mana: " 
				+ String.format("%.2f", data.getMainChar().atts.getMana()));
		Assets.font.draw(batch, texts, 0,
				ui.viewportHeight - texts.height - 0.5f);

		batch.end();
	}

	public void updateCam() {
		// update the camera position
		// mainChar must always be in the middle
		
		for(GameChar c : data.chars) {
		
			if(c.getID() == Constants.CHAR_MAIN) {
//				Gdx.app.log("Painter", "mainChar found");
				cam.position.set(c.x + (Constants.CHAR_WIDTH / 2), 
					c.y + (Constants.CHAR_HEIGHT / 2), 0);
				cam.update();
				return;
			}
		}
		
	}	// updateCam()'s end

}
