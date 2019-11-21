/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.gui.mouselisteners;

import racetrack.domain.LineSegment;
import racetrack.domain.Point;
import racetrack.gui.CourseDisplay;

import java.awt.event.MouseEvent;

/**
 * for drawing checkpoint
 */
public class MouseDrawCheckListener extends CourseMouseListener {
    private int x1, y1, x2, y2;
    private boolean secondClick = false;

    public MouseDrawCheckListener(CourseDisplay coursePanel) {
        super(coursePanel);
    }

    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        // get start of line with first click
        // second click gets end of line and does add/remove
        if(!secondClick) {
            x1 = x;
            y1 = y;
            secondClick = true;
        } else {
            x2 = x;
            y2 = y;
            secondClick = false;

            LineSegment checkLine = new LineSegment(new Point(x1, y1), new Point(x2, y2));
            coursePanel.getCourse().setCheckPoint(checkLine);

            coursePanel.repaint();
        }
    }
}
