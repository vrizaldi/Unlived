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

class Assets {

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
	
	public TextureAtlas uiImgs;
	public Sprite blackBox;
	public Sprite haloOff;
	public Sprite haloOn;
	public Animation haloOnAnim;
	public Sprite[] beepSprites;

	public BitmapFont font; // default font


// constructor ------------------------------------------------------------------------------------------------
	private Assets() {
	
		initMapAssets();

		// get the collections ready
		charImgs = new ArrayList<TextureAtlas>();
		charSprites = new HashMap<String, Sprite>();
		charAnims = new HashMap<String, Animation>();
		
		magicImgs = new ArrayList<TextureAtlas>();
		magicSprites = new HashMap<String, Sprite>();
		magicAnims = new HashMap<String, Animation>();
		
		// default font
		FreeTypeFontParameter param = new FreeTypeFontParameter();
		param.size = 10;
		param.color = Color.WHITE;
		font = new FreeTypeFontGenerator(
				Gdx.files.internal("res/font/PressStart2P.ttf")) 
				.generateFont(param);
		
		// ui images
		uiImgs = new TextureAtlas("res/ui/ui.pack");
		blackBox = new Sprite(uiImgs.findRegion("blackBox"));
		initHaloAssets();
		initBeepAssets();
	}

	private void initMapAssets() {

		mapImgs = new TextureAtlas("res/map/map.pack");

		roomSprite = new Sprite(mapImgs.findRegion("map"));
		roomSprite.setSize(
				Constants.ins.ROOM_WIDTH, Constants.ins.ROOM_HEIGHT);
		
		doorVSprite = new Sprite(mapImgs.findRegion("door"));
		doorVSprite.setSize(Constants.ins.ROOMS_INTERVAL + Constants.ins.DOOR_OFFSET, 
					Constants.ins.DOOR_OFFSET + Constants.ins.CHAR_HEIGHT);
		
		doorHSprite = new Sprite(mapImgs.findRegion("door"));
		doorHSprite.setSize(
					Constants.ins.CHAR_WIDTH + Constants.ins.DOOR_OFFSET, 
					Constants.ins.DOOR_OFFSET + Constants.ins.ROOMS_INTERVAL);
			
		shadowSprite = 
				new Sprite(mapImgs.findRegion("shadow"));
		shadowSprite.setSize(
					Constants.ins.CHAR_WIDTH, 1 * Constants.ins.UNIT_CONV);

		portalSprite = 
				new Sprite(mapImgs.findRegion("portalImg"));
		portalSprite.setSize(
				Constants.ins.PORTAL_WIDTH, Constants.ins.PORTAL_HEIGHT);

	}

	private void initHaloAssets() {
	
		haloOff = new Sprite(uiImgs.findRegion("haloOff"));
		haloOff.setSize(Constants.ins.HALO_SIZE, Constants.ins.HALO_SIZE);
		Array<TextureRegion> haloOnFrames = new Array<TextureRegion>(4);
		for(int i = 1;; i++) {
			TextureRegion frame = uiImgs.findRegion("haloOn" + i);
			if(frame == null) {
				// has finished
				break;
			}
				
			try {
				haloOnFrames.add(frame);
			} catch(Exception e) {
				// increase the array size
				haloOnFrames.ensureCapacity(1);
				haloOnFrames.add(frame);
			}
		}
		haloOnAnim = new Animation(0.2f / haloOnFrames.size,
				haloOnFrames, Animation.PlayMode.LOOP);
		haloOn = new Sprite(haloOnAnim.getKeyFrame(0));
		haloOn.setSize(Constants.ins.HALO_SIZE, Constants.ins.HALO_SIZE);
	}

	private void initBeepAssets() {
	
		beepSprites = new Sprite[3];
		beepSprites[0] = new Sprite(uiImgs.findRegion("beep1"));
		beepSprites[1] = new Sprite(uiImgs.findRegion("beep2"));
		beepSprites[2] = new Sprite(uiImgs.findRegion("beep3"));
		for(Sprite s : beepSprites) {
			s.setSize(Constants.ins.BEEP_SIZE, Constants.ins.BEEP_SIZE);
		}
	}


// initialise and dispose resources ---------------------------------------------------------------------------
	public static void init() {
		// initialise all the assets
	
		if(!initialised) {
			Gdx.app.log(TAG, "Initialising assets...");
			ins = new Assets();
		
			initialised = true;	// flag it
			Gdx.app.log(TAG, "Assets initialised");
			
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
						"res/chars/" + c.getName() + "/" 
						+ c.getCostume() + "/" + c.getName() + ".pack");
			
				// retrieve all the char sprites
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
				cSp.setSize(Constants.ins.CHAR_WIDTH, Constants.ins.CHAR_HEIGHT);

				// put them into the collections
				ins.charImgs.add(cImgs);
				ins.charSprites.put(c.getName() + c.getCostume(), cSp);
				ins.charAnims.put(c.getName() + c.getCostume(), cAnim);

				// magic sprites
				TextureAtlas mImgs = new TextureAtlas("res/magic/sprites/" + c.getName() + ".pack");
				Array<TextureRegion> magicFrames = new Array<TextureRegion>(1);
				// retrieve all the magic sprites
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
				Animation mAnim = new Animation(0.03f / magicFrames.size,
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
			
			// halo
			ins.haloOn.setRegion(ins.haloOnAnim.getKeyFrame(stateTime));
			
		} else {
//			Gdx.app.error(TAG, "Trying to update unitialised assets");
		}
	}	// update(float)'s
	
	public static void resize() {
		// resize all the assets according to the current unit
	
		ins.roomSprite.setSize(Constants.ins.ROOM_WIDTH, Constants.ins.ROOM_HEIGHT);
	
		ins.doorVSprite.setSize(Constants.ins.ROOMS_INTERVAL + Constants.ins.DOOR_OFFSET, 
				Constants.ins.DOOR_OFFSET + Constants.ins.CHAR_HEIGHT);
		ins.doorHSprite.setSize(Constants.ins.CHAR_WIDTH + Constants.ins.DOOR_OFFSET, 
				Constants.ins.DOOR_OFFSET + Constants.ins.ROOMS_INTERVAL);
		
		ins.shadowSprite.setSize(Constants.ins.CHAR_WIDTH, 1 * Constants.ins.UNIT_CONV);

		ins.portalSprite.setSize(Constants.ins.PORTAL_WIDTH, Constants.ins.PORTAL_HEIGHT);
		
		for(String key : ins.charSprites.keySet()) {
			ins.charSprites.get(key).setSize(
					Constants.ins.CHAR_WIDTH, Constants.ins.CHAR_HEIGHT);
			
		}
		for(String key : ins.magicSprites.keySet()) {
			ins.magicSprites.get(key).setSize(MagicFactory.getSpell(key).getWidth(),
					MagicFactory.getSpell(key).getHeight());
		}
		
		ins.haloOff.setSize(Constants.ins.HALO_SIZE, Constants.ins.HALO_SIZE);
		ins.haloOn.setSize(Constants.ins.HALO_SIZE, Constants.ins.HALO_SIZE);

		for(Sprite s : ins.beepSprites) {
				s.setSize(Constants.ins.BEEP_SIZE, Constants.ins.BEEP_SIZE);
		}
	}

	public static void dispose() {
		// dispose all the resources 	

		Gdx.app.debug(TAG, "dispose() called");
		if(initialised) {
			Gdx.app.log(TAG, "Disposing assets...");
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
			
		}		
	}	// dispose()'s end

}
