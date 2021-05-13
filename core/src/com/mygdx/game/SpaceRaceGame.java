package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpaceRaceGame extends Game {

	private AssetManager assetManager;

	@Override
	public void create() {
		assetManager = new AssetManager();
		this.setScreen(new StartScreen(this));
	}

	public AssetManager getAssetManager() {
		return this.assetManager;
	}
}
