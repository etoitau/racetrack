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
 * When user is done drawing and can proceed to race setup
 */
public class RunButtonListener extends DrawButtonListener {

    public RunButtonListener(UserInterface ui) {
        super(ui);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        if (ui.getCourseDisplay().getCourse().getStartLine() == null) {
            ui.getMessage().setText("Starting line required");
            return;
        }
        if (ui.getCourseDisplay().getCourse().getCheckPoint() == null) {
            ui.getMessage().setText("Checkpoint line required");
            return;
        }

        // get new buttons
        ui.raceSetup();
    }
}
