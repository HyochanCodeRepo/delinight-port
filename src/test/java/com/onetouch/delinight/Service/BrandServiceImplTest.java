package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.BranchDTO;
import com.onetouch.delinight.DTO.BrandDTO;
import com.onetouch.delinight.DTO.HotelDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.BranchEntity;
import com.onetouch.delinight.Entity.BrandEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.BranchRepository;
import com.onetouch.delinight.Repository.BrandRepository;
import com.onetouch.delinight.Repository.HotelRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class BrandServiceImplTest {

    @Autowired
    BrandRepository brandRepository;
    @Autowired
    BrandService brandService;
    @Autowired
    HotelRepository hotelRepository;
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    MembersRepository membersRepository;

    @Autowired
    BranchRepository branchRepository;



    @Test
    public void createTest() {
        BrandDTO brandDTO = new BrandDTO();
        brandDTO.setName("신라");
        brandDTO.setContent("신라");
        brandService.create(brandDTO);

    }

    @Test
    public void createTest2() {
        HotelDTO hotelDTO = new HotelDTO();
        BrandEntity brandEntity =
            brandRepository.findById(9L).get();

        BrandDTO brandDTO =
            modelMapper.map(brandEntity, BrandDTO.class);

        MembersEntity membersEntity =
            membersRepository.findById(65L).get();
        MembersDTO membersDTO =
            modelMapper.map(membersEntity, MembersDTO.class);
        BranchEntity branchEntity =
            branchRepository.findById(1L).get();
        BranchDTO branchDTO =
            modelMapper.map(branchEntity, BranchDTO.class);

        
        hotelDTO.setName("신라호텔bb ");
        hotelDTO.setContent("여수bb");
        hotelDTO.setMembersDTO(membersDTO);
//        hotelDTO.setBrandDTO(brandDTO);
        hotelDTO.setBranchDTO(branchDTO);

        log.info(branchDTO);
        log.info(brandDTO);
        log.info(membersDTO);

        HotelEntity hotelEntity =
            modelMapper.map(hotelDTO, HotelEntity.class);
        hotelRepository.save(hotelEntity);

    }


}