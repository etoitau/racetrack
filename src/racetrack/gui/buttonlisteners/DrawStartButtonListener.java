/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.gui.buttonlisteners;

import racetrack.gui.mouselisteners.MouseDrawStartListener;
import racetrack.gui.UserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * prepare ui to receive input about starting line from user via mouse
 */
public class DrawStartButtonListener extends DrawButtonListener {

    public DrawStartButtonListener(UserInterface ui) {
        super(ui);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        super.actionPerformed(e);

        // highlight this button
        JToggleButton thisButton = (JToggleButton) e.getSource();
        thisButton.setSelected(true);

        // add mouse listener for this function
        ui.getCourseDisplay().addMouseListener(new MouseDrawStartListener(ui.getCourseDisplay()));

        // add instructions to user
        ui.getMessage().setText("Click two points to draw the starting line from racer's left to right");
    }
}
