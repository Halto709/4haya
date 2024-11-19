package oit.is.quizknockn.yonhaya.model;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Component;

@Component
public class Room {
  int roomNo = 1;
  private int maxquizINdex = 10;
  ArrayList<String> users = new ArrayList<>();
  ArrayList<Integer> n = new ArrayList<>();

  public boolean addUser(String name) {
    // 同名のユーザが居たら何もせずにreturn
    for (String s : this.users) {
      if (s.equals(name)) {
        return false;
      }
    }
    // 同名のユーザが居なかった場合はusersにnameを追加する
    this.users.add(name);

    if (users.size() == 2) {
      int i = 0;
      while (i < 10) {
        int tmp = ThreadLocalRandom.current().nextInt(1, maxquizINdex + 1);
        if (checkNumber(tmp)) {
          n.add(tmp);
          i++;
        }
      }
    }
    return true;
  }

  public boolean checkNumber(int tmp) {
    for (int N : n) {
      if (tmp == N) {
        return false;
      }
    }
    return true;
  }

  // 以降はフィールドのgetter/setter
  // これらがないとThymeleafで値を取得できない
  public int getRoomNo() {
    return roomNo;
  }

  public void setRoomNo(int roomNo) {
    this.roomNo = roomNo;
  }

  public ArrayList<String> getUsers() {
    return users;
  }

  public void setUsers(ArrayList<String> users) {
    this.users = users;
  }

  public ArrayList<Integer> getN() {
    return n;
  }

  public void setN(ArrayList<Integer> n) {
    this.n = n;
  }

}
