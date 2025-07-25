package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.CheckInStatus;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.*;
import com.onetouch.delinight.Repository.*;
import com.onetouch.delinight.Util.EmailService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.annotations.Check;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class CheckInServiceImpl implements CheckInService{

    private final CheckInRepository checkInRepository;
    private  final OrdersService ordersService;
    private final UsersRepository usersRepository;
    private final ModelMapper modelMapper;
    private final GuestRepository guestRepository;
    private final CheckOutLogRepository checkOutLogRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;
    private final InquireService inquireService;
    private final RoomCareService roomCareService;
    private final StoreRepository storeRepository;
    private final EmailService emailService;




    @Override
    public List<CheckInDTO> listCheckInWithPrice(String email) {
        List<CheckInEntity> checkInEntities = checkInRepository.findByRoomEntity_HotelEntity_MembersEntity_Email(email);
        log.debug("🔍 [STEP 1] 전체 체크인 데이터 조회: {}", checkInEntities.size());

        return checkInEntities.stream().map(checkInEntity -> {
            CheckInDTO checkInDTO = modelMapper.map(checkInEntity, CheckInDTO.class);

            log.debug("🔍 [STEP 2] 현재 체크인 ID: {}", checkInEntity.getId());

            // ✅ RoomDTO 항상 설정
            checkInDTO.setRoomDTO(modelMapper.map(checkInEntity.getRoomEntity(), RoomDTO.class));

            // ✅ CHECKIN 상태인 경우 GuestDTO 또는 UsersDTO 설정
            if (checkInEntity.getCheckInStatus().equals(CheckInStatus.CHECKIN)) {
                if (checkInEntity.getGuestEntity() != null) {
                    checkInDTO.setGuestDTO(modelMapper.map(checkInEntity.getGuestEntity(), GuestDTO.class))
                            .setPassword(checkInDTO.getPassword())
                            .setCertId(checkInDTO.getCertId())
                            .setEmail(checkInDTO.getEmail())
                            .setPhone(checkInDTO.getPhone());

                    log.debug("🔍 [STEP 3] GuestDTO 설정 완료: {}", checkInDTO.getGuestDTO());
                } else if (checkInEntity.getUsersEntity() != null) {
                    checkInDTO.setUsersDTO(modelMapper.map(checkInEntity.getUsersEntity(), UsersDTO.class))
                            .setEmail(checkInEntity.getUsersEntity().getEmail())
                            .setPhone(checkInEntity.getUsersEntity().getPhone());

                    log.debug("🔍 [STEP 4] UsersDTO 설정 완료: {}", checkInDTO.getUsersDTO());
                }
            }

            // ✅ 결제 금액 조회 및 설정
            Integer totalPrice = checkInRepository.selectPriceByCheckinId(checkInEntity.getId());
            checkInDTO.setPrice(totalPrice != null ? (long) totalPrice : 0L);

            log.debug("🔍 [STEP 5] 최종 DTO에 반영된 price: {}", checkInDTO.getPrice());

            return checkInDTO;
        }).collect(Collectors.toList());
    }


    @Override
    public void create(RoomEntity roomEntity) {

        CheckInEntity checkInEntity = CheckInEntity.builder().roomEntity(roomEntity).checkInStatus(CheckInStatus.VACANCY).build();
        checkInRepository.save(checkInEntity);


    }
//
//    @Override
//    public void create(CheckInDTO checkInDTO) {
//
//        CheckInEntity checkInEntity =
//                modelMapper.map(checkInDTO, CheckInEntity.class)
//                        .setUsersEntity(modelMapper.map(checkInDTO.getUsersDTO(), UsersEntity.class));
//
//        RoomEntity roomEntity =
//                roomRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
//        UsersEntity usersEntity =
//                usersRepository.findById(1L).orElseThrow(EntityNotFoundException::new);
//
//        checkInEntity.builder()
//                .checkinDate(checkInEntity.getCheckinDate())
//                .checkoutDate(checkInEntity.getCheckoutDate())
//                .checkInStatus(checkInEntity.getCheckInStatus())
//                .price(checkInEntity.getPrice())
//                .guestEntity(checkInEntity.getGuestEntity())
//                .usersEntity(usersEntity)
//                .roomEntity(roomEntity)
//                .build();
//
//        checkInRepository.save(checkInEntity);
//
//    }


    @Override
    public List<CheckInDTO> list2() {
        List<CheckInEntity> checkInEntityList =
                checkInRepository.findAll();
        List<CheckInDTO> checkInDTOList =
                checkInEntityList.stream().map(checkInEntity -> {
                    CheckInDTO checkInDTO = modelMapper.map(checkInEntity, CheckInDTO.class);

                    if (checkInEntity.getCheckInStatus().equals(CheckInStatus.CHECKIN)) {

                        // getGuestEntity가 null이 아니면 setGuestDTO 호출
                        if (checkInEntity.getGuestEntity() != null) {
                            checkInDTO.setGuestDTO(modelMapper.map(checkInEntity.getGuestEntity(), GuestDTO.class))
                                    .setPassword(checkInDTO.getPassword())
                                    .setCertId(checkInEntity.getGuestEntity().getCertId())
                                    .setEmail(checkInEntity.getGuestEntity().getEmail())
                                    .setPhone(checkInEntity.getPhone());

                        }
                        else {
                            checkInDTO.setUsersDTO(modelMapper.map(checkInEntity.getUsersEntity(), UsersDTO.class))
                                    .setEmail(checkInEntity.getUsersEntity().getEmail())
                                    .setPhone(checkInEntity.getUsersEntity().getPhone());
                        }
                    }
                    // setRoomDTO는 항상 호출
                    checkInDTO.setRoomDTO(modelMapper.map(checkInEntity.getRoomEntity(), RoomDTO.class));
                    return checkInDTO;
                }).collect(Collectors.toList());
        return checkInDTOList;
    }

    @Override
    public List<CheckInDTO> list() {
        List<CheckInEntity> checkInEntityList =
                checkInRepository.findAll();




        List<CheckInDTO> checkInDTOList =
                checkInEntityList.stream().map(checkInEntity -> {
                    CheckInDTO checkInDTO = modelMapper.map(checkInEntity, CheckInDTO.class);

                    if (checkInEntity.getCheckInStatus().equals(CheckInStatus.CHECKIN)) {

                        // getGuestEntity가 null이 아니면 setGuestDTO 호출
                        if (checkInEntity.getGuestEntity() != null) {
                            checkInDTO.setGuestDTO(modelMapper.map(checkInEntity.getGuestEntity(), GuestDTO.class))
                                    .setPassword(checkInDTO.getPassword())
                                    .setCertId(checkInEntity.getGuestEntity().getCertId())
                                    .setEmail(checkInEntity.getGuestEntity().getEmail())
                                    .setPhone(checkInEntity.getPhone());

                        }
                        else {
                            checkInDTO.setUsersDTO(modelMapper.map(checkInEntity.getUsersEntity(), UsersDTO.class))
                                    .setEmail(checkInEntity.getUsersEntity().getEmail())
                                    .setPhone(checkInEntity.getUsersEntity().getPhone());
                        }
                    }
                    // setRoomDTO는 항상 호출
                    checkInDTO.setRoomDTO(modelMapper.map(checkInEntity.getRoomEntity(), RoomDTO.class));
                    return checkInDTO;
                }).collect(Collectors.toList());

        return checkInDTOList;
    }


    @Override
    public void checkin(CheckInDTO checkInDTO) {
        CheckInEntity checkInEntity =
                modelMapper.map(checkInDTO, CheckInEntity.class);


        if (checkInDTO.getUserId() == null) {
            int certNum = (int) (Math.random() * 8999) + 1000;
            checkInDTO.setPassword(String.valueOf(certNum));
        }

        CheckInEntity check =
                checkInRepository.findById(checkInEntity.getId()).orElseThrow(EntityNotFoundException::new);

        check.setPassword(checkInDTO.getPassword());
        check.setCheckinDate(checkInEntity.getCheckinDate());
        check.setCheckoutDate(checkInEntity.getCheckoutDate());


        check.setCheckInStatus(CheckInStatus.CHECKIN);

        if (checkInDTO.getUserId() == null) {
            GuestEntity guestEntity = new GuestEntity();
            guestEntity.setPhone(checkInDTO.getPhone());
            guestEntity.setEmail(checkInDTO.getEmail());
            guestEntity.setCertId(check.getPassword());
            guestEntity.setPassword(passwordEncoder.encode(check.getPassword()));

            check.setGuestEntity(guestEntity);

            String reservationNum = check.getRoomEntity().getId() + "/" + check.getCheckinDate() + "/";
            guestEntity.setReservationNum(reservationNum);
            checkInRepository.save(check);
            guestRepository.save(guestEntity);
            cartService.makeCart(2, guestEntity.getId());


        } else {
            UsersEntity usersEntity =
                    usersRepository.findById(checkInDTO.getUserId()).orElseThrow(EntityNotFoundException::new);
            check.setUsersEntity(usersEntity);

            List<StoreEntity> storeEntityList = storeRepository.findByHotelEntity_Id(check.getRoomEntity().getHotelEntity().getId());
            List<String> storeNames = new ArrayList<>();
            for (StoreEntity store : storeEntityList) {
                storeNames.add(store.getName());
            }
            String hotelName = check.getRoomEntity().getHotelEntity().getName();

            emailService.sendCheckInEmail(check, usersEntity.getEmail(), storeNames, hotelName, usersEntity.getName());

            //회원이면 핸드폰 번호 뒤 4자리로 비밀번호 설정
            String phone = usersEntity.getPhone();
            if (phone != null && phone.length() >= 4) {
                String lastFour = phone.substring(phone.length() - 4);
                check.setPassword(lastFour);
            }


            cartService.makeCart(1, usersEntity.getId());
            checkInRepository.save(check);

        }
    }


        @Override
    public void checkout(Long id) {
        CheckInEntity checkInEntity =
                checkInRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        if(checkInEntity.getGuestEntity()!=null){
            Long cartId = cartService.cartCheck(checkInEntity.getGuestEntity().getPhone());
            cartService.clear(cartId);
            cartService.deleteCart(2,checkInEntity.getGuestEntity().getId());
        }
        else {
            Long cartId = cartService.cartCheck(checkInEntity.getUsersEntity().getEmail());
            cartService.clear(cartId);
            cartService.deleteCart(1,checkInEntity.getUsersEntity().getId());
        }

        CheckOutLogEntity checkOutLogEntity = new CheckOutLogEntity();


        checkOutLogEntity.setRoomEntity(checkInEntity.getRoomEntity());
        checkOutLogEntity.setCheckinDate(checkInEntity.getCheckinDate());
        checkOutLogEntity.setCheckoutDate(checkInEntity.getCheckoutDate());
        checkOutLogEntity.setPhone(checkInEntity.getPhone());
        checkOutLogEntity.setPrice(checkInEntity.getPrice());
        checkOutLogEntity.setUsersEntity(checkInEntity.getUsersEntity());
        checkOutLogEntity.setGuestEntity(checkInEntity.getGuestEntity());

        checkOutLogRepository.save(checkOutLogEntity);

        checkInEntity.setCheckInStatus(CheckInStatus.VACANCY);
        checkInEntity.setCheckinDate(null);
        checkInEntity.setCheckoutDate(null);
        checkInEntity.setPhone(null);
        checkInEntity.setPrice(0);
        checkInEntity.setGuestEntity(null);
        checkInEntity.setUsersEntity(null);
        checkInEntity.setPassword(null);

        checkInRepository.save(checkInEntity);
        ordersService.checkInToCheckOut(checkInEntity.getId(), checkOutLogEntity.getId());
        roomCareService.checkInToCheckOut(checkInEntity.getId(), checkOutLogEntity.getId());
        inquireService.checkInToCheckOut(checkInEntity.getId(), checkOutLogEntity.getId());



    }

    @Override
    public UsersDTO checkEmail(String email) {
        UsersEntity usersEntity =
                usersRepository.findByEmail(email);

        if(usersEntity!=null) {
            UsersDTO usersDTO =
                    modelMapper.map(usersEntity, UsersDTO.class);


            return usersDTO;
        }
        else {
            return null;
        }

    }

    @Override
    public HotelEntity findHotelInByEmail(Long CheckInId) {

        HotelEntity hotelEntity = checkInRepository.findById(CheckInId).get().getRoomEntity().getHotelEntity();
        return hotelEntity;
    }

    @Override
    public CheckInDTO findCheckInByEmail(String email) {
        CheckInEntity checkInEntity = checkInRepository.findByUsersEntity_Email(email);
        CheckInDTO checkInDTO;
        if(checkInEntity==null){
            CheckInEntity checkInEntity1 = checkInRepository.findByGuestEntity_Phone(email);
            checkInDTO = modelMapper.map(checkInEntity1, CheckInDTO.class).setRoomDTO(modelMapper.map(checkInEntity1.getRoomEntity(), RoomDTO.class).setHotelDTO(modelMapper.map(checkInEntity1.getRoomEntity().getHotelEntity(),HotelDTO.class)));
        }
        else {
            checkInDTO = modelMapper.map(checkInEntity, CheckInDTO.class).setRoomDTO(modelMapper.map(checkInEntity.getRoomEntity(), RoomDTO.class).setHotelDTO(modelMapper.map(checkInEntity.getRoomEntity().getHotelEntity(),HotelDTO.class)));
        }
        return checkInDTO;
    }

    @Override
    public List<CheckInDTO> getListCheckinByStatus(CheckInStatus checkInStatus, String email) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));

        List<CheckInEntity> checkInEntityList = checkInRepository.findByCheckInStatusAndRoomEntity_HotelEntity_MembersEntity_Email(checkInStatus, email);

        List<CheckInDTO> checkInDTOList =
                checkInEntityList.stream().map(checkInEntity -> {
                    CheckInDTO checkInDTO = modelMapper.map(checkInEntity, CheckInDTO.class);

                    if (checkInEntity.getCheckInStatus().equals(CheckInStatus.CHECKIN)) {

                        // getGuestEntity가 null이 아니면 setGuestDTO 호출
                        if (checkInEntity.getGuestEntity() != null) {
                            checkInDTO.setGuestDTO(modelMapper.map(checkInEntity.getGuestEntity(), GuestDTO.class))
                                    .setPassword(checkInDTO.getPassword())
                                    .setCertId(checkInEntity.getGuestEntity().getCertId())
                                    .setEmail(checkInEntity.getGuestEntity().getEmail())
                                    .setPhone(checkInEntity.getPhone());

                        }
                        else {
                            checkInDTO.setUsersDTO(modelMapper.map(checkInEntity.getUsersEntity(), UsersDTO.class))
                                    .setEmail(checkInEntity.getUsersEntity().getEmail())
                                    .setPhone(checkInEntity.getUsersEntity().getPhone());
                        }
                    }
                    // setRoomDTO는 항상 호출
                    checkInDTO.setRoomDTO(modelMapper.map(checkInEntity.getRoomEntity(), RoomDTO.class));
                    return checkInDTO;
                }).collect(Collectors.toList());
        return checkInDTOList;
    }

    @Override
    public void del(Long id) {
        checkInRepository.deleteById(id);
    }
}
