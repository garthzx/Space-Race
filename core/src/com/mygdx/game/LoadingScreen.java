package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LoadingScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 480;

    private static final float PROGRESS_BAR_WIDTH = 100;
    private static final float PROGRESS_BAR_HEIGHT = 25;

    private ShapeRenderer shapeRenderer;

    private Camera camera;
    private Viewport viewport;

    private float progress = 0;
    SpaceRaceGame spaceRaceGame;

    public LoadingScreen(SpaceRaceGame spaceRaceGame) {
        this.spaceRaceGame = spaceRaceGame;
    }

    @Override
    public void show() {
        shapeRenderer = new ShapeRenderer();
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        spaceRaceGame.getAssetManager().getLogger().setLevel(Logger.DEBUG);
        BitmapFontLoader.BitmapFontParameter bitmapFontParameter =
                new BitmapFontLoader.BitmapFontParameter();
        bitmapFontParameter.atlasName = "Space-Race-Game.atlas";
        spaceRaceGame.getAssetManager().load("gomarice.fnt", BitmapFont.class);
        spaceRaceGame.getAssetManager().load("Space-Race-Game.atlas", TextureAtlas.class);

    }

    @Override
    public void render(float delta) {
        update();

        clearScreen();

        draw();
    }

    private void update() {
        if (spaceRaceGame.getAssetManager().update()) {
            spaceRaceGame.getAssetManager().finishLoading();
            spaceRaceGame.setScreen(new GameScreen(spaceRaceGame));
        }
        else {
            progress = spaceRaceGame.getAssetManager().getProgress();
        }
    }

    private void clearScreen() {
        Gdx.gl.glClearColor(Color.valueOf("#2b2e3b").r, Color.valueOf("#2b2e3b").g,
                Color.valueOf("#2b2e3b").b, Color.valueOf("#2b2e3b").a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void draw() {
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.rect(
                (WORLD_WIDTH - PROGRESS_BAR_WIDTH) / 2,
                (WORLD_HEIGHT - PROGRESS_BAR_HEIGHT) / 2,
                progress * PROGRESS_BAR_WIDTH,
                PROGRESS_BAR_HEIGHT
        );
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
    }
}
