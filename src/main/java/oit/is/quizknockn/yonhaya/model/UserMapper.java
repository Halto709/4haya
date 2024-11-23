package oit.is.quizknockn.yonhaya.model;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {

  @Select("SELECT * FROM users WHERE id = #{id}")
  User selectById(int id);

  @Select("SELECT * FROM users WHERE userName = #{userName}")
  User selectByUserName(String userName);

  @Update("UPDATE users SET point = point + 1 WHERE userName = #{userName}")
  void updateByUserpoint(String userName);
}
