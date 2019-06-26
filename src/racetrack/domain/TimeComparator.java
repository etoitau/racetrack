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

import java.util.Comparator;

public class TimeComparator implements Comparator<Car> {

    @Override
    public int compare(Car o1, Car o2) {
        return o2.getPath().size() - o1.getPath().size();
    }
}
