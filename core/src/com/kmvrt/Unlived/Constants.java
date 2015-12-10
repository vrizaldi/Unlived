package com.kmvrt.Unlived;

public class Constants {
	// store all the constants
	
	// game state IDs
	public static final int STATE_ARENA = 202;

	// sprites movement speed
	public static final int NORMAL_SPEED = 20;
		// default moving speed per second
	public static final int FRICTION = 10;
		// force decrease every second
	
	public static final int MAGIC_SPEED = 60;
	public static final int MAGIC_MAX_DISTANCE = 10;

	public static final float ANIMATION_CHAR_DURATION = 0.1f;	// in sec
	public static final float ANIMATION_SHADOW_DURATION = 0.5f;
	
	// sprites moving and facing directions
	public static final int DIR_W = 1;	// west
	public static final int DIR_E = 2;	// east
	public static final int DIR_N = 3;
	public static final int DIR_S = 4;

	// size of things in world unit
	public static final int CAM_WIDTH = 40;
	public static final int CAM_HEIGHT = 30;
	public static final int ROOM_WIDTH = 20;
	public static final int ROOM_HEIGHT = 20;
	public static final int CHAR_WIDTH = 2;
	public static final int CHAR_HEIGHT = 3;
	public static final int SHADOW_WIDTH = 2;
	public static final int SHADOW_HEIGHT = 1;
	public static final int SHADOW_OFFSET_Y = -1;
	public static final int PORTAL_WIDTH = 2;
	public static final int PORTAL_HEIGHT = 2;

	// map 
	public static final int MAP_NORMAL = 148;
	public static final int CHARS_MIN = 2;
	public static final int CHARS_MAX = 4;
		// total of the normal room types
		// will be changed as types added

	// char IDs
	public static final int CHAR_MAIN = 98;
	public static final int CHAR_CREEP_FOLLOW = 99;
	public static final int CHAR_CREEP_FOLLOW_N = 100;
	public static final int CHAR_CREEP_FOLLOW_S = 101;
	public static final int CHAR_CREEP_FOLLOW_E = 102;
	public static final int CHAR_CREEP_FOLLOW_W = 103;
	public static final int CHAR_CREEP_AVOID = 102;
//	public static final int CHAR_CREEP_INACTIVE = 103;

	// safe distance between creep and mainChar
	public static final int SAFE_DIST_X = 10;
	public static final int SAFE_DIST_Y = 10;

	// intervals between things happening
	public static final float CREEPS_CHANGE_INTERVAL = 0.5f;	// in second

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
