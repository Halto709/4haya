package oit.is.quizknockn.yonhaya.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchResultMapper {
  @Insert("Insert into MatchResult (roomNo,userName,point,rank) VALUES(#{roomno},#{username},#{point},#{rank});")
  void insertMatchResult(int roomno, String username, int point, int rank);

  @Select("Select * from MatchResult where roomNo = #{roomNo} AND userName = #{userName}")
  MatchResult selectMatchResult(int roomNo, String username);

  @Select("Select * from MatchResult where roomNo = #{roomNo}")
  ArrayList<MatchResult> selectMatchResultByRoomNo(int roomNo);

  @Select("Select * from MatchResult")
  ArrayList<MatchResult> selectMatchResultByAll();
}
