package ru.geekuniversity.engine.pools;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

import ru.geekuniversity.engine.sprites.Sprite;

public abstract class SpritesPool<T extends Sprite> {

    protected final ArrayList<T> activeObjects = new ArrayList<>();
    protected final ArrayList<T> freeObjects = new ArrayList<>();

    protected abstract T newObject();

    public T obtain() {
        T object;
        if (freeObjects.isEmpty())
             object = newObject();
        else
            object =  freeObjects.remove(freeObjects.size() - 1);
        activeObjects.add(object);
        debugLog();
        return object;
    }

    private void free(T object) {
        if (!activeObjects.remove(object)) throw new RuntimeException("Попытка удаления несуществующего object = " + object);
        freeObjects.add(object);
        debugLog();
    }

    public void freeAllActiveObjects() {
        final int cnt = activeObjects.size();
        for (int i = 0; i < cnt; i++) freeObjects.add(activeObjects.get(i));
        activeObjects.clear();
    }

    public void updateActiveSprites(float deltaTime) {
        final int cnt = activeObjects.size();
        for (int i = 0; i < cnt; i++) {
            Sprite sprite = activeObjects.get(i);
            if(sprite.isDestroyed()) throw new RuntimeException("Update destroyed sprite");
            sprite.update(deltaTime);
        }
    }

    public ArrayList<T> getActiveObjects() {
        return activeObjects;
    }

    public void freeAllDestroyedActiveObjects() {
        for (int i = 0; i < activeObjects.size(); i++) {
            T sprite = activeObjects.get(i);
            if(sprite.isDestroyed()) {
                free(sprite);
                i--;
                sprite.flushDestroy();
            }
        }
    }

    public void drawActiveSprites(SpriteBatch batch) {
        final int cnt = activeObjects.size();
        for (int i = 0; i < cnt; i++) {
            Sprite sprite = activeObjects.get(i);
            if(sprite.isDestroyed()) throw new RuntimeException("Draw destroyed sprite");
            sprite.draw(batch);
        }
    }

    protected void debugLog() {
    }

    public void dispose() {
        freeObjects.clear();
        activeObjects.clear();
    }
}
