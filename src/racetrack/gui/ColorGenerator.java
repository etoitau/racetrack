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

import java.awt.Color;
import java.util.*;

/**
 * an object for randomly assigning colors. Note color won't be returned twice until all have been used
 */
public class ColorGenerator {
    // thanks to https://sashat.me/2017/01/11/list-of-20-simple-distinct-colors/
    private static Color[] colorLibrary = {
            new Color(230, 25, 75),
            new Color(60, 180, 75),
            new Color(0, 130, 200),
            new Color(245, 130, 48),
            new Color(145, 30, 180),
            new Color(240, 50, 230),
            new Color(0, 128, 128),
            new Color(170, 110, 40),
            new Color(128, 0, 0),
    };
    private List<Color> colorList;
    private Random rand;

    // constructor
    public ColorGenerator() {
        this.colorList = new ArrayList<>(Arrays.asList(colorLibrary));
        rand = new Random();
    }

    // get random color and remove from list, refill from library if empty
    public Color getColor() {
        if (colorList.isEmpty()) {
            colorList.addAll(Arrays.asList(colorLibrary));
        }
        Color out = colorList.get(rand.nextInt(colorList.size()));
        colorList.remove(out);
        return out;
    }
}
