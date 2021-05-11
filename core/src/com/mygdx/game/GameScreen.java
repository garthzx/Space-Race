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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
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

    private Array<Circle> circleArrayToLeft;
    private Array<Circle> circleArrayToRight;

    private static final float CIRCLE_RADIUS = 4;

    private float timer = 0;
    private static final float CIRCLE_SPEED = 75;
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

        circleArrayToLeft = new Array<>();
        circleArrayToRight = new Array<>();
        for (int i = 0; i < 25; i++) {
            createCirclesToRight();
        }
        for (int i = 0; i < 25; i++) {
            createCirclesToLeft();
        }
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
        drawDebugAllCircles();

        shapeRenderer.end();
    }
    private void createCirclesToRight() {
        float randX = MathUtils.random(WORLD_WIDTH);
        float randY = MathUtils.random(60, WORLD_HEIGHT);
        circleArrayToLeft.add(new Circle(randX, randY, CIRCLE_RADIUS));
    }
    private void createCirclesToLeft() {
        float randX = MathUtils.random(WORLD_WIDTH);
        float randY = MathUtils.random(60, WORLD_HEIGHT);
        circleArrayToRight.add(new Circle(randX, randY, CIRCLE_RADIUS));
    }
    private void drawDebugAllCircles() {
        for (Circle c : circleArrayToLeft) {
            shapeRenderer.circle(c.x, c.y, c.radius);
        }
        for (Circle c : circleArrayToRight) {
            shapeRenderer.circle(c.x, c.y, c.radius);
        }
    }
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.valueOf("#2b2e3b").r, Color.valueOf("#2b2e3b").g,
                Color.valueOf("#2b2e3b").b, Color.valueOf("#2b2e3b").a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    }

    private void update(float dt) {
        timer += dt;
        // move circles
        for (Circle c : circleArrayToLeft) {
            c.x = c.x  - (CIRCLE_SPEED * dt);

            if (c.x + c.radius < 0) {
                float randY = MathUtils.random(60, WORLD_HEIGHT);
                c.x = WORLD_WIDTH + c.radius;
                c.y = randY;
            }
        }

        for (Circle c : circleArrayToRight) {
            c.x = c.x + (CIRCLE_SPEED * dt);

            if (c.x + c.radius > WORLD_WIDTH) {
                c.x = 0 - c.radius;
                float randY = MathUtils.random(60, WORLD_HEIGHT);
                c.y = randY;
            }
        }
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
