package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

public class Assets {

	private static final String TAG = Assets.class.getName();
	
	private static boolean initialised = false; // flag

	public static TextureAtlas spriteImgs;
	public static Sprite roomImg; // room image
	public static Sprite portalImg;	// portal
	public static Sprite charImg;	// character
	public static Sprite magicImg;	// magic

	public static BitmapFont font; // default font


// constructor ------------------------------------------------------------------------------------------------
	private Assets() {}	// make this a static class


// initialise and dispose resources ---------------------------------------------------------------------------
	public static void init() {
		// initialise all the assets
	
		spriteImgs = new TextureAtlas("spriteImgs.pack");

		roomImg = new Sprite(spriteImgs.findRegion("roomImg"));
		roomImg.setSize(Constants.ROOM_WIDTH, Constants.ROOM_HEIGHT);
		roomImg.setPosition(0, 0);
		
		portalImg = new Sprite(spriteImgs.findRegion("portalImg"));
		portalImg.setSize(Constants.PORTAL_WIDTH, Constants.PORTAL_HEIGHT);

		charImg = new Sprite(spriteImgs.findRegion("charImg"));
		charImg.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

		magicImg = new Sprite(spriteImgs.findRegion("magicImg"));
		magicImg.setSize(Constants.CHAR_WIDTH, Constants.CHAR_HEIGHT);

		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 10;
		
		font = new FreeTypeFontGenerator(Gdx.files.internal("PressStart2P.ttf")) 
			.generateFont(param);

		initialised = true;	// flag it
	} // init()'s end

	public static void dispose() {
		// dispose all the resources 	

		if(initialised) {
			spriteImgs.dispose();
			spriteImgs = null;
			roomImg = null;
			charImg = null;

			initialised = false;	// unflag it
		} else {
			Gdx.app.log(TAG, "ERROR: dispose called before init");
			Gdx.app.exit();
		}
	}	// dispose()'s end

}
