package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.Animation;
import java.util.HashMap;
import java.util.ArrayList;

public class Assets {
	// store all the assets to be used in game

	private static final String TAG = Assets.class.getName();
	
	private static boolean initialised = false; // flag

	public static TextureAtlas mapImgs;
	public static Sprite roomSprite; // room image
	public static Sprite portalSprite;	// portal
	public static Sprite magicSprite;	// magic

	public static Sprite shadowSprite;	// shadow
	public static Animation shadowAnim;

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
		
		mapImgs = new TextureAtlas("map/map.pack");

		roomSprite = new Sprite(mapImgs.findRegion("map"));
		roomSprite.setSize(Constants.ROOM_WIDTH, Constants.ROOM_HEIGHT);
		roomSprite.setPosition(0, 0);
		
		charImgs = new ArrayList<TextureAtlas>();
		charSprites = new HashMap<String, Sprite>();
		charAnims = new HashMap<String, Animation>();

		portalSprite = new Sprite(mapImgs.findRegion("portalImg"));
		portalSprite.setSize(Constants.PORTAL_WIDTH, Constants.PORTAL_HEIGHT);

		magicSprite = new Sprite(mapImgs.findRegion("magicImg"));
		magicSprite.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

		shadowAnim = new Animation(Constants.ANIMATION_FRAME_DURATION,
				mapImgs.findRegion("shadow1"), mapImgs.findRegion("shadow2"));
		shadowAnim.setPlayMode(PlayMode.LOOP_PINGPONG);
		shadowSprite = new Sprite(shadowAnim.getKeyFrame(0));
		shadowSprite.setSize(Constants.SHADOW_WIDTH, Constants.SHADOW_HEIGHT);

		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 10;
		param.color = Color.BLACK;
		
		font = new FreeTypeFontGenerator(Gdx.files.internal("PressStart2P.ttf")) 
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
			TextureAtlas cImgs = new TextureAtlas("chars/" + c.getName() + ".pack");

			// animation (west facing)
			Animation wAnim = new Animation(Constants.ANIMATION_FRAME_DURATION,
					cImgs.findRegion(c.getName() + "W1"),
					cImgs.findRegion(c.getName() + "W2"), 
					cImgs.findRegion(c.getName() + "W3"),
					cImgs.findRegion(c.getName() + "W4"));
			wAnim.setPlayMode(PlayMode.LOOP_PINGPONG);

			// (east facing)
			Animation eAnim = new Animation(Constants.ANIMATION_FRAME_DURATION,
					cImgs.findRegion(c.getName() + "E1"),
					cImgs.findRegion(c.getName() + "E2"), 
					cImgs.findRegion(c.getName() + "E3"),
					cImgs.findRegion(c.getName() + "E4"));
			eAnim.setPlayMode(PlayMode.LOOP_PINGPONG);

			// sprites
			Sprite wSp = new Sprite(wAnim.getKeyFrame(0));
			wSp.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);
			Sprite eSp = new Sprite(eAnim.getKeyFrame(0));
			eSp.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

			// put them into the collections
			charImgs.add(cImgs);
			charSprites.put(c.getName() + "W", wSp);
			charSprites.put(c.getName() + "E", eSp);
			charAnims.put(c.getName() + "W", wAnim);
			charAnims.put(c.getName() + "E", eAnim);

			Gdx.app.log(TAG, "Character " + c.getName() + " initialised");
		}
	}	// initChars(ArrayList<GameChar>)'s

	public static void update(float stateTime) {
		// update the animations

		// shadow
		shadowSprite.setRegion(shadowAnim.getKeyFrame(stateTime));

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
