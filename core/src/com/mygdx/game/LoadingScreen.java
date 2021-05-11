package com.mygdx.game;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 480;

    private ShapeRenderer shapeRenderer;

    private Camera camera;
    private Viewport viewport;

    private SpriteBatch batch;


    SpaceRaceGame spaceRaceGame;

    public LoadingScreen(SpaceRaceGame spaceRaceGame) {
        this.spaceRaceGame = spaceRaceGame;
    }

    @Override
    public void show() {
        // === temporary === //
        spaceRaceGame.setScreen(new GameScreen(spaceRaceGame));
    }
    @Override
    public void render(float delta) {

    }

    @Override
    public void resize(int width, int height) {

    }



    @Override
    public void dispose() {

    }
}
