package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Logger;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends ScreenAdapter implements InputProcessor {
    private enum State {
        RUNNING, PAUSED, ROUND
    }
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 480;
    private Camera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;
    private SpaceRaceGame spaceRaceGame;
    private SpriteBatch batch;

    private Array<Circle> circleArrayToLeft;
    private Array<Circle> circleArrayToRight;

    private Rectangle rectangleBetweenPlayers;

    // originally 4
    private static final float CIRCLE_RADIUS = 4;

    private float timer = 0;
    private static final float CIRCLE_SPEED = 75;
    private static final float START_LINE = 75;

    private Player player1;
    private Player player2;

    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    private int player1Score = 0;
    private int player2Score = 0;

    private boolean player1PointClaimed = false;
    private boolean player2PointClaimed = false;

    private State state = State.RUNNING;

    private String messageWhoWon;

    private boolean pressedEscape = false;

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

        TextureAtlas textureAtlas = spaceRaceGame.getAssetManager().get("Space-Race-Game.atlas");
        player1 = new Player(textureAtlas.findRegion("Space Race Ship"));
        player2 = new Player(textureAtlas.findRegion("Space Race Ship"));
        player1.setPosition(WORLD_WIDTH / 3, START_LINE / 2);
        player2.setPosition(WORLD_WIDTH / 1.5f, START_LINE / 2);

        for (int i = 0; i < 15; i++) {
            createCirclesToRight();
        }

        for (int i = 0; i < 15; i++) {
            createCirclesToLeft();
        }

        rectangleBetweenPlayers = new Rectangle((player1.getX() + player2.getX()) / 2,
                                                10, 10,50);

        bitmapFont = spaceRaceGame.getAssetManager().get("gomarice.fnt");
        glyphLayout = new GlyphLayout();

        state = State.RUNNING;
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        draw();
        renderDebugAll();
        switch (state){
            case RUNNING:
                update(delta);
                break;
            case ROUND:
                showMessageOnRound(delta);
                break;
            case PAUSED:
                showMessageOnPause();
                break;
        }
    }

    private void updateScore() {
        if (player1.getY() > WORLD_HEIGHT + player1.getTextureRegion().getRegionHeight()
                && !isPlayer1PointClaimed()) {
            player1Score++;
            setPlayer1MarkPointClaimed(true);
            System.out.println("PLAYER 1= " + player1Score);
        }

        else if (player2.getY() > WORLD_HEIGHT + player2.getTextureRegion().getRegionHeight()
                && !isPlayer2PointClaimed()) {
            player2Score++;
            setPlayer2MarkPointClaimed(true);
            System.out.println("PLAYER 2= " + player2Score);
        }
    }
    private void showMessageOnRound(float delta) {
        if (timer <= 3){
            timer += delta;
            batch.begin();
            glyphLayout.setText(bitmapFont, messageWhoWon);
            bitmapFont.draw(batch, glyphLayout, (WORLD_WIDTH / 2) - (glyphLayout.width / 2),
                    WORLD_HEIGHT / 2);
            batch.end();
        }
        else {
            state = State.RUNNING;
            timer = 0;
        }
    }
    private void newRound() {
        // return them to their current position
        setPlayer1MarkPointClaimed(false);
        setPlayer2MarkPointClaimed(false);
        player1.setPosition(WORLD_WIDTH / 3, START_LINE / 2);
        player2.setPosition(WORLD_WIDTH / 1.5f, START_LINE / 2);
    }

    private void drawScore() {
        String player1ScoreAsString = Integer.toString(player1Score);
        glyphLayout.setText(bitmapFont, player1ScoreAsString);

        bitmapFont.draw(batch, player1ScoreAsString,
                ((player1.getX() + rectangleBetweenPlayers.x) / 2) + glyphLayout.width,
                START_LINE / 2);

        String player2ScoreAsString = Integer.toString(player2Score);
        glyphLayout.setText(bitmapFont, player2ScoreAsString);

        bitmapFont.draw(batch, player2ScoreAsString,
                ((player2.getX() + rectangleBetweenPlayers.x) / 2) - glyphLayout.width,
                START_LINE / 2);
    }

    private void renderDebugAll() {

        shapeRenderer.setColor(Color.WHITE);
        shapeRenderer.setProjectionMatrix(camera.projection);
        shapeRenderer.setTransformMatrix(camera.view);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        drawDebugAllCircles();
        shapeRenderer.rect(rectangleBetweenPlayers.x, rectangleBetweenPlayers.y,
                rectangleBetweenPlayers.width, rectangleBetweenPlayers.height);
        shapeRenderer.end();

        // new Shape batch
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        player1.drawDebugCircle(shapeRenderer);
        player2.drawDebugCircle(shapeRenderer);
        shapeRenderer.end();
    }

    private void createCirclesToRight() {
        float randX = MathUtils.random(WORLD_WIDTH);
        float randY = MathUtils.random(START_LINE, WORLD_HEIGHT);
        circleArrayToLeft.add(new Circle(randX, randY, CIRCLE_RADIUS));
    }

    private void createCirclesToLeft() {
        float randX = MathUtils.random(WORLD_WIDTH);
        float randY = MathUtils.random(START_LINE, WORLD_HEIGHT);
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

    private boolean checkCollisionPlayer(Circle player, Circle c) {
        return Intersector.overlaps(c, player);
    }

    private void update(float dt) {
        messageWhoWon = player1PointClaimed ? "Left Player Got First!" : "Right Player Got First!";
        if (player1PointClaimed || player2PointClaimed) {
            state = State.ROUND;
            newRound();
        }

        for (Circle c : circleArrayToLeft) {

            if (checkCollisionPlayer(player1.getCircle(), c)) {
                // == FOR DEBUGGING PURPOSES === //
//                System.out.println("c= " + c.toString());
//                System.out.println("player1= " + player1.getCircle().toString());
//                System.out.println("player2= " + player2.getCircle().toString());
//
//                System.out.println("PLAYER 1 COLLISION DETECTED");
            }

            if (checkCollisionPlayer(player2.getCircle(), c)) {
                // == FOR DEBUGGING PURPOSES === //
//                System.out.println("c= " + c.toString());
//                System.out.println("player1= " + player1.getCircle().toString());
//                System.out.println("player2= " + player2.getCircle().toString());
//
//                System.out.println("PLAYER 2 COLLISION DETECTED");
            }

            // == move circles == //
            // update every circle's x
            c.x = c.x  - (CIRCLE_SPEED * dt);

            if (c.x + c.radius < 0) {
                float randY = MathUtils.random(START_LINE, WORLD_HEIGHT);
                c.x = WORLD_WIDTH + c.radius;
                c.y = randY;
            }
        }

        for (Circle c : circleArrayToRight) {

            if (checkCollisionPlayer(player1.getCircle(), c)) {
                // == FOR DEBUGGING PURPOSES === //
//                System.out.println("c= " + c.toString());
//                System.out.println("player1= " + player1.getCircle().toString());
//                System.out.println("player2= " + player2.getCircle().toString());
//
//                System.out.println("PLAYER 1 COLLISION DETECTED");
            }

            if (checkCollisionPlayer(player2.getCircle(), c)) {
                // == FOR DEBUGGING PURPOSES === //
//                System.out.println("c= " + c.toString());
//                System.out.println("player1= " + player1.getCircle().toString());
//                System.out.println("player2= " + player2.getCircle().toString());
//
//                System.out.println("PLAYER 2 COLLISION DETECTED");
            }

            // == move circles == //
            c.x = c.x + (CIRCLE_SPEED * dt);

            if (c.x + c.radius > WORLD_WIDTH) {
                c.x = 0 - c.radius;
                float randY = MathUtils.random(START_LINE, WORLD_HEIGHT);
                c.y = randY;
            }
        }

        // === Left Player 1 controls === //
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            player1.moveUp(dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            player1.moveDown(dt);
        }

        // === Right Player 2 controls === //
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            player2.moveUp(dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)){
            player2.moveDown(dt);
        }

        updateScore();
    }

    // === pointClaimed methods fixes issue in setting score. === //
    private boolean isPlayer1PointClaimed() {
        return player1PointClaimed;
    }

    private boolean isPlayer2PointClaimed() {
        return player2PointClaimed;
    }

    private void setPlayer1MarkPointClaimed(boolean b) {
        player1PointClaimed = b;
    }

    private void setPlayer2MarkPointClaimed(boolean b) {
        player2PointClaimed = b;
    }

    private void draw() {
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        batch.begin();
        player1.draw(batch);
        player2.draw(batch);
        drawScore();
        batch.end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        spaceRaceGame.getAssetManager().dispose(); // this disposes all textures
        bitmapFont.dispose();
    }

    private void showMessageOnPause() {
        batch.begin();
        glyphLayout.setText( bitmapFont,"PAUSED");
        bitmapFont.draw(batch, glyphLayout, (WORLD_WIDTH / 2) - (glyphLayout.width / 2),
                WORLD_HEIGHT / 2);
        batch.end();
    }
    @Override
    public boolean keyDown(int keycode) {
        // == This pauses the game == //
        if (keycode == Input.Keys.ESCAPE && !pressedEscape) {
            pressedEscape = true;
            state = State.PAUSED;
            System.out.println("keyDown() called!");
            return true;
        }
        // == This resumes the game == //
        if (keycode == Input.Keys.ESCAPE) {
            state = State.RUNNING;
            pressedEscape = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
