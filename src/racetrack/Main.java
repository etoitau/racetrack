/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
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
