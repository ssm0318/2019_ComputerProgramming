package target;

import datastructure.*;

import java.util.*;

public class Server {
    private StorageManager storageManager;

    public List<Course> search(Map<String,Object> searchConditions, String sortCriteria){
        storageManager = new StorageManager();
        List<Course> result = storageManager.searchCourses(searchConditions, sortCriteria);

        return result;
    }

    public int bid(int courseid, int mileage, String userid){
        // TODO Problem 2.2.
        return ErrorCode.IO_ERROR;
    }

    public Pair<Integer,List<Bidding>> retrieveBids(String userid){
        // TODO Problem 2.2.
        return new Pair<>(ErrorCode.IO_ERROR,new ArrayList<>());
    }

    public boolean clearBids(){
        // TODO Problem 2.3.
        return false;
    }

    public Pair<Integer,List<Course>> retrieveRegisteredCourse(String userid){
        // TODO Problem 2.3.
        return new Pair<>(ErrorCode.IO_ERROR,new ArrayList<>());
    }
}