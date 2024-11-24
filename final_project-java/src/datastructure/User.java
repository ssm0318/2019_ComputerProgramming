package datastructure;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class User {
    private String userid;
    private List<Bidding> bidList;
    private int currMileage;
    private List<Course> registeredCourses;

    public User(String userid, List<Bidding> bidList) {
        this.userid = userid;
        this.bidList = bidList;
        calculateCurrentMileage();
        registeredCourses = new ArrayList<>();
    }

    public String getUserId() {
        return userid;
    }

    public List<Bidding> getBidList() {
        return bidList;
    }

    public int getCurrMileage() {
        return currMileage;
    }

    public List<Course> getRegisteredCourses() { return registeredCourses; }

    private void calculateCurrentMileage() {
        int sum = 0;
        for (Bidding bid : bidList) {
            sum += bid.mileage;
        }
        currMileage = sum;
    }

    public Pair findBid(int courseid) { // returns index of bidding and its mileage
        int index = (int) bidList.stream().takeWhile(bidding -> bidding.courseId != courseid).count();
        return index >= bidList.size() ? new Pair(-1, -1) : new Pair(index, bidList.get(index).mileage);
    }

    public void updateBidding(int index, Bidding updatedBid) {
        currMileage = currMileage - bidList.get(index).mileage + updatedBid.mileage;
        bidList.set(index, updatedBid);
    }

    public void addBidding(Bidding newBid) {
        currMileage += newBid.mileage;
        bidList.add(newBid);
    }

    public void removeBidding(Integer bidIndex) {
        currMileage = currMileage - bidList.get(bidIndex).mileage;
        bidList.remove(bidIndex.intValue());
    }

    public void addCourse(Course course) {
        registeredCourses.add(course);
    }

    public void clearBidList() {
        bidList = new ArrayList<>();
    }

    public static class BidPrioritySorter implements Comparator<User> {
        @Override
        public int compare(User A, User B) {
            int bidSizeComp = Integer.compare(A.getBidList().size(), B.getBidList().size());

            if (bidSizeComp != 0) {
                return bidSizeComp;
            }

            return A.getUserId().compareTo(B.getUserId());
        }
    }

    @Override
    public String toString() {
        return userid;
    }

    @Override
    public boolean equals (Object o) {
        if (o instanceof User) {
            return userid == ((User) o).userid;
        } else {
            return false;
        }
    }
}
