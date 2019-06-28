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

import racetrack.gui.UserInterface;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
 * The drawing buttons all extend this
 * for each of them we want buttons to be highlighted while drawing option is active
 */
public class DrawButtonListener implements ActionListener {

    protected UserInterface ui;

    public DrawButtonListener(UserInterface ui) {
        this.ui = ui;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        clearMouseListeners();
        clearButtonHighlights();
    }

    // clean up any old mouse listeners
    protected void clearMouseListeners() {
        MouseListener[] mouseListeners = ui.getCourseDisplay().getMouseListeners();
        if (mouseListeners != null && mouseListeners.length > 0) {
            for (int i = 0; i < mouseListeners.length; i++) {
                ui.getCourseDisplay().removeMouseListener(mouseListeners[i]);
            }
        }
    }

    // clear old button highlights
    private void clearButtonHighlights() {
        for (JToggleButton aButton: ui.getBuildButtons()) {
            aButton.setSelected(false);
        }
    }
}
