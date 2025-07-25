package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.DTO.*;
import com.onetouch.delinight.Entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Log4j2
public class CustomPaymentRepositoryImpl implements CustomPaymentRepository {

    @PersistenceContext
    private EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final MembersRepository membersRepository;

    public List<PaymentDTO> findPaymentByCriteria(PaidCheck paidCheck, Long memberId, LocalDate startDate, LocalDate endDate) {
        QMembersEntity membersEntity = QMembersEntity.membersEntity;
        QCenterEntity centerEntity = QCenterEntity.centerEntity;
        QBranchEntity branchEntity = QBranchEntity.branchEntity;
        QHotelEntity hotelEntity = QHotelEntity.hotelEntity;
        QStoreEntity storeEntity = QStoreEntity.storeEntity;
        QPaymentEntity paymentEntity = QPaymentEntity.paymentEntity;
        QOrdersEntity ordersEntity = QOrdersEntity.ordersEntity;
        QOrdersItemEntity ordersItemEntity = QOrdersItemEntity.ordersItemEntity;
        QMenuEntity menuEntity = QMenuEntity.menuEntity;
        BooleanBuilder builder = new BooleanBuilder();

        JPAQuery<PaymentEntity> query = new JPAQuery<>(entityManager);


        // 1. from 절 설정
        query.from(paymentEntity)
                .join(paymentEntity.ordersEntityList, ordersEntity).fetchJoin()
                .join(ordersEntity.ordersItemEntities, ordersItemEntity)
                .join(ordersItemEntity.menuEntity, menuEntity)
                .fetchJoin();

        // 2. 멤버 ID 필터링 추가
        if (memberId != null) {
            Role role = membersRepository.findById(memberId).get().getRole();

            if(role.equals(Role.STOREADMIN)){
                query.join(ordersEntity.storeEntity, storeEntity)                                // PaymentEntity에 있는 membersEntity와 join
                        .where(storeEntity.membersEntity.id.eq(memberId));                       // 멤버 ID로 필터링
            }
            else if(role.equals(Role.ADMIN)){
                query.join(ordersEntity.storeEntity.hotelEntity, hotelEntity).where(hotelEntity.membersEntity.id.eq(memberId));
            }

            else if(role.equals(Role.SUPERADMIN)){

                query.join(ordersEntity.storeEntity, storeEntity)
                        .join(storeEntity.hotelEntity, hotelEntity)
                        .join(hotelEntity.branchEntity, branchEntity)
                        .join(branchEntity.centerEntity, centerEntity)
                        .where(centerEntity.membersEntity.id.eq(memberId));
            }

        }


        // 3. 정산 상태 필터링
        if (paidCheck != null) {
            switch (paidCheck){
                case paid : builder.and(paymentEntity.paidCheck.eq(paidCheck));
                break;
                case none : builder.and(paymentEntity.paidCheck.eq(PaidCheck.none));
                break;
                case both :
                    break;
            }
        }

        // 4. 날짜로 필터링
        if (startDate != null && endDate != null) {
            LocalDateTime startDate1 = startDate.atTime(LocalTime.MIDNIGHT);
            LocalDateTime endDate1 = endDate.atTime(LocalTime.MIDNIGHT);

            builder.and(paymentEntity.regTime.between(startDate1, endDate1));
        }

        query.where(builder);

        // 5. 쿼리 실행
        List<PaymentEntity> paymentEntities = query.fetch();

        // 6. PaymentEntity → PaymentDTO 변환
        List<PaymentDTO> paymentDTOList = paymentEntities.stream().map(payment -> {
            // OrdersDTO 변환
            List<OrdersDTO> ordersDTOList = payment.getOrdersEntityList().stream()
                    .filter(Objects::nonNull)
                    .distinct()
                    .map(order -> OrdersDTO.builder()
                            .id(order.getId())
                            .totalPrice(order.getTotalPrice())
                            .ordersStatus(order.getOrdersStatus())
                            .pendingTime(order.getPendingTime())
                            .deliveredTime(order.getDeliveredTime())
                            .ordersItemDTOList(order.getOrdersItemEntities().stream().map(orderItem -> OrdersItemDTO.builder()
                                    .id(orderItem.getId())
                                    .quantity(orderItem.getQuantity())
                                    .menuDTO(modelMapper.map(orderItem.getMenuEntity(), MenuDTO.class))
                                    .build()
                            ).collect(Collectors.toList()))
                            .storeDTO(modelMapper.map(order.getStoreEntity(), StoreDTO.class)
                                    .setHotelDTO(modelMapper.map(order.getStoreEntity().getHotelEntity(), HotelDTO.class)
                                            .setBranchDTO(modelMapper.map(order.getStoreEntity().getHotelEntity().getBranchEntity(), BranchDTO.class)
                                                    .setCenterDTO(modelMapper.map(order.getStoreEntity().getHotelEntity().getBranchEntity().getCenterEntity(), CenterDTO.class)))))
                            .build()).toList();

            // PaymentDTO 변환
            return PaymentDTO.builder()
                    .totalId(payment.getId())                           // 정산 ID
                    .checkPaid(payment.getPaidCheck())                  // 정산 상태
                    .regTime(payment.getRegTime())                      // 등록일
                    .ordersDTOList(ordersDTOList)                       // 주문 내역 리스트
                    .build();
        }).collect(Collectors.toList());

        return paymentDTOList;
    }




}
