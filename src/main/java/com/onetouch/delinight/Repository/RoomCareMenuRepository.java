package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.AbleCheck;
import com.onetouch.delinight.Entity.RoomCareMenuEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomCareMenuRepository extends JpaRepository<RoomCareMenuEntity, Long> {

    List<RoomCareMenuEntity> findByHotelEntity_IdAndAbleCheck(Long id, AbleCheck ableCheck);
}
