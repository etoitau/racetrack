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


import racetrack.domain.LineSegment;
import racetrack.domain.Point;
import racetrack.gui.UserInterface;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // test intersection
//        Point aStart = new Point(0, 0);
//        Point aEnd = new Point(0, 3);
//        LineSegment a = new LineSegment(aStart, aEnd);
//
//        Point bStart = new Point(0, 2);
//        Point bEnd = new Point(0, 4);
//        LineSegment b = new LineSegment(bStart, bEnd);
//
//        System.out.println(b.crosses(a));

        // test move
//        Point start = new Point(0,0);
//        Point end = new Point(start);
//        LineSegment vector = new LineSegment(start, end);
        // run gui
        SwingUtilities.invokeLater(new UserInterface());
    }
}
