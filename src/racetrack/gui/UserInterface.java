/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

package racetrack.gui;

import racetrack.game.Course;
import racetrack.gui.buttonlisteners.DrawCheckButtonListener;
import racetrack.gui.buttonlisteners.DrawStartButtonListener;
import racetrack.gui.buttonlisteners.DrawWallButtonListener;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class UserInterface implements Runnable {

    private JFrame window;
    private int scale = 25, length = 30, height = 20;
    private CourseDisplay courseDisplay;
    private List<JToggleButton> buildButtons;
    private JLabel message;

    @Override
    public void run() {
        window = new JFrame("Course Creation");
        window.setBackground(Color.WHITE);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createComponents(window.getContentPane());

        window.pack();
        window.setVisible(true);
    }

    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());

        Course course = new Course(length, height);
        courseDisplay = new CourseDisplay(course, scale);
        courseDisplay.setPreferredSize(new Dimension(length * scale + scale, height * scale + scale));
        container.add(courseDisplay, BorderLayout.CENTER);

        JLabel title = new JLabel("Racetrack Builder");
        container.add(title, BorderLayout.NORTH);

        message = new JLabel("");
        container.add(message, BorderLayout.SOUTH);

        container.add(builderButtons(), BorderLayout.WEST);


    }

    private JPanel builderButtons() {
        JPanel buttonPanel = new JPanel();
        BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(layout);

        JToggleButton drawWalls = new JToggleButton("Draw Walls");
        JToggleButton drawStart = new JToggleButton("Draw Start");
        JToggleButton drawCheck = new JToggleButton("Draw CheckPoint");
        JButton race = new JButton("Done");

        buildButtons = new ArrayList<JToggleButton>();
        buildButtons.add(drawWalls);
        buildButtons.add(drawStart);
        buildButtons.add(drawCheck);

        drawWalls.addActionListener(new DrawWallButtonListener(this));
        drawStart.addActionListener(new DrawStartButtonListener(this));
        drawCheck.addActionListener(new DrawCheckButtonListener(this));
//        race.addActionListener();

        buttonPanel.add(drawWalls);
        buttonPanel.add(drawStart);
        buttonPanel.add(drawCheck);
        buttonPanel.add(race);
        return buttonPanel;
    }

    public JFrame getWindow() {
        return window;
    }

    public int getScale() {
        return scale;
    }

    public CourseDisplay getCourseDisplay() {
        return courseDisplay;
    }

    public List<JToggleButton> getBuildButtons() {
        return buildButtons;
    }

    public JLabel getMessage() {
        return message;
    }
}
