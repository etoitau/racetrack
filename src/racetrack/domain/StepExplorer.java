/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * an object for exploring all possible race paths
 * uses a trie structure to store each path that is attempted
 * when the possibilities of a path are exhausted that path is set null
 */
public class StepExplorer {
    private StepNode head; // head of the trie structure
    private List<Short> current; // list of the steps of this run so far

    // constructor
    public StepExplorer(short startBias) {
        head = new StepNode();
        // first move should never be to stay put
        head.getChildren().put((short) (4 - startBias), null);
        current = new ArrayList<>();
    }

    /**
     * navigates trie to current position and get's next step if available
     * if none are available (getNext returns -1) that path is set null
      */
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

    // sets current leaf to null, indicating all paths that way have been explored
    public void killCurrent() {
        if (current.size() > 0) {
            this.getCurrentLeaf().killNode(current.get(current.size() - 1));
        } else {
            head = null;
        }
    }

    // resets list of steps and gets first
    public Short getFirstStep() {
        current = new ArrayList<Short>();
        return this.getNextStep();
    }

    // navigates structure to leaf representing last move
    private StepNode getCurrentLeaf() {
        StepNode leaf = head;
        for (short i = 0; i < current.size(); i++) {
            HashMap<Short, StepNode> kids = leaf.getChildren();
            leaf = kids.get(current.get(i));
        }
        return leaf;
    }

    // if all paths have been tried, head will have been set null
    public boolean hasNext() {
        if (head == null) {
            return false;
        }
        return true;
    }
}
