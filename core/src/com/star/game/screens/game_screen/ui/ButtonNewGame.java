package com.star.game.screens.game_screen.ui;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

import ru.geekuniversity.engine.ui.ScaledTouchUpButton;

public class ButtonNewGame extends ScaledTouchUpButton {

    private final static float HEIGHT = 0.05f;
    private final static float TOP = -0.012f;
    private final static float PRESS_SCALE = 0.9f;
    private int setNewGame;

    public ButtonNewGame(TextureAtlas atlas) {
        super(atlas.findRegion("button_new_game"),PRESS_SCALE);
        setHeightProportion(HEIGHT);
        setTop(TOP);
    }

    @Override
    public boolean touchUp(Vector2 touch, int pointer) {
        if(isMe(touch)) {
            setNewGame=1;
            return true;
        }
        return false;
    }

    public int getSetNewGame() {
        return setNewGame;
    }

    public void setSetNewGame(int setNewGame) {
        this.setNewGame = setNewGame;
    }
}
