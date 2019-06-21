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
import java.util.Iterator;
import java.util.List;


public class Car {
    // velocity line has start at current position and end at where current velocity would land it
    private LineSegment vector;
    private Color color;
    private Course course;
    private final int SIZE = 10;


    public Car(LineSegment v, Color c) {
        this.vector = v;
        this.color = c;
    }

    public void move(Point destination) {
        // correct projected destination (end) to actual destination
        vector.setEnd(destination);
        // subtract start vector to get new velocity (with start at 0, 0)
        vector.getEnd().subtract(vector.getStart());
        // move start of vector to destination by adding actual velocity
        vector.getStart().add(vector.getEnd());
        // move end to new projected end by adding current velocity to start
        vector.getEnd().add(vector.getStart());
    }

    private boolean hitsWall(LineSegment trial) {
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

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval(vector.getStart().getX() + SIZE / 2, vector.getStart().getY() + SIZE / 2, SIZE, SIZE);
    }

}
