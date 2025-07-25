/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Constant.Status;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.Entity.CenterEntity;
import com.onetouch.delinight.Entity.HotelEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import com.onetouch.delinight.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class MembersServiceImpl implements MembersService{
    private final StoreRepository storeRepository;
    private final MembersRepository membersRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final StoreService storeService;
    private final HotelService hotelService;
    private final CenterRepository centerRepository;
    private final HotelRepository hotelRepository;
    private final CenterService centerService;
    private final OrdersRepository ordersRepository;




    @Override
    public MembersDTO findByEmail(String email) {

        Optional<MembersEntity> membersEntity = membersRepository.findByEmail2(email);

        if(membersEntity.isPresent()){
            MembersDTO membersDTO = modelMapper.map(membersEntity.get(), MembersDTO.class);
            return membersDTO;
        }
        else {
            new MembersDTO();
            MembersDTO membersDTO = MembersDTO.builder().email("ghost").build();
            return membersDTO;
        }

    }

    @Override
    public boolean assignCheck(String email, int sep) {

        boolean result = false;
        if(sep == 1){
            result = centerRepository.existsByMembersEntity_Email(email);
        }
        else if(sep ==2){
            result = hotelRepository.existsByMembersEntity_Email(email);
        }
        else {
            result = storeRepository.existsByMembersEntity_Email(email);
        }

        return result;
    }

    @Override
    public void makeSysA() {
        MembersEntity membersEntity = new MembersEntity();
        membersEntity.setPhone("010-5629-1665");
        membersEntity.setRole(Role.SYSTEMADMIN);
        membersEntity.setStatus(Status.VALID);
        membersEntity.setEmail("sys@test.com");
        membersEntity.setName("시스템 관리자");
        membersEntity.setPassword(passwordEncoder.encode("akqjqtk12!"));
        membersRepository.save(membersEntity);
    }

    @Override
    public List<MembersDTO> findPendingMembersListByCenterMembers(MembersDTO membersDTO) {

        List<MembersEntity> result = membersRepository.findByStatusIs(Status.WAIT);
        List<MembersDTO> resultDTOS = result.stream().map(resultOne->{

            if(resultOne.getCenterEntity()==centerRepository.findById(centerService.findCenter(membersDTO.getEmail())).get()){
                MembersDTO membersDTO1 = modelMapper.map(resultOne, MembersDTO.class);
                return membersDTO1;
            }
            return null;
        }).toList();
        return resultDTOS;
    }

    @Override
    public boolean checkOperation(MembersDTO membersDTO) {
        LocalDate yesterDay = LocalDate.now().minusDays(1);
        if(membersDTO.getRole().equals(Role.SUPERADMIN)){
            return ordersRepository.existsByStoreEntity_HotelEntity_BranchEntity_CenterEntity_MembersEntity_IdAndAndPendingTimeAfter(membersDTO.getId(), yesterDay.atTime(0,0));
        }
        else if(membersDTO.getRole().equals(Role.ADMIN)){
            return ordersRepository.existsByStoreEntity_HotelEntity_MembersEntity_IdAndAndPendingTimeAfter(membersDTO.getId(), yesterDay.atTime(0,0));

        }
        else {
            return ordersRepository.existsByStoreEntity_MembersEntity_IdAndAndPendingTimeAfter(membersDTO.getId(), yesterDay.atTime(0,0));

        }
    }

    @Override
    public List<MembersDTO> findMembersListByCenterEmail(String email) {
//        Long id = centerService.findCenter(email);
//        List<MembersEntity> resultEntities = membersRepository.findByCenterEntity_Id(id);
//        List<MembersDTO> resultDTOS = resultEntities.stream()
//                .map(resultEntity -> modelMapper.map(resultEntity, MembersDTO.class))
//                .filter(dto -> dto.getHotelId() == null) // ✅ hotelId가 null인 멤버만 필터링
//                .toList();
//        log.info(resultDTOS);
//        log.info(resultDTOS);
//        return resultDTOS;
        Long id =
            centerService.findCenter(email);
        List<MembersEntity> membersEntityList =
            membersRepository.selectAdListByHotelIdIsNull(id);

        List<MembersDTO> membersDTOList = membersEntityList.stream().map(entity -> modelMapper.map(entity, MembersDTO.class)).toList();

        return membersDTOList;
    }

    @Override
    public List<MembersDTO> findMembersListByHotelEmail(String email) {
        Long id = hotelService.findHotelByEmail(email);
        List<MembersEntity> resultEntities = membersRepository.selectHotelIdandstoreAd(id);
        List<MembersDTO> resultDTOS = resultEntities.stream().map(resultEntity ->
                modelMapper.map(resultEntity, MembersDTO.class)).toList();
        return resultDTOS;
    }

    @Override
    public Long create(MembersDTO membersDTO) {

        MembersEntity membersEntity =
                modelMapper.map(membersDTO, MembersEntity.class);
        String password = passwordEncoder.encode(membersDTO.getPassword());
        membersEntity.setRole(membersDTO.getRole());
        membersEntity.setStatus(Status.WAIT);
        membersEntity.setPassword(password);

        if(membersDTO.getRole().equals(Role.SUPERADMIN)){

        }
        if(membersDTO.getRole().equals(Role.ADMIN)){
            Optional<CenterEntity> optionalCenterEntity = centerRepository.findById(membersDTO.getParentId());
            optionalCenterEntity.ifPresent(membersEntity::setCenterEntity);
        }
        if(membersDTO.getRole().equals(Role.STOREADMIN)){
            Optional<HotelEntity> optionalHotelEntity = hotelRepository.findById(membersDTO.getParentId());
            optionalHotelEntity.ifPresent(membersEntity::setHotelEntity);
        }
        MembersEntity members = membersRepository.save(membersEntity);
        return members.getId();


    }

    @Override
    public void update(MembersDTO membersDTO, String newPhone, String newPassword) {

        MembersEntity membersEntity =
                modelMapper.map(membersDTO, MembersEntity.class);
        MembersEntity members =
                membersRepository.findById(membersEntity.getId()).orElseThrow(EntityNotFoundException::new);

        if (newPhone != null && !newPhone.trim().isEmpty()){
            members.setPhone(newPhone);
        }

        if (newPassword != null && !newPassword.trim().isEmpty()){
            String encodePassword = passwordEncoder.encode(newPassword);
            members.setPassword(encodePassword);
        }

        membersRepository.save(members);
    }

    @Override
    public List<MembersDTO> findAll() {
        List<MembersEntity> membersEntityList = membersRepository.findAll();

        List<MembersDTO> membersDTOList =
                membersEntityList.stream().toList().stream().map(
                        membersEntity -> modelMapper.map(membersEntity, MembersDTO.class)
                ).collect(Collectors.toList());

        return membersDTOList;
    }

    //@Override
    //public List<MembersDTO> findSuper() {
    //    List<MembersEntity> membersEntityList = membersRepository.selectSuperAd();
    //
    //    List<MembersDTO> membersDTOList =
    //    membersEntityList.stream().toList().stream().map(
    //            membersEntity -> modelMapper.map(membersEntity, MembersDTO.class)
    //    ).collect(Collectors.toList());
    //
    //    return membersDTOList;
    //}


    @Override
    public Page<MembersEntity> findAccount(Status status, int page, String email, String sep) {

        if(sep.equals("hotel")){

            Long id = centerService.findCenter(email);
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
            return membersRepository.selectHotelAdByStatus(status,pageable, id);

        }
        else {
            Long id = hotelService.findHotelByEmail(email);
            List<Sort.Order> sorts = new ArrayList<>();
            sorts.add(Sort.Order.desc("id"));
            Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
            return membersRepository.selectStoreAdByStatus(status,pageable, id);

        }


    }


    //
    //@Override
    //public Page<MembersEntity> findStoreAd(Status status, int page) {
    //
    //
    //    List<Sort.Order> sorts = new ArrayList<>();
    //    sorts.add(Sort.Order.desc("id"));
    //    Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
    //    return this.membersRepository.selectStoreAd(pageable);
    //
    //}

    @Override
    public Page<MembersEntity> getList(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.membersRepository.selectSuperAd(pageable);
    }

    @Override
    public Page<MembersEntity> getListHotel(int page, String email) {
        Long id = centerService.findCenter(email);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        Page<MembersEntity> membersEntities = membersRepository.selectHotelAd(pageable,id);
        return membersEntities;
    }

    @Override
    public Page<MembersEntity> getListStore(int page, String email) {
        Long id = hotelService.findHotelByEmail(email);
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.membersRepository.selectStoreAd(pageable, id);
    }

    @Override
    public Page<MembersEntity> getListBystatus(Status status, int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
        return this.membersRepository.selectSuperAdByStatus(status,pageable);
    }

    @Override
    public Integer countOfRequestAccount(String email) {
        Long centerId = centerService.findCenter(email);
        Integer result = membersRepository.countByCenterEntity_IdAndStatus(centerId, Status.WAIT);
        return result;
    }

    @Override
    public MembersDTO approve(Long id) {

        MembersEntity membersEntity =
                membersRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        membersEntity.setStatus(Status.VALID);

        MembersDTO membersDTO =
                modelMapper.map(membersEntity, MembersDTO.class);
        return membersDTO;
    }

    @Override
    public MembersDTO Disapprove(Long id) {

        MembersEntity membersEntity =
                membersRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        membersEntity.setStatus(Status.NOTVALID);

        MembersDTO membersDTO =
                modelMapper.map(membersEntity, MembersDTO.class);
        return membersDTO;
    }

    @Override
    public Role findOnlyRoleByEmail(String email) {
        Role role = membersRepository.findByEmail(email).getRole();
        return role;
    }

    @Override
    public Map<Role, Long> findRoleByEmail(String email) {

        MembersEntity membersEntity = membersRepository.findByEmail(email);
        if(membersEntity.getRole().equals(Role.STOREADMIN)){
            Long id = storeService.findStoreByEmail(email);
            return Map.of(Role.STOREADMIN, id);
        }
        else if(membersEntity.getRole().equals(Role.ADMIN)){
            Long id = hotelService.findHotelByEmail(email);
            return Map.of(Role.ADMIN, id);
        }

        return null;
    }


}