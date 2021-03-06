package com.kmvrt.Unlived.menu;

import com.kmvrt.Unlived.*;
import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Graphics.DisplayMode;
//import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.Preferences;
//import com.badlogic.gdx.utils.Timer;

class HeadClerk {
	// update the menu screen
	
	public static final String TAG = HeadClerk.class.getName();

	private Menu menu;
	private StateData data;
	
	private Preferences prefs;
	
	private int cDIndex;
	private boolean fullscreen;
//	private boolean vSync;
	//private boolean procInput;
//	private boolean justChange = false;


// constructor ------------------------------------------------------------------------------------------------
	public HeadClerk(Menu menu, StateData data, Preferences prefs) {

		Assets.init();
		this.menu = menu;
		this.data = data;
		
		this.prefs = prefs;
		int width = Gdx.graphics.getWidth();
		int height = Gdx.graphics.getHeight();
		for(int i = 0; i < Assets.ins.availableRes.length; i++) {
			DisplayMode nD = Assets.ins.availableRes[i];
			if(width == nD.width
					&& height == nD.height) {
				cDIndex = i;
				break;
			}
		}
		fullscreen = Gdx.graphics.isFullscreen();
		data.vSync = prefs.getBoolean("vSync", true); 
		Gdx.graphics.setVSync(data.vSync);
	} 


// update the game ---------------------------------------------------------------------------------------------
	public void update() {

		procInput();
	}
	
