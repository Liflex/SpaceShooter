package com.star.game.screens.game_screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.star.game.common.Background;
import com.star.game.common.bullets.Bullet;
import com.star.game.common.bullets.BulletPool;
import com.star.game.common.enemies.EnemiesEmitter;
import com.star.game.common.enemies.Enemy;
import com.star.game.common.enemies.EnemyPool;
import com.star.game.common.explosions.ExplosionPool;
import com.star.game.screens.game_screen.ui.ButtonNewGame;
import com.star.game.screens.game_screen.ui.MessageGameOver;

import java.util.ArrayList;

import ru.geekuniversity.engine.Base2DScreen;
import ru.geekuniversity.engine.Font;
import ru.geekuniversity.engine.Sprite2DTexture;
import ru.geekuniversity.engine.math.Rect;
import ru.geekuniversity.engine.utils.StrBuilder;

@SuppressWarnings("ForLoopReplaceableByForEach")
public class GameScreen extends Base2DScreen{

    private enum State { GAMING, GAME_OVER }

    private State state;
    private Sprite2DTexture textureBackground;
    private TextureAtlas atlas;
    private Font font;

    private Background background;
    private MainShip mainShip;
    private MessageGameOver messageGameOver;
    private ButtonNewGame buttonNewGame;
    private EnemiesEmitter enemiesEmitter;
    private BulletPool bulletPool;
    private EnemyPool enemyPool;
    private ExplosionPool explosionPool;
    private Music music;
    private Sound sndLaser;
    private Sound sndBullet;
    private Sound sndExplosion;
    private int frags;
    private static float volume = 0.1f;

    public GameScreen(Game game) {
        super(game);
    }

    @Override
    public void show() {
        super.show();

        music = Gdx.audio.newMusic(Gdx.files.internal("sounds/Asura.mp3"));
        sndBullet = Gdx.audio.newSound(Gdx.files.internal("sounds/bullet.wav"));
        sndLaser = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.wav"));
        sndExplosion = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.wav"));

        textureBackground = new Sprite2DTexture(Gdx.files.internal("bg.png"));
        atlas = new TextureAtlas("textures/mainAtlas.tpack");
        bulletPool = new BulletPool();
        explosionPool = new ExplosionPool(atlas, sndExplosion);
        mainShip = new MainShip(atlas, bulletPool, worldBounds, explosionPool, sndLaser);
        enemyPool = new EnemyPool(bulletPool, explosionPool, worldBounds, mainShip);
        enemiesEmitter = new EnemiesEmitter(enemyPool, worldBounds, atlas, sndBullet);

        background = new Background(new TextureRegion(textureBackground));

        for (int i = 0; i < star.length; i++) {
            star[i].setSpeed(speed);
        }

        messageGameOver = new MessageGameOver(atlas);
        buttonNewGame = new ButtonNewGame(atlas);

        font = new Font("fonts/font.fnt", "fonts/font.png");
        font.setWorldSize(0.02f);

        music.setLooping(true);
        music.setVolume(volume);
        music.play();
        startNewGame();
    }

    private void startNewGame() {
        state = State.GAMING;
        buttonNewGame.setSetNewGame(0);
        frags = 0;
        mainShip.setToNewGame();
        enemiesEmitter.setToNewGame();
        bulletPool.freeAllActiveObjects();
        enemyPool.freeAllActiveObjects();
        explosionPool.freeAllActiveObjects();

    }

    @Override
    protected void resize(Rect worldBounds) {
        background.resize(worldBounds);
        for (int i = 0; i < STAR_COINT; i++) {
            star[i].resize(worldBounds);
            star[i].pos.set(posx[i],posy[i]);
        }
        mainShip.resize(worldBounds);
    }

    @Override
    protected void touchDown(Vector2 touch, int pointer) {
        switch (state) {
            case GAMING:
                mainShip.touchDown(touch, pointer);
                break;
            case GAME_OVER:
                buttonNewGame.touchDown(touch, pointer);
                break;
            default:
                throw new RuntimeException("Unknown state = " + state);
        }
    }

