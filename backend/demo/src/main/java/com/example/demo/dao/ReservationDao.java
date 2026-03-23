package com.example.demo.dao;

import com.example.demo.entity.Reservation;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Component
public interface ReservationDao {
    @Insert("insert into reservation(user_id,room_id,title,reserve_date,start_time,end_time,status)" + "values (#{userId},#{roomId},#{title},#{reserveDate},#{startTime},#{endTime},'1')")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(Reservation reservation);

    @Select("select * from reservation where room_id = #{roomId} and reserve_date =#{date} and status ='1' ")
    List<Reservation> selectByRoomAndDate(@Param("roomId") Integer roomId,@Param("date") LocalDate date);

    @Select("select * from reservation where user_id =#{userId} ORDER BY reserve_date DESC, start_time DESC")
    List<Reservation> selectByUserId(@Param("userId") Long userId);

    @Select("select * from reservation where reserve_date between #{startDate} and #{endDate} and status = '1' ORDER BY reserve_date,start_time")
    List<Reservation> selectByDate(@Param("startDate") LocalDate startDate,@Param("endDate") LocalDate endDate);

    @Update("update reservation SET status = '0' where id =#{id} and user_id = #{userId}")
    int updateStatus(@Param("id") Integer id, @Param("userId") Long userId);
}
