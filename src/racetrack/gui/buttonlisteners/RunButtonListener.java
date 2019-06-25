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
import racetrack.gui.mouselisteners.MouseDrawWallsListener;

import javax.swing.*;
import java.awt.event.ActionEvent;

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
