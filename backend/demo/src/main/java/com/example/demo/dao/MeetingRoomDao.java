package com.example.demo.dao;

import com.example.demo.entity.MeetingRoom;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MeetingRoomDao {
    @Select("select * from meeting_room where status='1'")
    List<MeetingRoom> findAllMeetingRoom();

    @Select("select * from meeting_room where id = #{id}")
    MeetingRoom findMeetingRoomById(int id);
}
