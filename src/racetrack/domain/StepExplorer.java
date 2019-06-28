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
    private StepNode head;
    private List<Short> current;

    public StepExplorer(short startBias) {
        head = new StepNode();
        // first move should never be to stay put
        head.getChildren().put((short) (4 - startBias), null);
        current = new ArrayList<>();
    }

    public Short getNextStep() {
        short next;
        next = this.getCurrentLeaf().getNext();
        if (next < 0) {
            if (current.size() > 0) {
                this.getCurrentLeaf().killNode(current.get(current.size() - 1));
            } else {
                head = null;
            }

        } else{
            current.add(next);
        }
        return next;
    }

    public void killCurrent() {
        if (current.size() > 0) {
            this.getCurrentLeaf().killNode(current.get(current.size() - 1));
        } else {
            head = null;
        }
    }

    public Short getFirstStep() {
        current = new ArrayList<Short>();
        return this.getNextStep();
    }

    private StepNode getParentOfCurrent() {
        StepNode temp = head;
        for (short i = 0; i < current.size() - 1; i++) {
            HashMap<Short, StepNode> kids = temp.getChildren();
            temp = kids.get(current.get(i));
        }
        return temp;
    }

    private StepNode getCurrentLeaf() {
        StepNode temp = head;
        for (short i = 0; i < current.size(); i++) {
            HashMap<Short, StepNode> kids = temp.getChildren();
            temp = kids.get(current.get(i));
        }
        return temp;
    }

    public boolean hasNext() {
        if (head == null) {
            return false;
        }
        return true;
    }




}
