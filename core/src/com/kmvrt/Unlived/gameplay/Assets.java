package com.kmvrt.Unlived.gameplay;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import java.util.HashMap;
import java.util.ArrayList;

public class Assets {

	private static final String TAG = Assets.class.getName();
	
	private static boolean initialised = false; // flag
	public static Assets ins;

	public TextureAtlas mapImgs;
	public Sprite roomSprite; // room image
	public Sprite doorVSprite;	// vertical door
	public Sprite doorHSprite;	// horizontal
	public Sprite portalSprite;	// portal

	public Sprite shadowSprite;	// shadow
//	public Animation shadowAnim;

	// characters
	public ArrayList<TextureAtlas> charImgs;	
	public HashMap<String, Sprite> charSprites;
	public HashMap<String, Animation> charAnims;
	
	// magics
	public ArrayList<TextureAtlas> magicImgs;
	public HashMap<String, Sprite> magicSprites;
	public HashMap<String, Animation> magicAnims;

	public BitmapFont font; // default font


// constructor ------------------------------------------------------------------------------------------------
	private Assets() {}	// make this a static class


// initialise and dispose resources ---------------------------------------------------------------------------
	public static void init() {
		// initialise all the assets
	
		if(!initialised) {
			Gdx.app.log(TAG, "Initialising assets...");
			ins = new Assets();
		
			ins.mapImgs = new TextureAtlas("res/map/map.pack");

			ins.roomSprite = new Sprite(ins.mapImgs.findRegion("map"));
			ins.roomSprite.setSize(Constants.ROOM_WIDTH, Constants.ROOM_HEIGHT);
		
			ins.doorVSprite = new Sprite(ins.mapImgs.findRegion("door"));
			ins.doorVSprite.setSize(Constants.ROOMS_INTERVAL + Constants.DOOR_OFFSET, 
					Constants.DOOR_OFFSET + Constants.CHAR_HEIGHT);
		
			ins.doorHSprite = new Sprite(ins.mapImgs.findRegion("door"));
			ins.doorHSprite.setSize(Constants.CHAR_WIDTH + Constants.DOOR_OFFSET, 
					Constants.DOOR_OFFSET + Constants.ROOMS_INTERVAL);
			
			ins.shadowSprite = new Sprite(ins.mapImgs.findRegion("shadow"));
			ins.shadowSprite.setSize(Constants.CHAR_WIDTH, 1 * Manager.UNIT_CONV);
		
			ins.charImgs = new ArrayList<TextureAtlas>();
			ins.charSprites = new HashMap<String, Sprite>();
			ins.charAnims = new HashMap<String, Animation>();
		
			ins.magicImgs = new ArrayList<TextureAtlas>();
			ins.magicSprites = new HashMap<String, Sprite>();
			ins.magicAnims = new HashMap<String, Animation>();

			ins.portalSprite = new Sprite(ins.mapImgs.findRegion("portalImg"));
			ins.portalSprite.setSize(Constants.PORTAL_WIDTH, Constants.PORTAL_HEIGHT);

			FreeTypeFontParameter param = new FreeTypeFontParameter();
			param.size = 10;
			param.color = Color.RED;
			ins.font = new FreeTypeFontGenerator(Gdx.files.internal("res/font/PressStart2P.ttf")) 
				.generateFont(param);
		
			initialised = true;	// flag it
			Gdx.app.log(TAG, "Assets initialised");
			
		} else {
			Gdx.app.error(TAG, "Tried to initialise assets again");
//			Gdx.app.exit();
		}
	} // init()'s end

