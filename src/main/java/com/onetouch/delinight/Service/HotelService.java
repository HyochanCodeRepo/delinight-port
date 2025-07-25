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

import java.util.List;

public interface HotelService {

    int assignCheck(String email);
    Integer unansweredCheck(Long id);
    void addMembers(Long memberId, Long hotelId);
    void create(HotelDTO hotelDTO);

    void update(HotelDTO hotelDTO);

    List<HotelDTO> list(MembersDTO membersDTO);

    HotelDTO findHotelDTOById(Long id);
    Long findHotelByEmail(String email);



    void del(Long id);

    void modify(Long memberId, Long hotelId);






}
