package target;

import datastructure.*;

import java.io.*;
import java.util.*;

public class StorageManager {
    private final static String COURSE_DIRECTORY_PATH = "data/Courses/2020_Spring";
    private Storage storage;

    StorageManager() {
        storage = new Storage();
        loadCourses(COURSE_DIRECTORY_PATH);
    }

    public void printCourses() {
        for (Course c : storage.getCourses()) {
            System.out.println(c);
        }
    }

    private void loadCourses(String directoryName) {
        File directory = new File(directoryName);
        File[] courseDirectories = directory.listFiles();
        for (File dir : courseDirectories) {
            storage.addCourses(dir.getName(), dir.listFiles());
        }
    }

    public List<Course> searchCourses(Map<String, Object> searchConditions, String sortCriteria) {
        List<Course> searchedCourses = storage.getCourses();
        for (String key : searchConditions.keySet()) {
            if (key.equals("dept")) {
                searchedCourses.retainAll(storage.searchByDepartment((String) searchConditions.get(key)));
            } else if (key.equals("ay")) {
                searchedCourses.retainAll(storage.searchByAcademicYear((Integer) searchConditions.get(key)));
            } else if (key.equals("name")) {
                searchedCourses.retainAll(storage.searchByName((String) searchConditions.get(key)));
            }
        }
        return storage.sortSearch(searchedCourses, sortCriteria);
    }
}
