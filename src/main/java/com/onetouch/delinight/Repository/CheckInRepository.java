/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CheckInRepository extends JpaRepository<CheckInEntity, Long> {

    CheckInEntity findByRoomEntity_Id(Long id);

    List<CheckInEntity> findByCheckInStatusAndRoomEntity_HotelEntity_MembersEntity_Email(CheckInStatus checkinstatus, String email);
    List<CheckInEntity> findByRoomEntity_HotelEntity_MembersEntity_Email(String email);

    CheckInEntity findByGuestEntity_Phone(String phone);

    CheckInEntity findByUsersEntity_Email(String email);

    @Query("select c from CheckInEntity c where c.roomEntity.id = :id")
    CheckInEntity selectRoom(Long id);

    @Query("select sum(o.totalPrice) from OrdersEntity o where o.paymentEntity.orderType = 'PAYLATER' and o.checkInEntity.id = :id")
    Integer selectPriceByCheckinId(@Param("id") Long id);


}
