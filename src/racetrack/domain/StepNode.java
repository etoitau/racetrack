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

public class StepNode {
    private HashMap<Short,StepNode> children;
    private StepNode parent;
    private boolean crashed;

    public StepNode() {
        children = new HashMap<Short, StepNode>();
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
        for (short i = 0; i < 8; i++) {
            // if we've tried this option before
            if (children.containsKey(i)) {
                // if it was a crash, try next option, else it's viable - return it
                if (children.get(i) == null) {
                    continue;
                } else {
                    return i;
                }
            // if we have't tried this option before, set it up and return
            } else {
                children.put(i, new StepNode(this));
                return i;
            }
        }
        // if we make it this far, all children are crashes, so kill parent
        children = null;
        return -1;
    }

    public void killNode() {
        children = null;
    }

    public HashMap<Short,StepNode> getChildren() {
        return children;
    }


}
