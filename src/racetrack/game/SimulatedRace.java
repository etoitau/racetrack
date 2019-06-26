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
import racetrack.domain.TimeComparator;
import racetrack.gui.ColorGenerator;

import java.util.*;

public class SimulatedRace {
    private Course course;
    private Race race;

    public SimulatedRace(Course course) {
        this.course = course;
        simulate();
    }

    private void simulate() {
        race = new Race(course, 1);
        List<Car> cars = race.getCars();
        ColorGenerator colorGen = new ColorGenerator();
        boolean cont = true;
        while (cont) {
            cont = false;
            // advance each car and add branches to take every option
            ListIterator<Car> carIterator = cars.listIterator();
            while (carIterator.hasNext()) {
                Car car = carIterator.next();
                if (car.isCrashed()) {
                    carIterator.remove();
                    continue;
                }
                if (car.isFinished())
                    continue;
                cont = true;
                List<Point> options = car.options();
                if (options.size() == 0) {
                    car.setCrashed(true);
                    continue;
                }
                if (options.size() > 1) {
                    for (int i = 1; i < options.size(); i++) {
                        Car branchCar = new Car(car, colorGen.getColor());
                        carIterator.add(branchCar);
                        race.updateCar(branchCar, options.get(i));
                    }
                }
                race.updateCar(car, options.get(0));
            }
            race.setTurn(race.getTurn() + 1);
            if (race.getTurn() > maxPath())
                break;
            crashCars();

        }
    }

    private void crashCars() {
        // if looping, crash it
        cars:
        for (Car car: race.getCars()) {
            List<LineSegment> path = car.getPath();
            LineSegment last = car.getLastMove();
            for (int i = 0; i < path.size() - 1; i++) {
                if (path.get(i).equals(last)) {
                    car.setCrashed(true);
                    continue cars;
                }
            }
        }
    }

    private int maxPath() {
        // assuming you move forward one space each turn and walls are laid out for maximum path
        return 2 * (course.getLength() - 2) + 2 * (course.getHeight() - 2) + (course.getLength() - 4) / 2 * (course.getHeight() - 4);
    }

    public List<Car> finishers() {
        List<Car> finishers = new ArrayList<Car>();
        for (Car car: race.getCars()) {
            if (car.isFinished())
                finishers.add(car);
        }
        Collections.sort(finishers, new TimeComparator());
        return finishers;
    }

    public Race getRace() {
        return race;
    }
}
