package com.example.demo.service;

import com.example.demo.dao.MeetingRoomDao;
import com.example.demo.entity.MeetingRoom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingRoomService {
    @Autowired
    private MeetingRoomDao meetingRoomDao;

    public List<MeetingRoom> getAllMeetingRooms() {
        return meetingRoomDao.findAllMeetingRoom();
    }
    public MeetingRoom getMeetingRoomById(int id) {
        return meetingRoomDao.findMeetingRoomById(id);
    }
}
