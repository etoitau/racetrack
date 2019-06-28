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

import racetrack.domain.Car;
import racetrack.game.Course;
import racetrack.game.CourseSolver;
import racetrack.game.Race;
import racetrack.gui.buttonlisteners.*;
import racetrack.gui.keylisteners.SolverKeyListener;
import racetrack.gui.mouselisteners.MouseRaceListener;
import racetrack.gui.mouselisteners.runSolverMouseListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

public class UserInterface implements Runnable {

    private JFrame window;
    private int scale = 25, length = 10, height = 8;
    private CourseDisplay courseDisplay;
    private List<JToggleButton> buildButtons;
    private List<JButton> raceSetupButtons;
    private JLabel message;
    private JTextPane info;
    private Race race;
    private JButton back;
    private Dimension infoPanelDim = new Dimension(200, height * scale),
                infoLabelDim = new Dimension(180, height * scale - 30);

    public CourseDisplay getCourseDisplay() {
        return courseDisplay;
    }

    public List<JToggleButton> getBuildButtons() {
        return buildButtons;
    }

    public JLabel getMessage() {
        return message;
    }

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
        courseDisplay.setPreferredSize(new Dimension(length * scale, height * scale));
        container.add(courseDisplay, BorderLayout.CENTER);

        JLabel title = new JLabel("Racetrack");
        container.add(title, BorderLayout.NORTH);

        message = new JLabel("");
        container.add(message, BorderLayout.SOUTH);

        // at first, want drawing buttons
        drawSetup();
    }

    // set up drawing button pane
    public void drawSetup() {
        // set up drawing buttons
        if (window.getContentPane().getComponentCount() == 4)
            window.getContentPane().remove(3);
        window.getContentPane().add(builderButtons(), BorderLayout.WEST);
        message.setText("Choose drawing option");
    }

    // make panel with drawing buttons
    private JPanel builderButtons() {
        JPanel buttonPanel = new JPanel();
        BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(layout);
        buttonPanel.setPreferredSize(infoPanelDim);

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
        race.addActionListener(new RunButtonListener(this)); // calls raceSetup if done drawing

        buttonPanel.add(drawWalls);
        buttonPanel.add(drawStart);
        buttonPanel.add(drawCheck);
        buttonPanel.add(race);
        return buttonPanel;
    }

    // set up race configuration pane
    public void raceSetup() {
        window.getContentPane().remove(3);
        window.getContentPane().add(raceSetupButtons(), BorderLayout.WEST);
        message.setText("Choose racing mode");
        courseDisplay.setRace(null);
        courseDisplay.paintNow();
    }

    // make panel with race configuration buttons
    private JPanel raceSetupButtons() {
        JPanel buttonPanel = new JPanel();
        BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(layout);
        buttonPanel.setPreferredSize(infoPanelDim);

        JButton aiRace = new JButton("AI Race");
        JButton soloRace = new JButton("Solo Race");
        JButton twoRacers = new JButton("Two Racers");
        JButton threeRacers = new JButton("Three Racers");
        JButton fourRacers = new JButton("Four Racers");
        JButton backToDraw = new JButton("Back to Draw");

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

    // sets up side pane for race info
    public void setupRace(int racers) {
        race = new Race(courseDisplay.getCourse(), racers);
        courseDisplay.setRace(race);

        window.getContentPane().remove(3);

        JPanel raceInfo = new JPanel();
        raceInfo.setPreferredSize(infoPanelDim);
        BoxLayout layout = new BoxLayout(raceInfo, BoxLayout.Y_AXIS);
        raceInfo.setLayout(layout);

        String infoText = (racers > 0) ? raceStatus() : "<html>Click course<br>to start</html>";
        info = new JTextPane();
        info.setContentType("text/html");
        info.setMinimumSize(infoLabelDim);
        info.setOpaque(false);
        info.setEditable(false);
        info.setText(infoText);

        back = new JButton("Back to Setup");
        back.addActionListener(new RunButtonListener(this));

        raceInfo.add(info);
        raceInfo.add(back);

        window.getContentPane().add(raceInfo, BorderLayout.WEST);

        message.setText((racers > 0 ? "Go!": "Click course to start"));

        if (racers > 0) {
            raceStart();
        } else {
            window.getContentPane().addMouseListener(new runSolverMouseListener(this));
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

    // runs AI course solver
    public void runSolver() {
        // remove the starting mouse listener
        window.getContentPane().removeMouseListener(window.getContentPane().getMouseListeners()[0]);
        back.setEnabled(false);
        back.paintImmediately(back.getVisibleRect());
        message.setText("Red: random recent run, Blue: fastest finish so far");
        message.paintImmediately(message.getVisibleRect());
        info.setText("Working...");
        info.paintImmediately(info.getVisibleRect());

        CourseSolver solver = new CourseSolver(courseDisplay);
        long runStartTime = solver.getRunStartTime();
        long startTime = runStartTime;

        while(solver.hasNext()) {
            Car car = solver.nextCar();
            long thisTime = System.currentTimeMillis();
            // every few seconds, but most recent run on the screen
            if (thisTime - startTime > 4000) {
                startTime = thisTime;
                if (courseDisplay.getRace().getCars().size() == 0) {
                    courseDisplay.getRace().getCars().add(car);
                } else {
                    courseDisplay.getRace().getCars().set(0, car);
                }

                courseDisplay.paintNow();
                info.setText(solver.report());
                info.paintImmediately(info.getVisibleRect());
                // also best finisher if there is one
                if (solver.getBestCar() != null) {
                    courseDisplay.getRace().setActiveCar(new Car(solver.getBestCar(), Color.BLUE));
                }
            }
        }
        info.setText(solver.report());
        courseDisplay.getRace().getCars().clear();
        if (solver.getBestCar() != null) {
            courseDisplay.getRace().setActiveCar(new Car(solver.getBestCar(), Color.BLUE));
            courseDisplay.paintNow();
        }
        back.setEnabled(true);
    }
}