	public static void initChars(ArrayList<GameChar> chars) {
		// initialise the chars
		
		if(initialised) {
			for(TextureAtlas atlas : ins.charImgs) {
				// dispose all images
				atlas.dispose();
			}
			ins.charImgs.clear();		// clear the collections
			ins.charSprites.clear();
			ins.charAnims.clear();

			for(GameChar c : chars) {
				if(ins.charSprites.containsKey(c.getName())) {
					// don't initialise the on that already been initialised
					continue;
				}
			
				// atlas
				TextureAtlas cImgs = new TextureAtlas(
						"res/chars/" + c.getName() + ".pack");
			
				Array<TextureRegion> charFrames = new Array<TextureRegion>(1);
				for(int i = 1;; i++) {
					TextureRegion frame = cImgs.findRegion(c.getName() + i);
					if(frame == null) {
						// has finished
						break;
					}
				
					try {
						charFrames.add(frame);
					} catch(Exception e) {
						// increase the array size
						charFrames.ensureCapacity(1);
						charFrames.add(frame);
					}
				}
			
				// animation (west facing)
				Animation cAnim = new Animation((float)1 / (charFrames.size * 3), 
						charFrames, Animation.PlayMode.LOOP);

				// sprites
				Sprite cSp = new Sprite(cAnim.getKeyFrame(0));
				cSp.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

				// put them into the collections
				ins.charImgs.add(cImgs);
				ins.charSprites.put(c.getName(), cSp);
				ins.charAnims.put(c.getName(), cAnim);

				// magic sprites
				TextureAtlas mImgs = new TextureAtlas("res/magic/sprites/" + c.getName() + ".pack");
				Array<TextureRegion> magicFrames = new Array<TextureRegion>(1);
				for(int i = 1;; i++) {
					TextureRegion frame = mImgs.findRegion(c.getName() + i);
					if(frame == null) {
						// has finished
						break;
					}
				
					try {
						magicFrames.add(frame);
					} catch(Exception e) {
						// increase the array size
						magicFrames.ensureCapacity(1);
						magicFrames.add(frame);
					}
				}
				Animation mAnim = new Animation(0.1f / magicFrames.size,
						magicFrames, Animation.PlayMode.LOOP);
			
				Sprite mSp = new Sprite(mAnim.getKeyFrame(0));
				mSp.setSize(MagicFactory.getSpell(c.getName()).getWidth(),
						MagicFactory.getSpell(c.getName()).getHeight());
			
				ins.magicImgs.add(mImgs);
				ins.magicAnims.put(c.getName(), mAnim);
				ins.magicSprites.put(c.getName(), mSp);
			
				Gdx.app.log(TAG, "Character " + c.getName() + " initialised");
			}	// char iter's
		
		} else {
			Gdx.app.error(TAG, "Trying to access unitialised assets");
//			Gdx.app.exit();
		}
	}	// initChars(ArrayList<GameChar>)'s

	public static void update(float stateTime) {
		// update the animations

		if(initialised) {
			// chars
			for(String key : ins.charSprites.keySet()) {
				ins.charSprites.get(key).setRegion(
						ins.charAnims.get(key).getKeyFrame(stateTime));
			}
		
			// magics
			for(String key : ins.magicSprites.keySet()) {
				ins.magicSprites.get(key).setRegion(
						ins.magicAnims.get(key).getKeyFrame(stateTime));
			}
			
		} else {
//			Gdx.app.error(TAG, "Trying to update unitialised assets");
		}
	}	// update(float)'s

	public static void dispose() {
		// dispose all the resources 	

		if(initialised) {
			ins.mapImgs.dispose();
			for(TextureAtlas img : ins.charImgs) {
				img.dispose();
			}
			ins.charImgs.clear();
			ins.charSprites.clear();
			ins.charAnims.clear();
			
			for(TextureAtlas img : ins.magicImgs) {
				img.dispose();
			}
			ins.magicImgs.clear();
			ins.magicSprites.clear();
			ins.charAnims.clear();
			
			ins.font.dispose();

			ins = null;
			initialised = false;	// unflag it
			
		} else {
			Gdx.app.error(TAG, "Dispose called before init");
//			Gdx.app.exit();
		}
		
	}	// dispose()'s end

}
