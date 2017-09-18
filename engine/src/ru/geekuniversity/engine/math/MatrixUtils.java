package ru.geekuniversity.engine.math;

import com.badlogic.gdx.math.*;

public class MatrixUtils {

    //приватный конструктор чтобы нельзя было создать объект класса MatrixUtils
    private MatrixUtils() {
    }

    //Расчёт матрицы перехода 4x4
    public static void calcTransitionMatrix(Matrix4 mat, Rect src, Rect dst) {
        float scaleX = dst.getWidth() / src.getWidth();
        float scaleY = dst.getHeight() / src.getHeight();
        mat.idt().translate(dst.pos.x, dst.pos.y, 0).scale(scaleX, scaleY, 1).translate(-src.pos.x, -src.pos.y, 0);
    }

    //Расчёт матрицы перехода 3x3
    public static void calcTransitionMatrix(Matrix3 mat, Rect src, Rect dst) {
        float scaleX = dst.getWidth() / src.getWidth();
        float scaleY = dst.getHeight() / src.getHeight();
        mat.idt().translate(dst.pos.x, dst.pos.y).scale(scaleX, scaleY).translate(-src.pos.x, -src.pos.y);
    }
}
