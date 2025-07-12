package com.raven.api.meetings.repositories;

import com.raven.api.meetings.entity.Room;
import com.raven.api.meetings.enums.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    void deleteRoomBySid(String sid);
    List<Room> findAllByStatusAndStartTimeBefore(RoomStatus status, LocalDateTime time);
}
