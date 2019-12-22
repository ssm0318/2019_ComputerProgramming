package target;

import datastructure.*;

import java.util.*;

public class Server {
    private StorageManager storageManager;

    public Server() {
        storageManager = new StorageManager();
    }

    public List<Course> search(Map<String,Object> searchConditions, String sortCriteria) {
        return storageManager.searchCourses(searchConditions, sortCriteria);
    }

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
            storageManager.cancelBid(courseid, mileage, userid);
            return ErrorCode.SUCCESS;
        }
        return storageManager.processBid(courseid, mileage, userid) ? ErrorCode.SUCCESS : ErrorCode.OVER_MAX_MILEAGE;
    }

    public Pair<Integer,List<Bidding>> retrieveBids(String userid) {
        return storageManager.getUserBids(userid);
    }

    public boolean clearBids(){
        // TODO Problem 2.3.
        return false;
    }

    public Pair<Integer,List<Course>> retrieveRegisteredCourse(String userid) {
        // TODO Problem 2.3.
        return new Pair<>(ErrorCode.IO_ERROR,new ArrayList<>());
    }
}