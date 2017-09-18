package com.star.game.screens.menu_screen.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekuniversity.engine.math.Rect;
import ru.geekuniversity.engine.ui.ScaledTouchUpButton;

public class ButtonExit extends ScaledTouchUpButton{

    private final Vector2 v = new Vector2();
    private float button_speed = 0.001f;

    public ButtonExit(TextureAtlas atlas, float pressScale) {
        super(atlas.findRegion("btExit"), pressScale);
        v.set(0, 1);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if (isMe(touch)) {
            Gdx.app.exit();
        }
        return false;
    }

    @Override
    public void resize(Rect worldBounds) {
        setRight(worldBounds.getRight());
        setBottom(worldBounds.getBottom());
    }

    @Override
    public void update(float deltaTime) {
        if (change == 0) {
            button_speed = button_speed + 0.0002f;
            pos.mulAdd(v, button_speed);
            if (button_speed > 0.006f) {
                button_speed = button_speed + 0.004f;
            }
        }
    }

}
