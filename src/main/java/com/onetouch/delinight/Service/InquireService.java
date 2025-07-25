/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.InquireEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;

import java.lang.reflect.Member;
import java.util.List;

public interface InquireService{
    //문의사항 등록
    InquireDTO register(InquireDTO inquireDTO, String email);
    List<InquireDTO> findUnprocessedInquire(MembersDTO membersDTO);
    //목록
    Page<InquireDTO> inquireList(Pageable pageable, Long hotelId);
    Page<InquireDTO> inquireList(Pageable pageable, String email);

    void checkInToCheckOut(Long checkInId, Long checkOutId);


    //상세보기
    InquireDTO read(Long id);
    //수정
    InquireDTO update(InquireDTO inquireDTO);
    //삭제
    void delete(Long id);

}
