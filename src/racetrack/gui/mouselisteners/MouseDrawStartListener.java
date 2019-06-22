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

package racetrack.gui.mouselisteners;

import racetrack.domain.LineSegment;
import racetrack.domain.Point;
import racetrack.gui.CourseDisplay;

import java.awt.event.MouseEvent;

public class MouseDrawStartListener extends CourseMouseListener {
    private int x1, y1, x2, y2;
    private boolean secondClick = false;


    public MouseDrawStartListener(CourseDisplay coursePanel) {
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

            LineSegment wall = new LineSegment(new Point(x1, y1), new Point(x2, y2));
            coursePanel.getCourse().setStartLine(wall);

            coursePanel.repaint();
        }
    }
}
