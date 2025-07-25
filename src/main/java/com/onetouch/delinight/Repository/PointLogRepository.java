package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.PointLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointLogRepository extends JpaRepository<PointLogEntity,Long> {

//    List<PointLogEntity> findByPaymentId(Long paymentId);
}
