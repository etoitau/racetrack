/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

package racetrack.gui.buttonlisteners;

import racetrack.gui.UserInterface;

import java.awt.event.ActionEvent;

public class RaceButtonListener extends DrawButtonListener {

    private int numberOfRacers;

    public RaceButtonListener(UserInterface ui, int numberOfRacers) {
        super(ui);
        this.numberOfRacers = numberOfRacers;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clearMouseListeners();
        if (numberOfRacers > 0) {
            ui.raceStart(numberOfRacers);
        } else {
            ui.runSolver();
        }
    }
}
