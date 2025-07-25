/*********************************************************************
 * 클래스명 : RoomCareItemRepository
 * 기능 : 룸케어 메뉴 항목들
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.RoomCareEntity;
import com.onetouch.delinight.Entity.RoomCareItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomCareRepository extends JpaRepository<RoomCareEntity, Long> {
    List<RoomCareEntity> findByCheckInEntity_RoomEntity_HotelEntity_MembersEntity_Id(Long membersId);
    List<RoomCareEntity> findByCheckInEntity_Id(Long checkInId);
    List<RoomCareEntity> findByCheckOutLogEntity_UsersEntity_Id(Long usersId);
}
