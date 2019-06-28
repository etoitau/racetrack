/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

package racetrack.gui;

import racetrack.domain.Car;
import racetrack.domain.LineSegment;
import racetrack.domain.Point;
import racetrack.game.Course;
import racetrack.game.Race;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class CourseDisplay extends JPanel {
    private Course course;
    private Race race;
    private int scale;
    private boolean simMode = false;
    private final int DOT = 2, WALL_DOT = 4, TYP_CAR_DOT = 8, ACTIVE_CAR_DOT = 10;
    private final Color WALL_COLOR = Color.BLACK, START_COLOR = new Color(50, 160, 30), CHECK_COLOR = Color.BLUE;

    public CourseDisplay(Course course, int scale) {
        super.setBackground(Color.WHITE);
        this.course = course;
        this.scale = scale;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g);
        drawWalls(g);
        drawStartAndCheck(g);
        drawActiveCar(g);
        drawCars(g);
    }

    public void paintNow() {
        paintComponent(this.getGraphics());
    }

    public int getScale() {
        return scale;
    }

    public Course getCourse() {
        return course;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public void setSimMode(boolean isSim) {
        this.simMode = isSim;
    }

    public boolean isSimMode() {
        return simMode;
    }

    private void drawGrid(Graphics g) {
        g.setColor(Color.LIGHT_GRAY);
        // draw vert grids
        for (int i = 0; i <= course.getLength() ; i++) {
            g.drawLine(i * scale, 0, i * scale, course.getHeight() * scale);
        }
        // draw horiz grids
        for (int i = 0; i <= course.getHeight(); i++) {
            g.drawLine(0, i * scale, course.getLength() * scale, i * scale);
        }
        // draw grid dots
        for (int i = 0; i <= course.getLength() ; i++) {
            for (int j = 0; j <= course.getHeight(); j++) {
                g.drawOval(i * scale - DOT / 2, j * scale - DOT / 2, DOT, DOT);
            }
        }
    }

    private void drawWalls(Graphics g) {
        List<LineSegment> walls = course.getWalls();
        if (walls == null)
            return;
        g.setColor(WALL_COLOR);
        for (LineSegment wall: walls) {
            g.drawLine(wall.getStart().getX() * scale, wall.getStart().getY() * scale,
                    wall.getEnd().getX() * scale, wall.getEnd().getY() * scale);
            g.fillOval(wall.getStart().getX() * scale - WALL_DOT / 2, wall.getStart().getY() * scale - WALL_DOT / 2, WALL_DOT, WALL_DOT);
            g.fillOval(wall.getEnd().getX() * scale - WALL_DOT / 2, wall.getEnd().getY() * scale - WALL_DOT / 2, WALL_DOT, WALL_DOT);
        }
    }

    private void drawStartAndCheck(Graphics g) {
        g.setColor(START_COLOR);
        LineSegment startLine = course.getStartLine();
        if (startLine != null) {
            g.drawLine(startLine.getStart().getX() * scale, startLine.getStart().getY() * scale,
                    startLine.getEnd().getX() * scale, startLine.getEnd().getY() * scale);
            g.fillOval(startLine.getStart().getX() * scale - WALL_DOT / 2, startLine.getStart().getY() * scale - WALL_DOT / 2, WALL_DOT, WALL_DOT);
            g.fillOval(startLine.getEnd().getX() * scale - WALL_DOT / 2, startLine.getEnd().getY() * scale - WALL_DOT / 2, WALL_DOT, WALL_DOT);
            drawArrows(startLine, g);
        }

        g.setColor(CHECK_COLOR);
        LineSegment checkPoint = course.getCheckPoint();
        if (checkPoint != null) {
            g.drawLine(checkPoint.getStart().getX() * scale, checkPoint.getStart().getY() * scale,
                    checkPoint.getEnd().getX() * scale, checkPoint.getEnd().getY() * scale);
            g.fillOval(checkPoint.getStart().getX() * scale - WALL_DOT / 2, checkPoint.getStart().getY() * scale - WALL_DOT / 2, WALL_DOT, WALL_DOT);
            g.fillOval(checkPoint.getEnd().getX() * scale - WALL_DOT / 2, checkPoint.getEnd().getY() * scale - WALL_DOT / 2, WALL_DOT, WALL_DOT);
            drawArrows(checkPoint, g);
        }
    }

    private void drawArrows(LineSegment line, Graphics g) {
        // model arrow with return to left
        double tipX = 0, tipY = -1 * scale;
        double retX = -1 * scale / 4, retY = tipY * 2/3;

        // info about this line
        int dx = (line.getEnd().getX() - line.getStart().getX()) * scale;
        int dy = (line.getEnd().getY() - line.getStart().getY()) * scale;
        double length = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
        double theta = Math.atan(1.0 * dy / dx);
        if (dx < 0)
            theta += Math.PI;

        // rotate model
        int tipXPrime = (int) Math.round(tipX * Math.cos(theta) - tipY * Math.sin(theta));
        int tipYPrime = (int) Math.round(tipY * Math.cos(theta) + tipX * Math.sin(theta));
        int retXPrimeLeft = (int) Math.round(retX * Math.cos(theta) - retY * Math.sin(theta));
        int retYPrimeLeft = (int) Math.round(retY * Math.cos(theta) + retX * Math.sin(theta));
        int retXPrimeRight = (int) Math.round(-1 * retX * Math.cos(theta) - retY * Math.sin(theta));
        int retYPrimeRight = (int) Math.round(retY * Math.cos(theta) - retX * Math.sin(theta));

        int tipYPrimeRight = (int) Math.round(tipY * Math.cos(theta) - tipX * Math.sin(theta));

        // draw arrows
        // left side
        g.drawLine(line.getStart().getX() * scale, line.getStart().getY() * scale,
                line.getStart().getX() * scale + tipXPrime, line.getStart().getY() * scale + tipYPrime);
        g.drawLine(line.getStart().getX() * scale + tipXPrime, line.getStart().getY() * scale + tipYPrime,
                line.getStart().getX() * scale + retXPrimeLeft, line.getStart().getY() * scale + retYPrimeLeft);
        g.drawLine(line.getStart().getX() * scale + tipXPrime, line.getStart().getY() * scale + tipYPrime,
                line.getStart().getX() * scale + retXPrimeRight, line.getStart().getY() * scale + retYPrimeRight);
        // right side
        g.drawLine(line.getEnd().getX() * scale, line.getEnd().getY() * scale,
                line.getEnd().getX() * scale + tipXPrime, line.getEnd().getY() * scale + tipYPrime);
        g.drawLine(line.getEnd().getX() * scale + tipXPrime, line.getEnd().getY() * scale + tipYPrime,
                line.getEnd().getX() * scale + retXPrimeRight, line.getEnd().getY() * scale + retYPrimeRight);
        g.drawLine(line.getEnd().getX() * scale + tipXPrime, line.getEnd().getY() * scale + tipYPrime,
                line.getEnd().getX() * scale + retXPrimeLeft, line.getEnd().getY() * scale + retYPrimeLeft);
    }

    public void drawCars(Graphics g) {
        if (race == null) { return; }
        if(race.getCars() == null) { return; }
        // draw all the cars (with paths)
        for (Car car : race.getCars()) {
            if(simMode) {
                car.draw(g, scale, race.getTurn());
            } else {
                car.draw(g, scale, TYP_CAR_DOT);
            }
        }
    }

    private void drawActiveCar(Graphics g) {
        if (race == null || simMode) { return; }
        Car car = race.getActiveCar();
        if(car == null)
            return;
        car.draw(g, scale, ACTIVE_CAR_DOT);
        if (!car.isFinished() && !car.isCrashed())
            car.drawOptions(g, scale);
    }
}

