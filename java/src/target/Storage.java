package target;

import datastructure.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Storage {
    private Map<Integer, Course> courses;
    private Map<String, User> users;

    public Storage() {
        courses = new HashMap<>();
        users = new HashMap<>();
    }

    public List<Integer> getCourseIDs() {
        return courses.keySet().stream().collect(toList());
    }

    public List<Course> getCourses() {
        return courses.values().stream().collect(toList());
    }

    public Map<String, User> getUserDB() { return users; }

    public void addCourses(String subDirectoryName, File[] courseFiles) {
        for (File file : courseFiles) {
            courses.put(Integer.parseInt(file.getName().split("\\.")[0]), readCourseFile(subDirectoryName, file));
        }
    }

    public void clearUserDB() {
        users = new HashMap<>();
    }

    private static Course readCourseFile(String collegeName, File file) {
        int id = Integer.parseInt((file.getName().split("\\."))[0]);
        String[] courseInfo = readFileLine(file).split("\\|");
        int idx = 0;
        Course course = new Course(
                id,
                collegeName,
                courseInfo[idx++], // department
                courseInfo[idx++], // academic degree
                Integer.parseInt(courseInfo[idx++]), // academic year
                courseInfo[idx++], // course name
                Integer.parseInt(courseInfo[idx++]), // credit
                courseInfo[idx++], // location
                courseInfo[idx++], // instructor
                Integer.parseInt(courseInfo[idx++]) // quota
        );
        return course;
    }

    private static String readFileLine(File file) {
        try {
            Scanner scanner = new Scanner(file);
            return scanner.nextLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Course> searchByDepartment(String searchCondition) {
        return courses.values().stream().filter(course -> course.department.equals(searchCondition)).collect(toList());
    }

    public List<Course> searchByAcademicYear(Integer searchCondition) {
        return courses.values().stream().filter(course -> course.academicYear == searchCondition).collect(toList());
    }

    public List<Course> searchByName(String searchCondition) {
        String[] keywords = searchCondition.split(" ");
        List<Course> result = new ArrayList<>(getCourses());
        for (String keyword : keywords) {
            result.retainAll((result.stream().filter(course -> course.courseName.contains(keyword))).collect(toList()));
        }
        return result;
    }

    public List<Course> sortSearch(List<Course> list, String sortCriteria) {
        list = list.stream().sorted(Comparator.comparingInt(c -> c.courseId)).collect(toList());
        if (sortCriteria == null) {
            return list;
        } else if (sortCriteria.equals("name")) {
            return list.stream().sorted(Comparator.comparing(c -> c.courseName)).collect(toList());
        } else if (sortCriteria.equals("dept")) {
            return list.stream().sorted(Comparator.comparing(c -> c.department)).collect(toList());
        } else if (sortCriteria.equals("ay")) {
            return list.stream().sorted(Comparator.comparing(c -> c.academicYear)).collect(toList());
        } else {
            return list;
        }
    }

    public void addUsers(String name, String bidFilePath) {
        users.put(name, new User(name, readBidFile(bidFilePath)));
    }

    private List<Bidding> readBidFile(String bidFilePath) {
        File bidFile = new File(bidFilePath);
        List<Bidding> bidList = new ArrayList<>();
        try {
            Scanner scanner = new Scanner(bidFile);
            while (scanner.hasNextLine()) {
                String[] bidInfo = scanner.nextLine().split("\\|");
                if (bidInfo[0].length() == 0) {
                    continue;
                }
                bidList.add(new Bidding(Integer.parseInt(bidInfo[0]), Integer.parseInt(bidInfo[1])));
            }
            return bidList;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
