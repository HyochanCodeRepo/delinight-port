/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.BranchDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.MembersEntity;

import java.util.List;

public interface BranchService {

    void create(BranchDTO branchDTO);

    void update(BranchDTO branchDTO);

    List<BranchDTO> list(String email);

    void del(Long id);






}
