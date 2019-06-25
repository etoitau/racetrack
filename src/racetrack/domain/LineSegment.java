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

import java.util.Objects;

public class LineSegment {
    private Point start, end;

    public LineSegment(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public LineSegment(LineSegment toCopy) {
        this.start = new Point(toCopy.getStart());
        this.end = new Point(toCopy.getEnd());
    }

    public Point getStart() {
        return start;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public Point getEnd() {
        return end;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    // do two line segments intersect?
    public boolean crosses(LineSegment other) {
        int check1 = LineSegment.orient(this.start, this.end, other.getStart());
        int check2 = LineSegment.orient(this.start, this.end, other.getEnd());
        int check3 = LineSegment.orient(other.getStart(), other.getEnd(), this.start);
        int check4 = LineSegment.orient(other.getStart(), other.getEnd(), this.end);
        // if segments are colinear
        if (check1 == 0 && check2 == 0) {
            if (Math.min(this.start.manhattanLength(), this.end.manhattanLength()) <
                    Math.max(other.start.manhattanLength(), other.end.manhattanLength())) {
                return (Math.max(this.start.manhattanLength(), this.end.manhattanLength()) >
                        Math.min(other.start.manhattanLength(), other.end.manhattanLength()));
            } else {
                return false;
            }
        }
        // check that for each segment, the ends of the other are on opposite sides
        return (check1 * check2 < 1) && (check3 * check4 < 1);
    }

    private static int orient(Point a, Point b, Point c) {
        // www.cs.cmu.edu/~quake/robust.html
        // returns positive int if a, b, and c are in ccw arrangement, negative if clockwise, 0 if colinear
        return (a.getX() - c.getX()) * (b.getY() - c.getY()) - (b.getX() - c.getX()) * (a.getY() - c.getY());
    }

    // this crosses gate such that start of gate is to this' left and end of gate is to this' right
    public boolean gateCross(LineSegment gate) {
        return this.crosses(gate) && LineSegment.orient(gate.getStart(), gate.getEnd(), this.end) > -1 &&
                LineSegment.orient(gate.getStart(), gate.getEnd(), this.start) < 0;
    }

    @Override
    public String toString() {
        return "Start: " + start + ", End: " + end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineSegment that = (LineSegment) o;
        return this.getEnd().equals(that.getEnd()) && this.getStart().equals(that.getStart());
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
