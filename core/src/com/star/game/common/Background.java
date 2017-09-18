package com.star.game.common;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ru.geekuniversity.engine.math.Rect;
import ru.geekuniversity.engine.sprites.Sprite;

public class Background extends Sprite{

    public Background(TextureRegion region) {
        super(region);
    }

    @Override
    public void resize(Rect worldBounds) {
        setHeightProportion(worldBounds.getHeight());
    }
}
