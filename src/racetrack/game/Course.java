/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.game;

import racetrack.domain.LineSegment;
import racetrack.domain.Point;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Course object has information about the course geometry
 */
public class Course {
    private ArrayList<LineSegment> walls;
    private LineSegment startLine, checkPoint;
    private int length, height;

    // getters / setters
    public List<LineSegment> getWalls() {
        return walls;
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

    public int getLength() {
        return length;
    }

    public int getHeight() {
        return height;
    }

    // constructor
    public Course(int length, int height) {
        this.length = length;
        this.height = height;
        this.walls = new ArrayList<>();
        // set border walls
        walls.add(new LineSegment(new Point(0, 0), new Point(length, 0)));
        walls.add(new LineSegment(new Point(0, 0), new Point(0, height)));
        walls.add(new LineSegment(new Point(0, height), new Point(length, height)));
        walls.add(new LineSegment(new Point(length, 0), new Point(length, height)));
    }

    // add wall to list
    public void addWall(LineSegment wall) {
        walls.add(wall);
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
