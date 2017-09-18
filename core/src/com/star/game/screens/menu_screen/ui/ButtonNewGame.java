package com.star.game.screens.menu_screen.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekuniversity.engine.math.Rect;
import ru.geekuniversity.engine.ui.ScaledTouchUpButton;

public class ButtonNewGame extends ScaledTouchUpButton {

    private final Vector2 v = new Vector2();
    private float button_speed = 0.001f;

    public ButtonNewGame(TextureAtlas atlas, float pressScale) {
        super(atlas.findRegion("btPlay"), pressScale);
        v.set(0, 1);
    }

    @Override
    public void resize(Rect worldBounds) {
        setLeft(worldBounds.getLeft());
        setBottom(worldBounds.getBottom());
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if(this.pointer != pointer || !pressed) return false;
        pressed = false;
        scale = 1f;
        if(isMe(touch)) {
            change=0;
            return true;
        }
        return false;
    }

    @Override
    public void update(float deltaTime) {
        if (change==0) {
            button_speed = button_speed + 0.0001f;
            pos.mulAdd(v, button_speed);
            System.out.println(button_speed);
            if (button_speed > 0.006f){
                button_speed = button_speed + 0.004f;
            }
        }
    }

    public float getButton_speed() {
        return button_speed;
    }
}
