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

//    private static final float CIRCLE_RADIUS = 14f; // == temporary, prone to change == //
    private static final float SPEED = 100f;
    private final TextureRegion textureRegion;
    private float x;
    private float y;
    private Ellipse ellipse;

    public Player(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
        Circle collisionCircle = new Circle();
        ellipse = new Ellipse(collisionCircle);
        ellipse.setSize(30, 35);
    }
    /**
     * NOTE - This methods starts a new rendering.
     * It does not use shapeRenderer.begin and .end in the GameScreen class
     * because the ShapeType needs to be Line, not Filled.
     * @param shapeRenderer
     */
    public void drawDebugCircle(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.circle(collisionCircle.x, collisionCircle.y, CIRCLE_RADIUS);
        shapeRenderer.ellipse(ellipse.x, ellipse.y, ellipse.width, ellipse.height, 40);
        shapeRenderer.end();
    }

    public void draw(SpriteBatch batch) {
        // Center Player's x and y relative to the center of the Circle's.
//        float textureX = ellipse.x - (float) textureRegion.getRegionWidth() / 2;
//        float textureY = ellipse.y - (float) textureRegion.getRegionHeight() / 2;
//        batch.draw(textureRegion, textureX, textureY);
//        batch.draw(textureRegion,
//                ellipse.x - (textureRegion.getRegionWidth()), ellipse.y);
        batch.draw(textureRegion,ellipse.x, ellipse.y);
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
        updateCollisionCircle();
    }
    private void updateCollisionCircle() {
        ellipse.x = this.x;
        ellipse.y = this.y;
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

    public Ellipse getEllipse() {
        return this.ellipse;
    }
}
