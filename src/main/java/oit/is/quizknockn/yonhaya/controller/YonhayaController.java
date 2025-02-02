package oit.is.quizknockn.yonhaya.controller;

import java.security.Principal;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.quizknockn.yonhaya.model.Room;
import oit.is.quizknockn.yonhaya.service.AsyncJoinRoom;
import oit.is.quizknockn.yonhaya.service.AsyncWaitRoom;
import oit.is.quizknockn.yonhaya.model.MatchResult;
import oit.is.quizknockn.yonhaya.model.MatchResultMapper;
import oit.is.quizknockn.yonhaya.model.Quiz;
import oit.is.quizknockn.yonhaya.model.QuizMapper;
import oit.is.quizknockn.yonhaya.model.QuizChoices;
import oit.is.quizknockn.yonhaya.model.QuizChoicecsMapper;
import oit.is.quizknockn.yonhaya.model.User;
import oit.is.quizknockn.yonhaya.model.UserMapper;

@Controller
@RequestMapping("/yonhaya")
public class YonhayaController {

  private final Logger logger = LoggerFactory.getLogger(YonhayaController.class);

  @Autowired
  private Room room;

  @Autowired
  private AsyncJoinRoom asyncJoinRoom;

  @Autowired
  private AsyncWaitRoom asyncWaitRoom;

  @Autowired
  private QuizMapper quizMapper;

  @Autowired
  private QuizChoicecsMapper quizChoicecsMapper;

  @Autowired
  private UserMapper userMapper;

  @Autowired
  private MatchResultMapper matchResultMapper;

  // デモ用のクイズインデックス
  private int currentQuestionIndex = 0;
  private int userAnsQuiz = 0;
  private int quizID = 1;
  private int corect = 1;
  private int finishNumber = 0;
  private final int MAX_QUESTIONS = 10;
  private final int MAX_USER_NUMBER = 2;
  private int scoreWeight = 4;
  // 試合回数の記録用
  private int Match_history = 0;
  private int Match_history_flag = 0;

  @GetMapping("")
  public String showHomePage(Principal prin, ModelMap model) {
    User loginUser = userMapper.selectByUserName(prin.getName());
    model.addAttribute("loginUser", loginUser.getUserName());
    if (Match_history > 0) {
      model.addAttribute("Match_history", Match_history);
    }
    return "4haya.html";
  }

  @GetMapping("help")
  public String showHelp() {
    return "help.html";
  }

  @GetMapping("di")
  public String joinRoom(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    if (this.room.addUser(loginUser)) {
      // ユーザがルームに追加される
      this.asyncJoinRoom.userJoin(this.room.getUsers());
      model.addAttribute("room", this.room);
      // ユーザがルームに追加されたら、isActiveをtrueにする。
      userMapper.updateByUserIsActive(loginUser, true);

      // 検証用
      System.out.println("User Login");
      for (int i = 0; i < room.getUsers().size(); i++) {
        System.out.println(room.getUsers().get(i));
      }
      return "joinRoom.html";
    }
    model.addAttribute("error", loginUser);
    return "4haya.html";
  }

  // ルームに誰が入っているか非同期で表示
  @GetMapping("roomInfo")
  @Transactional
  public SseEmitter roomInfo() {
    logger.info("pushRoomUsers");
    SseEmitter emitter = new SseEmitter((long) (1 * 60 * 1000));
    this.asyncJoinRoom.pushRoomUsers(emitter);
    return emitter;
  }

  @GetMapping("waitInfo")
  @Transactional
  public SseEmitter waitInfo() {
    logger.info("pushWaitRoom");
    SseEmitter emitter = new SseEmitter((long) (1 * 60 * 1000));
    this.asyncWaitRoom.pushWaitRoom(emitter);
    return emitter;
  }

  @GetMapping("quiz")
  public String shiftQuiz(ModelMap model) {
    if (MAX_QUESTIONS <= currentQuestionIndex) {
      return "finish.html";
    }
    // quizIndexを1から10のランダムな値に設定
    quizID = room.getQuizOrder().get(currentQuestionIndex);
    Quiz quiz = quizMapper.selectById(quizID);
    QuizChoices quizChoices = quizChoicecsMapper.selectAllById(quizID);
    model.addAttribute("quiz", quiz);
    model.addAttribute("Choices", quizChoices);
    model.addAttribute("currentQuestionIndex", currentQuestionIndex + 1);
    userAnsQuiz++;
    if (userAnsQuiz == MAX_USER_NUMBER) {
      currentQuestionIndex++;
      userAnsQuiz = 0;
    }
    scoreWeight = 2;
    corect = 1;

    return "quiz.html";
  }

