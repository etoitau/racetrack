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
import racetrack.game.SimulatedRace;
import racetrack.gui.buttonlisteners.*;
import racetrack.gui.mouselisteners.MouseRaceListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class UserInterface implements Runnable {

    private JFrame window;
    private int scale = 25, length = 10, height = 8;
    private CourseDisplay courseDisplay;
    private List<JToggleButton> buildButtons;
    private List<JButton> raceSetupButtons;
    private JLabel message, info;
    private Race race;

    @Override
    public void run() {
        window = new JFrame("Racetrack");
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

        JLabel title = new JLabel("Racetrack");
        container.add(title, BorderLayout.NORTH);

        message = new JLabel("");
        container.add(message, BorderLayout.SOUTH);

        drawSetup();
    }

    public void drawSetup() {
        if (window.getContentPane().getComponentCount() == 4)
            window.getContentPane().remove(3);
        window.getContentPane().add(builderButtons(), BorderLayout.WEST);
        message.setText("Choose drawing option");
    }

    public void raceSetup() {
        window.getContentPane().remove(3);
        window.getContentPane().add(raceSetupButtons(), BorderLayout.WEST);
        message.setText("Choose racing mode");
        courseDisplay.setRace(null);
    }

    public void raceStart(int racers) {
        race = new Race(courseDisplay.getCourse(), racers);
        courseDisplay.setRace(race);
        window.getContentPane().remove(3);

        JPanel raceInfo = new JPanel();
        BoxLayout layout = new BoxLayout(raceInfo, BoxLayout.Y_AXIS);
        raceInfo.setLayout(layout);

        info = new JLabel(raceStatus());
        JButton back = new JButton("Back to Setup");
        back.addActionListener(new RunButtonListener(this));
        raceInfo.add(info);
        raceInfo.add(back);

        window.getContentPane().add(raceInfo, BorderLayout.WEST);

        message.setText("Go!");

        race.nextCar();
        courseDisplay.addMouseListener(new MouseRaceListener(courseDisplay, this));
    }

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
        race.addActionListener(new RunButtonListener(this));

        buttonPanel.add(drawWalls);
        buttonPanel.add(drawStart);
        buttonPanel.add(drawCheck);
        buttonPanel.add(race);
        return buttonPanel;
    }

    private JPanel raceSetupButtons() {
        JPanel buttonPanel = new JPanel();
        BoxLayout layout = new BoxLayout(buttonPanel, BoxLayout.Y_AXIS);
        buttonPanel.setLayout(layout);

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

        aiRace.addActionListener(new RaceButtonListener(this, 0));
        soloRace.addActionListener(new RaceButtonListener(this, 1));
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

    public void runSolver() {
        race = new Race(courseDisplay.getCourse(), 0);
        courseDisplay.setRace(race);
        CourseSolver solver = new CourseSolver(courseDisplay);
        System.out.println(solver.report());
    }

    public void runSimulation() {
        SimulatedRace simRace = new SimulatedRace(courseDisplay.getCourse());
        race = simRace.getRace();
        courseDisplay.setRace(race);
        courseDisplay.setSimMode(true);
        race.setTurn(0);

        List<Car> finishers = simRace.finishers();

        window.getContentPane().remove(3);

        JPanel raceInfo = new JPanel();
        BoxLayout layout = new BoxLayout(raceInfo, BoxLayout.Y_AXIS);
        raceInfo.setLayout(layout);
        String infoText;
        if (finishers.isEmpty()) {
            infoText = "Problem with course, no finishers";
        } else {
            infoText = "<html><body>Shortest possible run: \n" + finishers.get(0).getPath().size() + " turns</body></html>";
        }
        info = new JLabel(infoText);
        JButton back = new JButton("Back to Setup");
        back.addActionListener(new RunButtonListener(this));
        raceInfo.add(info);
        raceInfo.add(back);

        window.getContentPane().add(raceInfo, BorderLayout.WEST);

        message.setText("Go!");

        if (finishers.isEmpty()) { return; }

        replay:
        for (int i = 0; i < finishers.get(finishers.size() - 1).getPath().size(); i++) {
            race.setTurn(i);
            courseDisplay.repaint();
//            try {
//                TimeUnit.SECONDS.sleep(1);
//            } catch (InterruptedException e) {
//                break replay;
//            }
        }
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
