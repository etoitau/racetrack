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

import java.util.HashMap;
import java.util.Random;

public class StepNode {
    private HashMap<Short,StepNode> children;
    private StepNode parent;
    private Random rand;

    public StepNode() {
        children = new HashMap<Short, StepNode>();
        rand = new Random();
    }

    public StepNode(StepNode parent) {
        this();
        this.parent = parent;
    }

    public Short getNext() {
        // if we've already killed this node
        if (children == null)
            return -1;
        // try possible next moves 0-8
        int shuffle = rand.nextInt(9);
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

    public HashMap<Short,StepNode> getChildren() {
        return children;
    }


}
