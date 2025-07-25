/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long>, CustomPaymentRepository {

    PaymentEntity findByOrdersEntityList_Id(Long id);


    @Query("select p from PaymentEntity p JOIN p.ordersEntityList o where o.checkInEntity.usersEntity.email =:email")
    List<PaymentEntity> findPaymentEntitiesByUsersEmail(@Param("email") String email);

    List<PaymentEntity> findByRegTimeBetween(LocalDateTime a, LocalDateTime b);

}
