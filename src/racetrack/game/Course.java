/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

package racetrack.game;

import racetrack.domain.LineSegment;
import racetrack.domain.Point;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Course {
    private ArrayList<LineSegment> walls;
    private LineSegment startLine, checkPoint;
    private int length, height;
    private final Color WALL_COLOR = Color.BLACK;
    private final Color START_COLOR = Color.GREEN;
    private final Color CHECK_COLOR = Color.BLUE;

    public Course(int length, int height) {
        this.length = length;
        this.height = height;
        this.walls = new ArrayList<LineSegment>();
        // set border walls
        walls.add(new LineSegment(new Point(0, 0), new Point(length, 0)));
        walls.add(new LineSegment(new Point(0, 0), new Point(0, height)));
        walls.add(new LineSegment(new Point(0, height), new Point(length, height)));
        walls.add(new LineSegment(new Point(length, 0), new Point(length, height)));
    }

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    public void addWall(LineSegment wall) {
        walls.add(wall);
    }

    public List<LineSegment> getWalls() {
        return walls;
    }

    // let user remove wall by picking it's ends, note they might pick ends in reversed order
    // return true if a wall was removed
    public boolean removeWall(LineSegment removeWall) {
        boolean removed = false;
        LineSegment flipRemoveWall = new LineSegment(removeWall.getEnd(), removeWall.getStart());
        // start at 4 to skip the course border walls which should not be deleted
        Iterator<LineSegment> it = walls.listIterator(4);
        while (it.hasNext()) {
            LineSegment checkWall = it.next();
            if(checkWall.equals(removeWall) || checkWall.equals(flipRemoveWall)) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    public void setStartLine(LineSegment start) {
        this.startLine = start;
    }

    public LineSegment getStartLine() {
        return startLine;
    }

    public void setCheckPoint(LineSegment check) {
        this.checkPoint = check;
    }

    public LineSegment getCheckPoint() {
        return checkPoint;
    }

    public void clearAll() {
        walls = new ArrayList<LineSegment>();
        startLine = null;
        checkPoint = null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(startLine).append("\n");
        sb.append(checkPoint);
        for (LineSegment wall : walls) {
            sb.append("\n").append(wall);
        }
        return sb.toString();
    }
}
