package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter {

    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 480;
    private Camera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    private SpaceRaceGame spaceRaceGame;
    private SpriteBatch batch;

    private Circle circle;

    public GameScreen(SpaceRaceGame spaceRaceGame) {
        this.spaceRaceGame = spaceRaceGame;
    }

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
        viewport = new ExtendViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();

        circle = new Circle(50, 50, 4);
    }
    @Override
    public void render(float delta) {
        clearScreen();

        draw();

        drawDebugAll();

        update(delta);


    }

    private void drawDebugAll() {
        shapeRenderer.setColor(Color.WHITE);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(circle.x, circle.y, circle.radius);

        shapeRenderer.end();
    }

    private void drawAllCircles() {
        // init empty
    }
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.valueOf("#2b2e3b").r, Color.valueOf("#2b2e3b").g,
                Color.valueOf("#2b2e3b").b, Color.valueOf("#2b2e3b").a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float dt) {

    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();

    }
}
