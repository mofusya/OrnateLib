package net.mofusya.ornatelib.util.screen;

import org.joml.Vector2i;

import java.util.function.Function;

public class Size {

    private final int defaultX;
    private final int defaultY;

    private int x;
    private int y;

    public Size() {
        this(0, 0);
    }

    public Size(Vector2i vec) {
        this(vec.x(), vec.y());
    }

    public Size(int[] xy) {
        this(xy[0], xy[1]);
    }

    public Size(int x, int y) {
        this.defaultX = x;
        this.defaultY = y;
        this.x = x;
        this.y = y;
    }

    protected Size(int defaultX, int defaultY, int x, int y) {
        this.defaultX = defaultX;
        this.defaultY = defaultY;
        this.x = x;
        this.y = y;
    }

    public int defaultX() {
        return this.defaultX;
    }

    public int defaultY() {
        return this.defaultY;
    }

    public int x() {
        return this.x;
    }

    public int y() {
        return this.y;
    }

    public Size setX(Function<Integer, Integer> func) {
        return this.setX(func.apply(this.x));
    }

    public Size setX(int x) {
        this.x = x;
        return this;
    }

    public Size setY(Function<Integer, Integer> func) {
        return this.setY(func.apply(this.y));
    }

    public Size setY(int y) {
        this.y = y;
        return this;
    }

    public Size set(int x, int y) {
        return this.setX(x).setY(y);
    }

    public Size set(Function<Integer, Integer> xFunc, Function<Integer, Integer> yFunc) {
        return this.setX(xFunc).setY(yFunc);
    }

    public Size set() {
        return this.set(0, 0);
    }

    public Size copy(){
        return new Size(this.defaultX, this.defaultY, this.x, this.y);
    }
}
