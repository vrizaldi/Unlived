package com.kmvrt.Unlived;

public class Constants {
	// store all the constants
	
	// game state IDs
	public static final int STATE_ARENA = 202;

	public static final float ANIMATION_CHAR_DURATION = 0.1f;	// in sec
	public static final float ANIMATION_SHADOW_DURATION = 0.5f;
	
	// sprites moving and facing directions
	public static final int DIR_W = 1;	// west
	public static final int DIR_E = 2;	// east
	public static final int DIR_N = 3;
	public static final int DIR_S = 4;

	// size of things in world unit
	// 1 unit = 16 px
	public static final int CAM_WIDTH = 800;
	public static final int CAM_HEIGHT = 600;
	public static final int ROOM_WIDTH = 400;
	public static final int ROOM_HEIGHT = 400;
	public static final int ROOMS_INTERVAL = 60;
	public static final int CHAR_WIDTH = 40;
	public static final int CHAR_HEIGHT = 60;
//	public static final int SHADOW_WIDTH = 2;
//	public static final int SHADOW_HEIGHT = 1;
//	public static final int SHADOW_OFFSET_Y = -1;
	public static final int PORTAL_WIDTH = 40;
	public static final int PORTAL_HEIGHT = 40;
	public static final int NORMAL_SPEED = 400;
		// default moving speed per second
	public static final int MAGIC_SPEED = 1000;
	public static final int MAGIC_MAX_DISTANCE = 200;
	public static final int DOOR_OFFSET = 20;
	// safe distance between creep and mainChar
	public static final int SAFE_DIST_X = 200;
	public static final int SAFE_DIST_Y = 200;
		
	
	// map 
	public static final int MAP_NORMAL = 148;
	public static final int CHARS_MIN = 4;
	public static final int CHARS_MAX = 7;
		// total of the normal room types
		// will be changed as types added

	// char IDs
	public static final int CHAR_MAIN = 98;
	public static final int CHAR_CREEP_FOLLOW = 99;
	public static final int CHAR_CREEP_FOLLOW_N = 100;
	public static final int CHAR_CREEP_FOLLOW_S = 101;
	public static final int CHAR_CREEP_AVOID = 103;
//	public static final int CHAR_CREEP_INACTIVE = 103;

	// intervals between things happening
	public static final float CREEPS_CHANGE_INTERVAL = 1f;	// in second

	// number of rooms in a map
	public static final int ROOMS_NUM_X = 5;
	public static final int ROOMS_NUM_Y = 4;
	public static final int ROOMS_NUM_MIN = 6;

	// room type id
	public static final int ROOM_NORMAL = 430;
	public static final int ROOM_SPAWN = 431;
	public static final int ROOM_PORTAL = 432;
	public static final int ROOM_BLANK = 433;

}	// public class' end
