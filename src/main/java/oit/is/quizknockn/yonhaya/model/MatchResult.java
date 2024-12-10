package oit.is.quizknockn.yonhaya.model;

public class MatchResult {
  int roomNo;
  String userName;
  int point;
  int rank;

  public void setRoomNo(int roomNo) {
    this.roomNo = roomNo;
  }

  public int getPoint() {
    return point;
  }

  public int getRank() {
    return rank;
  }

  public int getRoomNo() {
    return roomNo;
  }

  public String getUserName() {
    return userName;
  }

  public void setPoint(int point) {
    this.point = point;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

}
