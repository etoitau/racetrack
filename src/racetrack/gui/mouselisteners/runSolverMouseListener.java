/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
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
