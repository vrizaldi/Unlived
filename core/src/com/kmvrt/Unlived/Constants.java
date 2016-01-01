package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
//import com.kmvrt.Unlived.gameplay.Manager;

public class Constants {
	// store all the constants
	
	public static Constants ins;
	
	public static void init() {
		
		ins = new Constants();
		
		ins.UNIT_CONV = 16 * Gdx.graphics.getHeight() / 480;
		ins.CAM_WIDTH = Gdx.graphics.getWidth();
		ins.CAM_HEIGHT = Gdx.graphics.getHeight();
		
		// visible screen must always have 4 : 3 screen ratio
		ins.VISIBLE_SCR_WIDTH = 4 * ins.CAM_HEIGHT / 3;
		ins.VISIBLE_SCR_HEIGHT = ins.CAM_HEIGHT;
		
		ins.ROOM_WIDTH = 20 * ins.UNIT_CONV;
		ins.ROOM_HEIGHT = 20 * ins.UNIT_CONV;
		ins.ROOMS_INTERVAL = 3 * ins.UNIT_CONV;
		ins.DOOR_OFFSET = 1 * ins.UNIT_CONV;
		ins.PORTAL_WIDTH = 2 * ins.UNIT_CONV;
		ins.PORTAL_HEIGHT = 2 * ins.UNIT_CONV;
		
		ins.CHAR_WIDTH = 2 * ins.UNIT_CONV;
		ins.CHAR_HEIGHT = 3 * ins.UNIT_CONV;
		ins.NORMAL_SPEED = 20 * ins.UNIT_CONV;
		ins.SAFE_DIST_X = 10 * ins.UNIT_CONV;
		ins.SAFE_DIST_Y = 10 * ins.UNIT_CONV;
		
		ins.HALO_SIZE = 2 * ins.UNIT_CONV;
		ins.BEEP_SIZE = 10 * ins.UNIT_CONV;
	}
	
	// game state IDs
//	public static final int STATE_Manager = 202;

	public static final float ANIMATION_CHAR_DURATION = 0.1f;	// in sec
	public static final float ANIMATION_SHADOW_DURATION = 0.5f;
	public static final float INIT_AFFECTING_TIME = 0.1f;
	
	// sprites moving and facing directions
	public static final int DIR_W = 1;	// west
	public static final int DIR_E = 2;	// east
	public static final int DIR_N = 3;
	public static final int DIR_S = 4;

	// size of things in world unit
	public int UNIT_CONV;
		// number of px/unit
	public int CAM_WIDTH;
	public int CAM_HEIGHT;
	public int VISIBLE_SCR_WIDTH;
	public int VISIBLE_SCR_HEIGHT;
	public int ROOM_WIDTH;
	public int ROOM_HEIGHT;
	public int PORTAL_WIDTH;
	public int PORTAL_HEIGHT;
	public int ROOMS_INTERVAL;
	public int DOOR_OFFSET;
	public int HALO_SIZE;
	public int BEEP_SIZE;
	
	public int CHAR_WIDTH;
	public int CHAR_HEIGHT;
	public int NORMAL_SPEED;
		// default moving speed per second
	public int SAFE_DIST_X;	// safe distance between creep and mainChar
	public int SAFE_DIST_Y;
	
	// map 
	public static final int MAP_NORMAL = 148;
		// ID
	public static final int CHARS_MIN = 4;
	public static final int CHARS_MAX = 7;
		// total of the normal room types
		// will be changed as types added

	// char IDs
	public static final int CHAR_MAIN = 98;
	public static final int CHAR_CREEP_FOLLOW = 99;
//	public final int CHAR_CREEP_FOLLOW_N = 100;
//	public final int CHAR_CREEP_FOLLOW_S = 101;
	public static final int CHAR_CREEP_AVOID = 103;
//	public final int CHAR_CREEP_INACTIVE = 103;

	// intervals between things happening
	public static final float CREEPS_CHANGE_INTERVAL = 1f;	// in second

	// number of rooms in a map
	public static final int ROOMS_NUM_X = 4;
	public static final int ROOMS_NUM_Y = 4;
	public static final int ROOMS_NUM_MIN = 4;//6;

	// room type id
	public static final int ROOM_NORMAL = 430;
	public static final int ROOM_SPAWN = 431;
	public static final int ROOM_PORTAL = 432;
	public static final int ROOM_BLANK = 433;
	
	// option ids
	public static final int OPT_START = 622;
	public static final int OPT_QUIT = 623;
	public static final int OPT_SETTING = 624;
	public static final int OPT_RES = 625;
	public static final int OPT_VOL = 626;
	public static final int OPT_SND = 627;
	public static final int OPT_F11 = 628;
	public static final int OPT_VSNC = 629;
	public static final int OPT_RESUME = 630;
	public static final int OPT_TOMENU = 631;
	public static final int OPT_BACK = 632;
	public static final int OPT_YES = 633;
	public static final int OPT_NO = 634;
	
	// prompts ids
	public static final int PROMPT_TOMENU = 781;
	public static final int PROMPT_QUIT = 782;

}	// public class' end
