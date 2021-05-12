package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player {

    private static final float CIRCLE_RADIUS = 14f; // == temporary, prone to change == //
    private static final float SPEED = 100f;
    private final TextureRegion textureRegion;
    private float x;
    private float y;
    private final Circle circle;

    public Player(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        // === NOTE TO SELF == //
        // - This was originally new Circle() only - which caused collision detection
        // - issues - always include x,y, and radius when creating Circle objects - //
        circle = new Circle(x, y, CIRCLE_RADIUS);
    }

    /**
     * Renders circle to the screen
     * @param shapeRenderer
     */
    public void drawDebugCircle(ShapeRenderer shapeRenderer) {
        shapeRenderer.circle(circle.x, circle.y, CIRCLE_RADIUS);
    }

    public void draw(SpriteBatch batch) {
        // Center Player's x and y relative to the center of the Circle's.
        float textureX = circle.x - (float) textureRegion.getRegionWidth() / 2;
        float textureY = circle.y - (float) textureRegion.getRegionHeight() / 2;
        batch.draw(textureRegion,textureX, textureY);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }
    private void updateCollisionCircle(){
        circle.x = x;
        circle.y = y;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public void moveUp(float dt) {
        setPosition(x, y + (SPEED * dt));
    }
    public void moveDown(float dt) {
        setPosition(x, y - (SPEED * dt));
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public Circle getCircle() {
        return this.circle;
    }


}
