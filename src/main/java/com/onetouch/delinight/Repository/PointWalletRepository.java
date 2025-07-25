package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.PointWalletEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PointWalletRepository extends JpaRepository<PointWalletEntity,Long> {
//
//    Optional <PointWalletEntity> findByUsersId(Long usersId);
//
//    public PointWalletEntity deleteByUsersId(Long usersId);
//
//    boolean existsByUsersId(Long usersId);
//
////    //주문 기록에서 총 금액을 구함, 결제 방법이 선결제(PAYNOW)인 주문만 필터링 특정 사용자의 주문만 선택(userId)
////    @Query("select sum(o.totalPrice) from OrdersEntity o where o.paymentEntity.orderType = 'PAYNOW' and o.checkInEntity.usersEntity.id = :userId")
////    Long selectTotalPrice(Long userId);
//
//    @Query("""
//    select sum(o.totalPrice)
//    from OrdersEntity o
//    where o.paymentEntity.orderType = 'PAYNOW'
//      and o.paymentEntity.amount > 0
//      and o.checkInEntity.usersEntity.id = :userId
//""")
//    Long selectTotalPrice(Long userId);
//
//
////    //주문 기록에서 총 금액을 구함, 결제 방법이 후결제(PAYLATER)인 주문만 필터링 특정 사용자의 주문만 선택(userId)
////    @Query("select sum(o.totalPrice) from OrdersEntity o where o.paymentEntity.orderType = 'PAYLATER' and o.checkInEntity.usersEntity.id = :userId")
////    Long selectTotalPriceA(Long userId);

}
