package com.kmvrt.Unlived.gameplay;

import com.kmvrt.Unlived.*;
import java.util.Iterator;

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
	private int mapCache;

	private OrthographicCamera cam;	// viewport
	private OrthographicCamera ui; // ui

	private GlyphLayout texts;	// text bounds, etc.
	
	private float stateTime;


// constructor ------------------------------------------------------------------------------------------------
	public Painter(StateData data) {
	
		this.data = data;

		batch = new SpriteBatch();
		cacheBatch = new SpriteCache();

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
		if(data.newMap || data.map.justChanged) {
			data.newMap = false;
			data.map.justChanged = false;
			updateMap();
		}
		Assets.update(stateTime);
		updateCam();

		// clear the canvas
		Gdx.gl.glClearColor(0, 0, 0, 1); // black
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		cacheBatch.setProjectionMatrix(cam.combined);
		cacheBatch.begin();
		// draw map ****************
		cacheBatch.draw(mapCache);
		cacheBatch.end();
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();

		if(data.isLevelFinished()) {
			// draw the portal
			Assets.portalSprite.setPosition(
				data.map.getPortalPosX(), data.map.getPortalPosY());
			Assets.portalSprite.draw(batch);
		}

		// draw chars **************
		for(GameChar c : data.chars) {		
			
/*			// draw the shadow
			Assets.shadowSprite.setPosition(c.x, c.y + Constants.SHADOW_OFFSET_Y);
			Assets.shadowSprite.draw(batch); */
			
			Sprite cSprite = Assets.charSprites.get(c.getName());
			cSprite.setPosition(c.x, c.y);
			if(c.getDir() == Constants.DIR_W) {
				cSprite.draw(batch);
			} else {
				cSprite.flip(true, false);
				cSprite.draw(batch);
				cSprite.flip(true, false);
			}
			
			// render the mana percentage
			if(c.cRoom == data.getMainChar().cRoom) {
				texts.setText(Assets.font, (int)c.atts.getMana() + "%");
				Assets.font.draw(batch, texts, 
						(c.x + ((Constants.CHAR_WIDTH - texts.width) / 2)),
						c.y + Constants.CHAR_HEIGHT + 10);
			}
		}

		// draw magics *************
		if(data.magics.size() != 0) {
			for(Iterator<Magic> iter = data.magics.iterator(); iter.hasNext();) {
				Magic m = iter.next();
				
				if(m.cRoom == null) {
					iter.remove();
				} else if(m.cRoom.isVisited()) {
					Sprite mSprite = Assets.magicSprites.get(m.getSpell().getName());
					mSprite.setPosition(m.x, m.y);
					mSprite.draw(batch);
				}
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
	
	private void updateMap() {
		
		cacheBatch.clear();
		cacheBatch.beginCache();
		
		for(int y = 0; y < Constants.ROOMS_NUM_Y; y++) {
			for(int x = 0; x < Constants.ROOMS_NUM_X; x++) {
				Room cRoom = data.map.getRoom(x, y);
				
				if(cRoom == null || !cRoom.isVisited()) {
					// only show visited room
					continue;
				}
				
				float roomX = (x * Constants.ROOM_WIDTH) 
						+ (x * Constants.ROOMS_INTERVAL);
				float roomY = (y * Constants.ROOM_HEIGHT) 
						+ (y * Constants.ROOMS_INTERVAL);
				Assets.roomSprite.setPosition(roomX, roomY);
				cacheBatch.add(Assets.roomSprite);
				
				if(cRoom.east) {
					// create door in the east wall
					Assets.doorVSprite.setPosition(
						Navigator.getDoorPosX(roomX, Constants.DIR_E) - 2, 
						Navigator.getDoorPosY(roomY, Constants.DIR_E));
					cacheBatch.add(Assets.doorVSprite);
				}
				if(cRoom.west) {
					// west wall
					Assets.doorVSprite.setPosition(
						Navigator.getDoorPosX(roomX, Constants.DIR_W) + 2, 
						Navigator.getDoorPosY(roomY, Constants.DIR_W));
					cacheBatch.add(Assets.doorVSprite);
				}
				if(cRoom.north) {
					// north wall
					Assets.doorHSprite.setPosition(
							Navigator.getDoorPosX(roomX, Constants.DIR_N),
							Navigator.getDoorPosY(roomY, Constants.DIR_N) - 2);
					cacheBatch.add(Assets.doorHSprite);
				}
				if(cRoom.south) {
					// south wall
					Assets.doorHSprite.setPosition(
							Navigator.getDoorPosX(roomX, Constants.DIR_S),
							Navigator.getDoorPosY(roomY, Constants.DIR_S) + 2);
					cacheBatch.add(Assets.doorHSprite);

				}
			}
		}
		
		mapCache = cacheBatch.endCache();
	}

}