  @PostMapping("judge")
  public String judgeQuiz(@RequestParam String choice, ModelMap model, Principal prin) {
    String result;
    String answer = quizChoicecsMapper.selectById(quizID);

    if (choice.equals(answer)) {
      result = corect++ + "番目に正解!";
      userMapper.updatePointByUserName(prin.getName(), scoreWeight--);
    } else {
      result = "不正解";
    }

    ArrayList<User> UserResult = userMapper.selectByResult(true);

    for (int i = 1; i < UserResult.size(); i++) {
      User key = UserResult.get(i);
      int j = i - 1;

      // key の point より大きい値を右にシフトする
      while (j >= 0 && UserResult.get(j).getPoint() < key.getPoint()) {
        UserResult.set(j + 1, UserResult.get(j));
        j--;
      }
      UserResult.set(j + 1, key);
    }

    int rankNumber = 1;
    for (int i = 0; i < UserResult.size(); i++) {
      // 前のユーザーとポイントが同じ場合、同じランクを設定

      if (i > 0 && UserResult.get(i).getPoint() == UserResult.get(i -
          1).getPoint()) {
        UserResult.get(i).setRank(UserResult.get(i - 1).getRank());
      } else {
        UserResult.get(i).setRank(rankNumber);
      }
      rankNumber++;

    }

    asyncWaitRoom.userRank(UserResult);
    asyncWaitRoom.userWait();

    if (MAX_QUESTIONS <= currentQuestionIndex) {
      finishNumber++;
    }
    if (finishNumber == MAX_USER_NUMBER) {
      asyncWaitRoom.quizFinish();
    }

    model.addAttribute("result", result);
    return "wait.html";
  }

  @GetMapping("finish")
  public String finish(ModelMap model, Principal prin) {
    ArrayList<User> UserResult = userMapper.selectByResult(true);

    // 挿入ソートを使用して UserResult を point で降順に並び替える
    for (int i = 1; i < UserResult.size(); i++) {
      User key = UserResult.get(i);
      int j = i - 1;

      // key の point より大きい値を右にシフトする
      while (j >= 0 && UserResult.get(j).getPoint() < key.getPoint()) {
        UserResult.set(j + 1, UserResult.get(j));
        j--;
      }
      UserResult.set(j + 1, key);
    }

    // 並び替え後に rankNumber を設定する
    int rankNumber = 1;
    for (int i = 0; i < UserResult.size(); i++) {
      // 前のユーザーとポイントが同じ場合、同じランクを設定

      if (i > 0 && UserResult.get(i).getPoint() == UserResult.get(i -
          1).getPoint()) {
        UserResult.get(i).setRank(UserResult.get(i - 1).getRank());
        userMapper.updateRank(UserResult.get(i).getUserName(), UserResult.get(i - 1).getRank());
      } else {
        UserResult.get(i).setRank(rankNumber);
        userMapper.updateRank(UserResult.get(i).getUserName(), rankNumber);
      }
      rankNumber++;
    }

    User user_result = userMapper.selectByUserName(prin.getName());
    matchResultMapper.insertMatchResult(room.getRoomNo(), prin.getName(),
        user_result.getPoint(),
        user_result.getRank());

    model.addAttribute("UserResult", UserResult);
    return "finish.html";
  }

  @GetMapping("exit")
  public String exit(ModelMap model, Principal prin) {
    String loginUser = prin.getName();
    resetGame(loginUser);

    if (Match_history_flag == 0) {
      Match_history++;
      Match_history_flag++;
    } else if (Match_history_flag != 0) {
      Match_history_flag++;
    } else if (Match_history_flag == MAX_USER_NUMBER) {
      Match_history_flag = 0;
    }

    model.addAttribute("loginUser", loginUser);
    model.addAttribute("Match_history", Match_history);
    return "4haya.html";
  }

  @GetMapping("Match_history")
  public String Match_History(ModelMap model) {
    ArrayList<MatchResult> Match_Result = matchResultMapper.selectMatchResultByAll();
    model.addAttribute("Match_Result", Match_Result);
    return "MatchHistory.html";
  }

  // ルーム情報をリセットする
  private void resetGame(String loginUser) {
    // 現在のroomNoが過去の試合結果に存在する場合
    // （roomNoに重複がある場合）
    if (matchResultMapper.existMatchResultByRoomNo(room.getRoomNo())) {
      room.clearRoomInfo();
    }
    userMapper.updateByUserIsActive(loginUser, false);
    userMapper.setPointZero();
    userMapper.setRankZero();
    currentQuestionIndex = 0;
    quizID = 1;
    asyncJoinRoom.clearuserJoin();
    asyncWaitRoom.clearWait();
    finishNumber = 0;

  }

  @GetMapping("ownerReset")
  public String ownerReset() {
    resetGame(null);
    return "4haya.html";
  }

}
