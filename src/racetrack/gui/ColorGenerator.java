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

public class ColorGenerator {
    // thanks to https://sashat.me/2017/01/11/list-of-20-simple-distinct-colors/
    private static Color[] colorLibrary = {
            new Color(230, 25, 75),
            new Color(60, 180, 75),
//            new Color(255, 255, 25),
            new Color(0, 130, 200),
            new Color(245, 130, 48),
            new Color(145, 30, 180),
//            new Color(70, 240, 240),
            new Color(240, 50, 230),
            new Color(210, 245, 60),
            new Color(250, 190, 190),
            new Color(0, 128, 128),
            new Color(230, 190, 255),
            new Color(170, 110, 40),
//            new Color(255, 250, 200),
            new Color(128, 0, 0),
    };
    private List<Color> colorList;
    private Random rand;

    public ColorGenerator() {
        this.colorList = new ArrayList<Color>(Arrays.asList(colorLibrary));
        rand = new Random();
    }

    public Color getColor() {
        if (colorList.isEmpty()) {
            colorList.addAll(Arrays.asList(colorLibrary));
        }
        Color out = colorList.get(rand.nextInt(colorList.size()));
        colorList.remove(out);
        return out;
    }
}
