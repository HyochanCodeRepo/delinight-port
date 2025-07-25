/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.DTO.InquireDTO;
import com.onetouch.delinight.Entity.InquireEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Entity.InquireEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.List;

public interface InquireRepository extends JpaRepository<InquireEntity, Long> {

    Integer countByCheckInEntity_RoomEntity_HotelEntity_IdAndResponseTimeIsNull(Long id);


    //inquire에 속한 호텔 id를 기준으로 찾는다
    Page<InquireEntity> findByHotelEntity_Id(Long hotelId, Pageable pageable);

    Page<InquireEntity> findInquireEntitiesByCheckInEntity_Id(Long email, Pageable pageable);

    //InquireEntity에서 체크인한 id가 누구인가를 찾는다
    @Query("select c from CheckInEntity c where c.id = :id")
    InquireEntity selectId(Long id);

    //hotelId가 특정 값인 inquire만 전부 찾아서 리스트로 리턴
    List<InquireEntity> findByHotelEntity_Id(Long hotelId);
    List<InquireEntity> findByHotelEntity_MembersEntity_EmailAndResponseTimeIsNull(String email);
    //
    Page<InquireEntity> findByCheckInEntity_UsersEntity_Id(Long usersId, Pageable pageable);
    Page<InquireEntity> findByCheckInEntity_GuestEntity_Id(Long usersId, Pageable pageable);

    List<InquireEntity> findByCheckInEntity_Id(Long checkInId);

    //호텔id로 inquire를 하나 찾아줘
    @Query("select q from InquireEntity q where q.hotelEntity = :id")
    InquireEntity selectHotelId(Long id);



    @Query("select i from InquireEntity i where i.hotelEntity.id = :hotelId")
    //"문의글 테이블에서, 특정 호텔(hotelId)에 대한 모든 문의글(InquireEntity)을 가져와라"
    List<InquireEntity> findAllByHotelId(@Param("hotelId") Long hotelId);
    //이 메서드는 호텔id를 넘기면 해당 호텔에 대한 모든 문의글 목록을 리스트형태로 반환해줌

    @Query("select i from InquireEntity i where i.hotelEntity.id = :hotelId")
    List<InquireEntity> selectList(Long hotelId);

















}
