package ru.geekuniversity.engine.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekuniversity.engine.sprites.Sprite;

public class ScaledTouchUpButton extends Sprite {

    protected int pointer;
    protected boolean pressed;
    protected float pressScale;

    public ScaledTouchUpButton(TextureRegion region, float pressScale) {
        super(region);
        this.pressScale = pressScale;
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {
        if(pressed || !isMe(touch)) return false;
        this.pointer = pointer;
        scale = pressScale;
        pressed = true;
        return true;
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if(this.pointer != pointer || !pressed) return false;
        pressed = false;
        scale = 1f;
        if(isMe(touch)) {
            return true;
        }
        return false;
    }
}
