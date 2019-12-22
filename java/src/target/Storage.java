package target;

import datastructure.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

public class Storage {
    private List<Course> courses;

    Storage() {
        courses = new ArrayList<>();
    }

    public void addCourses(String subDirectoryName, File[] courseFiles) {
        for (File file : courseFiles) {
            courses.add(readCourseFile(subDirectoryName, file));
        }
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
        }
        return null;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public List<Course> searchByDepartment(String searchCondition) {
        return courses.stream().filter(course -> course.department.equals(searchCondition)).collect(Collectors.toList());
    }

    public List<Course> searchByAcademicYear(Integer searchCondition) {
        return courses.stream().filter(course -> course.academicYear == searchCondition).collect(Collectors.toList());
    }

    public List<Course> searchByName(String searchCondition) {
        String[] keywords = searchCondition.split("\\s+");
        List<Course> result = courses;
        for (String keyword : keywords) {
            result.retainAll((result.stream().filter(course -> course.courseName.contains(keyword))).collect(Collectors.toList()));
        }
        return result;
    }

    public List<Course> sortSearch(List<Course> list, String sortCriteria) {
        list = list.stream().sorted(Comparator.comparingInt(c -> c.courseId)).collect(Collectors.toList());
        if (sortCriteria == null) {
            return list;
        } else if (sortCriteria.equals("name")) {
            return list.stream().sorted(Comparator.comparing(c -> c.courseName)).collect(Collectors.toList());
        } else if (sortCriteria.equals("dept")) {
            return list.stream().sorted(Comparator.comparing(c -> c.department)).collect(Collectors.toList());
        } else if (sortCriteria.equals("ay")) {
            return list.stream().sorted(Comparator.comparing(c -> c.academicYear)).collect(Collectors.toList());
        } else {
            return list;
        }
    }
}
