package com.kmvrt.Unlived.desktop;

//import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.kmvrt.Unlived.Manager;
//import com.kmvrt.Unlived.Constants;
//import java.util.Arrays;

public class DesktopLauncher {
	
//	private static final String TAG = DesktopLauncher.class.getName();
	
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//		config.width = 1280;
//		config.height = 720;
		config.title = "Unlived";
		config.fullscreen = false;
		config.vSyncEnabled = true;
//		config.samples = 1;
		
	//	useRes(config, 1920, 1080);
		new LwjglApplication(new Manager(), config);
	}
	
}
