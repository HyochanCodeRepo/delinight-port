/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.*;
import com.onetouch.delinight.Repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
@Log4j2

public class InquireServiceImpl implements InquireService {


    private final InquireRepository inquireRepository;
    private final ModelMapper modelMapper;
    private final CheckInRepository checkInRepository;
    private final HotelRepository hotelRepository;
    private final UsersRepository usersRepository;
    private final SseService sseService;
    private final CheckOutLogRepository checkOutLogRepository;



    @Override
    public void checkInToCheckOut(Long checkInId, Long checkOutId) {
        List<InquireEntity> inquireEntities = inquireRepository.findByCheckInEntity_Id(checkInId);
        if (inquireEntities == null) {
            return;
        }
        inquireEntities.forEach(inquireEntity -> {
            inquireEntity.setCheckInEntity(null);

            Optional<CheckOutLogEntity> optionalCheckOutLogEntity = checkOutLogRepository.findById(checkOutId);
            CheckOutLogEntity checkOutLogEntity = optionalCheckOutLogEntity.get();
            inquireEntity.setCheckOutLogEntity(checkOutLogEntity);
            inquireRepository.save(inquireEntity);
        });

    }
    @Override
    public InquireDTO register(InquireDTO inquireDTO, String email) {
        InquireEntity inquireEntity = modelMapper.map(inquireDTO, InquireEntity.class);
        //체크인 id을 찾아와서

        CheckInEntity checkInEntity;
        if(checkInRepository.findByUsersEntity_Email(email)!=null){
         checkInEntity = checkInRepository.findByUsersEntity_Email(email);
        }
        else {
            checkInEntity = checkInRepository.findByGuestEntity_Phone(email);
        }

        //체크인에서 호텔 정보 추출
        HotelEntity hotelEntity = checkInEntity.getRoomEntity().getHotelEntity();

        //문의엔티티에 체크인, 호텔 정보 연결
        inquireEntity.setCheckInEntity(checkInEntity);
        inquireEntity.setHotelEntity(hotelEntity);

        //문의글 작성시에는 초기에는 null로 설정
        inquireEntity.setResponseTime(null);

        //모든 정보를 채운 inquireEntity를 DB에 저장
        inquireEntity = inquireRepository.save(inquireEntity);
        inquireDTO = modelMapper.map(inquireEntity, InquireDTO.class);

        sseService.sendToHAdmin("H"+inquireEntity.getCheckInEntity().getRoomEntity().getHotelEntity().getId(),"new-inquire",inquireEntity.getCheckInEntity().getRoomEntity().getName()+"방으로 부터 새로운 문의가 들어왔습니다.");

        return inquireDTO;

    }

    @Override
    public List<InquireDTO> findUnprocessedInquire(MembersDTO membersDTO) {

        List<InquireEntity> inquireEntities = inquireRepository.findByHotelEntity_MembersEntity_EmailAndResponseTimeIsNull(membersDTO.getEmail());
        List<InquireDTO> resultDTOList = inquireEntities.stream().map(resultDTO->modelMapper.map(resultDTO, InquireDTO.class)).toList();
        return resultDTOList;

    }

    @Override
    public Page<InquireDTO> inquireList(Pageable pageable,String email) {

        log.info("히히"+email);
        Page<InquireEntity> pageList;

        if(checkInRepository.findByUsersEntity_Email(email)!=null){


            UsersEntity usersEntity = checkInRepository.findByUsersEntity_Email(email).getUsersEntity();
            log.info("히히 엔티티" + usersEntity);
            //이메일로 체크인 정보를 찾는다

            //최신순으로 정렬
            Pageable sortedPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "regTime"));


            pageList = inquireRepository.findByCheckInEntity_UsersEntity_Id(usersEntity.getId(),sortedPageable);
            //방금 찾은 체크인 기록의 id로 문의글을 찾고, 한페이지씩 잘라서 가져와

        }
            else {

            GuestEntity guestEntity = checkInRepository.findByGuestEntity_Phone(email).getGuestEntity();

            Pageable sortedPageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "regTime"));


            pageList = inquireRepository.findByCheckInEntity_GuestEntity_Id(guestEntity.getId(),sortedPageable);
            //방금 찾은 체크인 기록의 id로 문의글을 찾고, 한페이지씩 잘라서 가져와


        }


        return pageList.map(data -> modelMapper.map(data, InquireDTO.class));

    }
    @Override
    public Page<InquireDTO> inquireList(Pageable pageable,Long hotelId) {

        //최신순으로 정렬
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.DESC, "regTime"));

        Page<InquireEntity> pageList = inquireRepository.findByHotelEntity_Id(hotelId,pageable);
        //방금 찾은 체크인 기록의 id로 문의글을 찾고, 한페이지씩 잘라서 가져와

        return pageList.map(data -> modelMapper.map(data, InquireDTO.class));

    }

    @Override
    public InquireDTO read(Long id) {
        Optional<InquireEntity>optionalInquireEntity = inquireRepository.findById(id);
        InquireEntity inquireEntity = optionalInquireEntity.get();
        InquireDTO inquireDTO;
        if(inquireEntity.getCheckInEntity() !=null){
            inquireDTO = modelMapper.map(inquireEntity, InquireDTO.class).setCheckInDTO(modelMapper.map(inquireEntity.getCheckInEntity(), CheckInDTO.class).setRoomDTO(modelMapper.map(inquireEntity.getCheckInEntity().getRoomEntity(), RoomDTO.class)));
        }
        else
        {
            inquireDTO = modelMapper.map(inquireEntity, InquireDTO.class).setCheckOutLogDTO(modelMapper.map(inquireEntity.getCheckOutLogEntity(), CheckOutLogDTO.class).setRoomDTO(modelMapper.map(inquireEntity.getCheckOutLogEntity().getRoomEntity(), RoomDTO.class)));

        }
                return inquireDTO;
    }

    @Override
    public InquireDTO update(InquireDTO inquireDTO) {
        Optional<InquireEntity> optionalInquireEntity = inquireRepository.findById(inquireDTO.getId());
        InquireEntity inquireEntity = optionalInquireEntity.get();
        inquireEntity.setTitle(inquireDTO.getTitle());
        inquireEntity.setContent(inquireDTO.getContent());
        inquireEntity.setInquireType(inquireDTO.getInquireType());
        inquireRepository.save(inquireEntity);
        return null;
    }

    @Override
    public void delete(Long id) {
        inquireRepository.deleteById(id);

    }






}
