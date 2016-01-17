// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

public class Spell {
	// represents a spell in a game
	// store properties of magic to be used in magic factories

	private String name;
	
	// properties
	private float speed;
		// speed of the magic (x / sec)
	private int burst;
		// the number of shots once casted
	private float burstInterval;
		// the time between each burst shots (in sec)
	private float interval;
		// the interval between each cast (in sec)
	private float width, height;
	private float travelDist;
		// distance travelled by the magic casted
	private boolean propsInitialised;

	// attributes
	public GameChar.Attributes hit;
		// attributes applied to characters affected
	public GameChar.Attributes cast;
		// attributes applied to mainchar casting
	private boolean attsInitialised;
	

// constructor ------------------------------------------------------------------------------------------------
	public Spell(String name) {

		this.name = name;
		
		hit = null;
		cast = null;
		attsInitialised = false;
		
		burst = 1;
		burstInterval = 0;
		interval = 0.5f;
		width = 0;
		height = 0;
		speed = 0;
		travelDist = 0;
		propsInitialised = false;
	} // new's end
	
	
// initialisation --------------------------------------------------------------------------------------------
	public void initAtts(GameChar.Attributes hit, GameChar.Attributes cast) {
		// init the attributes of the magic
		
		if(!attsInitialised) {
			attsInitialised = true;

			this.hit = hit;
			this.cast = cast;
		}
	}
	
	public void initProps(float speed, float travelDist,
			int burst, float burstInterval, float interval,
			float width, float height) {
		// initiate the properties of the magic
		
		if(!propsInitialised) {
			propsInitialised = true;
			
			this.speed = speed;
			this.travelDist = travelDist;
			this.burst = burst;
			this.burstInterval = burstInterval;
			this.interval = interval;
			this.width = width;
			this.height = height;
		}
	}
	
	
	
// getters ---------------------------------------------------------------------------------------------
	public String getName() {
		
		return name;
	}
	
	public float getSpeed() {
	
		return speed * Constants.ins.UNIT_CONV;
	}
	
	public float getTravelDist() {
		
		return travelDist * Constants.ins.UNIT_CONV;
	}
	
	public int getBurst() {
		
		return burst;
	}
	
	public float getBurstInterval() {
		
		return burstInterval;
	}
	
	public float getInterval() {
		
		return interval;
	}
	
	public float getWidth() {
		
		return width * Constants.ins.UNIT_CONV;
	}
	
	public float getHeight() {
		
		return height * Constants.ins.UNIT_CONV;
	}
}
