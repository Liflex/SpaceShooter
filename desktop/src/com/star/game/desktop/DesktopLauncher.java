package com.star.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.star.game.StarGame;

public class DesktopLauncher {
//19.09.2017. Можно считать законченным определенный виток в обучении.
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		float aspect = 480f / 854f;
//        float aspect = 9f / 16f;
//        float aspect = 3f / 4f;
//		config.height = 900;
//		config.width = (int)(config.height * aspect);

		config.width = 500;
		config.height = (int)(config.width / aspect);
		new LwjglApplication(new StarGame(), config);
	}
}
