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

    protected void clearMouseListeners() {
        // clear any mouse listeners for other functions
        MouseListener[] mouseListeners = ui.getCourseDisplay().getMouseListeners();
        if (mouseListeners != null && mouseListeners.length > 0) {
            for (int i = 0; i < mouseListeners.length; i++) {
                ui.getCourseDisplay().removeMouseListener(mouseListeners[i]);
            }
        }
    }

    private void clearButtonHighlights() {
        // clear button highlights
        for (JToggleButton aButton: ui.getBuildButtons()) {
            aButton.setSelected(false);
        }
    }
}
