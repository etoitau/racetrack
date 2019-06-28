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

import racetrack.domain.Point;
import racetrack.game.Race;
import racetrack.gui.CourseDisplay;
import racetrack.gui.UserInterface;

import java.awt.event.MouseEvent;
import java.util.List;

/**
 * during race, picking which move the user wants
 */
public class MouseRaceListener extends CourseMouseListener {
    private UserInterface ui;

    public MouseRaceListener(CourseDisplay coursePanel, UserInterface ui) {
        super(coursePanel);
        this.ui = ui;
    }

    // if they clicked on an available option, update the race with their selection
    @Override
    public void mousePressed(MouseEvent e) {
        super.mousePressed(e);
        if(validOption()) {
            Race race = coursePanel.getRace();
            race.updateCar(race.getActiveCar(), new Point(x, y));
            ui.raceUpdate();
            ui.getCourseDisplay().repaint();
        }
    }

    // check if click corresponds with an available option
    private boolean validOption() {
        List<Point> options = coursePanel.getRace().getActiveCar().options();
        for (Point point: options) {
            if (point.getX() == x && point.getY() == y) {
                return true;
            }
        }
        return false;
    }
}
