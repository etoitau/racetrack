/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.gui;

import racetrack.domain.Car;
import racetrack.game.Course;
import racetrack.game.Race;
import racetrack.gui.buttonlisteners.*;
import racetrack.gui.mouselisteners.MouseRaceListener;
import racetrack.gui.mouselisteners.runSolverMouseListener;
import racetrack.gui.swingworkers.SolverWorker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class UserInterface implements Runnable {
    // ui components
    private JFrame window;
    private CourseDisplay courseDisplay;
    private List<JToggleButton> buildButtons;
    private List<JButton> raceSetupButtons;
    private JLabel message;
    private JButton back;
    private JTextPane info;
    // ai solver
    private Race race;
    private SolverWorker worker;
    private boolean firstRun = true;
    // configuration
    private int scale = 25, length = 10, height = 8;
    private final Dimension COURSE_DIM = new Dimension(length * scale + 1, height * scale + 1),
            INFO_PANEL_DIM = new Dimension(200, height * scale),
            INFO_LABEL_DIM = new Dimension(180, height * scale - 40),
            BUTTON_DIM = new Dimension( 180, 30);
    private final int FONT_SIZE = 14;

    // getters and setters
    public CourseDisplay getCourseDisplay() {
        return courseDisplay;
    }

    public List<JToggleButton> getBuildButtons() {
        return buildButtons;
    }

    public JLabel getMessage() {
        return message;
    }

    public JButton getBack() {
        return back;
    }

    public JTextPane getInfo() {
        return info;
    }

    // methods
    @Override
    public void run() {
        window = new JFrame("Racetrack");
        window.setBackground(Color.WHITE);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        createComponents(window.getContentPane());

        window.pack();
        window.setVisible(true);
    }

    // initialize and display ui components
    private void createComponents(Container container) {
        container.setLayout(new BorderLayout());

        Course course = new Course(length, height);
        courseDisplay = new CourseDisplay(course, scale);
        courseDisplay.setPreferredSize(COURSE_DIM);

        message = new JLabel(" ");

        container.add(courseDisplay, BorderLayout.CENTER); // 1
        container.add(new JLabel("<html>&nbsp</html>"), BorderLayout.NORTH); // 2
        container.add(message, BorderLayout.SOUTH); // 3
        container.add(new JLabel("<html>&nbsp</html>"), BorderLayout.EAST); // 4

        // at first, want drawing buttons
        drawSetup();
    }

    // set up drawing button pane
    public void drawSetup() {
        // set up drawing buttons
        if (window.getContentPane().getComponentCount() == 5)
            window.getContentPane().remove(4);
        window.getContentPane().add(builderButtons(), BorderLayout.WEST); // 5
        message.setText("Choose drawing option");
    }

    // make panel with drawing buttons
    private JPanel builderButtons() {
        JPanel buttonPanel = new JPanel();
        BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(layout);
        buttonPanel.setPreferredSize(INFO_PANEL_DIM);

        JToggleButton drawWalls = new JToggleButton("Draw Walls");
        JToggleButton drawStart = new JToggleButton("Draw Start");
        JToggleButton drawCheck = new JToggleButton("Draw CheckPoint");
        JButton drawDone = new JButton("Done");

        formatButton(drawWalls, drawStart, drawCheck, drawDone);

        buildButtons = new ArrayList<>();
        buildButtons.add(drawWalls);
        buildButtons.add(drawStart);
        buildButtons.add(drawCheck);

        drawWalls.addActionListener(new DrawWallButtonListener(this));
        drawStart.addActionListener(new DrawStartButtonListener(this));
        drawCheck.addActionListener(new DrawCheckButtonListener(this));
        drawDone.addActionListener(new RunButtonListener(this)); // calls raceSetup if done drawing

        buttonPanel.add(drawWalls);
        buttonPanel.add(drawStart);
        buttonPanel.add(drawCheck);
        buttonPanel.add(drawDone);
        return buttonPanel;
    }

    // set up race configuration pane / reset for new race
    public void raceSetup() {
        removeMouseListeners();
        window.getContentPane().remove(4);
        window.getContentPane().add(raceSetupButtons(), BorderLayout.WEST);
        message.setText("Choose racing mode");
        courseDisplay.setRace(null);
        courseDisplay.paintNow();
    }

    // clean up any drawing or ai control mouse listeners
    private void removeMouseListeners() {
        for (MouseListener listener : window.getContentPane().getMouseListeners())
            window.getContentPane().removeMouseListener(listener);
        for (MouseListener listener : courseDisplay.getMouseListeners())
            window.getContentPane().removeMouseListener(listener);
    }

    // make panel with race configuration buttons
    private JPanel raceSetupButtons() {
        JPanel buttonPanel = new JPanel();
        BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(layout);
        buttonPanel.setPreferredSize(INFO_PANEL_DIM);

        JButton aiRace = new JButton("AI Solver");
        JButton soloRace = new JButton("Solo Race");
        JButton twoRacers = new JButton("Two Racers");
        JButton threeRacers = new JButton("Three Racers");
        JButton fourRacers = new JButton("Four Racers");
        JButton backToDraw = new JButton("Back to Draw");

        formatButton(aiRace, soloRace, twoRacers, threeRacers, fourRacers, backToDraw);

        raceSetupButtons = new ArrayList<JButton>();
        raceSetupButtons.add(aiRace);
        raceSetupButtons.add(soloRace);
        raceSetupButtons.add(twoRacers);
        raceSetupButtons.add(threeRacers);
        raceSetupButtons.add(fourRacers);
        raceSetupButtons.add(backToDraw);

        aiRace.addActionListener(new RaceButtonListener(this, 0)); // calls runSolver
        soloRace.addActionListener(new RaceButtonListener(this, 1)); // rest call raceStart
        twoRacers.addActionListener(new RaceButtonListener(this, 2));
        threeRacers.addActionListener(new RaceButtonListener(this, 3));
        fourRacers.addActionListener(new RaceButtonListener(this, 4));
        backToDraw.addActionListener(new BackToDrawButtonListener(this));

        buttonPanel.add(aiRace);
        buttonPanel.add(soloRace);
        buttonPanel.add(twoRacers);
        buttonPanel.add(threeRacers);
        buttonPanel.add(fourRacers);
        buttonPanel.add(backToDraw);
        return buttonPanel;
    }

    // sets up for AI or player race
    public void setupRace(int racers) {
        race = new Race(courseDisplay.getCourse(), racers);
        courseDisplay.setRace(race);

        window.getContentPane().remove(4);

        JPanel raceInfo = new JPanel();
        raceInfo.setPreferredSize(INFO_PANEL_DIM);
        BorderLayout layout = new BorderLayout();
        raceInfo.setLayout(layout);

        String infoText = (racers > 0) ? raceStatus() : "<html>Click course<br>to start</html>";
        info = new JTextPane();
        info.setContentType("text/html");
        info.setMinimumSize(INFO_LABEL_DIM);
        info.setOpaque(false);
        info.setEditable(false);
        info.setText(infoText);

        back = new JButton("Back to Setup");
        formatButton(back);

        back.addActionListener(new RunButtonListener(this));

        raceInfo.add(info);
        raceInfo.add(back, BorderLayout.SOUTH);

        window.getContentPane().add(raceInfo, BorderLayout.WEST);

        message.setText((racers > 0 ? "Go!": "Click course to start"));

        // if player or AI race
        if (racers > 0) {
            raceStart();
        } else {
            // calls runSolver with each click
            window.getContentPane().addMouseListener(new runSolverMouseListener(this));
            firstRun = true;
        }
    }

    // initialize a non-ai race
    public void raceStart() {
        race.nextCar();
        courseDisplay.paintNow();
        courseDisplay.addMouseListener(new MouseRaceListener(courseDisplay, this)); // calls raceUpdate
    }

    // after player makes a move, update the race accordingly
    public void raceUpdate() {
        info.setText(raceStatus());
        List<Car> cars = race.getCars();
        int racers = cars.size();
        int crashed = 0, indexFirstNotCrashed = -1, finished = 0, indexFinished = 0;

        // survey cars
        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            if (car.isCrashed()) {
                crashed++;
            } else {
                if (indexFirstNotCrashed == -1) {
                    indexFirstNotCrashed = i;
                }
                if (car.isFinished()) {
                    finished++;
                    indexFinished = i;
                }
            }
        }

        // all crashed?
        if (crashed == racers) {
            message.setText("All racers crashed! Race over");
            MouseListener[] mouseListeners = courseDisplay.getMouseListeners();
            courseDisplay.removeMouseListener(mouseListeners[0]);
            return;
        }

        // set next non-crashed car as active
        race.nextCar();
        while(race.getActiveCar().isCrashed())
            race.nextCar();

        // if someone has finished and turn is completed
        if (finished > 0 && race.getActiveCar() == cars.get(indexFirstNotCrashed)) {
            MouseListener[] mouseListeners = courseDisplay.getMouseListeners();
            courseDisplay.removeMouseListener(mouseListeners[0]);
            String messageText;
            if (finished > 1) {
                messageText = "Tie!";
            } else {
                messageText = "Player " + (indexFinished + 1) + " is the winner!";
            }
            message.setText(messageText);
            return;
        }

    }

    // to display in race info pane
    public String raceStatus() {
        StringBuilder sb = new StringBuilder("<html><body>Turn: ");
        sb.append(race.getTurn());
        sb.append("<br><br>");
        List<Car> cars = race.getCars();
        for (int i = 1; i <= cars.size(); i++) {
            sb.append("Player ").append(i).append(":<br>");
            if (cars.get(i - 1).isCrashed()) {
                sb.append("Crashed!<br><br>");
                continue;
            }
            if (cars.get(i - 1).isFinished()) {
                sb.append("Finished!<br><br>");
                continue;
            }
            if (cars.get(i - 1).isPastCheckpoint()) {
                sb.append("Passed checkpoint<br><br>");
                continue;
            }
            if (cars.get(i - 1).getPath().size() > 0) {
                sb.append("is off!<br><br>");
                continue;
            }
            sb.append("Ready<br><br>");
        }
        sb.append("</body></html>");
        return sb.toString();
    }

    // starts, pauses, or unpauses AI course solver
    public void runSolver() {
        if (firstRun) {
            firstRun = false;
            back.setEnabled(false);
            back.paintImmediately(back.getVisibleRect());
            worker = new SolverWorker(this);
            worker.execute();
        } else if (!worker.isPaused()) {
            worker.pause();
            back.setEnabled(true);
            back.paintImmediately(back.getVisibleRect());
        } else {
            back.setEnabled(false);
            back.paintImmediately(back.getVisibleRect());
            worker.resume();
        }
    }

    // formats button
    private void formatButton(Dimension dim, int textSize, AbstractButton...buttonList) {
        for (int i = 0; i < buttonList.length; i++) {
            buttonList[i].setMinimumSize(dim);
            buttonList[i].setMaximumSize(dim);
            buttonList[i].setPreferredSize(dim);
            buttonList[i].setFont(new Font("Arial", Font.PLAIN, textSize));
        }
    }

    // formats button with default
    private void formatButton(AbstractButton...buttonList) {
        this.formatButton(BUTTON_DIM, FONT_SIZE, buttonList);
    }
}
