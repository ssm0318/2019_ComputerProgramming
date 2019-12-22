package target;

import datastructure.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StorageManager {
    private final static String COURSE_DIRECTORY_PATH = "data/Courses/2020_Spring";
    private final static String USER_DIRECTORY_PATH = "data/Users";
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
        File directory = new File(StorageManager.USER_DIRECTORY_PATH);
        File[] userDirectories = directory.listFiles();

        for (File dir : userDirectories) {
            if (dir.isDirectory()) {
                storage.addUsers(dir.getName(), dir.listFiles()[0].getAbsolutePath());
            }
        }
    }

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

    public Pair<Integer, List<Bidding>> getUserBids(String userid) {
        if (!storage.getUserDB().containsKey(userid)) {
            return new Pair(ErrorCode.USERID_NOT_FOUND, new ArrayList<>());
        }

        return new Pair(ErrorCode.SUCCESS, storage.getUserDB().get(userid).getBidList());
    }

    public boolean hasUser(String userid) {
        return storage.getUserDB().containsKey(userid);
    }

    public boolean hasCourse(int courseid) {
        return storage.getCourseIDs().contains(courseid);
    }

    public boolean processBid(int courseid, int mileage, String userid) {
        User user = storage.getUserDB().get(userid);
        Integer bidIndex = (Integer) user.hasBidden(courseid).key;
        Integer prevBid = bidIndex == -1 ? 0 : (Integer) user.hasBidden(courseid).value;

        if (canBid(user.getCurrMileage(), prevBid, mileage)) {
            if (bidIndex == -1) {
                writeBidding(courseid, mileage, userid);
                user.addBidding(new Bidding(courseid, mileage));
            } else {
                updateBidFile(courseid + "|" + prevBid, courseid + "|" + mileage, userid, false);
                user.updateBidding(bidIndex, new Bidding(courseid, mileage));
            }
            return true;
        }
        return false;
    }

    public void cancelBid(int courseid, int mileage, String userid) {
        User user = storage.getUserDB().get(userid);
        Integer bidIndex = (Integer) user.hasBidden(courseid).key;
        Integer prevBid = bidIndex == -1 ? 0 : (Integer) user.hasBidden(courseid).value;

        if (bidIndex != -1) { // user has bidden
            user.removeBidding(bidIndex);
            updateBidFile(courseid + "|" + prevBid, "", userid, true);
        }
    }

    private boolean canBid(int currentMileage, int mileageUpdate, int newBid) {
        return (currentMileage - mileageUpdate + newBid) <= Config.MAX_MILEAGE;
    }

    private void updateBidFile(String current, String update, String userid, boolean remove) {
        try {
            File file = new File(USER_DIRECTORY_PATH + "/" + userid + "/bid.txt");
            BufferedReader reader = new BufferedReader(new FileReader(file));
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            String currLine = "";
            while ((currLine = reader.readLine()) != null) {
                System.out.println(currLine);
                if (currLine.equals(current)) {
                    if (!remove) {
                        writer.write(update + System.getProperty("line.separator"));
                    }
                    continue;
                }
                writer.write(currLine + System.getProperty("line.separator"));
            }
            writer.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeBidding(int courseid, int mileage, String userid) {
        try {
            File file = new File(USER_DIRECTORY_PATH + "/" + userid + "/bid.txt");
            FileWriter writer = new FileWriter(file, true);
            writer.write(System.getProperty("line.separator") + courseid + "|" + mileage + System.getProperty("line.separator"));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
