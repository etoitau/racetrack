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

// button to take you back to drawing menu
public class BackToDrawButtonListener extends DrawButtonListener {
    public BackToDrawButtonListener(UserInterface ui) {
        super(ui);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.clearMouseListeners();

        // setup for drawing
        ui.drawSetup();
    }
}
