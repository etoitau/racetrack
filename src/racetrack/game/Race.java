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

import racetrack.domain.Car;
import racetrack.domain.LineSegment;
import racetrack.domain.Point;
import racetrack.gui.ColorGenerator;

import java.util.ArrayList;
import java.util.List;

public class Race {
    private List<Car> cars;
    private Car activeCar;
    private int turn = 0, index = 0;
    private ColorGenerator colorGen;
    private Course course;

    public Race(Course course, int numberOfRacers) {
        colorGen = new ColorGenerator();
        cars = new ArrayList<Car>(numberOfRacers);
        this.course = course;
        LineSegment start = course.getStartLine();
        int startX = (start.getStart().getX() + start.getEnd().getX()) / 2;
        int startY = (start.getStart().getY() + start.getEnd().getY()) / 2;
        Point startPoint = new Point(startX, startY);
        for (int i = 0; i < numberOfRacers; i++) {
            cars.add(new Car(new LineSegment(startPoint, startPoint), course, colorGen.getColor()));
        }
    }

    public void nextCar() {
        activeCar = cars.get(index);
        index++;
        index %= cars.size();
        if (activeCar.isCrashed())
            this.nextCar();
    }

    public void setActiveCar(Car car) {
        this.activeCar = car;
    }

    public Car getActiveCar() {
        return activeCar;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void updateCar(Car car, Point point) {
        // move car to point and do checks
        car.move(point);
        // check if all possible moves are crashes
        if (car.options().size() == 0) {
            car.setCrashed(true);
        }
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

    }

    public int getTurn() {
        return turn;
    }

    public int getIndex() {
        return index;
    }


}
