/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Constant.Status;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.MembersEntity;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface MembersService {

    void makeSysA();

    List<MembersDTO> findPendingMembersListByCenterMembers(MembersDTO membersDTO);
    boolean checkOperation(MembersDTO membersDTO);
    List<MembersDTO> findMembersListByCenterEmail(String email);

    List<MembersDTO> findMembersListByHotelEmail(String email);

    boolean assignCheck(String email, int sep);
    Long create(MembersDTO membersDTO);
    void update(MembersDTO membersDTO, String newPhone, String newPassword);

    Page<MembersEntity> getList(int page);
    Page<MembersEntity> getListHotel(int page, String email);
    Page<MembersEntity> getListStore(int page, String email);
    Page<MembersEntity> getListBystatus(Status status, int page);

    List<MembersDTO> findAll();

    //    public List<MembersDTO> findSuper();

    Page<MembersEntity> findAccount(Status status, int page, String email, String sep);


    Integer countOfRequestAccount(String email);

    MembersDTO approve(Long id);
    MembersDTO Disapprove(Long id);

    Map<Role, Long> findRoleByEmail(String email);
    Role findOnlyRoleByEmail(String email);

    MembersDTO findByEmail(String email);

}