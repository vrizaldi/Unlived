// code by Muhammad Noorghifari

package com.kmvrt.Unlived;

public class Spell {
	// represents a spell in a game
	// store properties of magic to be used in magic factories

	private String name;
	
	// properties
	private int burst;
		// the number of shots once casted
	private float burstInterval;
		// the time between each burst shots (in sec)
	private float interval;
		// the interval between each cast (in sec)
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
		propsInitialised = false;
	} // new's end
	
	
// initialisation --------------------------------------------------------------------------------------------
	public void initAtts(GameChar.Attributes hit, GameChar.Attributes cast) {
		
		if(!attsInitialised) {
			attsInitialised = true;

			this.hit = hit;
			this.cast = cast;
		}
	}
	
	public void initProps(int burst, float burstInterval, float interval) {
		
		if(!propsInitialised) {
			propsInitialised = true;
			
			this.burst = burst;
			this.burstInterval = burstInterval;
			this.interval = interval;
		}
	}
	
	
	
// getters ---------------------------------------------------------------------------------------------
	public String getName() {
		
		return name;
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
	
}