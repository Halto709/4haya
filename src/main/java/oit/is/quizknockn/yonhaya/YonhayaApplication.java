package oit.is.quizknockn.yonhaya;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class YonhayaApplication {

  public static void main(String[] args) {
    SpringApplication.run(YonhayaApplication.class, args);
  }

}
