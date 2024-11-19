package oit.is.quizknockn.yonhaya.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface QuizChoicecsMapper {

  @Select("SELECT * FROM quizoptions WHERE id = #{id}")
  QuizChoices selectAllById(int id);

  @Select("SELECT correct FROM quizoptions WHERE id = #{id}")
  Integer selectById(int id);

  @Select("SELECT Choice_1 FROM QUIZOPTIONS WHERE Choice_1 = #{Choice1}")
  String selectByChoice1(String Choice1);

  @Select("SELECT Choice_2 FROM QUIZOPTIONS WHERE Choice_2 = #{Choice2}")
  String selectByChoice2(String Choice2);

  @Select("SELECT Choice_3 FROM QUIZOPTIONS WHERE Choice_3 = #{Choice3}")
  String selectByChoice3(String Choice3);

  @Select("SELECT Choice_4 FROM QUIZOPTIONS WHERE Choice_4 = #{Choice4}")
  String selectByChoice4(String Choice4);
}
