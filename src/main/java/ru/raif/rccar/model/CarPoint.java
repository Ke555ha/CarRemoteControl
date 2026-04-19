package ru.raif.rccar.model;

public class CarPoint {

    private final int x;
    private final int y;

    public CarPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static CarPoint of(int x, int y) {
        return new CarPoint(x, y);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
