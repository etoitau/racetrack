/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.gui.mouselisteners;

import racetrack.gui.CourseDisplay;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Basis for several mouse listeners that need to pick points on the course
 */
public class CourseMouseListener extends MouseAdapter {
    protected CourseDisplay coursePanel;
    protected int x, y;

    public CourseMouseListener(CourseDisplay coursePanel) {
        this.coursePanel = coursePanel;
    }

    // converts mouse coordinates into game grid coordinates
    @Override
    public void mousePressed(MouseEvent e) {
        x = (int) Math.round(1.0 * e.getX() / coursePanel.getScale());
        y = (int) Math.round(1.0 * e.getY() / coursePanel.getScale());
    }

}
