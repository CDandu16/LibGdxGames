package com.dungeonFinder.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.dungeonFinder.game.MyGdxGame;

public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.height = MyGdxGame.HEIGHT;
		config.width = MyGdxGame.WIDTH;
		config.title = "Dungeon Crawlers";
		new LwjglApplication(new MyGdxGame(), config);
	}
}
