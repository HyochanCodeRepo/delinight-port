/*********************************************************************
 * 클래스명 : RoomCareItemServiceImpl
 * 기능 :
 * 작성자 : 박한나
 * 작성일 : 2025-04-25
 * 수정 : 2025-04-25
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.RoomCareItemDTO;
import com.onetouch.delinight.Entity.*;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import com.onetouch.delinight.Repository.RoomCareItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class RoomCareItemServiceImpl implements RoomCareItemService{

    private final MembersRepository membersRepository;
    private final RoomCareItemRepository roomCareItemRepository;
    private final ModelMapper modelMapper;

    @Override
    public void create(RoomCareItemDTO roomCareItemDTO, String email) {
        RoomCareItemEntity roomCareItemEntity = modelMapper.map(roomCareItemDTO, RoomCareItemEntity.class);

        MembersEntity membersEntity = membersRepository.findByEmail(email);
        if (membersEntity == null) throw new EntityNotFoundException("어드민 정보 없음");

        HotelEntity hotelEntity = membersEntity.getHotelEntity();
        if (hotelEntity == null) throw new EntityNotFoundException("어드민이 소속된 호텔 정보 없음");


        roomCareItemRepository.save(roomCareItemEntity);
    }

    @Override
    public void update(RoomCareItemDTO roomCareItemDTO, String email) {
        RoomCareItemEntity roomCareItemEntity = modelMapper.map(roomCareItemDTO, RoomCareItemEntity.class);

        MembersEntity membersEntity = membersRepository.findByEmail(email);
        if (membersEntity == null) throw new EntityNotFoundException("어드민 정보 없음");
        HotelEntity hotelEntity = membersEntity.getHotelEntity();
        if (hotelEntity == null) throw new EntityNotFoundException("어드민이 소속된 호텔 정보 없음");

        RoomCareItemEntity roomCareItemEntityUpdate = roomCareItemRepository.findById(roomCareItemEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException("해당 RoomCareItem을 찾을 수 없음"));


        roomCareItemRepository.save(roomCareItemEntityUpdate);
    }

    @Override
    public List<RoomCareItemDTO> list() {
        List<RoomCareItemEntity> roomCareItemEntityList =
                roomCareItemRepository.findAll();
        List<RoomCareItemDTO> roomCareItemDTOList =
                roomCareItemEntityList.stream().toList().stream().map(
                        roomCareItemEntity -> modelMapper.map(roomCareItemEntity, RoomCareItemDTO.class)
                ).collect(Collectors.toList());
        return roomCareItemDTOList;
    }

    @Override
    public void del(Long roomCareItemId, String email) {
        MembersEntity membersEntity = membersRepository.findByEmail(email);
        if (membersEntity == null) throw new EntityNotFoundException("어드민 정보 없음");

        if (!roomCareItemRepository.existsById(roomCareItemId)) {
            throw new EntityNotFoundException("해당 RoomCareItem을 찾을 수 없음");
        }

        // 삭제
        roomCareItemRepository.deleteById(roomCareItemId);
    }
}
