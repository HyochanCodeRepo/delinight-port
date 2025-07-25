/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.HotelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HotelRepository extends JpaRepository<HotelEntity, Long> {


    List<HotelEntity> findByBranchEntity_CenterEntity_MembersEntity_Email(String email);
    HotelEntity findByMembersEntity_Email(String email);

    boolean existsByMembersEntity_Email(String email);

    @Query("select h from HotelEntity h where h.branchEntity.centerEntity.membersEntity.email = :email")
    List<HotelEntity> selectallBySuper(String email);












}
