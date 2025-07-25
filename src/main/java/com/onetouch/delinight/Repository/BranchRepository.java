/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.DTO.BranchDTO;
import com.onetouch.delinight.Entity.BranchEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BranchRepository extends JpaRepository<BranchEntity, Long> {


        List<BranchEntity> findByCenterEntity_MembersEntity_Email(String email);


}
