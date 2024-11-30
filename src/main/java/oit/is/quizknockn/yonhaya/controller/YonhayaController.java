package oit.is.quizknockn.yonhaya.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.quizknockn.yonhaya.model.Room;
import oit.is.quizknockn.yonhaya.service.AsyncJoinRoom;
import oit.is.quizknockn.yonhaya.service.AsyncWaitRoom;
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

  // デモ用のクイズインデックス
  private int currentQuestionIndex = 0;
  private int j = 0;
  private int quizID = 1;
  private final int MAX_QUESTIONS = 2;

  @GetMapping("")
  public String showHomePage(Principal prin, ModelMap model) {
    User loginUser = userMapper.selectByUserName(prin.getName());
    model.addAttribute("loginUser", loginUser);
    return "4haya.html";
  }

  @GetMapping("di")
  public String joinRoom(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    if (this.room.addUser(loginUser)) {
      // ユーザがルームに追加される
      this.asyncJoinRoom.userJoin(this.room.getUsers());
      model.addAttribute("room", this.room);

      return "joinRoom.html";
    }
    model.addAttribute("error", loginUser);
    return "4haya.html";
  }

  // ルームに誰が入っているか非同期で表示
  @GetMapping("roomInfo")
  public SseEmitter roomInfo() {
    logger.info("pushRoomUsers");
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    this.asyncJoinRoom.pushRoomUsers(emitter);
    return emitter;
  }

  @GetMapping("waitInfo")
  public SseEmitter waitInfo() {
    logger.info("pushWaitRoom");
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
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
    j++;
    if (j == 2) {
      currentQuestionIndex++;
      j = 0;
    }

    return "quiz.html";
  }

  @PostMapping("judge")
  public String judgeQuiz(@RequestParam String choice, ModelMap model, Principal prin) {
    String result;
    String answer = quizChoicecsMapper.selectById(quizID);
    if (choice.equals(answer)) {
      result = "正解";
      userMapper.updateByUserpoint(prin.getName());
    } else {
      result = "不正解";
    }

    asyncWaitRoom.userWait();

    if (MAX_QUESTIONS <= currentQuestionIndex) {
      model.addAttribute("finish", 1);
    }

    model.addAttribute("result", result);
    return "wait.html";
  }

  @GetMapping("finish")
  public String finish() {
    return "finish.html";
  }
}
