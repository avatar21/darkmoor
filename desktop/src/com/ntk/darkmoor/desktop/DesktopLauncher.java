package com.ntk.darkmoor.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ntk.darkmoor.DarkmoorGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = DarkmoorGame.DISPLAY_HEIGHT;
		config.width = DarkmoorGame.DISPLAY_WIDTH;
		new LwjglApplication(new DarkmoorGame(), config);
	}
}
