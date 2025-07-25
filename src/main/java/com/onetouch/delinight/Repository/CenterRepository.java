/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.CenterEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CenterRepository extends JpaRepository<CenterEntity, Long> {

    boolean existsByMembersEntity_Email(String email);
    CenterEntity findByMembersEntity_Email(String email);


}
