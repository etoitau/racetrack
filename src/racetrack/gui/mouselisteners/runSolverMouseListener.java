/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

package racetrack.gui.mouselisteners;

import racetrack.gui.UserInterface;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * start or pause ai solver
 */
public class runSolverMouseListener extends MouseAdapter {
    protected UserInterface ui;

    public runSolverMouseListener(UserInterface ui) {
        this.ui = ui;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        ui.runSolver();
    }
}
