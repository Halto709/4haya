package oit.is.quizknockn.yonhaya.service;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
public class AsyncEnterRoom {
  private final Logger logger = LoggerFactory.getLogger(AsyncEnterRoom.class);

  private ArrayList<String> joinedUser = new ArrayList<>();
  private boolean userUpdate = false;

  @Async
  public void userEnter(String name) {
    logger.info("User Enters a Room");
    this.joinedUser.add(name);
    this.userUpdate = true;
  }

  @Async
  public void pushRoomUsers(SseEmitter emitter) {
    logger.info("pushRoomUsers start");

    while (true) {
      try {
        // 新しいユーザが追加されていないなら0.5s休み
        if (userUpdate == false) {
          TimeUnit.MILLISECONDS.sleep(500);
          continue;
        }

        // 新しいユーザが追加されていれば，その情報を送信
        logger.info("send(RoomUsers)");
        TimeUnit.SECONDS.sleep(1);// 1秒STOP
        // JSONオブジェクトがクライアントに送付される
        emitter.send(joinedUser);
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
