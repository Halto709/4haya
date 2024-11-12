package oit.is.quizknockn.yonhaya.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface QuizMapper {

  @Select("SELECT * FROM quizzes WHERE id = #{id}")
  Quiz selectById(int id);
}
