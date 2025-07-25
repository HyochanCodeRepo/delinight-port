/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.BranchDTO;
import com.onetouch.delinight.DTO.CenterDTO;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.BranchEntity;
import com.onetouch.delinight.Entity.CenterEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.BranchRepository;
import com.onetouch.delinight.Repository.CenterRepository;
import com.onetouch.delinight.Repository.MembersRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class BranchServiceImpl implements BranchService{
    private final BranchRepository branchRepository;
    private final CenterRepository centerRepository;
    private final ModelMapper modelMapper;


    @Override
    public void create(BranchDTO branchDTO) {
        BranchEntity branchEntity =
            modelMapper.map(branchDTO, BranchEntity.class);

        CenterEntity centerEntity =
                centerRepository.findById(branchDTO.getCenterId()).orElseThrow(EntityNotFoundException::new);
        branchEntity.setCenterEntity(centerEntity);


        branchRepository.save(branchEntity);
    }

    @Override
    public void update(BranchDTO branchDTO) {


        BranchEntity branchEntity =
            modelMapper.map(branchDTO, BranchEntity.class);
        BranchEntity branch =
                branchRepository.findById(branchEntity.getId()).orElseThrow(EntityNotFoundException::new);
        branch.setName(branchEntity.getName());
        branch.setContent(branchEntity.getContent());

    }

    @Override
    public List<BranchDTO> list(String email) {
        List<BranchEntity> branchEntityList =
            branchRepository.findByCenterEntity_MembersEntity_Email(email);
        List<BranchDTO> branchDTOList =
                branchEntityList.stream().toList().stream().map(
                        branchEntity -> modelMapper.map(branchEntity, BranchDTO.class)
                ).collect(Collectors.toList());

        return branchDTOList;
    }

    @Override
    public void del(Long id) {
        branchRepository.deleteById(id);
    }
}
