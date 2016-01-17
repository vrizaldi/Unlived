package com.kmvrt.Unlived.gameplay;

import com.kmvrt.Unlived.*;
import java.util.Iterator;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteCache;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class Painter {
	// render in-game sprites on-screen

	private StateData data;

	private SpriteBatch batch;	// rendering batch

	private SpriteCache cacheBatch;
	private int mapCache;

	private OrthographicCamera cam;	// viewport
	private OrthographicCamera ui; // ui

//	private GlyphLayout texts;	// text bounds, etc.
	
	private float stateTime;

	private final int textOffsetY = -15;
	private float statHeight;
	private float statY;


// constructor ------------------------------------------------------------------------------------------------
	public Painter(StateData data) {
	
		this.data = data;

		batch = new SpriteBatch();
		cacheBatch = new SpriteCache();

		cam = new OrthographicCamera(Constants.ins.CAM_WIDTH, 
			Constants.ins.CAM_HEIGHT);
		ui = new OrthographicCamera(Gdx.graphics.getWidth(),
			Gdx.graphics.getHeight());
		ui.position.set(ui.viewportWidth / 2f, ui.viewportHeight / 2f, 0f);
		ui.update();

		statHeight = ui.viewportHeight * 0.1f;
		statY = ui.viewportHeight - statHeight;

//		texts = new GlyphLayout();
		
		stateTime = 0;
	}



// update the assets ------------------------------------------------------------------------------------------
	private void update() {
		// update the stuff

		if(!data.slowMo) {
			stateTime += Gdx.graphics.getDeltaTime();
		} else {
			stateTime += Gdx.graphics.getDeltaTime() * Constants.SLOWMO_RATIO;
		}
		if(data.newMap || data.map.justChanged) {
			data.newMap = false;
			data.map.justChanged = false;
			updateMap();
		}
		if(!data.justResumed) {
			Assets.update(stateTime);
		}
		updateCam();
	}	// update()'s

	public void updateCam() {
		// update the camera position
		// mainChar must always be in the middle
		
		for(GameChar c : data.chars) {
		
			if(c.getID() == Constants.CHAR_MAIN) {
//				Gdx.app.log("Painter", "mainChar found");
				float distX = ((c.x + Constants.ins.CHAR_WIDTH / 2) 
						- cam.position.x) * 0.3f;
				float distY = ((c.y + Constants.ins.CHAR_WIDTH / 2) 
						- cam.position.y) * 0.3f;
				cam.translate(distX, distY, 0);
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
				
				float roomX = (x * Constants.ins.ROOM_WIDTH) 
						+ (x * Constants.ins.ROOMS_INTERVAL);
				float roomY = (y * Constants.ins.ROOM_HEIGHT) 
						+ (y * Constants.ins.ROOMS_INTERVAL);
				Assets.ins.roomSprite.setPosition(roomX, roomY);
				cacheBatch.add(Assets.ins.roomSprite);
				
				if(cRoom.east) {
					// create door in the east wall
					Assets.ins.doorVSprite.setPosition(
						Navigator.getDoorPosX(roomX, Constants.DIR_E) - 2, 
						Navigator.getDoorPosY(roomY, Constants.DIR_E));
					cacheBatch.add(Assets.ins.doorVSprite);
				}
				if(cRoom.west) {
					// west wall
					Assets.ins.doorVSprite.setPosition(
						Navigator.getDoorPosX(roomX, Constants.DIR_W) + 2, 
						Navigator.getDoorPosY(roomY, Constants.DIR_W));
					cacheBatch.add(Assets.ins.doorVSprite);
				}
				if(cRoom.north) {
					// north wall
					Assets.ins.doorHSprite.setPosition(
							Navigator.getDoorPosX(roomX, Constants.DIR_N),
							Navigator.getDoorPosY(roomY, Constants.DIR_N) - 2);
					cacheBatch.add(Assets.ins.doorHSprite);
				}
				if(cRoom.south) {
					// south wall
					Assets.ins.doorHSprite.setPosition(
							Navigator.getDoorPosX(roomX, Constants.DIR_S),
							Navigator.getDoorPosY(roomY, Constants.DIR_S) + 2);
					cacheBatch.add(Assets.ins.doorHSprite);

				}
			}
		}
		
		mapCache = cacheBatch.endCache();
	}



// render to screen -------------------------------------------------------------------------------------------
	public void render() {
		// render the objects to the screen, based on the data
		
		update();
		
		// clear the canvas
		Gdx.gl.glClearColor(0, 0, 0, 1); // black
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

		if(data.beep) {
			// just render the beep
			renderBeep();
			return;
		}

		// render the map
		cacheBatch.setProjectionMatrix(cam.combined);
		cacheBatch.begin();
		cacheBatch.draw(mapCache);
		cacheBatch.end();
		
		batch.setProjectionMatrix(cam.combined);
		batch.begin();
		
		// render the shadow
		Assets.ins.shadowSprite.setPosition(
				cam.position.x - (Constants.ins.CHAR_WIDTH / 2),
				cam.position.y - (Constants.ins.CHAR_HEIGHT / 2));
		Assets.ins.shadowSprite.draw(batch);

		renderChars();	
		renderMagics();

		// draw UIs
		// switch to ui cam
		batch.setProjectionMatrix(ui.combined);
		renderBlackBoxes();
		renderHalo();
		renderStats();
	
		batch.end();
	}

	private void renderBeep() {

		batch.setProjectionMatrix(ui.combined);
		batch.begin();
		Sprite cBeep = Assets.ins.beepSprites[data.beepIter];	
		cBeep.setPosition((ui.viewportWidth - cBeep.getWidth()) / 2,
				(ui.viewportHeight - cBeep.getHeight()) / 2);
		cBeep.draw(batch);
		batch.end();
	}	// renderBeep()'s

	private void renderChars() {

		for(GameChar c : data.chars) {		
			Sprite cSprite = Assets.ins.charSprites.get(c.getName() + c.getCostume());
			cSprite.setPosition(c.x, c.y);
			if(c.getDir() == Constants.DIR_W) {
				cSprite.draw(batch);
			} else {
				cSprite.flip(true, false);
				cSprite.draw(batch);
				cSprite.flip(true, false);
			}
			
			// render the mana percentage
			Assets.ins.font.setColor(Color.BLACK);
			if(c.cRoom == data.getMainChar().cRoom) {
				String text = String.format("%.1f", c.atts.getMana()) + '%';
				BitmapFont.TextBounds textBound = 
						Assets.ins.font.getBounds(text);
				Assets.ins.font.draw(batch, text, 
						(c.x + ((Constants.ins.CHAR_WIDTH - textBound.width) / 2)),
						c.y + Constants.ins.CHAR_HEIGHT - textOffsetY);
			}
			Assets.ins.font.setColor(Color.WHITE);
		}
	}	// renderChar()'s

	private void renderMagics() {

		if(data.magics.size() != 0) {
			for(Iterator<Magic> iter = data.magics.iterator(); iter.hasNext();) {
				Magic m = iter.next();
				
				if(m.cRoom == null) {
					iter.remove();
				} else if(m.cRoom.isVisited()) {
					Sprite mSprite = Assets.ins.magicSprites
							.get(m.getSpell().getName());
					mSprite.setPosition(m.x, m.y);
					mSprite.draw(batch);
				}
			}
		}
	}	// renderMagic()'s

	private void renderBlackBoxes() {
		// render black boxes around the visible screen
		// fix the aspect ratio

		float visibleScrX = (ui.viewportWidth - Constants.ins.VISIBLE_SCR_WIDTH) / 2;
		
		Assets.ins.blackBox.setSize(visibleScrX, ui.viewportHeight);
		Assets.ins.blackBox.setPosition(0, 0);	// blackbox in the west
		Assets.ins.blackBox.draw(batch);
		Assets.ins.blackBox.setPosition(visibleScrX 	// east
				+ Constants.ins.VISIBLE_SCR_WIDTH, 0);
		Assets.ins.blackBox.draw(batch);
		
		// blackbox in the north
		// will be used as the canvas for the current stats
		Assets.ins.blackBox.setSize(ui.viewportWidth, statHeight);
		Assets.ins.blackBox.setPosition(0, statY);	
		Assets.ins.blackBox.draw(batch);
	}	// renderBlackBoxes()'s

	private void renderHalo() {
		// draw the halo

		Sprite halo = null;
		if(data.getMainChar().isAbleToShoot()) {
			halo = Assets.ins.haloOn;
		} else {
			halo = Assets.ins.haloOff;
		}
		halo.setPosition(
				(ui.viewportWidth - halo.getWidth()) / 2, 
				statY + (statHeight - halo.getHeight()) / 2);
		halo.draw(batch);
	}	// renderHalo()'s

	private void renderStats() {
		// render all the stats

		// kill/death count on top-right screen
		// set texts to be kill/death
		String text = String.format("%.2f", data.getKillPerDeath()) 
				+ " kills/death";
		BitmapFont.TextBounds textBound = 
				Assets.ins.font.getBounds(text);
		Assets.ins.font.setColor(Color.WHITE);
		Assets.ins.font.draw(batch, text, 
			cam.viewportWidth - textBound.width - 5,
			statY + (statHeight / 2));

		// mainChar mana on top left screen
		text = "Mana: " + String.format("%.2f", data.getMainChar()
				.atts.getMana());
		textBound = Assets.ins.font.getBounds(text);
		Assets.ins.font.setColor(Color.WHITE);
		Assets.ins.font.draw(batch, text, 
				5, statY + (statHeight / 2));		
	}	// renderStats()'s
	
	public void resize() {
		Assets.resize();
		Assets.update(stateTime);
		cam.viewportWidth = Constants.ins.CAM_WIDTH;
		cam.viewportHeight = Constants.ins.CAM_HEIGHT;
		ui.viewportWidth = Constants.ins.CAM_WIDTH;
		ui.viewportHeight = Constants.ins.CAM_HEIGHT;
		ui.position.set(ui.viewportWidth / 2, ui.viewportHeight / 2, 0);
		cam.update();
		ui.update();
		updateMap();

		statHeight = ui.viewportHeight * 0.1f;
		statY = ui.viewportHeight - statHeight;
	}
}
