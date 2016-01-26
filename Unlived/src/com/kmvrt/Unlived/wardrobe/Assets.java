package com.kmvrt.Unlived.wardrobe;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Array;
import java.util.ArrayList;
import java.util.HashMap;

class Assets {
	
	private static final String TAG = Assets.class.getName();

	public static Assets ins;

	private static boolean initialised = false;
	
	private ArrayList<TextureAtlas> charImgs;
	public HashMap<String, Sprite> charSprites;
	private HashMap<String, Animation> charAnims;
	
	private TextureAtlas uiImgs;
	public Sprite boxSprite;


// constructor --------------------------------------------------------------------------------------------------
	private Assets() {
		// prevent outside init

		// init the assets **************************
		// init the chars
		charImgs = new ArrayList<TextureAtlas>();
		charSprites = new HashMap<String, Sprite>();
		charAnims = new HashMap<String, Animation>();
		Gdx.app.debug(TAG, "Initialising chars...");
		for(String name : MagicFactory.getSpellNames()) {
			for(int costume = 1; costume < Identities.ins.getCostumeNum(name); 
					costume++) {
				Gdx.app.debug(TAG, "Initialising " + name + " " + costume);
				TextureAtlas img = new TextureAtlas("res/chars/" + name + "/" + costume
						+ "/" + name + ".pack");
				Animation anim = getAnim(img, name);
				Sprite s = new Sprite(anim.getKeyFrame(0));
				s.setSize(Constants.ins.CHAR_WIDTH, Constants.ins.CHAR_HEIGHT);

				charImgs.add(img);
				charSprites.put(name + costume, s);
				charAnims.put(name + costume, anim);
			}
		}

		// UI components
		uiImgs = new TextureAtlas("res/wardrobe/wardrobe.pack");
		boxSprite = new Sprite(uiImgs.findRegion("box"));
		boxSprite.setSize(Constants.ins.CHAR_WIDTH + 1,
				Constants.ins.CHAR_HEIGHT + 1);
	}

	private Animation getAnim(TextureAtlas img, String name) {
	
		Array<TextureRegion> frames = new Array<TextureRegion>(1);
		for(int i = 1; true; i++) {
			TextureRegion frame = img.findRegion(name + i);
			if(frame == null){
				break;
			}

			try {
				frames.add(frame);
			} catch(Exception e) {
				frames.ensureCapacity(1);
				frames.add(frame);
			}
//			Gdx.app.debug(TAG, "i = " + i);
		}
		return new Animation((float)1 / (frames.size * 3),
				frames, Animation.LOOP);
	}



// init & dispose -----------------------------------------------------------------------------------------------
	public static void init() {
		// initialise the assets
	
		if(initialised) {
			return;
		}
		initialised = true;
	
		ins = new Assets();
	}

	public static void dispose() {
		// dispose the assets
	
		if(!initialised) {
			return;
		}
		initialised = false;
		
		// dispose the assets ******************************
		for(TextureAtlas charImg : ins.charImgs) {
			charImg.dispose();
		}
		ins.charImgs.clear();
		ins.charAnims.clear();
		ins.charSprites.clear();
		ins.uiImgs.dispose();
	}
	
	public static void resize() {
		
		for(String key : ins.charSprites.keySet()) {
			Sprite sp = ins.charSprites.get(key);
			sp.setSize(Constants.ins.CHAR_WIDTH, Constants.ins.CHAR_HEIGHT);
		}
		ins.boxSprite.setSize(Constants.ins.CHAR_WIDTH + 1,
				Constants.ins.CHAR_HEIGHT + 1);
	}

}
