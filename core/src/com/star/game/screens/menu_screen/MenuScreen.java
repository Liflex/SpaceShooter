package com.star.game.screens.menu_screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.star.game.common.Background;
import com.star.game.screens.game_screen.GameScreen;
import com.star.game.screens.menu_screen.ui.ButtonExit;
import com.star.game.screens.menu_screen.ui.ButtonNewGame;
import com.star.game.screens.menu_screen.ui.epicText.FirstLine;

import ru.geekuniversity.engine.Base2DScreen;
import ru.geekuniversity.engine.Sprite2DTexture;
import ru.geekuniversity.engine.math.Rect;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class MenuScreen extends Base2DScreen {

    private static final float BUTTONS_HEIGHT = 0.15f;
    private static final float BUTTONS_PRESS_SCALE = 0.9f;

    private Sprite2DTexture textureBackground;
    private Background background;
    private ButtonNewGame btnNewGame;
    private ButtonExit btnExit;
    private Music musicMenu;
    private Music startGame;
    private GameScreen gameScreen;
    private TextureAtlas atlasEpicText;
    private FirstLine firstLine;
    protected static TextureRegion regionEpicText;
    private static float volume = 1f;

    public MenuScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();
        gameScreen = new GameScreen(game);
        textureBackground = new Sprite2DTexture(Gdx.files.internal("bg.png"));
        atlasEpicText = new TextureAtlas("fonts/epictext.tpack");
        regionEpicText = atlasEpicText.findRegion("EpicText");
        background = new Background(new TextureRegion(textureBackground));
        firstLine = new FirstLine(regionEpicText);

        btnNewGame = new ButtonNewGame(atlas, BUTTONS_PRESS_SCALE);
        btnNewGame.setHeightProportion(BUTTONS_HEIGHT);
        btnExit = new ButtonExit(atlas,BUTTONS_PRESS_SCALE);
        btnExit.setHeightProportion(BUTTONS_HEIGHT);

        musicMenu = Gdx.audio.newMusic(Gdx.files.internal("sounds/music.mp3"));
        startGame = Gdx.audio.newMusic(Gdx.files.internal("sounds/dota2.mp3"));
        musicMenu.setLooping(true);
        musicMenu.play();
    }

    @Override
    public void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (int i = 0; i < STAR_COINT; i++) {
            star[i].resize(worldBounds);
        }
        firstLine.resize(worldBounds);
        btnNewGame.resize(worldBounds);
        btnExit.resize(worldBounds);

    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        btnNewGame.touchDown(touch, pointer);
        btnExit.touchDown(touch, pointer);
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        btnNewGame.touchUp(touch, pointer);
        btnExit.touchUp(touch, pointer);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float deltaTime) {
        btnNewGame.update(deltaTime);
        btnExit.update(deltaTime);
        firstLine.update(deltaTime);
        for (int i = 0; i < STAR_COINT; i++) {
            star[i].update(deltaTime);
        }
        if(btnNewGame.getButton_speed()>0.0011f && volume>=0.01){
            musicMenu.stop();
            startGame.play();
        }
        if(btnNewGame.getButton_speed()>0.4f) {
            speed = star[1].getSpeed();
            for (int i = 0; i < STAR_COINT ; i++) {
                posx[i] = star[i].pos.x;
                posy[i] = star[i].pos.y;
            }
            dispose();
            game.setScreen(gameScreen);
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < STAR_COINT; i++) {
            star[i].draw(batch);
        }
        btnNewGame.draw(batch);
        btnExit.draw(batch);
        firstLine.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        atlasEpicText.dispose();
        musicMenu.dispose();
    }
}