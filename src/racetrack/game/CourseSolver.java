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

/**
 * Manager for ai solution
 * keeps track of persistent solution data and serves as interface for ui
 */
public class CourseSolver {
    private Course course;
    private LineSegment start;
    private LineSegment init;

    private StepExplorer explorer;
    private int maxTurns, minToCheck = Integer.MAX_VALUE, minToFinish = Integer.MAX_VALUE, checkPointGrace = 3;
    private Car bestCar;
    private long runs = 0, runStartTime;
    private short startBias;

    // getters / setters
    public LineSegment getStart() {
        return start;
    }

    public LineSegment getInit() {
        return init;
    }

    public Course getCourse() {
        return course;
    }

    public int getMaxTurns() {
        return maxTurns;
    }

    public int getMinToCheck() {
        return minToCheck;
    }

    public void setMinToCheck(int minToCheck) {
        this.minToCheck = minToCheck;
    }

    public int getMinToFinish() {
        return minToFinish;
    }

    public void setMinToFinish(int minToFinish) {
        this.minToFinish = minToFinish;
    }

    public int getCheckPointGrace() {
        return checkPointGrace;
    }

    public Car getBestCar() {
        return bestCar;
    }

    public void setBestCar(Car car) {
        this.bestCar = car;
    }

    public StepExplorer getExplorer() {
        return explorer;
    }

    public short getStartBias() {
        return startBias;
    }

    public long getRunStartTime() {
        return runStartTime;
    }

    public void setRunStartTime(long time) {
        this.runStartTime = time;
    }

    // constructor
    public CourseSolver(CourseDisplay cd) {
        this.course = cd.getCourse();
        this.maxTurns = maxPath();
        this.start = course.getStartLine();
        this.init = initialVector();
        this.runStartTime = System.currentTimeMillis();
        this.startBias = findStartDirection(); // try this starting move first
        this.explorer = new StepExplorer(startBias);
    }

    // car's initial vector
    private LineSegment initialVector() {
        int startX = (start.getStart().getX() + start.getEnd().getX()) / 2;
        int startY = (start.getStart().getY() + start.getEnd().getY()) / 2;
        return new LineSegment(new Point(startX, startY), new Point(startX, startY));
    }

    // check if explorer has another path to try
    public boolean hasNext() {
        return explorer.hasNext();
    }

    // use Solution to run a car and return it
    public Car nextCar() {
        runs++;
        Solution solution = new Solution(this);
        return solution.runCar();
    }

    // what is the max possible optimum solution length? If run is going longer it can be stopped
    private int maxPath() {
        // assuming you move forward one space each turn and walls are laid out for maximum path:
        // labyrinth from one corner to other then backtrack
        return 2 * (course.getLength() - 2 + (course.getHeight() - 2) * course.getLength() / 2);
    }

    // get html report of solution progress
    public String report() {
        if (runs == 0) {
            return "Not run yet";
        }
        StringBuilder sb = new StringBuilder("<html><body>Solver Report");
        sb.append("<br>Runs performed: ").append(runs);
        sb.append("<br>Elapsed time: ").append((System.currentTimeMillis() - runStartTime) / 1000).append(" seconds");
        if (minToCheck > maxTurns) {
            sb.append(("<br>None made it to checkpoint"));
        } else {
            sb.append("<br>Best time to checkpoint: ").append(minToCheck);
        }
        if (bestCar == null) {
            sb.append("<br>No finishers");
        } else {
            sb.append("<br>Best finisher: ").append(minToFinish);
        }
        if (!explorer.hasNext()) {
            sb.append("<br><br><b>Run complete</b>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    /**
     * for most courses, the best first move will be to move away from starting line in direction it points
     * point option indexes correspond to directions as follows:
     * 0 3 6
     * 1 4 7
     * 2 5 8
     */
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
        // if diagonal
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
