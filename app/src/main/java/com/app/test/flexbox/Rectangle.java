package com.app.test.flexbox;

public class Rectangle {
    public int x = 0;
    public int y = 0;
    public int width = 0;
    public int height = 0;

    public Rectangle() {

    }

    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle clone() {
        return new Rectangle(x, y, width, height);
    }

    public String toString() {
        return "x:" + x + ",y:" + y + ",width:" + width + ",height:" + height;
    }
}
