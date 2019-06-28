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

import racetrack.game.Course;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;


public class Car {
    // velocity line has start at current position and end at where current velocity would land it
    private LineSegment vector;
    private Color color;
    private Course course;
    private final int SIZE = 10;
    private List<LineSegment> path;
    private boolean crashed = false, pastCheckpoint = false, finished = false;


    public Car(LineSegment v, Course course, Color c) {
        this.vector = v;
        this.course = course;
        this.color = c;
        this.path = new ArrayList<LineSegment>();
    }

    public Car(Car toCopy, Color c) {
        this.vector = new LineSegment(toCopy.getVector());
        this.course = toCopy.getCourse();
        this.color = c;
        this.path = new ArrayList<LineSegment>(toCopy.getPath());
        this.pastCheckpoint = toCopy.isPastCheckpoint();
    }

    public List<LineSegment> getPath() {
        return path;
    }

    public List<LineSegment> getPathCopy() {
        return new ArrayList<LineSegment>(path);
    }

    public LineSegment getLastMove() {
        return path.get(path.size() - 1);
    }

    public boolean isCrashed() {
        return crashed;
    }

    public boolean isPastCheckpoint() {
        return pastCheckpoint;
    }

    public boolean isFinished() {
        return finished;
    }

    public LineSegment getVector() {
        return vector;
    }

    public Course getCourse() {
        return course;
    }

    public void setIsPastCheckpoint(boolean state) {
        pastCheckpoint = state;
    }

    public void setCrashed(boolean state) {
        crashed = state;
    }

    public void setFinished(boolean state) {
        finished = state;
    }

    public void move(Point destination) {
        // correct projected destination (end) to actual destination
        vector.setEnd(destination);
        // add copy of actual vector to path
        path.add(new LineSegment(vector));
        // subtract start vector to get new velocity (with start at 0, 0)
        vector.getEnd().subtract(vector.getStart());
        // move start of vector to destination by adding actual velocity
        vector.getStart().add(vector.getEnd());
        // move end to new projected end by adding current velocity to start
        vector.getEnd().add(vector.getStart());
    }

    public boolean hitsWall(LineSegment trial) {
        // check if a move would cross a wall
        for (LineSegment wall : course.getWalls()) {
            if (trial.crosses(wall))
                return true;
        }
        return false;
    }

    public List<Point> options() {
        // return all legal options for next move
        List<Point> pointSet = vector.getEnd().adjacents();
        Iterator<Point> it = pointSet.iterator();
        while (it.hasNext()) {
            if (this.hitsWall(new LineSegment(vector.getStart(), it.next())))
                it.remove();
        }
        return pointSet;
    }

    public void draw(Graphics g, int scale, int turn, int size) {
        g.setColor(color);
        for (int i = 0; i < Math.min(turn, path.size()); i++) {
            LineSegment line = path.get(i);
            g.fillOval(line.getStart().getX() * scale - size / 2, line.getStart().getY() * scale - size / 2, size, size);
            g.drawLine(line.getStart().getX() * scale, line.getStart().getY() * scale,
                    line.getEnd().getX() * scale, line.getEnd().getY() * scale);
        }
        g.fillOval(vector.getStart().getX() * scale - size / 2, vector.getStart().getY() * scale - size / 2, size, size);
    }

    public void draw(Graphics g, int scale) {
        this.draw(g, scale, path.size(), SIZE);
    }

    public void draw(Graphics g, int scale, int size) {
        this.draw(g, scale, path.size(), size);
    }

    public void drawOptions(Graphics g, int scale) {
        g.setColor(color);
        for (Point point: this.options()) {
            g.drawOval(point.getX() * scale - SIZE / 2, point.getY() * scale - SIZE / 2, SIZE, SIZE);
        }
    }

}
