package com.kmvrt.Unlived;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;

public class Painter {

	private StateData data;

	private SpriteBatch batch;	// rendering batch
	private OrthographicCamera cam;	// viewport
	private OrthographicCamera ui; // ui

	private GlyphLayout texts;	// text bounds, etc.


// constructor ------------------------------------------------------------------------------------------------
	public Painter(StateData data) {
	
		this.data = data;

		batch = new SpriteBatch();
		cam = new OrthographicCamera(Constants.CAM_WIDTH, 
			Constants.CAM_HEIGHT);
		ui = new OrthographicCamera(Gdx.graphics.getWidth(),
			Gdx.graphics.getHeight());
		ui.position.set(ui.viewportWidth / 2f, ui.viewportHeight / 2f, 0f);
		ui.update();

		texts = new GlyphLayout();
	}



// render to screen -------------------------------------------------------------------------------------------
	public void render() {
		// render the objects to the screen, based on the data
		
		updateCam();

		// clear the canvas
		Gdx.gl.glClearColor(0, 0, 0, 1); // black
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		// draw map ****************
		Assets.roomImg.draw(batch);
		if(data.isLevelFinished()) {
			// draw the portal
			Assets.portalImg.setPosition(
				data.cRoom.getPortalX(), data.cRoom.getPortalY());
			Assets.portalImg.draw(batch);
		}

		// draw chars **************
		for(GameChar c : data.chars) {
		
			Assets.charImg.setPosition(c.getX(), c.getY());
			Assets.charImg.draw(batch);
		}

		// draw magics *************
		if(data.magics.size() != 0) {
			for(Magic  m : data.magics) {
			
				Assets.magicImg.setPosition(m.getX(), m.getY());
				Assets.magicImg.draw(batch);
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

		// ammo on top-left screen
		// set text to be ammo
		String ammo = "";
		for(int i = 0; i < data.getAmmoLeft(); i++) {
			ammo += 'I';
		}
		texts.setText(Assets.font, ammo);
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
				cam.position.set(c.getX() + (Constants.CHAR_WIDTH / 2), 
					c.getY() + (Constants.CHAR_HEIGHT / 2), 0);
				cam.update();
				return;
			}
		}
		
	}	// updateCam()'s end

}
