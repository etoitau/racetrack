/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

package racetrack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Point {
    protected int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point toCopy) {
        this.x = toCopy.getX();
        this.y = toCopy.getY();
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void add(Point point) {
        this.x += point.getX();
        this.y += point.getY();
    }

    public void subtract(Point point) {
        this.x -= point.getX();
        this.y -= point.getY();
    }


    public void reset(int x, int y) {
        this.x = x;
        this.y = y;
    }



    public int manhattanLength() {
        return x + y;
    }

    public List<Point> adjacents() {
        ArrayList<Point> points = new ArrayList<Point>(8);
        for (int i = -1; i < 2; i++) {
            for (int j = -1; j < 2; j++) {
                points.add(new Point(this.x + i, this.y + j)) ;
            }
        }
        return points;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x &&
                y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
