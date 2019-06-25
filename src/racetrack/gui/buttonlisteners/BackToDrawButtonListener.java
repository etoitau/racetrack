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

public class BackToDrawButtonListener extends DrawButtonListener {
    public BackToDrawButtonListener(UserInterface ui) {
        super(ui);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.clearMouseListeners();

        // get draw buttons
        ui.drawSetup();
    }
}
