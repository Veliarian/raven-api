package com.raven.api.meetings.repositories;

import com.raven.api.meetings.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
