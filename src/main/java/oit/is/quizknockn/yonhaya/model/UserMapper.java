package oit.is.quizknockn.yonhaya.model;

import java.util.ArrayList;

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

  @Select("SELECT * FROM users WHERE isactive = #{isActive}")
  ArrayList<User> selectByResult(boolean isActive);

  @Update("UPDATE users SET ISACTIVE = #{isActive} WHERE userName = #{userName}")
  void updateByUserIsActive(String userName,boolean isActive);

  @Update("UPDATE users SET point = point + #{point} WHERE userName = #{userName}")
  void updatePointByUserName(String userName, int point);

  @Update("UPDATE users SET point = 0 ")
  void setPointZero();

}
