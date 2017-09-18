package com.star.game.screens.game_screen;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.star.game.common.Ship;
import com.star.game.common.bullets.BulletPool;
import com.star.game.common.explosions.ExplosionPool;

import ru.geekuniversity.engine.math.Rect;

public class MainShip extends Ship {

    private static final float SHIP_HEIGHT = 0.1f;
    private static final float  BOTTOM_MARGIN = 0.05f;
    private final Vector2 v = new Vector2();
    private Vector2 v0 = new Vector2(0.5f, 0f);
    private final float stop = 0f;
    private final float acceleration = 0.0005f;

    MainShip(TextureAtlas atlas, BulletPool bulletPool, Rect worldBounds, ExplosionPool explosionPool, Sound soundShoot) {
        super(atlas.findRegion("main_ship"), 1, 2, 2, bulletPool, explosionPool, worldBounds);
        bulletRegion = atlas.findRegion("bulletMainShip");
        this.bulletSound = soundShoot;
        setHeightProportion(SHIP_HEIGHT);
        setToNewGame();
    }

    void setToNewGame() {
        pos.x = 0f;
        bulletHeight = 0.01f;
        reloadInterval = 0.15f;
        bulletV.set(0f, 0.5f);
        bulletDamage = 1;
        hp = 100;
        flushDestroy();
        change=3;
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getBottom() + BOTTOM_MARGIN);
    }

    private static final int INVALID_POINTER = -1;
    private int leftPointer = INVALID_POINTER;
    private int rightPointer = INVALID_POINTER;

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if(touch.x < worldBounds.pos.x) {
            if(leftPointer != INVALID_POINTER) return false;
            leftPointer = pointer;
            moveLeft();
        } else {
            if(rightPointer != INVALID_POINTER) return false;
            rightPointer = pointer;
            moveRight();
        }
        return false;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if(pointer == leftPointer) {
            leftPointer = INVALID_POINTER;
            if(rightPointer != INVALID_POINTER) moveRight(); else change=3;
        } else if(pointer == rightPointer) {
            rightPointer = INVALID_POINTER;
            if(leftPointer != INVALID_POINTER) moveLeft(); else change=3;
        }
        return false;
    }

    private boolean pressedLeft;
    private boolean pressedRight;

    void keyDown(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = true;
                moveLeft();
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = true;
                moveRight();
                break;
            case Input.Keys.UP:
                frame = 1;
                shoot();
                break;
        }
    }

    void keyUp(int keycode) {
        switch (keycode) {
            case Input.Keys.A:
            case Input.Keys.LEFT:
                pressedLeft = false;
                if(pressedRight) moveRight(); else change=3;
                break;
            case Input.Keys.D:
            case Input.Keys.RIGHT:
                pressedRight = false;
                if(pressedLeft) moveLeft(); else change=3;
                break;
            case Input.Keys.UP:
                frame = 0;
                break;
        }
    }

    private void moveRight() {
        change=2;
        v.set(v0);
    }

    private void moveLeft() {
        change=1;
        v.set(v0).rotate(180f);
    }

    private void stop() {
        change=3;
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final float hpRegenInterval = 1f;
    private float hpRegenTimer;

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        pos.mulAdd(v, speed);
        if(checkAndHandleBounds()){
            change=3;

        }
        if(change==2){
            speed = speed+acceleration;
            if(speed>0.018f)speed=0.018f;
        }else if (change==1){
            speed = speed+acceleration;
            if(speed>0.018f)speed=0.018f;
        }else if (change==3){
            if(speed<0.002){
                speed = speed-(acceleration-0.0002f);
                if(speed<0f ) {
                    speed = stop;
                }
            }
            else speed = speed-(acceleration+0.0005f);
        }
        reloadTimer += deltaTime;
        if(reloadTimer >= reloadInterval) {
            reloadTimer = 0f;
            shoot();
        }
        hpRegenTimer += deltaTime;
        if(hpRegenTimer >= hpRegenInterval) {
            hpRegenTimer = 0f;
            if(hp < 100) hp++;
        }
        if(getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            stop();
        }
        if(getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            stop();
        }
    }

    Vector2 getV() {
        return v;
    }

    int getHP() {
        return hp;
    }

    boolean isBulletCollision (Rect bullet) {
        return !(bullet.getRight() < getLeft() || bullet.getLeft() > getRight() || bullet.getBottom() > pos.y || bullet.getTop() < getBottom());
    }

    public boolean checkAndHandleBounds() {
        if (getLeft() < worldBounds.getLeft()) {
            setLeft(worldBounds.getLeft());
            v.set(v0);
            speed=0.008f;
            return true;
        }
        if (getRight() > worldBounds.getRight()) {
            setRight(worldBounds.getRight());
            v.set(v0).rotate(180);
            speed=0.008f;
            return true;
        }
        return false;
    }
}
