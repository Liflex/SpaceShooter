package ru.geekuniversity.engine.sprites.publicObjects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import ru.geekuniversity.engine.math.Rnd;
import ru.geekuniversity.engine.math.Rect;
import ru.geekuniversity.engine.sprites.Sprite;

public class Star extends Sprite {

    private final Vector2 v = new Vector2();
    private final Vector2 u = new Vector2(1f, -0.7f);
    private final Vector2 u1 = new Vector2(-1f, -0.7f);
    private Rect worldBounds;
    private float speedStar;
    private boolean start;

    public Star(TextureRegion region, float vx, float vy, float width) {
        super(region);
        v.set(vx, vy);
        setWidthProportion(width);
    }
    public void boostSpeed(){

    }
    public void slowlySpeed(){
        if (speedStar>0.007f) {
            speedStar = speedStar - 0.0004f;
            if(speedStar<=0.007f){
                speedStar=0.0069f;
                start = true;
            }
        }
    }

    @Override
    public void resize(Rect worldBounds) {
        this.worldBounds = worldBounds;
        float posX = Rnd.nextFloat(worldBounds.getLeft(), worldBounds.getRight());
        float posY = Rnd.nextFloat(worldBounds.getBottom(), worldBounds.getTop());
        pos.set(posX, posY);
    }

    @Override
    public boolean touchDown(Vector2 touch, int pointer) {return false;}

    @Override
    public void update(float deltaTime) {
        if(change==0){
            speedStar = speedStar + 0.0003f;
        }
        moveStars();
        checkAndHandleBounds();
    }

    private void checkAndHandleBounds() {
        if (getRight() < worldBounds.getLeft()) setLeft(worldBounds.getRight());
        if (getLeft() > worldBounds.getRight()) setRight(worldBounds.getLeft());
        if (getTop() < worldBounds.getBottom()) setBottom(worldBounds.getTop());
        if (getBottom() > worldBounds.getTop()) setTop(worldBounds.getBottom());
    }

    public void moveStars(){
        if(change==1){
            pos.mulAdd(u, speedStar);
        }else if(change==2){
            pos.mulAdd(u1, speedStar);
        }else pos.mulAdd(v, speedStar);
    }

    public float getSpeed() {
        return speedStar;
    }

    public void setSpeed(float speed) {
        this.speedStar = speed;
    }

    public boolean isStart() {
        return start;
    }
}
