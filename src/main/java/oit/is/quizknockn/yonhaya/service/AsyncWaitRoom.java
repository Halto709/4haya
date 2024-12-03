package oit.is.quizknockn.yonhaya.service;

import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class AsyncWaitRoom {
  private final Logger logger = LoggerFactory.getLogger(AsyncWaitRoom.class);

  private int waitingUser = 0;
  private boolean userUpdate = false;

  @Async
  public void userWait() {
    logger.info("User Enters a Room");
    waitingUser++;
    this.userUpdate = true;
  }

  public void clearWait() {
    waitingUser = 0;
    userUpdate = false;
  }

  @Async
  public void pushWaitRoom(SseEmitter emitter) {
    logger.info("pushWaitRoom start");

    while (true) {
      try {
        // 0.1s休み
        if (userUpdate == false) {
          TimeUnit.MILLISECONDS.sleep(100);
          continue;
        }

        // 新しいユーザが追加されていれば，その情報を送信
        logger.info("send(RoomUsers)");
        TimeUnit.MILLISECONDS.sleep(100);// 0.1秒STOP
        // JSONオブジェクトがクライアントに送付される
        emitter.send(waitingUser);
        userUpdate = false;

      } catch (Exception e) {
        // 例外の名前とメッセージだけ表示する
        logger.warn("Exception:" + e.getClass().getName() + ":" + e.getMessage());
        // 例外が発生したらカウントとsendを終了する
        break;
      }
    }
    emitter.complete();// emitterの後始末．明示的にブラウザとの接続を一度切る．

  }

}
