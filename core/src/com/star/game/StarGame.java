package com.star.game;

import com.badlogic.gdx.Game;
import com.star.game.screens.menu_screen.MenuScreen;

public class StarGame extends Game {

    public  MenuScreen intro;
    @Override
    public void create() {
        intro = new MenuScreen(this);
        setScreen(intro);
    }
}
