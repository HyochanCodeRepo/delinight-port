/*********************************************************************
 * 클래스명 : MemberServiceImpl
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
import com.onetouch.delinight.Entity.CenterEntity;
import com.onetouch.delinight.Entity.CheckInEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.CenterRepository;
import com.onetouch.delinight.Repository.CheckInRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.format.CellTextFormatter;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CenterServiceImpl implements CenterService{
    private final CenterRepository centerRepository;
    private final ModelMapper modelMapper;
    private final MembersRepository membersRepository;


    @Override
    public void settingAdmin(Long membersId, Long centerId) {
        Optional<CenterEntity> centerEntityOptional = centerRepository.findById(centerId);
        MembersEntity members = membersRepository.findById(membersId).get();
        CenterEntity center = centerEntityOptional.get();
        center.setMembersEntity(members);
        centerRepository.save(center);
    }

    @Override
    public void create(CenterDTO centerDTO, String email) {


        CenterEntity centerEntity =
            modelMapper.map(centerDTO, CenterEntity.class);

        centerRepository.save(centerEntity);
    }

    @Override
    public void update(CenterDTO centerDTO) {
        CenterEntity centerEntity =
                modelMapper.map(centerDTO, CenterEntity.class);
        CenterEntity center =
                centerRepository.findById(centerEntity.getId()).orElseThrow(EntityNotFoundException::new);

        center.setName(centerEntity.getName());
        center.setContent(centerEntity.getContent());

        centerRepository.save(center);
    }

    @Override
    public List<CenterDTO> list() {
        List<CenterEntity> centerEntityList =
                centerRepository.findAll();
        List<CenterDTO> centerDTOList =
        centerEntityList.stream().toList().stream().map(
                centerEntity -> modelMapper.map(centerEntity, CenterDTO.class)
        ).collect(Collectors.toList());

        return centerDTOList;
    }

    @Override
    public MembersDTO checkEmail(String email) {
        MembersEntity membersEntity =
            membersRepository.findByEmail(email);
        MembersDTO membersDTO =
        modelMapper.map(membersEntity, MembersDTO.class);
        return membersDTO;
    }

    @Override
    public Long findCenter(String email) {
        CenterEntity centerEntity = centerRepository.findByMembersEntity_Email(email);

        return centerEntity.getId();

    }

    @Override
    public CenterDTO read(String email) {
        CenterEntity centerEntity =
            centerRepository.findByMembersEntity_Email(email);
        CenterDTO centerDTO =
            modelMapper.map(centerEntity, CenterDTO.class);
        return centerDTO;
    }
}
