package datastructure;

import java.util.List;

public class User {
    private String userid;
    private List<Bidding> bidList;
    private int currMileage;

    public User(String userid, List<Bidding> bidList) {
        this.userid = userid;
        this.bidList = bidList;
        calculateCurrentMileage();
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

    private void calculateCurrentMileage() {
        int sum = 0;
        for (Bidding bid : bidList) {
            sum += bid.mileage;
        }
        currMileage = sum;
    }

    public Pair hasBidden(int courseid) {
        int index = (int) bidList.stream().takeWhile(bidding -> bidding.courseId != courseid).count();
        return index >= bidList.size() ? new Pair(-1, null) : new Pair(index, bidList.get(index).mileage);
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
        bidList.remove(bidIndex);
    }
}
