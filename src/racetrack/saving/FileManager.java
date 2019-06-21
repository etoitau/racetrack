/*
 * Developed by Kyle Chatman
 * kchatman.com
 *
 * Licenced under a Creative Commons Attribution-NonCommercial 4.0 International License
 * https://creativecommons.org/licenses/by-nc/4.0/
 *
 *
 */

package racetrack.saving;

import racetrack.game.Course;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private File folder;
    private List<String> fileNames;
    private final String saveFolder = "./savefiles";

    public FileManager() throws IOException {
        folder = new File(saveFolder);
        fileNames = new ArrayList<String>();
        if (!folder.exists()) {
            folder.createNewFile();
        }
        for (File file : folder.listFiles()) {
            fileNames.add(file.getName());
        }
    }

    public List<String> getFileNames() {
        return fileNames;
    }

    public void deleteFile(String fileName) {
        if (fileNames.contains(fileName)) {
            File toDelete = new File(saveFolder + "/" + fileName);
            toDelete.delete();
            fileNames.remove(fileName);
        }
    }

    public void saveCourse(String name, Course course) throws IOException {
        File saveFile = new File(saveFolder + "/" + name);
        FileWriter writer = new FileWriter(saveFile);
        writer.write(course.toString());
        writer.close();
    }
}
