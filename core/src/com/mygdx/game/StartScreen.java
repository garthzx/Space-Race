package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StartScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 480;

    private SpriteBatch batch;
    private Camera camera;
    private Viewport viewport;

    private final SpaceRaceGame spaceRaceGame;

    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    public StartScreen(SpaceRaceGame spaceRaceGame) {
        this.spaceRaceGame = spaceRaceGame;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(WORLD_WIDTH, WORLD_HEIGHT);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT /2, 0);
        camera.update();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        BitmapFontLoader.BitmapFontParameter bitmapFontParameter = new
                BitmapFontLoader.BitmapFontParameter();
        bitmapFontParameter.atlasName = "Space-Race-Game.atlas";
        spaceRaceGame.getAssetManager().load("gomarice.fnt", BitmapFont.class);
        spaceRaceGame.getAssetManager().finishLoading();
        bitmapFont = spaceRaceGame.getAssetManager().get("gomarice.fnt");
        glyphLayout = new GlyphLayout();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(Color.valueOf("#2b2e3b").r, Color.valueOf("#2b2e3b").g,
                Color.valueOf("#2b2e3b").b, Color.valueOf("#2b2e3b").a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            bitmapFont.getData().setScale(1); // reset scale (seems it solves that)
            spaceRaceGame.setScreen(new LoadingScreen(spaceRaceGame));
            return;
        }

        /**
         * It is not recommended to scale a Bitmap font because it looks pixelated.
         * It is recommended to use freetypefont generator for resizing fonts.
         */
        bitmapFont.getData().setScale(1.5f);
        String message = "SPACE RACE!!";
        glyphLayout.setText(bitmapFont, message);
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        batch.begin();
        bitmapFont.draw(batch, glyphLayout,
                (WORLD_WIDTH / 2) - (glyphLayout.width / 2),
                WORLD_HEIGHT - 100);

        String pressSpace = "PRESS SPACE TO START";
        glyphLayout.setText(bitmapFont, pressSpace);
        bitmapFont.draw(batch, glyphLayout,
                (WORLD_WIDTH / 2) - (glyphLayout.width / 2) ,
                WORLD_HEIGHT - 200);

        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {

    }
}
