package oit.is.quizknockn.yonhaya.model;

import org.springframework.stereotype.Component;

@Component
public class UserWaitRoom {
  private int waitingUser = 0;
  private boolean finishFlag = false;

  public void clear() {
    waitingUser = 0;
    finishFlag = false;
  }

  public int getWaitingUser() {
    return waitingUser;
  }

  public void setWaitingUser(int waitingUser) {
    this.waitingUser = waitingUser;
  }

  public void setFinishFlag(boolean finishFlag) {
    this.finishFlag = finishFlag;
  }

  public boolean isFinishFlag() {
    return finishFlag;
  }

}
