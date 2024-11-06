package oit.is.quizknockn.yonhaya.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class YonhayaController {

  @GetMapping("/yonhaya")
  public String sample21() {
    return "4haya.html";
  }

}
