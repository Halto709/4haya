package oit.is.quizknockn.yonhaya.model;

import java.util.ArrayList;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MatchResultMapper {
  @Insert("Insert into MatchResult (roomNo,username,point,rank) VALUES(#{roomNo},#{userName},#{point},#{rank});")
  void insertMatchResult(int roomNo, String username, int point, int rank);

  @Select("Select * from MatchResult where roomNo = #{roomNo} AND userName = #{userName}")
  MatchResult selectMatchResult(int roomNo, String username);

  @Select("Select * from MatchResult where roomNo = #{roomNo}")
  ArrayList<MatchResult> selectMatchResultByRoomNo(int roomNo);
}
