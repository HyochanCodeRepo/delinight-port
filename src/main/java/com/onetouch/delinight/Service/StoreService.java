/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.StoreDTO;

import java.util.List;

public interface StoreService {

    void addMembers(Long memberId, Long storeId) throws Exception;

    void modiMembers(Long memberId, Long storeId);

    int assignCheck(String email);
    Long findStoreByEmail(String email);
    void create(StoreDTO storeDTO);

    Integer awaitingCountCheck(Long storeId);

    List<StoreDTO> listMembers(MembersDTO membersDTO);

    List<StoreDTO> list(String email);

    void del(Long id);
    StoreDTO read(Long id);

    void update(StoreDTO storeDTO);

    List<StoreDTO> storeList(String email);

    List<StoreDTO> findOperationStore();




}
