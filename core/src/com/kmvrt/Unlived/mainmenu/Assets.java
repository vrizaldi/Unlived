package com.kmvrt.Unlived.mainmenu;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import java.util.HashMap;

public class Assets {
	// store all the assets to be used in menu

	private static final String TAG = Assets.class.getName();
	
	private static boolean initialised = false;
	
	public static TextureAtlas menuImgs;
	public static Sprite pntrSprite;
	
	public static BitmapFont fontNorm;
	public static BitmapFont fontSel;

	public static HashMap<Integer, String> labels;
	public static int[] optionsMenu;
	public static int[] optionsSetting;
	
	public static DisplayMode[] availableRes;
	public static float volMusic;
	public static float volSound;

		
// constructor -----------------------------------------------------------------------------------------------
	private Assets() {}

	public static void init() {

		if(!initialised) {
			Gdx.app.log(TAG, "Initialising assets...");
			optionsMenu = new int[3];
			optionsMenu[0] = Constants.OPT_START;
			optionsMenu[1] = Constants.OPT_SETTING;
			optionsMenu[2] = Constants.OPT_QUIT;

			optionsSetting = new int[4];
			optionsSetting[0] = Constants.OPT_RES;
			optionsSetting[1] = Constants.OPT_VOL;
			optionsSetting[2] = Constants.OPT_SND;
			optionsSetting[3] = Constants.OPT_QUIT;
			
			availableRes = Gdx.graphics.getDisplayModes();
			volMusic = 1f;
			volSound = 1f;
			
			labels = new HashMap<Integer, String>();
			labels.put(Constants.OPT_START, "START");
			labels.put(Constants.OPT_QUIT, "QUIT");
			labels.put(Constants.OPT_SETTING, "SETTING");
			labels.put(Constants.OPT_RES, "RESOLUTION");
			labels.put(Constants.OPT_VOL, "VOLUME");
			labels.put(Constants.OPT_SND, "SOUND");
			
			FreeTypeFontGenerator font = new 
				FreeTypeFontGenerator(Gdx.files.internal("res/font/PressStart2P.ttf"));

			FreeTypeFontParameter pNorm = new FreeTypeFontParameter();
			pNorm.size = 20;
			pNorm.color = Color.WHITE;
			fontNorm = font.generateFont(pNorm);

			FreeTypeFontParameter pSel = new FreeTypeFontParameter(); 
			pSel.size = 20;
			pSel.color = Color.BLACK;
			fontSel = font.generateFont(pSel);

			menuImgs = new TextureAtlas("res/menu/menu.pack");
			pntrSprite = new Sprite(menuImgs.findRegion("pointer"));

			initialised = true;
			Gdx.app.log(TAG, "Assets initialised");
		}
	}

	public static void dispose() {
		
		if(initialised) {
			fontNorm.dispose();
			fontSel.dispose();
		}
	}
}
