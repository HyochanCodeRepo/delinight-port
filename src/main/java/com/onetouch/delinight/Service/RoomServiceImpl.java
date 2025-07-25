/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.RoomDTO;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.RoomEntity;
import com.onetouch.delinight.Entity.UsersEntity;
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.RoomRepository;
import com.onetouch.delinight.Repository.UsersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class RoomServiceImpl implements RoomService{


    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;
    private final HotelRepository hotelRepository;
    private final CheckInService checkInService;
    private final HotelService hotelService;
    private final CheckInRepository checkInRepository;

    @Override
    public void create(RoomDTO roomDTO, String email) {


        RoomEntity roomEntity =
                modelMapper.map(roomDTO, RoomEntity.class);


        HotelEntity hotelEntity =
                hotelRepository.findByMembersEntity_Email(email);


        roomEntity.setHotelEntity(hotelEntity);


        checkInService.create(roomRepository.save(roomEntity));

    }

    @Override
    public void update(RoomDTO roomDTO) {


        RoomEntity roomEntity =
            modelMapper.map(roomDTO, RoomEntity.class);
        RoomEntity room =
                roomRepository.findById(roomEntity.getId()).orElseThrow(EntityNotFoundException::new);

        room.setName(roomEntity.getName());
        room.setContent(roomEntity.getContent());

    }

    @Override
    public List<RoomDTO> list(String email) {

        Long id = hotelService.findHotelByEmail(email);

        List<RoomEntity> roomEntityList =
            roomRepository.findByHotelEntity_Id(id);
        List<RoomDTO> roomDTOList =
                roomEntityList.stream().map(
                        roomEntity -> modelMapper.map(roomEntity, RoomDTO.class)
                                .setHotelDTO(modelMapper.map(roomEntity.getHotelEntity(), HotelDTO.class))).collect(Collectors.toList());
        return roomDTOList;
    }

    @Override
    public void del(Long id) {
        log.info("service에 들어온 id : "+ id);
        log.info("service에 들어온 id : "+ id);

        CheckInEntity checkInEntity =
            checkInRepository.selectRoom(id);

        log.info("가져온 checkinEntity" + checkInEntity);
        if (checkInEntity.getCheckInStatus().equals(CheckInStatus.VACANCY)) {
            checkInService.del(checkInEntity.getId());
            roomRepository.deleteById(id);
        } else {
            log.info("현재 체크인 상태인 방입니다.");
            throw new IllegalStateException("현재 체크인 상태인 방입니다.");
        }

    }
}
