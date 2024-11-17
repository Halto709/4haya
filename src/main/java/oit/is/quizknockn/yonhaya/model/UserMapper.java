package oit.is.quizknockn.yonhaya.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

  @Select("SELECT * FROM users WHERE id = #{id}")
  User selectById(int id);

  @Select("SELECT * FROM users WHERE userName = #{userName}")
  User selectByUserName(String userName);
}
