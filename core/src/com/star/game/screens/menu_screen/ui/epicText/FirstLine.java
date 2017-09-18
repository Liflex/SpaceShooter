package com.star.game.screens.menu_screen.ui.epicText;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekuniversity.engine.math.Rect;
import ru.geekuniversity.engine.sprites.Sprite;

/**
 * Created by Tom on 18.09.2017.
 */

public class FirstLine extends Sprite {
    private Sound sndExplosion;
    private final Vector2 v = new Vector2();
    private float width = 0.5f;
    private float speedText;
    private int increment = 0;
    public FirstLine(TextureRegion region) {
        super(region);
        v.set(0, -1);
        change=3;
        setWidthProportion(width);
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));
    }

    @Override
    public void resize(Rect worldBounds) {
        setBottom(worldBounds.getTop());
    }

    @Override
    public void update(float deltaTime) {
        System.out.println(change);
        if(change==3) {
                speedText = speedText + 0.0003f;
                pos.mulAdd(v, speedText);
                if(pos.y<=0){
                    change=4;
                }
                if (pos.y<=0.1){
                    if (increment == 0) {
                        sndExplosion.play();
                        increment++;
                    }
                }
            } else if  (change==4){
                v.set(0, 1);
                pos.mulAdd(v, 0.0005f);
                if(pos.y>=0.03){
                    change=5;
                }
            } else if (change==0){
            pos.mulAdd(v, 0.007f);
        }
    }

}
