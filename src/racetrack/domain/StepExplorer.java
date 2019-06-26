/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

package racetrack.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StepExplorer {
    private final StepNode head;
    private List<Short> current;

    public StepExplorer() {
        head = new StepNode();
        current = new ArrayList<Short>();
        current.add((short) 0);
    }

    public Short getNextStep() {
        Short next = this.getCurrentLeaf().getNext();
        current.add(next);
        return next;
    }

    public void killCurrent() {
        this.getCurrentLeaf().killNode();
    }

    public Short getFirstStep() {
        current = new ArrayList<Short>();
        return this.getNextStep();
    }

    private StepNode getCurrentLeaf() {
        StepNode temp = head;
        for (short i = 0; i < current.size(); i++) {
            HashMap<Short, StepNode> kids = temp.getChildren();
            temp = kids.get(i);
        }
        return temp;
    }




}
