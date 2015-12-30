package com.kmvrt.Unlived.menu;

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
	public static Assets ins;
	
	public TextureAtlas menuImgs;
	public Sprite pntrSprite;
	public Sprite pauseBGSprite;
	
	public BitmapFont fontNorm;
	public BitmapFont fontSel;

	public HashMap<Integer, String> labels;
	public int[] optionsMenu;
	public int[] optionsSetting;
	public int[] optionsPause;
	
	public DisplayMode[] availableRes;
	public float volMusic;
	public float volSound;

		
// constructor -----------------------------------------------------------------------------------------------
	private Assets() {}

	public static void init() {

		if(!initialised) {
			Gdx.app.log(TAG, "Initialising assets...");
			ins = new Assets();
			
			ins.optionsMenu = new int[3];
			ins.optionsMenu[0] = Constants.OPT_START;
			ins.optionsMenu[1] = Constants.OPT_SETTING;
			ins.optionsMenu[2] = Constants.OPT_QUIT;

			ins.optionsSetting = new int[6];
			ins.optionsSetting[0] = Constants.OPT_RES;
			ins.optionsSetting[1] = Constants.OPT_F11;
			ins.optionsSetting[2] = Constants.OPT_VSNC;
			ins.optionsSetting[3] = Constants.OPT_VOL;
			ins.optionsSetting[4] = Constants.OPT_SND;
			ins.optionsSetting[5] = Constants.OPT_BACK;
			
			ins.optionsPause = new int[4];
			ins.optionsPause[0] = Constants.OPT_RESUME;
			ins.optionsPause[1] = Constants.OPT_SETTING;
			ins.optionsPause[2] = Constants.OPT_TOMENU;
			ins.optionsPause[3] = Constants.OPT_QUIT;
			
			ins.availableRes = Gdx.graphics.getDisplayModes();
			ins.volMusic = 1f;
			ins.volSound = 1f;
			
			ins.labels = new HashMap<Integer, String>();
			ins.labels.put(Constants.OPT_START, "START");
			ins.labels.put(Constants.OPT_QUIT, "QUIT");
			ins.labels.put(Constants.OPT_SETTING, "SETTING");
			ins.labels.put(Constants.OPT_RES, "RESOLUTION");
			ins.labels.put(Constants.OPT_VOL, "VOLUME");
			ins.labels.put(Constants.OPT_SND, "SOUND");
			ins.labels.put(Constants.OPT_F11, "FULLSCREEN");
			ins.labels.put(Constants.OPT_VSNC, "V-SYNC");
			ins.labels.put(Constants.OPT_TOMENU, "BACK TO MAINMENU");
			ins.labels.put(Constants.OPT_RESUME, "RESUME");
			ins.labels.put(Constants.OPT_BACK, "BACK");
			
			FreeTypeFontGenerator font = new 
				FreeTypeFontGenerator(Gdx.files.internal("res/font/PressStart2P.ttf"));

			FreeTypeFontParameter pNorm = new FreeTypeFontParameter();
			pNorm.size = 20;
			pNorm.color = Color.WHITE;
			ins.fontNorm = font.generateFont(pNorm);

			FreeTypeFontParameter pSel = new FreeTypeFontParameter(); 
			pSel.size = 20;
			pSel.color = Color.BLACK;
			ins.fontSel = font.generateFont(pSel);

			ins.menuImgs = new TextureAtlas("res/menu/menu.pack");
			ins.pntrSprite = new Sprite(ins.menuImgs.findRegion("pointer"));
			ins.pauseBGSprite = new Sprite(ins.menuImgs.findRegion("pauseBG"));
			
			initialised = true;
			Gdx.app.log(TAG, "Assets initialised");
			
		} else {
			Gdx.app.error(TAG, "Tried to initialise assets again");
//			Gdx.app.exit();
		}
	}

	public static void dispose() {
		
		if(initialised) {
			Gdx.app.log(TAG, "Disposing assets...");
			
			ins.menuImgs.dispose();
			ins.fontNorm.dispose();
			ins.fontSel.dispose();
			
			ins.labels.clear();
			ins.optionsMenu = null;
			ins.optionsSetting = null;
			ins.optionsPause = null;
			ins.availableRes = null;
			
			ins = null;
			initialised = false;
			Gdx.app.log(TAG, "Assets disposed");
			
		} else {
			Gdx.app.error(TAG, "Dispose called before init");
//			Gdx.app.exit();
		}
	}	// dispose()'s
	
	public static boolean isInitialised() {
		
		return initialised;
	}
}
