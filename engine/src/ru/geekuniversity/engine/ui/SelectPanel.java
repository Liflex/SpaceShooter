package ru.geekuniversity.engine.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

import ru.geekuniversity.engine.math.Rect;
import ru.geekuniversity.engine.sprites.Sprite;

public class SelectPanel extends Rect {

    public enum Type { TOUCH_DOWN, TOUCH_MOVE, TOUCH_UP }
    protected final Type type;
    protected final SelectPanelListener listener;
    protected final ArrayList<Sprite> buttons = new ArrayList<Sprite>();
    protected Sprite selector;

    public SelectPanel(SelectPanelListener listener, Type type) {
        this.listener = listener;
        this.type = type;
    }

    public void touchUp(Vector2 touch, int pointer) {
        if(type != Type.TOUCH_UP) return;
        final int cnt = buttons.size();
        for (int i = 0; i < cnt; i++) {
            Sprite button = buttons.get(i);
            if(button.isMe(touch)) {
                listener.onSelect(this, button, i);
                selector.pos.set(button.pos);
            }
        }
    }

    public void update(float deltaTime) {
        final int cnt = buttons.size();
        for (int i = 0; i < cnt; i++) buttons.get(i).update(deltaTime);
    }

    public void draw(SpriteBatch batch) {
        final int cnt = buttons.size();
        for (int i = 0; i < cnt; i++) buttons.get(i).draw(batch);
        selector.draw(batch);
    }
}
