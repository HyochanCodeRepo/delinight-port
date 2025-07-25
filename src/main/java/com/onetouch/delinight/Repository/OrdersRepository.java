/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.OrdersStatus;
import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.OrdersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {

    boolean existsByStoreEntity_HotelEntity_BranchEntity_CenterEntity_MembersEntity_IdAndAndPendingTimeAfter(Long id, LocalDateTime time);
    boolean existsByStoreEntity_HotelEntity_MembersEntity_IdAndAndPendingTimeAfter(Long id, LocalDateTime time);
    boolean existsByStoreEntity_MembersEntity_IdAndAndPendingTimeAfter(Long id, LocalDateTime time);

    List<OrdersEntity> findByCheckInEntity_Id(Long id);
    Integer countByStoreEntityIdAndOrdersStatus(Long storeId, OrdersStatus status);

    List<OrdersEntity> findByCheckInEntity_UsersEntityEmail(String email);
    List<OrdersEntity> findByCheckInEntity_GuestEntityPhone(String phone);
    List<OrdersEntity> findByStoreEntity_MembersEntity_EmailAndOrdersStatusNotAndOrdersStatusIsNot(String email, OrdersStatus ordersStatus, OrdersStatus ordersStatus2);
    List<OrdersEntity> findByStoreEntity_MembersEntity_EmailAndOrdersStatusIs(String email, OrdersStatus ordersStatus);
    List<OrdersEntity> findByStoreEntity_MembersEntity_EmailAndPendingTimeIsAfter(String email, LocalDateTime yesterDay);

    List<OrdersEntity> findByCheckOutLogEntity_Id(Long checkOutId);





    List<OrdersEntity> findByPendingTimeBetween(LocalDateTime yd, LocalDateTime to);
    List<OrdersEntity> findByPaymentEntity_Id(Long id);




}
