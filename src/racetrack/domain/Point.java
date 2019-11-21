/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * a point object has an x and y coordinate
 * line segments use points
 * can also represent a vector with respect to origin
 */
public class Point {
    protected int x, y;

    // getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // constructor
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // get copy
    public Point(Point toCopy) {
        this.x = toCopy.getX();
        this.y = toCopy.getY();
    }

    // vector addition of points
    public void add(Point point) {
        this.x += point.getX();
        this.y += point.getY();
    }

    // vector subtraction of points
    public void subtract(Point point) {
        this.x -= point.getX();
        this.y -= point.getY();
    }

    // get manhattan distance from origin
    public int manhattanLength() {
        return x + y;
    }

    // return all adjacent points
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
