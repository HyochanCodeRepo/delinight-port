/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.CenterDTO;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.MembersDTO;

import java.util.List;

public interface CenterService {

    void settingAdmin(Long membersId, Long centerId);
    void create(CenterDTO centerDTO, String email);

    void update(CenterDTO centerDTO);

    List<CenterDTO> list();

    MembersDTO checkEmail(String email);

    CenterDTO read(String email);

    Long findCenter(String email);



}
