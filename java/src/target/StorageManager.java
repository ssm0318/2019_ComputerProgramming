package target;

import datastructure.*;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class StorageManager {
    private final static String COURSE_DIRECTORY_PATH = "data/Courses/2020_Spring";
    private final static String USER_DIRECTORY_PATH = "data/Users";
    private final static String USER_BACKUP_DIRECTORY_PATH = "data/Users_backup";
    private Storage storage;


    public StorageManager() {
        storage = new Storage();
        loadCourses();
        loadUsers();
    }

    private void loadCourses() {
        File directory = new File(StorageManager.COURSE_DIRECTORY_PATH);
        File[] courseDirectories = directory.listFiles();

        for (File dir : courseDirectories) {
            if (dir.isDirectory()) {
                storage.addCourses(dir.getName(), dir.listFiles());
            }
        }
    }

    private void loadUsers() {
        File directory = new File(StorageManager.USER_BACKUP_DIRECTORY_PATH);
        File[] userDirectories = directory.listFiles();

        storage.clearUserDB();
        for (File dir : userDirectories) {
            if (dir.isDirectory()) {
                storage.addUsers(dir.getName(), dir.listFiles()[0].getAbsolutePath());
            }
        }
    }


    // Question 2-1
    public List<Course> searchCourses(Map<String, Object> searchConditions, String sortCriteria) {
        List<Course> searchedCourses = new ArrayList<>(storage.getCourses());

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

    public List<Bidding> getUserBiddings(String userid) {
        return storage.getUserDB().get(userid).getBidList();
    }

    public List<Course> getUserCourses (String userid) {
        return storage.getUserDB().get(userid).getRegisteredCourses();
    }

    public boolean hasUser(String userid) {
        return storage.getUserDB().containsKey(userid);
    }

    public boolean hasCourse(int courseid) {
        return storage.getCourseIDs().contains(courseid);
    }


    // Question 2-2
    public boolean addBidding(int courseid, int mileage, String userid) {
        User user = storage.getUserDB().get(userid);
        Integer bidIndex = (Integer) user.findBid(courseid).key;
        Integer prevBid = bidIndex == -1 ? 0 : (Integer) user.findBid(courseid).value;

        if (canBid(user.getCurrMileage(), prevBid, mileage)) {
            if (bidIndex == -1) {
                updateBidFile("", courseid + "|" + mileage, userid, false, true);
                user.addBidding(new Bidding(courseid, mileage));
            } else {
                updateBidFile(courseid + "|" + prevBid, courseid + "|" + mileage, userid, false, false);
                user.updateBidding(bidIndex, new Bidding(courseid, mileage));
            }
            return true;
        }
        return false;
    }

    public void cancelBid(int courseid, String userid) {
        User user = storage.getUserDB().get(userid);
        Integer bidIndex = (Integer) user.findBid(courseid).key;
        Integer prevBid = bidIndex == -1 ? 0 : (Integer) user.findBid(courseid).value;

        if (bidIndex != -1) { // user has bidden
            user.removeBidding(bidIndex);
            updateBidFile(courseid + "|" + prevBid, "", userid, true, false);
        }
    }

    private boolean canBid(int currentMileage, int mileageUpdate, int newBid) {
        return (currentMileage - mileageUpdate + newBid) <= Config.MAX_MILEAGE;
    }

    private void updateBidFile(String current, String update, String userid, boolean remove, boolean appending) {
        try (FileReader fr = new FileReader(USER_DIRECTORY_PATH + "/" + userid + "/bid.txt");
             BufferedReader br = new BufferedReader(fr)) {
            StringBuilder result = new StringBuilder();
            String currLine;
            while ((currLine = br.readLine()) != null) {
                if (currLine.equals(current)) {
                    if (!remove) {
                        result.append(update).append(System.getProperty("line.separator"));
                    }
                    continue;
                } else if (currLine.equals("")) {
                    continue;
                }
                result.append(currLine.trim()).append(System.getProperty("line.separator"));
            }
            if (appending) {
                result.append(update);
            }
            FileWriter fw = new FileWriter(USER_DIRECTORY_PATH + "/" + userid + "/bid.txt");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(String.valueOf(result).trim());
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Question 2-3
    public void processAllBidding() throws IOException {
        for (Course course : storage.getCourses()) {
            clearCourseBidding(course);
        }
        clearBidData();
    }

    private void clearCourseBidding(Course course) throws IOException {
        Deque<User> biddenUserQueue = getBiddenUsers(course);
        List<User> orderedUserList = orderUsers(biddenUserQueue, course.courseId);

        int count = 0;
        ListIterator<User> iter = orderedUserList.listIterator();

        while (iter.hasNext() && count < course.quota) {
            register(course, iter.next());
            count++;
        }
    }

    private List<User> orderUsers(Deque<User> userQueue, int courseid) {
        List<User> userList = new ArrayList<>();

        while (!userQueue.isEmpty()) {
            User user = userQueue.pollLast();
            int bid = (int) user.findBid(courseid).value;
            if (!userQueue.isEmpty() && userQueue.peekLast().findBid(courseid).value.equals(bid)) {
                Set<User> sameMileage = new TreeSet<>(new User.BidPrioritySorter());
                while (!userQueue.isEmpty() && userQueue.peekLast().findBid(courseid).value.equals(bid)) {
                    sameMileage.add(user);
                    sameMileage.add(userQueue.pollLast());
                }
                userList.addAll(sameMileage);
                continue;
            }
            userList.add(user);
        }

        return userList;
    }

    private Deque<User> getBiddenUsers(Course course) {
        return storage.getUserDB().values().stream().
                filter(user -> (int) user.findBid(course.courseId).key != -1).
                sorted(Comparator.comparingInt(user ->
                        user.getBidList().get((int) user.findBid(course.courseId).key).mileage)).
                collect(Collectors.toCollection(LinkedList::new));
    }

    private void register(Course course, User user) throws IOException {
        try (FileWriter fw = new FileWriter(USER_DIRECTORY_PATH + "/" + user.getUserId() + "/registration.txt", true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(course.courseId + "|" + course.courseName + System.getProperty("line.separator"));
            user.addCourse(course);
        } catch (IOException ie) {
            throw new IOException();
        }
    }

    private void clearBidData() {
        File directory = new File(StorageManager.USER_DIRECTORY_PATH);
        File[] userDirectories = directory.listFiles();

        for (File dir : userDirectories) {
            if (dir.isDirectory()) {
                try (FileWriter fw = new FileWriter(dir + "/bid.txt")) {
                    storage.getUserDB().get(dir.getName()).clearBidList();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
