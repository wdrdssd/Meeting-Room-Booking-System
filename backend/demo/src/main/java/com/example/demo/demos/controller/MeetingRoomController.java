package com.example.demo.demos.controller;

import com.example.demo.config.Result;
import com.example.demo.entity.MeetingRoom;
import com.example.demo.service.MeetingRoomService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
@Tag(name = "会议室管理", description = "会议室相关的所有接口")
public class MeetingRoomController {

    @Autowired
    private MeetingRoomService meetingRoomService;

    @GetMapping
    @Operation(summary = "获取会议室列表", description = "获取所有可用的会议室（状态为1）")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "获取成功，返回会议室列表"),
            @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    public Result<List<MeetingRoom>> getAllMeetingRooms() {
        List<MeetingRoom> meetingRooms = meetingRoomService.getAllMeetingRooms();
        return Result.success(meetingRooms);
    }
}