	private void procInput() {
	
		int p = data.cOptions[data.pointer];
			// the selected option
		if(data.receptionist.moveUp()) {
			// move pointer up
			--data.pointer;
			if(data.pointer < 0) {
				data.pointer += (data.cOptions.length - 1);
			}
		// if up is just pressed
			
		} else if(data.receptionist.moveDown()) {
			// move pointer down
			data.pointer = ++data.pointer % (data.cOptions.length - 1);
		// if down is just pressed
		
		} else if(data.receptionist.shootRight()) {
			// select the pointed button
			if(data.cOptions == Assets.ins.optionsPromptQuit) {
				if(p == Constants.OPT_NO) {
					if(data.paused) {
						data.cOptions = Assets.ins.optionsPause;
					} else {
						data.cOptions = Assets.ins.optionsMenu;
					}
				} else if(p == Constants.OPT_YES) {
					Gdx.app.exit();
				}
			// if on quit prompt
				
			} else if(data.cOptions == Assets.ins.optionsPromptToMenu) {
				if(p == Constants.OPT_NO) {
					data.cOptions = Assets.ins.optionsPause;
				} else if(p == Constants.OPT_YES) {
					menu.toMainMenu();
				}
			// if on to mainmenu prompt
				
			} else if(data.cOptions == Assets.ins.optionsSetting) {
				if(p == Constants.OPT_BACK) {
					// get back to the main menu
					if(data.paused) {
						data.pointer = 0;
						data.cOptions = Assets.ins.optionsPause;
						
					} else {
						data.cOptions = Assets.ins.optionsMenu;
						data.pointer = 0;
					}
				}
			// if on setting
				
			} else if(data.cOptions == Assets.ins.optionsMenu) {
				if(p == Constants.OPT_START) {
					// start the game
					menu.toWardrobe();

				} else if(p == Constants.OPT_SETTING) {
					// open the settings
					data.cOptions = Assets.ins.optionsSetting;
					data.pointer = 0;	// reset the pointer to the top
				
				} else if(p == Constants.OPT_QUIT) {
					// quit
					data.cOptions = Assets.ins.optionsPromptQuit;
					data.pointer = 1;
					
				}
			// if on mainmenu's
				
			} else if(data.cOptions == Assets.ins.optionsPause) {
				if(p == Constants.OPT_RESUME) {
					// resume the game
					menu.resumeArena();
					
				} else if(p == Constants.OPT_SETTING) {
					data.pointer = 0;
					data.cOptions = Assets.ins.optionsSetting;
					
				} else if(p == Constants.OPT_TOMENU) {
					data.cOptions = Assets.ins.optionsPromptToMenu;
					data.pointer = 1;
					
				} else if(p == Constants.OPT_QUIT) {
					data.cOptions = Assets.ins.optionsPromptQuit;
					data.pointer = 1;
				}
			}
		// if D is pressed's
			
		} else if(data.receptionist.moveRight()) {
			if(data.cOptions == Assets.ins.optionsSetting) {
				if(p == Constants.OPT_RES) {
					cDIndex = (cDIndex + 1) % Assets.ins.availableRes.length;
					DisplayMode d = Assets.ins.availableRes[cDIndex];
					Gdx.app.log(TAG, "Setting resolution to " + d.width + "x" + d.height + "...");
					Gdx.graphics.setDisplayMode(d.width, d.height, fullscreen);
					prefs.putInteger("resWidth", d.width);
					prefs.putInteger("resHeight", d.height);
					
				} else if(p == Constants.OPT_F11) {
					fullscreen = !fullscreen;
					DisplayMode d = Assets.ins.availableRes[cDIndex];
					String sw = fullscreen ? "on" : "of";
					Gdx.app.log(TAG, "Setting fullscreen " + sw);
					Gdx.graphics.setDisplayMode(
							d.width, d.height, fullscreen);
					prefs.putBoolean("fullscreen", fullscreen);
					
				} else if(p == Constants.OPT_VSNC) {
					data.vSync = !data.vSync;
					Gdx.graphics.setVSync(data.vSync);
					prefs.putBoolean("vSync", data.vSync);
					
				} else if(data.cOptions == Assets.ins.optionsSetting) {
					if(p == Constants.OPT_VOL) {
						Assets.ins.volMusic =
								Math.min(Assets.ins.volMusic + 0.01f, 1f);
						// the maximum is 1 (100%)
					}
	
				} else if(p == Constants.OPT_SND) {
					Assets.ins.volSound =
							Math.min(Assets.ins.volSound + 0.01f, 1f);				
				} 
			} 	// if on setting
		// if right is pressed
		
		} else if(data.receptionist.moveLeft()) {
			if(data.cOptions == Assets.ins.optionsSetting) {
				if(p == Constants.OPT_RES) {
					cDIndex = (cDIndex - 1) % Assets.ins.availableRes.length;
					if(cDIndex < 0) {
						cDIndex += Assets.ins.availableRes.length;
					}
					DisplayMode d = Assets.ins.availableRes[cDIndex];
					Gdx.app.log(TAG, "Setting resolution to " + d.width + "x" + d.height + "...");
					Gdx.graphics.setDisplayMode(d.width, d.height, fullscreen);
					prefs.putInteger("resWidth", d.width);
					prefs.putInteger("resHeight", d.height);
					
				} else if(p == Constants.OPT_F11) {
					fullscreen = !fullscreen;
					DisplayMode d = Assets.ins.availableRes[cDIndex];
					String sw = fullscreen ? "on" : "of";
					Gdx.app.log(TAG, "Setting fullscreen " + sw);
					Gdx.graphics.setDisplayMode(
							d.width, d.height, fullscreen);
					prefs.putBoolean("fullscreen", fullscreen);
						
				} else if(p == Constants.OPT_VSNC) {
					data.vSync = !data.vSync;
					Gdx.graphics.setVSync(data.vSync);
					prefs.putBoolean("vSync", data.vSync);
					
				} else if(p == Constants.OPT_VOL) {
					Assets.ins.volMusic =
							Math.max(Assets.ins.volMusic - 0.01f, 0);
						// the minimum is 0 (0%)
	
				} else if(p == Constants.OPT_SND) {
					Assets.ins.volSound =
							Math.max(Assets.ins.volSound - 0.01f, 0);
					
				}
			}
		} // if left is pressed
		
		prefs.flush();
	}	// procInput()'s

/*	private void justChange() {
		justChange = true;
		Timer.schedule(
				new Timer.Task() {
					
					@Override
					public void run() {
						justChange = false;
					}
				}, 0.8f);
	}*/
	
}	// public class'
