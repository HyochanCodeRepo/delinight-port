package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.CheckOutLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface CheckOutLogRepository extends JpaRepository<CheckOutLogEntity, Long> {

        @Query("select c from CheckOutLogEntity  c join fetch c.usersEntity where c.checkoutDate = :date")
        List<CheckOutLogEntity> findByCheckoutDate(@Param("date") LocalDate checkoutDate);


        List<CheckOutLogEntity> findByRoomEntity_HotelEntity_IdAndUsersEntityIsNotNull(Long hotelId);



}
