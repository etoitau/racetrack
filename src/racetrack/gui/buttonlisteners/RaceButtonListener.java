/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.gui.buttonlisteners;

import racetrack.gui.UserInterface;

import java.awt.event.ActionEvent;

/**
 * when user chooses race option and race can start
 */
public class RaceButtonListener extends DrawButtonListener {
    private int numberOfRacers;

    public RaceButtonListener(UserInterface ui, int numberOfRacers) {
        super(ui);
        this.numberOfRacers = numberOfRacers;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clearMouseListeners();
        ui.setupRace(numberOfRacers);
    }
}
