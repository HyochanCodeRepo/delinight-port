/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.StoreEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<StoreEntity, Long> {

    StoreEntity findByMembersEntity_Email(String email);
    List<StoreEntity> findByHotelEntity_MembersEntity_Id(Long id);

    boolean existsByMembersEntity_Email(String email);

    List<StoreEntity> findByHotelEntity_Id(Long hotelId);

    @Query("select s from StoreEntity s where s.hotelEntity.membersEntity.email = :email")
    List<StoreEntity> selectallByHotelAdmin(String email);




}
