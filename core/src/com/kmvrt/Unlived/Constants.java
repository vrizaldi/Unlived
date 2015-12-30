package com.kmvrt.Unlived;

import com.badlogic.gdx.Gdx;
//import com.kmvrt.Unlived.gameplay.Manager;

public class Constants {
	// store all the constants
	
	// game state IDs
	public static final int STATE_Manager = 202;

	public static final float ANIMATION_CHAR_DURATION = 0.1f;	// in sec
	public static final float ANIMATION_SHADOW_DURATION = 0.5f;
	public static final float INIT_AFFECTING_TIME = 0.1f;
	
	// sprites moving and facing directions
	public static final int DIR_W = 1;	// west
	public static final int DIR_E = 2;	// east
	public static final int DIR_N = 3;
	public static final int DIR_S = 4;

	// size of things in world unit
	public static int CAM_WIDTH = Gdx.graphics.getWidth();
	public static int CAM_HEIGHT = Gdx.graphics.getHeight();
	public static final int ROOM_WIDTH = 20 * Manager.UNIT_CONV;
	public static final int ROOM_HEIGHT = 20 * Manager.UNIT_CONV;
	public static final int ROOMS_INTERVAL = 3 * Manager.UNIT_CONV;
	public static final int CHAR_WIDTH = 2 * Manager.UNIT_CONV;
	public static final int CHAR_HEIGHT = 3 * Manager.UNIT_CONV;
	public static final int PORTAL_WIDTH = 2 * Manager.UNIT_CONV;
	public static final int PORTAL_HEIGHT = 2 * Manager.UNIT_CONV;
	public static final int NORMAL_SPEED = 20 * Manager.UNIT_CONV;
		// default moving speed per second
	public static final int DOOR_OFFSET = 1 * Manager.UNIT_CONV;
	// safe distance between creep and mainChar
	public static final int SAFE_DIST_X = 10 * Manager.UNIT_CONV;
	public static final int SAFE_DIST_Y = 10 * Manager.UNIT_CONV;
	public static final float SHADOW_OFFSET_Y = -0.5f * Manager.UNIT_CONV;
	
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
//	public static final int CHAR_CREEP_FOLLOW_N = 100;
//	public static final int CHAR_CREEP_FOLLOW_S = 101;
	public static final int CHAR_CREEP_AVOID = 103;
//	public static final int CHAR_CREEP_INACTIVE = 103;

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

}	// public class' end
