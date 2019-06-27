/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

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

import racetrack.domain.*;
import racetrack.domain.Point;
import racetrack.gui.ColorGenerator;
import racetrack.gui.CourseDisplay;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

public class CourseSolver {
    private Course course;
    private CourseDisplay courseDisplay;
    private int maxTurns, minToCheck = Integer.MAX_VALUE, minToFinish = Integer.MAX_VALUE, checkPointGrace = 3;
    private List<Short> bestSteps;
    private Car bestCar;
    private StepExplorer explorer;
    private long runs = 0;

    public CourseSolver(CourseDisplay cd) {
        this.course = cd.getCourse();
        this.courseDisplay = cd;
        this.maxTurns = maxPath();
        this.explorer = new StepExplorer();
        simulate();
    }

    private void simulate() {
        LineSegment start = course.getStartLine();
        int startX = (start.getStart().getX() + start.getEnd().getX()) / 2;
        int startY = (start.getStart().getY() + start.getEnd().getY()) / 2;
        LineSegment init = new LineSegment(new Point(startX, startY), new Point(startX, startY));
        long startTime = System.currentTimeMillis();
        short startBias = findStartDirection();
        short stdBias = 4;
        // bias says which direction to try first
        // for first step we pick direction start is pointing
        // after that default is to coast
        
        eachCar:
        while (explorer.hasNext()) {

            // initialize car for run
            List<Short> steps = new ArrayList<>();
            Car car = new Car(new LineSegment(init), course, Color.RED);
            short step = explorer.getFirstStep();
            steps.add(step);
            runs++;
            // termporary limit on how long it runs
//            if (runs > 500000) {
//                break;
//            }

            // run course using steps
            runCar:
            while (true) {
                short bias = stdBias;
                if (steps.size() == 1) {
                    bias = startBias;
                }
                // execute step
                Point dest = car.getVector().getEnd().adjacents().get( (step + bias) % 9);
                car.move(dest);
                int outcome = this.checkMove(car, steps);
                // display run on exit
                long thisTime = System.currentTimeMillis();
                if (outcome != 1 && thisTime - startTime > 4000) {
                    startTime = thisTime;
                    courseDisplay.getRace().setActiveCar(car);
                    courseDisplay.paintNow();
                    System.out.println(this.report());
                }

                // if no good
                if (outcome == 0) {
                    explorer.killCurrent();
//                    System.out.println(steps);
//                    System.out.println(car.getPath());
                    continue eachCar;
                // if just passed checkpoint
                } else if (outcome == 2) {
                    if (steps.size() < minToCheck) {
                        minToCheck = steps.size();
                    }
                // if just passed finish
                } else if (outcome == 3) {
                    if (steps.size() < minToFinish) {
                        minToFinish = steps.size();
                        bestSteps = steps;
                        bestCar = car;
                        if (courseDisplay.getRace().getCars().size() == 0) {
                            courseDisplay.getRace().getCars().add(new Car(car, Color.BLUE));
                        } else {
                            courseDisplay.getRace().getCars().set(0, new Car(car, Color.BLUE));
                        }
                    }
                    explorer.killCurrent();
                    continue eachCar;
                }
                // get next step
                step = explorer.getNextStep();
                if (step < 0) {
                    continue eachCar;
                }
                // add step to steps
                steps.add(step);
            }
        }
        System.out.println("Solver done");
        courseDisplay.getRace().setActiveCar(bestCar);
        courseDisplay.paintNow();
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
        if (lastMove.gateCross(course.getCheckPoint())) {
            car.setIsPastCheckpoint(true);
            outcome = 2;
        }
        // check if just crossed finish line (after checkpoint)
        if (car.isPastCheckpoint()) {
            if (lastMove.gateCross(course.getStartLine())) {
                car.setFinished(true);
                return 3;
            }
        }
        // check for too slow to checkpoint
        if (!car.isPastCheckpoint()) {
            if (steps.size() - checkPointGrace > minToCheck) {
                return 0;
            }
        }
        // check for time to finish
        if (!car.isFinished() && steps.size() > minToFinish) {
            return 0;
        }
        // check run is too long
        if (steps.size() > maxTurns) {
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

    private int maxPath() {
        // assuming you move forward one space each turn and walls are laid out for maximum path
        return 2 * (course.getLength() - 2) + 2 * (course.getHeight() - 2) + (course.getLength() - 4) / 2 * (course.getHeight() - 4);
    }

    public String report() {
        if (runs == 0) {
            return "Not run yet";
        }
        StringBuilder sb = new StringBuilder("Solver Report");
        sb.append("\nRuns performed: ").append(runs);
        sb.append("\nMax Turns allowed: ").append(maxTurns);
        if (minToCheck > maxTurns) {
            sb.append(("\nNone made it to checkpoint"));
        } else {
            sb.append("\nBest time to checkpoint: ").append(minToCheck);
        }
        if (bestSteps == null) {
            sb.append("\nNo finishers");
        } else {
            sb.append("\nBest finisher: ").append(minToFinish);
        }
        return sb.toString();
    }

    private short findStartDirection() {
        short choice;
        LineSegment startLine = course.getStartLine();
        int dx = startLine.getEnd().getX() - startLine.getStart().getX();
        int dy = startLine.getEnd().getY() - startLine.getStart().getY();
        float riseRun = ((float) dy) / ((float) dx);
        // if flat, want 3 or 5
        if (Math.abs(riseRun) < Math.tan(2 * Math.PI * 1 / 16)) {
            if (dx > 0) {
                choice = 3;
            } else {
                choice = 5;
            }
        // if vertical, want 1 or 7
        } else if (Math.abs(riseRun) > Math.tan(2 * Math.PI * 3 / 16)) {
            if (dy > 0) {
                choice = 7;
            } else {
                choice = 1;
            }
        } else if (dx > 0) {
            if (dy > 0) {
                choice = 6;
            } else {
                choice = 0;
            }
        } else {
            if (dy > 0) {
                choice = 8;
            } else {
                choice = 2;
            }
        }
        return choice;
    }

}
