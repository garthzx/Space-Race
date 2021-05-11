package com.mygdx.game;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Player {

    private static final float CIRCLE_RADIUS = 5f; // == temporary, prone to change == //
    private static final float SPEED = 10f;
    private TextureRegion textureRegion;
    private Circle collisionCircle;
    private float x;
    private float y;

    public Player(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        collisionCircle = new Circle(x, y, CIRCLE_RADIUS);
    }
    /**
     * NOTE - This methods starts a new rendering.
     * It does not use shapeRenderer.begin and .end in the GameScreen class
     * because the ShapeType needs to be Line, not Filled.
     * @param shapeRenderer
     */
    private void drawDebugCircle(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.circle(collisionCircle.x, collisionCircle.y, CIRCLE_RADIUS);
        shapeRenderer.end();
    }

    public void draw(SpriteBatch batch) {
        // Center Player's x and y relative to the center of the Circle's.
        float textureX = collisionCircle.x - (float) textureRegion.getRegionWidth() / 2;
        float textureY = collisionCircle.y - (float) textureRegion.getRegionHeight() / 2;
        batch.draw(textureRegion, textureX, textureY);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }
    private void updateCollisionCircle() {
        collisionCircle.x = x;
        collisionCircle.y = y;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
