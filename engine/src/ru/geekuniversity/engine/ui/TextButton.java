package ru.geekuniversity.engine.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.StringBuilder;

import ru.geekuniversity.engine.sprites.Sprite;

public class TextButton extends Sprite {

    private final BitmapFont font;
    private final StringBuilder text = new StringBuilder();

    public TextButton(TextureRegion region, BitmapFont font, String text) {
        super(region);
        this.font = font;
        this.text.append(text);
    }

    @Override
    public void draw(SpriteBatch batch) {
        super.draw(batch);
        font.draw(batch, text, pos.x, pos.y + font.getXHeight(), 0f, Align.center, false);
    }
}
