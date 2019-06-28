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

/**
 * runs a trial AI car and get's outcome
 */
public class Solution {
    private CourseSolver solver;
    private int bias;

    // constructor
    public Solution(CourseSolver solver) {
        this.solver = solver;
        this.bias = solver.getStartBias();
    }

    // run car and manage outcome
    public Car runCar() {
        // initialize car for run
        List<Short> steps = new ArrayList<>();
        Car car = new Car(new LineSegment(solver.getInit()), solver.getCourse(), Color.RED);
        StepExplorer explorer = solver.getExplorer();

        // use explorer to get first step
        short step = explorer.getFirstStep();
        if (step < 0) {
            return car;
        }
        steps.add(step);

        // keep getting next step and moving car until crashes, finishes, or runs out of moves
        while (true) {
            // get and execute step
            Point dest = car.getVector().getEnd().adjacents().get((step + bias) % 9);
            car.move(dest);

            // check outcome of move
            int outcome = this.checkMove(car, steps);
            // if no good
            if (outcome == 0) {
                explorer.killCurrent();
                return car;
            // if just passed checkpoint
            } else if (outcome == 2) {
                // if best time to checkpoint, update
                if (steps.size() < solver.getMinToCheck()) {
                    solver.setMinToCheck(steps.size());
                }
            // if just passed finish
            } else if (outcome == 3) {
                // if best time to finish, update
                if (steps.size() < solver.getMinToFinish()) {
                    solver.setMinToFinish(steps.size());
                    solver.setBestCar(car);
                }
                explorer.killCurrent();
                return car;
            }
            // get next step, if no step - return
            step = explorer.getNextStep();
            if (step < 0) {
                return car;
            }
            // add step to steps
            steps.add(step);
        }
    }

    /**
     * check latest move and return int indicating outcome
     * 0 - no good
     * 1 - nothing to report
     * 2 - just passed checkpoint
     * 3 - just passed finish line
     */
    private int checkMove(Car car, List<Short> steps) {
        LineSegment lastMove = car.getLastMove();
        // assume cruising unless we find otherwise
        int outcome = 1;
        // check for crash
        if (car.hitsWall(lastMove)) {
            return 0;
        }
        // check just passed checkpoint
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
        // check for too slow to finish
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
