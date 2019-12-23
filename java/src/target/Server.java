package target;

import datastructure.*;

import java.io.IOException;
import java.util.*;

public class Server {
    private StorageManager storageManager;

    public Server() {
        storageManager = new StorageManager();
    }

    // Question 2-1
    public List<Course> search(Map<String,Object> searchConditions, String sortCriteria) {
        return storageManager.searchCourses(searchConditions, sortCriteria);
    }

    // Question 2-2 (1)
    public int bid(int courseid, int mileage, String userid) {
        if (!storageManager.hasUser(userid)) {
            return ErrorCode.USERID_NOT_FOUND;
        } else if (!storageManager.hasCourse(courseid)) {
            return ErrorCode.NO_COURSE_ID;
        } else if (mileage < 0) {
            return ErrorCode.NEGATIVE_MILEAGE;
        } else if (mileage > Config.MAX_MILEAGE_PER_COURSE) {
            return ErrorCode.OVER_MAX_COURSE_MILEAGE;
        } else if (mileage == 0) {
            storageManager.cancelBid(courseid, userid);
            return ErrorCode.SUCCESS;
        }
        return storageManager.addBidding(courseid, mileage, userid) ? ErrorCode.SUCCESS : ErrorCode.OVER_MAX_MILEAGE;
    }

    // Question 2-2 (2)
    public Pair<Integer,List<Bidding>> retrieveBids(String userid) {
        if (!storageManager.hasUser(userid)) {
            return new Pair(ErrorCode.USERID_NOT_FOUND, new ArrayList<>());
        }
        return new Pair(ErrorCode.SUCCESS, storageManager.getUserBiddings(userid));
    }

    // Question 2-3 (1)
    public boolean clearBids(){
        try {
            storageManager.processAllBidding();
            return true;
        } catch (IOException ie) {
            return false;
        }
    }

    // Question 2-3 (2)
    public Pair<Integer,List<Course>> retrieveRegisteredCourse(String userid) {
        if (!storageManager.hasUser(userid)) {
            return new Pair(ErrorCode.USERID_NOT_FOUND, new ArrayList<>());
        }
        return new Pair(ErrorCode.SUCCESS, storageManager.getUserCourses(userid));
    }
}