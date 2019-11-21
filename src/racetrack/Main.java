/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack;

import racetrack.gui.UserInterface;

import javax.swing.*;

/**
 * Racetrack
 * a Java implementation of the classic pen and paper game
 * I learned about it here: http://www.papg.com/show?1TPE
 */
public class Main {
    public static void main(String[] args) {
        // run gui
        SwingUtilities.invokeLater(new UserInterface());
    }
}
