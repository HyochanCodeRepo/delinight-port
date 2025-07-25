package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.AbleCheck;
import com.onetouch.delinight.DTO.CheckInDTO;
import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.RoomCareMenuDTO;
import com.onetouch.delinight.Entity.ImageEntity;
import com.onetouch.delinight.Entity.RoomCareMenuEntity;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.ImageRepository;
import com.onetouch.delinight.Repository.RoomCareMenuRepository;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Log4j2
@RequiredArgsConstructor
public class RoomCareMenuService {

    private final RoomCareMenuRepository roomCareMenuRepository;
    private final HotelRepository hotelRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
    private final CheckInService checkInService;


    public String imgRead(Long menuId){
        if(imageRepository.findByRoomCareMenuEntity_Id(menuId).isPresent()) {
            String imgFullUrl = imageRepository.findByRoomCareMenuEntity_Id(menuId).get().getFullUrl();
            return imgFullUrl;

        }
        return null;
    }
    public void update(RoomCareMenuDTO roomCareMenuDTO){

        RoomCareMenuEntity roomCareMenuEntity = roomCareMenuRepository.findById(roomCareMenuDTO.getId()).get();
        roomCareMenuEntity.setName(roomCareMenuDTO.getName());
        roomCareMenuEntity.setContent(roomCareMenuDTO.getContent());
        RoomCareMenuEntity roomCareMenuEntity1 = roomCareMenuRepository.save(roomCareMenuEntity);
        if(roomCareMenuDTO.getImgNum()!=null) {
            imageRepository.deleteByRoomCareMenuEntity_Id(roomCareMenuEntity1.getId());
            ImageEntity imageEntity = imageRepository.findById(roomCareMenuDTO.getImgNum()).get();
            imageEntity.setRoomCareMenuEntity(roomCareMenuEntity1);
        }
    }

    public void delete(Long menuId){

        RoomCareMenuEntity roomCareMenuEntity = roomCareMenuRepository.findById(menuId).get();
        roomCareMenuEntity.setAbleCheck(AbleCheck.DISABLE);
        roomCareMenuRepository.save(roomCareMenuEntity);


    }

    public void register(RoomCareMenuDTO roomCareMenuDTO){

        log.info(roomCareMenuDTO);

        try {
            RoomCareMenuEntity roomCareMenuEntity = new RoomCareMenuEntity();
                    roomCareMenuEntity.setContent(roomCareMenuDTO.getContent());
                    roomCareMenuEntity.setName(roomCareMenuDTO.getName());
                    roomCareMenuEntity.setAbleCheck(AbleCheck.ENABLE);
                    roomCareMenuEntity.setHotelEntity(hotelRepository.findById(roomCareMenuDTO.getHotelId()).get());

        RoomCareMenuEntity roomCareMenuEntity1 = roomCareMenuRepository.save(roomCareMenuEntity);
        if(roomCareMenuDTO.getImgNum()!=null) {
            ImageEntity imageEntity = imageRepository.findById(roomCareMenuDTO.getImgNum()).get();
            imageEntity.setRoomCareMenuEntity(roomCareMenuEntity1);
        }

        }

        catch (Exception e){
            throw new RuntimeException("저장 오류"+e.getMessage());
        }
    }

    public List<RoomCareMenuDTO> list(Long hotelId){

        List<RoomCareMenuEntity> roomCareMenuEntityList = roomCareMenuRepository.findByHotelEntity_IdAndAbleCheck(hotelId, AbleCheck.ENABLE);
        List<RoomCareMenuDTO> roomCareMenuDTOList =
                roomCareMenuEntityList.stream().map(roomCareMenuEntity ->
                        modelMapper.map(roomCareMenuEntity, RoomCareMenuDTO.class).setHotelDTO(
                                modelMapper.map(roomCareMenuEntity.getHotelEntity(), HotelDTO.class))
                ).toList(); // 이미지 추가 필요
        return roomCareMenuDTOList;
    }

    public List<RoomCareMenuDTO> list(String email){
        CheckInDTO checkInDTO = checkInService.findCheckInByEmail(email);
        Long hotelId = checkInDTO.getRoomDTO().getHotelDTO().getId();
        List<RoomCareMenuEntity> roomCareMenuEntityList = roomCareMenuRepository.findByHotelEntity_IdAndAbleCheck(hotelId, AbleCheck.ENABLE);
        List<RoomCareMenuDTO> roomCareMenuDTOList =
                roomCareMenuEntityList.stream().map(roomCareMenuEntity ->
                        modelMapper.map(roomCareMenuEntity, RoomCareMenuDTO.class).setImgUrl(imageRepository.findByRoomCareMenuEntity_Id(roomCareMenuEntity.getId()).get().getFullUrl()).setHotelDTO(
                                modelMapper.map(roomCareMenuEntity.getHotelEntity(), HotelDTO.class).setImgUrl(imageRepository.findByHotelEntity_Id(roomCareMenuEntity.getHotelEntity().getId()).get().getFullUrl())
                        )).toList(); // 이미지 추가 필요
        return roomCareMenuDTOList;
    }

}
