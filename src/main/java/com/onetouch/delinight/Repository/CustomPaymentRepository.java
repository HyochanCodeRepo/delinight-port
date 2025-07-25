package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Constant.PaidCheck;
import com.onetouch.delinight.DTO.PaymentDTO;

import java.time.LocalDate;
import java.util.List;

public interface CustomPaymentRepository {

     List<PaymentDTO> findPaymentByCriteria(PaidCheck paidCheck, Long memberId, LocalDate startDate, LocalDate endDate);



}
