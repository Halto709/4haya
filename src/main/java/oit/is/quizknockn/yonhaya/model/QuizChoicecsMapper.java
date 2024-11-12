package oit.is.quizknockn.yonhaya.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface QuizChoicecsMapper {

  @Select("SELECT * FROM quizoptions WHERE id = #{id}")
  QuizChoices selectById(int id);
}
