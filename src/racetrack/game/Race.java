/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.game;

import racetrack.domain.Car;
import racetrack.domain.LineSegment;
import racetrack.domain.Point;
import racetrack.gui.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Race object has information about the cars in the race
 */
public class Race {
    private List<Car> cars;
    private Car activeCar;
    private int turn = 0, index = 0;
    private ColorGenerator colorGen;
    private Course course;

    // getters / setters
    public List<Car> getCars() {
        return cars;
    }

    public void setActiveCar(Car car) {
        this.activeCar = car;
    }

    public Car getActiveCar() {
        return activeCar;
    }

    public int getTurn() {
        return turn;
    }

    // constructor
    public Race(Course course, int numberOfRacers) {
        colorGen = new ColorGenerator();
        cars = new ArrayList<>(numberOfRacers);
        this.course = course;
        // initialize cars in the race
        LineSegment start = course.getStartLine();
        int startX = (start.getStart().getX() + start.getEnd().getX()) / 2;
        int startY = (start.getStart().getY() + start.getEnd().getY()) / 2;
        for (int i = 0; i < numberOfRacers; i++) {
            cars.add(new Car(new LineSegment(new Point(startX, startY), new Point(startX, startY)), course, colorGen.getColor()));
        }
        if (numberOfRacers > 0)
            activeCar = cars.get(index);
    }

    // get car whose turn it is, advance turn if it's back to first car
    public void nextCar() {
        activeCar = cars.get(index);
        index++;
        if (index == cars.size()) {
            index = 0;
            turn++;
        }
    }

    // update car's status based on recent move
    public void updateCar(Car car, Point point) {
        // move car to point
        car.move(point);
        // check if just crossed checkpoint
        LineSegment lastMove = car.getLastMove();
        if (lastMove.gateCross(course.getCheckPoint())) {
            car.setIsPastCheckpoint(true);
        }
        // check if just crossed finish line (after checkpoint)
        if (car.isPastCheckpoint()) {
            if (lastMove.gateCross(course.getStartLine())) {
                car.setFinished(true);
            }
        }
        // check if all possible moves from here are crashes
        if (car.options().size() == 0 && !car.isFinished()) {
            car.setCrashed(true);
        }
    }
}
