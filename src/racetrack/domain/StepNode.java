/*
 * Copyright 2019 Kyle Chatman
 * kchatman.com
 *
 * Licensed under the GNU General Public License v.3.0
 * https://www.gnu.org/licenses/gpl-3.0.txt
 *
 */

package racetrack.domain;

import java.util.HashMap;
import java.util.Random;

/**
 * Node (or leaf) of trie structure that keeps track of all paths attempted so far
 */
public class StepNode {
    private HashMap<Short,StepNode> children;
    private StepNode parent;
    private Random rand;

    public HashMap<Short,StepNode> getChildren() {
        return children;
    }

    // constructor
    public StepNode() {
        children = new HashMap<>();
        rand = new Random();
    }

    // constructor for typical leaf except head
    public StepNode(StepNode parent) {
        this();
        this.parent = parent;
    }

    // returns a new step from the current leaf. If all have been taken, report -1 so StepExplorer can set null
    public Short getNext() {
        // if we've already killed this node
        if (children == null)
            return -1;

        // if first move, start with zero so Solution can apply startBias, otherwise random
        int shuffle = (parent == null) ? 0 : rand.nextInt(9);

        // try possible next moves 0-8
        for (int i = 0; i < 9; i++) {
            short index = (short) ((i + shuffle) % 9);
            // if we've tried this option before
            if (children.containsKey(index)) {
                // if it was a crash, try next option, else it's viable - return it
                if (children.get(index) == null) {
                    continue;
                } else {
                    return index;
                }
            // if we have't tried this option before, set it up and return
            } else {
                children.put(index, new StepNode(this));
                return index;
            }
        }
        // if we make it this far, all children are crashes, so kill parent
        children = null;
        return -1;
    }

    public void killNode(short i) {
        parent.getChildren().put(i, null);
    }
}
