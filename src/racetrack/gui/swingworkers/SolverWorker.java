/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

package racetrack.gui.swingworkers;

import racetrack.domain.Car;
import racetrack.game.CourseSolver;
import racetrack.gui.CourseDisplay;
import racetrack.gui.UserInterface;

import javax.swing.*;
import java.awt.*;

/**
 * ai solution is run in separate thread so it can be paused or canceled if user wants
 */
public class SolverWorker extends SwingWorker {
    private UserInterface ui;
    private boolean paused = false;

    public boolean isPaused() {
        return paused;
    }

    public SolverWorker(UserInterface ui) {
        this.ui = ui;
    }

    // https://stackoverflow.com/a/26265786/11517662
    public void pause() {
        if (!isPaused() && !isDone()) {
            paused = true;
        }
    }

    public final void resume() {
        if (isPaused() && !isDone()) {
            paused = false;
        }
    }

    /**
     * manage ai solution of course
     */
    @Override
    protected Object doInBackground() {
        // get gui elements
        CourseDisplay courseDisplay = ui.getCourseDisplay();
        JTextPane info = ui.getInfo();

        // used for paused... animation
        int pauseCounter = 0;

        // initialize messages
        ui.getMessage().setText("Red: random recent run, Blue: fastest finish so far");
        ui.getMessage().paintImmediately(ui.getMessage().getVisibleRect());
        info.setText("Working...");
        info.paintImmediately(ui.getInfo().getVisibleRect());

        // setup the solver and start the clock
        CourseSolver solver = new CourseSolver(courseDisplay);
        long runStartTime = solver.getRunStartTime();
        long startTime = runStartTime;

        // as long as not all options are exhausted
        while(solver.hasNext()) {
            // if solver is not paused
            if (!isPaused()) {
                // get the next run from solver
                Car car = solver.nextCar();

                // every few seconds, but most recent run on the screen and show solver report in info pane
                long thisTime = System.currentTimeMillis();
                if (thisTime - startTime > 4000) {
                    startTime = thisTime;
                    if (courseDisplay.getRace().getCars().size() == 0) {
                        courseDisplay.getRace().getCars().add(car);
                    } else {
                        courseDisplay.getRace().getCars().set(0, car);
                    }
                    courseDisplay.paintNow();
                    info.setText(solver.report());
                    info.paintImmediately(info.getVisibleRect());
                    // also best finisher if there is one
                    if (solver.getBestCar() != null) {
                        courseDisplay.getRace().setActiveCar(new Car(solver.getBestCar(), Color.BLUE));
                    }
                }
            // if paused
            } else {
                try {
                    // show paused... animation and check for unpause every second
                    StringBuilder sb = new StringBuilder("Paused");
                    for (int i = 0; i < pauseCounter % 4; i++) {
                        sb.append(".");
                    }
                    info.setText(sb.toString());
                    Thread.sleep(1000);
                    pauseCounter++;
                } catch (InterruptedException e) {
                    // ignore
                }
            }
        }
        // when done, show final solution and report, enable back button
        info.setText(solver.report());
        courseDisplay.getRace().getCars().clear();
        if (solver.getBestCar() != null) {
            courseDisplay.getRace().setActiveCar(new Car(solver.getBestCar(), Color.BLUE));
            courseDisplay.paintNow();
        }
        ui.getBack().setEnabled(true);
        return null;
    }
}
