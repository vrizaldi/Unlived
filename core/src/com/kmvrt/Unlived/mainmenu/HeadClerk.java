package com.kmvrt.Unlived.mainmenu;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Graphics.DisplayMode;
import com.badlogic.gdx.utils.Timer;
//import com.badlogic.gdx.utils.Timer;

public class HeadClerk {
	// update the menu screen
	
	public static final String TAG = HeadClerk.class.getName();

	private Menu menu;
	private Courier courier;
	
	private int cDIndex;
	private boolean fullscreen;
	//private boolean procInput;
	private boolean justChange = false;


// constructor ------------------------------------------------------------------------------------------------
	public HeadClerk(Menu menu, Courier courier) {

		this.menu = menu;
		this.courier = courier;
		
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		for(int i = 0; i < Assets.availableRes.length; i++) {
			DisplayMode nD = Assets.availableRes[i];
			if(width == nD.width
					&& height == nD.height) {
				cDIndex = i;
				break;
			}
		}
		fullscreen = Gdx.graphics.isFullscreen();
	} 


// update the game ---------------------------------------------------------------------------------------------
	public void update() {
		
//		if(procInput) {
			procInput();
//			procInput = false;
//		}
	}
	
	private void procInput() {
	
		int p = courier.cOptions[courier.pointer];
		if(Gdx.input.isKeyJustPressed(Keys.UP)) {
			// move pointer up
			courier.pointer = (courier.pointer - 1) % courier.cOptions.length;
			if(courier.pointer < 0) {
				courier.pointer += courier.cOptions.length;
			}
			
		} else if(Gdx.input.isKeyJustPressed(Keys.DOWN)) {
			// move pointer down
			courier.pointer = (courier.pointer + 1) % courier.cOptions.length;
		
		} else if(Gdx.input.isKeyJustPressed(Keys.D)) {
			// select the pointed button
			if(courier.cOptions == Assets.optionsSetting) {
				if(p == Constants.OPT_QUIT) {
					// get back to the main menu
					courier.cOptions = Assets.optionsMenu;
					courier.pointer = 0;
				}
				
			} else if(courier.cOptions == Assets.optionsMenu) {
				if(p == Constants.OPT_START) {
					// start the game
					menu.startArena();

				} else if(p == Constants.OPT_SETTING) {
					// open the settings
					courier.cOptions = Assets.optionsSetting;
					courier.pointer = 0;	// reset the pointer to the top
				
				} else if(p == Constants.OPT_QUIT) {
					// quit
					Gdx.app.exit();
					
				}
			}	// pointer checker's
		// if D is pressed's
			
		} else if(Gdx.input.isKeyJustPressed(Keys.RIGHT)) {
			if(courier.cOptions == Assets.optionsSetting) {
				if(p == Constants.OPT_RES) {
					if(!justChange) {
						justChange();
						cDIndex = (cDIndex + 1) % Assets.availableRes.length;
						DisplayMode d = Assets.availableRes[cDIndex];
						Gdx.graphics.setDisplayMode(d.width, d.height, fullscreen);
					}
					
				} else if(p == Constants.OPT_F11) {
					fullscreen = !fullscreen;
					Gdx.graphics.setDisplayMode(
							Gdx.graphics.getWidth(), 
							Gdx.graphics.getHeight(), 
							fullscreen);
				}
			} 	// if on setting
			
		} else if(Gdx.input.isKeyPressed(Keys.RIGHT)) {
			if(courier.cOptions == Assets.optionsSetting) {
				if(p == Constants.OPT_VOL) {
					Assets.volMusic =
							Math.min(Assets.volMusic + 0.01f, 1f);
						// the maximum is 1 (100%)
	
				} else if(p == Constants.OPT_SND) {
					Assets.volSound =
							Math.min(Assets.volSound + 0.01f, 1f);
					
				} 
			} 	// if on setting
		// if right is pressed
		
		} else if(Gdx.input.isKeyJustPressed(Keys.LEFT)) {
			if(courier.cOptions == Assets.optionsSetting) {
				if(p == Constants.OPT_RES) {
					if(!justChange) {
						justChange();
						cDIndex = (cDIndex - 1) % Assets.availableRes.length;
						if(cDIndex < 0) {
							cDIndex += Assets.availableRes.length;
						}
						DisplayMode d = Assets.availableRes[cDIndex];
						Gdx.graphics.setDisplayMode(d.width, d.height, fullscreen);
					}
					
				} else if(p == Constants.OPT_F11) {
					fullscreen = !fullscreen;
					Gdx.graphics.setDisplayMode(
							Gdx.graphics.getWidth(), 
							Gdx.graphics.getHeight(), 
							fullscreen);
				}
			}
		// if left is just pressed
			
		} else if(Gdx.input.isKeyPressed(Keys.LEFT)) {
			if(courier.cOptions == Assets.optionsSetting) {
				if(p == Constants.OPT_VOL) {
					Assets.volMusic =
							Math.max(Assets.volMusic - 0.01f, 0);
						// the minimum is 0 (0%)
	
				} else if(p == Constants.OPT_SND) {
					Assets.volSound =
							Math.max(Assets.volSound - 0.01f, 0);
					
				}
			}
		} // if left is pressed
	}	// procInput()'s

	private void justChange() {
		justChange = true;
		Timer.schedule(
				new Timer.Task() {
					
					@Override
					public void run() {
						justChange = false;
					}
				}, 0.5f);
	}
	
}	// public class'
