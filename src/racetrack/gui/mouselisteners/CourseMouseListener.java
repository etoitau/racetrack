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

import racetrack.gui.CourseDisplay;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CourseMouseListener extends MouseAdapter {
    protected CourseDisplay coursePanel;
    protected int x, y;

    public CourseMouseListener(CourseDisplay coursePanel) {
        this.coursePanel = coursePanel;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = (int) Math.round(1.0 * e.getX() / coursePanel.getScale());
        y = (int) Math.round(1.0 * e.getY() / coursePanel.getScale());

    }

}
