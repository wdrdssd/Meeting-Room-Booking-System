package com.example.demo.demos.controller;

import com.example.demo.config.Result;
import com.example.demo.entity.MeetingRoom;
import com.example.demo.service.MeetingRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping
    public Result<List<MeetingRoom>> getAllMeetingRooms() {
        List<MeetingRoom> meetingRooms = meetingRoomService.getAllMeetingRooms();
        return Result.success(meetingRooms);
    }
}
