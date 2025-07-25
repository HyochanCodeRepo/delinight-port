/*********************************************************************
 * 클래스명 : MemberServiceImpl
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.OrderType;
import com.onetouch.delinight.Constant.OrdersStatus;
import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.*;
import com.onetouch.delinight.Repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
@Transactional
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
    private final ImageRepository imageRepository;
    private final CheckInRepository checkInRepository;
    private final CheckOutLogRepository checkOutLogRepository;
    private final StoreRepository storeRepository;
    private final OrdersRepository ordersRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final SseService sseService;

    @Override
    public OrdersDTO readOne(Long ordersId) {

        Optional<OrdersEntity> optionalOrdersEntity = ordersRepository.findById(ordersId);
        if (optionalOrdersEntity.isPresent()) {
            OrdersEntity ordersEntity = optionalOrdersEntity.get();
            OrdersDTO ordersDTO = modelMapper.map(ordersEntity, OrdersDTO.class).setStoreDTO(modelMapper.map(ordersEntity.getStoreEntity(), StoreDTO.class)).setOrdersItemDTOList(ordersEntity.getOrdersItemEntities().stream().map(ordersItemEntity -> modelMapper.map(ordersItemEntity, OrdersItemDTO.class).setMenuDTO(modelMapper.map(ordersItemEntity.getMenuEntity(), MenuDTO.class).setImgUrl(imageRepository.findByMenuEntity_Id(ordersItemEntity.getMenuEntity().getId()).get().getFullUrl()))).toList());
            return ordersDTO;

        } else {
            return null;

        }
    }

    @Override
    public List<OrdersDTO> dashboard(String email) {
        List<OrdersEntity> processEntityList = ordersRepository.findByStoreEntity_MembersEntity_EmailAndOrdersStatusIs(email, OrdersStatus.PENDING);
        log.info("확인중 "+processEntityList);
        if(!processEntityList.isEmpty()) {
            List<OrdersDTO> processDTOList = processEntityList.stream().map(data -> {
                OrdersDTO ordersDTO = modelMapper.map(data, OrdersDTO.class);
                if(data.getCheckInEntity() != null) {
                    CheckInDTO checkInDTO = modelMapper.map(data.getCheckInEntity(), CheckInDTO.class);
                    checkInDTO.setRoomDTO(modelMapper.map(data.getCheckInEntity().getRoomEntity(), RoomDTO.class));
                    checkInDTO.getRoomDTO().setHotelDTO(modelMapper.map(data.getCheckInEntity().getRoomEntity().getHotelEntity(),HotelDTO.class));
                    ordersDTO.setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class));
                    ordersDTO.setOrdersItemDTOList(data.getOrdersItemEntities().stream().map(
                            ordersItemEntity -> modelMapper.map(ordersItemEntity, OrdersItemDTO.class)
                    ).toList());
                    ordersDTO.setCheckInDTO(checkInDTO);
                    return ordersDTO;
                }
                else {
                    return null;
                }
            }).toList();
            return processDTOList;
        }
        return null;
    }

    @Override
    public List<OrdersDTO> processList( String email) {
        List<OrdersEntity> processEntityList = ordersRepository.findByStoreEntity_MembersEntity_EmailAndOrdersStatusNotAndOrdersStatusIsNot(email, OrdersStatus.DELIVERED, OrdersStatus.PENDING);
        if(!processEntityList.isEmpty()) {
            List<OrdersDTO> processDTOList = processEntityList.stream().map(data -> {
                OrdersDTO ordersDTO = modelMapper.map(data, OrdersDTO.class);
                if(data.getCheckInEntity() != null) {
                    CheckInDTO checkInDTO = modelMapper.map(data.getCheckInEntity(), CheckInDTO.class);
                    checkInDTO.setRoomDTO(modelMapper.map(data.getCheckInEntity().getRoomEntity(), RoomDTO.class));
                    checkInDTO.getRoomDTO().setHotelDTO(modelMapper.map(data.getCheckInEntity().getRoomEntity().getHotelEntity(),HotelDTO.class));
                    ordersDTO.setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class));
                    ordersDTO.setOrdersItemDTOList(data.getOrdersItemEntities().stream().map(
                            ordersItemEntity -> modelMapper.map(ordersItemEntity, OrdersItemDTO.class).setMenuDTO(modelMapper.map(ordersItemEntity.getMenuEntity(), MenuDTO.class))
                    ).toList());
                    ordersDTO.setCheckInDTO(checkInDTO);
                    return ordersDTO;
                }
                else {
                    return null;
                }
            }).toList();
            return processDTOList;
        }
        return null;
    }

    @Override
    public List<OrdersDTO> completeList(String email) {
        List<OrdersEntity> completeList = ordersRepository.findByStoreEntity_MembersEntity_EmailAndOrdersStatusIs(email, OrdersStatus.DELIVERED);
        if(!completeList.isEmpty()) {
            List<OrdersDTO> completeDTOList = completeList.stream().map(data -> {
                OrdersDTO ordersDTO = modelMapper.map(data, OrdersDTO.class);
                if(data.getCheckInEntity() != null) {
                    CheckInDTO checkInDTO = modelMapper.map(data.getCheckInEntity(), CheckInDTO.class);
                    checkInDTO.setRoomDTO(modelMapper.map(data.getCheckInEntity().getRoomEntity(), RoomDTO.class));
                    checkInDTO.getRoomDTO().setHotelDTO(modelMapper.map(data.getCheckInEntity().getRoomEntity().getHotelEntity(),HotelDTO.class));
                    ordersDTO.setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class));
                    ordersDTO.setOrdersItemDTOList(data.getOrdersItemEntities().stream().map(
                            ordersItemEntity -> modelMapper.map(ordersItemEntity, OrdersItemDTO.class).setMenuDTO(modelMapper.map(ordersItemEntity.getMenuEntity(), MenuDTO.class))
                    ).toList());
                    ordersDTO.setCheckInDTO(checkInDTO);
                    return ordersDTO;
                }
                else {
                    return null;
                }
            }).toList();
            return completeDTOList;
        }
        return null;
    }

    @Override
    public void checkInToCheckOut(Long checkInId, Long checkOutId) {
        List<OrdersEntity> ordersEntities = ordersRepository.findByCheckInEntity_Id(checkInId);
        if (ordersEntities == null) {
            return;
        }
        ordersEntities.forEach(ordersEntity -> {
            ordersEntity.setCheckInEntity(null);

            Optional<CheckOutLogEntity> optionalCheckOutLogEntity = checkOutLogRepository.findById(checkOutId);
            CheckOutLogEntity checkOutLogEntity = optionalCheckOutLogEntity.get();
            ordersEntity.setCheckOutLogEntity(checkOutLogEntity);
            ordersRepository.save(ordersEntity);
        });

    }


    @Override
    public StoreDTO findStoreByADMINEmail(String email) {
        StoreEntity store = storeRepository.findByMembersEntity_Email(email);
        StoreDTO dto = modelMapper.map(store, StoreDTO.class)
                .setHotelDTO(modelMapper.map(store.getHotelEntity(), HotelDTO.class))
                .setMemberDTO(modelMapper.map(store.getMembersEntity(), MembersDTO.class));
        return dto;
    }

    @Override
    public List<OrdersDTO> read(Long paymentNum) {

        PaymentEntity paymentEntity = paymentRepository.findById(paymentNum).get();
        List<OrdersEntity> ordersEntityList = paymentEntity.getOrdersEntityList();
        List<OrdersDTO> ordersDTOList = ordersEntityList.stream().map(data -> modelMapper.map(data, OrdersDTO.class)
                .setOrdersItemDTOList(data.getOrdersItemEntities().stream().map(data2 -> modelMapper.map(data2, OrdersItemDTO.class)
                        .setOrdersDTO(modelMapper.map(data2.getOrdersEntity(), OrdersDTO.class)).setMenuDTO(modelMapper.map(data2.getMenuEntity(), MenuDTO.class)
                        )).toList())
                .setStoreDTO(modelMapper.map(data.getStoreEntity(), StoreDTO.class).setImgUrl(imageRepository.findByStoreEntity_Id(data.getStoreEntity().getId()).get().getFullUrl()))
                .setCheckInDTO(modelMapper.map(data.getCheckInEntity(), CheckInDTO.class))).toList();

        return ordersDTOList;
    }

    @Override
    public void changePayNow(Long ordersId, String memo, String email) {
        OrdersEntity ordersEntity = ordersRepository.findById(ordersId).get();
        ordersEntity.setMemo(memo);
        ordersEntity.setOrdersStatus(OrdersStatus.AWAITING);
        ordersEntity.setAwaitingTime(LocalDateTime.now());
        PaymentEntity paymentEntity = paymentRepository.findByOrdersEntityList_Id(ordersId);
        paymentEntity.setOrderType(OrderType.PAYNOW);
        paymentEntity.setPaidCheck(PaidCheck.paid);
        ordersRepository.save(ordersEntity);
        paymentRepository.save(paymentEntity);
        Long id = cartService.cartCheck(email);
        cartService.clear(id);
        sseService.sendToSAdmin("S" + ordersEntity.getStoreEntity().getId(), "new-order", ordersId + "번 주문이 들어왔습니다.");

    }

    @Override
    public void changePayLater(Long ordersId, String memo, String email) {
        OrdersEntity ordersEntity = ordersRepository.findById(ordersId).get();
        ordersEntity.setMemo(memo);
        ordersEntity.setOrdersStatus(OrdersStatus.AWAITING);
        ordersEntity.setAwaitingTime(LocalDateTime.now());
        PaymentEntity paymentEntity = paymentRepository.findByOrdersEntityList_Id(ordersId);
        paymentEntity.setOrderType(OrderType.PAYLATER);
        ordersRepository.save(ordersEntity);
        paymentRepository.save(paymentEntity);
        Long id = cartService.cartCheck(email);
        cartService.clear(id);
        sseService.sendToSAdmin("S" + ordersEntity.getStoreEntity().getId(), "new-order", ordersId + "번 주문이 들어왔습니다.");

    }

    @Override
    public void changeStatus(Long ordersId, String ordersStatus) {
        OrdersEntity orders = ordersRepository.findById(ordersId).get();
        orders.setOrdersStatus(checkStatus(ordersStatus));
        if (ordersStatus.equals("preparing")) {
            orders.setPreparingTime(LocalDateTime.now());
        } else if (ordersStatus.equals("delivering")) {
            orders.setDeliveringTime(LocalDateTime.now());
        } else {
            orders.setDeliveredTime(LocalDateTime.now());
        }
        ordersRepository.save(orders);
        if (orders.getCheckInEntity().getUsersEntity() != null) {
            if (ordersStatus.equals("preparing")) {
                sseService.sendToUsers("U" + orders.getCheckInEntity().getUsersEntity().getId(), "new-changeStatus", orders.getId() + "번 주문이 승인되어 조리를 시작하였습니다.");

            } else if (ordersStatus.equals("delivering")) {
                sseService.sendToUsers("U" + orders.getCheckInEntity().getUsersEntity().getId(), "new-changeStatus", orders.getId() + "번 주문의 조리가 완료되어 배달을 시작합니다.");

            } else {
                sseService.sendToUsers("U" + orders.getCheckInEntity().getUsersEntity().getId(), "new-changeStatus", orders.getId() + "번 주문 배달이 완료되었습니다.");
            }

        } else {
            if (ordersStatus.equals("preparing")) {
                sseService.sendToUsers("G" + orders.getCheckInEntity().getGuestEntity().getId(), "new-changeStatus", orders.getId() + "번 주문이 승인되어 조리를 시작하였습니다.");

            } else if (ordersStatus.equals("delivering")) {
                sseService.sendToUsers("G" + orders.getCheckInEntity().getGuestEntity().getId(), "new-changeStatus", orders.getId() + "번 주문의 조리가 완료되어 배달을 시작합니다.");

            } else {
                sseService.sendToUsers("G" + orders.getCheckInEntity().getGuestEntity().getId(), "new-changeStatus", orders.getId() + "번 주문 배달이 완료되었습니다.");
            }

        }

    }

    @Override
    public List<OrdersDTO> ordersListByEmail(String email) {

        List<OrdersEntity> ordersEntityList = null;
        if (email.contains("@")) {
            ordersEntityList = ordersRepository.findByCheckInEntity_UsersEntityEmail(email);

        } else {
            ordersEntityList = ordersRepository.findByCheckInEntity_GuestEntityPhone(email);
        }

        List<OrdersDTO> ordersDTOList = ordersEntityList.stream().map(ordersEntity -> modelMapper.map(ordersEntity, OrdersDTO.class).setStoreDTO(modelMapper.map(ordersEntity.getStoreEntity(), StoreDTO.class)
                .setImgUrl(imageRepository.findByStoreEntity_Id(ordersEntity.getStoreEntity().getId()).get().getFullUrl())).setOrdersItemDTOList(ordersEntity.getOrdersItemEntities().stream().map(ordersItemEntity -> modelMapper.map(ordersItemEntity, OrdersItemDTO.class).setMenuDTO(modelMapper.map(ordersItemEntity.getMenuEntity(), MenuDTO.class))).toList())).toList();


        return ordersDTOList;
    }

    @Override
    public OrdersStatus checkStatus(String ordersStatus) {
        if (ordersStatus.equals("preparing")) {
            return OrdersStatus.PREPARING;
        } else if (ordersStatus.equals("delivering")) {
            return OrdersStatus.DELIVERING;
        } else {
            return OrdersStatus.DELIVERED;
        }
    }


}
