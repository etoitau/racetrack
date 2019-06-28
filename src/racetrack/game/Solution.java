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
import racetrack.domain.StepExplorer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Solution {
    private CourseSolver solver;

    public Solution(CourseSolver solver) {
        this.solver = solver;
    }

    public Car runCar() {
        // initialize car for run
        List<Short> steps = new ArrayList<>();
        Car car = new Car(new LineSegment(solver.getInit()), solver.getCourse(), Color.RED);
        StepExplorer explorer = solver.getExplorer();

        short step = explorer.getFirstStep();
        if (step < 0) {
            return car;
        }
        steps.add(step);

        runCar:
        while (true) {
            short bias = solver.getStdBias();
            if (steps.size() == 1) {
                bias = solver.getStartBias();
            }
            // execute step
            Point dest = car.getVector().getEnd().adjacents().get( (step + bias) % 9);
            car.move(dest);

            // check outcome of move
            int outcome = this.checkMove(car, steps);
            // if no good
            if (outcome == 0) {
                explorer.killCurrent();
                return car;
            // if just passed checkpoint
            } else if (outcome == 2) {
                if (steps.size() < solver.getMinToCheck()) {
                    solver.setMinToCheck(steps.size());
                }
            // if just passed finish
            } else if (outcome == 3) {
                if (steps.size() < solver.getMinToFinish()) {
                    solver.setMinToFinish(steps.size());
                    solver.setBestCar(car);
                }
                explorer.killCurrent();
                return car;
            }
            // get next step
            step = explorer.getNextStep();
            if (step < 0) {
                return car;
            }
            // add step to steps
            steps.add(step);
        }
    }

    private int checkMove(Car car, List<Short> steps) {
        // return:
        // 0 - no good
        // 1 - nothing to report
        // 2 - just passed checkpoint
        // 3 - just passed finish line
        LineSegment lastMove = car.getLastMove();
        int outcome = 1;
        // check for crash
        if (car.hitsWall(lastMove)) {
            return 0;
        }
        // check past checkpoint
        if (lastMove.gateCross(solver.getCourse().getCheckPoint())) {
            car.setIsPastCheckpoint(true);
            outcome = 2;
        }
        // check if just crossed finish line (after checkpoint)
        if (car.isPastCheckpoint()) {
            if (lastMove.gateCross(solver.getStart())) {
                return 3;
            }
        }
        // check for too slow to checkpoint
        if (!car.isPastCheckpoint()) {
            if (steps.size() - solver.getCheckPointGrace() > solver.getMinToCheck()) {
                return 0;
            }
        }
        // check for time to finish
        if (steps.size() > solver.getMinToFinish()) {
            return 0;
        }
        // check run is too long
        if (steps.size() > solver.getMaxTurns()) {
            return 0;
        }
        // check for looping
        List<LineSegment> path = car.getPath();
        for (int i = 0; i < path.size() - 1; i++) {
            if (path.get(i).equals(lastMove)) {
                return 0;
            }
        }
        return outcome;
    }



}
