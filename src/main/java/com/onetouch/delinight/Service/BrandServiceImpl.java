/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.BrandDTO;
import com.onetouch.delinight.Entity.BrandEntity;
import com.onetouch.delinight.Repository.BrandRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Id;
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
public class BrandServiceImpl implements BrandService{
    private final BrandRepository brandRepository;
    private final ModelMapper modelMapper;


    @Override
    public BrandDTO read(Long id) {

        BrandEntity brandEntity =
                brandRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        BrandDTO brandDTO =
            modelMapper.map(brandEntity, BrandDTO.class);
        return brandDTO;
    }

    @Override
    public void create(BrandDTO brandDTO) {

        BrandEntity brandEntity =
            modelMapper.map(brandDTO, BrandEntity.class);
        brandRepository.save(brandEntity);

    }

    @Override
    public List<BrandDTO> list() {
        List<BrandEntity> brandEntityList =
            brandRepository.findAll();

        List<BrandDTO> brandDTOList =
            brandEntityList.stream().toList().stream().map(
                brandEntity -> modelMapper.map(brandEntity, BrandDTO.class)
            ).collect(Collectors.toList());
        return brandDTOList;
    }



    @Override
    public void del(Long num) {
        log.info("service 단에 들어오는 num : "+num);
        brandRepository.deleteById(num);
    }
}