    @Override
    protected void touchUp(Vector2 touch, int pointer) {
        switch (state) {
            case GAMING:
                mainShip.touchUp(touch, pointer);
                break;
            case GAME_OVER:
                buttonNewGame.touchUp(touch, pointer);
                break;
            default:
                throw new RuntimeException("Unknown state = " + state);
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        mainShip.keyDown(keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainShip.keyUp(keycode);
        return false;
    }

    @Override
    public void render(float deltaTime) {
        update(deltaTime);
        if(state == State.GAMING) checkCollision();
        deleteAllDestroyed();
        draw();
    }

    private void update(float deltaTime) {
        System.out.println(music.getVolume());
        if(volume<=1f) {
            volume=volume+0.005f;
            music.setVolume(volume);
        }
        explosionPool.updateActiveSprites(deltaTime);
        switch (state) {
            case GAMING:
                enemiesEmitter.generateEnemies(deltaTime, frags);
                for (int i = 0; i < STAR_COINT; i++) {
                    star[i].update(deltaTime);
                    star[i].slowlySpeed();
                }
                mainShip.update(deltaTime);
                bulletPool.updateActiveSprites(deltaTime);
                enemyPool.updateActiveSprites(deltaTime);
                if(mainShip.isDestroyed()) {
                    mainShip.boom();
                    state = State.GAME_OVER;
                }
                break;
            case GAME_OVER:
                if(buttonNewGame.getSetNewGame()==1) {
                    startNewGame();
                    music.play();
                }
                break;
            default:
                throw new RuntimeException("Unknown state = " + state);
        }
    }

    private void checkCollision() {
        ArrayList<Enemy> enemies = enemyPool.getActiveObjects();
        final int enemyCount = enemies.size();
        ArrayList<Bullet> bullets = bulletPool.getActiveObjects();
        final int bulletCount = bullets.size();
        for (int i = 0; i < enemyCount; i++) {
            Enemy enemy = enemies.get(i);
            if(enemy.isDestroyed()) continue;
            float minDist = enemy.getHalfWidth() + mainShip.getHalfWidth();
            if(enemy.pos.dst2(mainShip.pos) < minDist * minDist) {
                enemy.boom();
                enemy.destroy();
                mainShip.boom();
                mainShip.destroy();
                state = State.GAME_OVER;
                music.pause();
                return;
            }
        }
        for (int i = 0; i < bulletCount; i++) {
            Bullet bullet = bullets.get(i);
            if(bullet.isDestroyed() || bullet.getOwner() == mainShip) continue;
            if(mainShip.isBulletCollision(bullet)) {
                mainShip.damage(bullet.getDamage());
                bullet.destroy();
                if(mainShip.isDestroyed()) {
                    mainShip.boom();
                    state = State.GAME_OVER;
                    return;
                }
            }
        }
        for (int i = 0; i < enemyCount; i++) {
            com.star.game.common.enemies.Enemy enemy = enemies.get(i);
            if(enemy.isDestroyed()) continue;
            for (int j = 0; j < bulletCount; j++) {
                Bullet bullet = bullets.get(j);
                if(bullet.getOwner() != mainShip || bullet.isDestroyed()) continue;
                if(enemy.isBulletCollision(bullet)) {
                    enemy.damage(bullet.getDamage());
                    if(enemy.isDestroyed()) {
                        enemy.boom();
                        frags++;
                    }
                    bullet.destroy();
                    break;
                }
            }
        }
    }

    private void deleteAllDestroyed() {
        bulletPool.freeAllDestroyedActiveObjects();
        enemyPool.freeAllDestroyedActiveObjects();
        explosionPool.freeAllDestroyedActiveObjects();
    }

    private void draw() {
        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.draw(batch);
        for (int i = 0; i < STAR_COINT; i++) {
            star[i].draw(batch);
        }
        mainShip.draw(batch);
        bulletPool.drawActiveSprites(batch);
        enemyPool.drawActiveSprites(batch);
        explosionPool.drawActiveSprites(batch);
        printInfo();
        if(state == State.GAME_OVER) {
            messageGameOver.draw(batch);
            buttonNewGame.draw(batch);
        }
        batch.end();
    }

    private static final String STR_FRAGS = "Frags: ";
    private final StrBuilder sbFrags = new StrBuilder();

    private static final String STR_HP = "HP: ";
    private final StrBuilder sbHP = new StrBuilder();

    private static final String STR_STAGE = "Stage: ";
    private final StrBuilder sbStage = new StrBuilder();

    private void printInfo() {
        font.draw(batch, sbFrags.clear().append(STR_FRAGS).append(frags), worldBounds.getLeft(), worldBounds.getTop(), 0f, Align.left, false);
        font.draw(batch, sbHP.clear().append(STR_HP).append(mainShip.getHP()), worldBounds.pos.x, worldBounds.getTop(), 0f, Align.center, false);
        font.draw(batch, sbStage.clear().append(STR_STAGE).append(enemiesEmitter.getStage()), worldBounds.getRight(), worldBounds.getTop(), 0f, Align.right, false);
    }

    @Override
    public void dispose() {
        sndBullet.dispose();
        sndLaser.dispose();
        sndExplosion.dispose();
        music.dispose();
        textureBackground.dispose();
        atlas.dispose();
        bulletPool.dispose();
        enemyPool.dispose();
        explosionPool.dispose();
        super.dispose();
    }
}
