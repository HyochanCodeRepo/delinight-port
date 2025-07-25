/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.MenuDTO;


import com.onetouch.delinight.Entity.ImageEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.DTO.StoreDTO;
import com.onetouch.delinight.Entity.MenuEntity;
import com.onetouch.delinight.Entity.StoreEntity;
import com.onetouch.delinight.Repository.ImageRepository;
import com.onetouch.delinight.Repository.MenuRepository;
import com.onetouch.delinight.Repository.StoreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service
@Transactional
@RequiredArgsConstructor
@Log4j2
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final ModelMapper modelMapper;
    private final ImageRepository imageRepository;
    private final StoreRepository storeRepository;

    @Override
    public void register(MenuDTO menuDTO, String email) {
        MenuEntity menuEntity = modelMapper.map(menuDTO, MenuEntity.class);
        StoreEntity storeEntity = storeRepository.findByMembersEntity_Email(email);

        menuEntity.setStoreEntity(storeEntity);
        menuEntity = menuRepository.save(menuEntity);


        Long imgNum = menuDTO.getImgNum(); //imgNum이 null인지 확인하였으나 null값이라 오류
        if (imgNum == null) {
            throw new IllegalArgumentException("이미지 ID(imgNum)가 null입니다.");
        }
        ImageEntity imageEntity = imageRepository.findById(imgNum)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 이미지가 존재하지 않습니다: " + imgNum));

        imageEntity.setMenuEntity(menuEntity);
        imageRepository.save(imageEntity);
    }

    @Override
    public MenuDTO read(Long id) {
        Optional<MenuEntity> optionalMenuEntity =
                menuRepository.findById(id);
        MenuEntity menuEntity = optionalMenuEntity.get();
        MenuDTO menuDTO = modelMapper.map(menuEntity, MenuDTO.class);
        Optional<ImageEntity> imageEntity = imageRepository.findByMenuEntity_Id(id);
        if (imageEntity.isPresent()) {
            String imgUrl = imageEntity.get().getFullUrl();
            menuDTO.setImgUrl(imgUrl);
        }
        return menuDTO;
    }

    @Override
    public List<MenuDTO> menuList(Long storeId) {

        List<MenuEntity> menuEntityList = menuRepository.findByStoreEntity_Id(storeId);
        List<MenuDTO> menuDTOList = menuEntityList.stream().map(data->modelMapper.map(data, MenuDTO.class)
                .setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class)).setImgUrl(imageRepository.findByMenuEntity_Id(data.getId()).get().getFullUrl())
        ).toList();
        return menuDTOList;
    }

    @Override
    public Page<MenuDTO> menuList(Pageable pageable, String email) {
        StoreEntity storeEntity = storeRepository.findByMembersEntity_Email(email);
        Page<MenuEntity> pageList = menuRepository.findByStoreEntity_Id(storeEntity.getId(), pageable);
        return pageList.map(data -> modelMapper.map(data, MenuDTO.class));
    }


    @Override
    public MenuDTO update(MenuDTO menuDTO) {
        Optional<MenuEntity> optionalMenuEntity = menuRepository.findById(menuDTO.getId());
        MenuEntity menuEntity = optionalMenuEntity.get();
        menuEntity.setName(menuDTO.getName());
        menuEntity.setContent(menuDTO.getContent());
        menuEntity.setPrice(menuDTO.getPrice());
        menuEntity.setMenuStatus(menuDTO.getMenuStatus());
        menuEntity.setMenu(menuDTO.getMenu());
        if (menuDTO.getImgNum() != null) {
            ImageEntity imageEntity = imageRepository.findById(menuDTO.getImgNum()).get();
            imageRepository.deleteByMenuEntity_Id(menuEntity.getId());
            imageEntity.setMenuEntity(menuEntity);
            imageRepository.save(imageEntity);
        }
        menuRepository.save(menuEntity);
        return null;
    }

    @Override
    public void delete(Long id) {
        menuRepository.deleteById(id);
    }


    @Override
    public List<MenuDTO> menuListByHotel(Long hotelNum) {
        List<MenuEntity> menuEntityList = menuRepository.findByStoreEntity_HotelEntity_Id(hotelNum);
        List<MenuDTO> menuDTOList = menuEntityList.stream().map(data -> modelMapper.map(data, MenuDTO.class)
                .setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class))).toList();
        return menuDTOList;
    }
}
