/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

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

import racetrack.gui.mouselisteners.MouseDrawStartListener;
import racetrack.gui.UserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;

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
