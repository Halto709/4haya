package oit.is.quizknockn.yonhaya.controller;

import java.security.Principal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import oit.is.quizknockn.yonhaya.model.Quiz;
import oit.is.quizknockn.yonhaya.model.QuizChoicecsMapper;
import oit.is.quizknockn.yonhaya.model.QuizChoices;
import oit.is.quizknockn.yonhaya.model.QuizMapper;
import oit.is.quizknockn.yonhaya.model.Room;
import oit.is.quizknockn.yonhaya.service.AsyncEnterRoom;

@Controller
@RequestMapping("/yonhaya")
public class YonhayaController {

  private final Logger logger = LoggerFactory.getLogger(YonhayaController.class);

  @Autowired
  private Room room;

  @Autowired
  private AsyncEnterRoom asyncEnterRoom;

  @Autowired
  private QuizMapper quizMapper;

  @Autowired
  private QuizChoicecsMapper quizChoicecsMapper;

  // デモ用のクイズインデックス
  private int quizIndex = 1;

  @GetMapping("")
  public String sample21() {
    return "4haya.html";
  }

  @GetMapping("di")
  public String sample38(Principal prin, ModelMap model) {
    String loginUser = prin.getName();
    if (this.room.addUser(loginUser)) {
      // ユーザがルームに追加される
      this.asyncEnterRoom.userEnter(this.room.getUsers());
      model.addAttribute("room", this.room);

      return "joinRoom.html";
    }
    model.addAttribute("error", loginUser);
    return "4haya.html";
  }

  @GetMapping("roomInfo")
  public SseEmitter roomInfo() {
    logger.info("pushRoomUsers");
    SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
    this.asyncEnterRoom.pushRoomUsers(emitter);
    return emitter;
  }

  @GetMapping("quiz")
  public String Shift_Quiz(ModelMap model) {
    Quiz quiz = quizMapper.selectById(quizIndex);
    QuizChoices quizChoices = quizChoicecsMapper.selectById(quizIndex);
    model.addAttribute("quiz", quiz);
    model.addAttribute("Choices", quizChoices);
    return "quiz.html";
  }
}
