package com.kmvrt.Unlived;

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
	// store all the assets to be used in game

	private static final String TAG = Assets.class.getName();
	
	private static boolean initialised = false; // flag

	public static TextureAtlas mapImgs;
	public static Sprite roomSprite; // room image
	public static Sprite doorVSprite;	// vertical door
	public static Sprite doorHSprite;	// horizontal
	public static Sprite portalSprite;	// portal
	public static Sprite magicSprite;	// magic

//	public static Sprite shadowSprite;	// shadow
//	public static Animation shadowAnim;

	public static ArrayList<TextureAtlas> charImgs;	// characters
	public static HashMap<String, Sprite> charSprites;
	public static HashMap<String, Animation> charAnims;

	public static BitmapFont font; // default font


// constructor ------------------------------------------------------------------------------------------------
	private Assets() {}	// make this a static class


// initialise and dispose resources ---------------------------------------------------------------------------
	public static void init() {
		// initialise all the assets
	
		Gdx.app.log(TAG, "Initialising assets...");
		
		mapImgs = new TextureAtlas("data/map/map.pack");

		roomSprite = new Sprite(mapImgs.findRegion("map"));
		roomSprite.setSize(Constants.ROOM_WIDTH, Constants.ROOM_HEIGHT);
		
		doorVSprite = new Sprite(mapImgs.findRegion("door"));
		doorVSprite.setSize(Constants.ROOMS_INTERVAL + Constants.DOOR_OFFSET, 
				Constants.DOOR_OFFSET + Constants.CHAR_HEIGHT);
		
		doorHSprite = new Sprite(mapImgs.findRegion("door"));
		doorHSprite.setSize(Constants.CHAR_WIDTH + Constants.DOOR_OFFSET, 
				Constants.DOOR_OFFSET + Constants.ROOMS_INTERVAL);
		
		charImgs = new ArrayList<TextureAtlas>();
		charSprites = new HashMap<String, Sprite>();
		charAnims = new HashMap<String, Animation>();

		portalSprite = new Sprite(mapImgs.findRegion("portalImg"));
		portalSprite.setSize(Constants.PORTAL_WIDTH, Constants.PORTAL_HEIGHT);

		magicSprite = new Sprite(mapImgs.findRegion("magicImg"));
		magicSprite.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

/*		shadowAnim = new Animation(Constants.ANIMATION_SHADOW_DURATION,
				mapImgs.findRegion("shadow1"), mapImgs.findRegion("shadow2"));
		shadowAnim.setPlayMode(PlayMode.LOOP_PINGPONG);
		shadowSprite = new Sprite(shadowAnim.getKeyFrame(0));
		shadowSprite.setSize(Constants.SHADOW_WIDTH, Constants.SHADOW_HEIGHT); */

		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 10;
		param.color = Color.RED;
		font = new FreeTypeFontGenerator(Gdx.files.internal("data/font/PressStart2P.ttf")) 
			.generateFont(param);
		
		initialised = true;	// flag it
		
		Gdx.app.log(TAG, "Assets initialised");
	} // init()'s end

	public static void initChars(ArrayList<GameChar> chars) {
		// initialise the chars
		
		for(TextureAtlas atlas : charImgs) {
			// dispose all images
			atlas.dispose();
		}
		charImgs.clear();		// clear the collections
		charSprites.clear();
		charAnims.clear();

		for(GameChar c : chars) {
			if(charSprites.containsKey(c.getName())) {
				// don't initialise the on that already been initialised
				continue;
			}
			
			// atlas
			TextureAtlas cImgs = new TextureAtlas("data/chars/" + c.getName() + ".pack");
			
			Array<TextureRegion> frames = new Array<TextureRegion>(10);
			for(int i = 1;; i++) {
				TextureRegion frame = cImgs.findRegion(c.getName() + i);
				if(frame == null) {
					// has finished
					break;
				}
				
				try {
					frames.add(frame);
				} catch(Exception e) {
					// increase the array size
					frames.ensureCapacity(1);
					frames.add(frame);
				}
			}

			// animation (west facing)
			Animation anim = new Animation((float)1 / (frames.size * 3), 
					frames, Animation.PlayMode.LOOP);

			// sprites
			Sprite wSp = new Sprite(anim.getKeyFrame(0));
			wSp.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

			// put them into the collections
			charImgs.add(cImgs);
			charSprites.put(c.getName(), wSp);
			charAnims.put(c.getName(), anim);

			Gdx.app.log(TAG, "Character " + c.getName() + " initialised");
		}
	}	// initChars(ArrayList<GameChar>)'s

	public static void update(float stateTime) {
		// update the animations

		// shadow
//		shadowSprite.setRegion(shadowAnim.getKeyFrame(stateTime));

		// chars
		for(String key : charSprites.keySet()) {
			charSprites.get(key).setRegion(
					charAnims.get(key).getKeyFrame(stateTime));
		}
	}

	public static void dispose() {
		// dispose all the resources 	

		if(initialised) {
			mapImgs.dispose();

			initialised = false;	// unflag it
		} else {
			Gdx.app.log(TAG, "ERROR: dispose called before init");
			Gdx.app.exit();
		}
	}	// dispose()'s end

}
