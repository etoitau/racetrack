/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.domain;

import racetrack.game.Course;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Car object to race around the track
 */
public class Car {
    // velocity line has start at current position and end at where current velocity would land it
    private LineSegment vector;
    private Color color;
    private Course course;
    private final int SIZE = 10;
    private List<LineSegment> path;
    private boolean crashed = false, pastCheckpoint = false, finished = false;

    // default constructor
    public Car(LineSegment v, Course course, Color c) {
        this.vector = v;
        this.course = course;
        this.color = c;
        this.path = new ArrayList<>();
    }

    // get copy of car with given color
    public Car(Car toCopy, Color c) {
        this.vector = new LineSegment(toCopy.getVector());
        this.course = toCopy.getCourse();
        this.color = c;
        this.path = new ArrayList<>(toCopy.getPath());
        this.pastCheckpoint = toCopy.isPastCheckpoint();
    }

    // getters / setters
    public LineSegment getVector() {
        return vector;
    }

    public Course getCourse() {
        return course;
    }

    public List<LineSegment> getPath() {
        return path;
    }

    public LineSegment getLastMove() {
        return path.get(path.size() - 1);
    }

    public boolean isCrashed() {
        return crashed;
    }

    public void setCrashed(boolean state) {
        crashed = state;
    }

    public boolean isPastCheckpoint() {
        return pastCheckpoint;
    }

    public void setIsPastCheckpoint(boolean state) {
        pastCheckpoint = state;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean state) {
        finished = state;
    }

    // methods
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

    // check if a move would cross or touch a wall
    public boolean hitsWall(LineSegment trial) {
        for (LineSegment wall : course.getWalls()) {
            if (trial.crosses(wall))
                return true;
        }
        return false;
    }

    // return all legal options for next move
    public List<Point> options() {
        List<Point> pointSet = vector.getEnd().adjacents();
        Iterator<Point> it = pointSet.iterator();
        while (it.hasNext()) {
            if (this.hitsWall(new LineSegment(vector.getStart(), it.next())))
                it.remove();
        }
        return pointSet;
    }

    // draw the car
    public void draw(Graphics g, int scale, int size) {
        g.setColor(color);
        for (LineSegment line : path) {
            g.fillOval(line.getStart().getX() * scale - size / 2, line.getStart().getY() * scale - size / 2, size, size);
            g.drawLine(line.getStart().getX() * scale, line.getStart().getY() * scale,
                    line.getEnd().getX() * scale, line.getEnd().getY() * scale);
        }
        g.fillOval(vector.getStart().getX() * scale - size / 2, vector.getStart().getY() * scale - size / 2, size, size);
    }

    public void draw(Graphics g, int scale) {
        this.draw(g, scale, SIZE);
    }

    // draw car's next move options
    public void drawOptions(Graphics g, int scale) {
        g.setColor(color);
        for (Point point: this.options()) {
            g.drawOval(point.getX() * scale - SIZE / 2, point.getY() * scale - SIZE / 2, SIZE, SIZE);
        }
    }
}
