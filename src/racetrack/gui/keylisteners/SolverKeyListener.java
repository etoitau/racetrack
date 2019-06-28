/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

package racetrack.gui.keylisteners;

import racetrack.gui.UserInterface;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SolverKeyListener implements KeyListener {
    UserInterface ui;

    public SolverKeyListener(UserInterface ui) {
        this.ui = ui;
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // nothing
    }

    @Override
    public void keyPressed(KeyEvent e) {
        ui.runSolver();
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // nothing
    }
}
