package org.toubassi.rl.catmouse;

/**
 */
public class Point {
    public int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point clone() {
        return new Point(x, y);
    }

    public boolean equals(Object other) {
        if (other instanceof Point) {
            Point p = (Point)other;
            return p.x == x && p.y == y;
        }
        return false;
    }
}
