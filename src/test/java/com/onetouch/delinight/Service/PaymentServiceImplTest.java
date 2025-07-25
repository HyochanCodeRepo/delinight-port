package com.onetouch.delinight.Service;

import com.onetouch.delinight.Entity.OrdersEntity;
import com.onetouch.delinight.Entity.OrdersItemEntity;
import com.onetouch.delinight.Entity.PaymentEntity;
import com.onetouch.delinight.Repository.OrdersItemRepository;
import com.onetouch.delinight.Repository.OrdersRepository;
import com.onetouch.delinight.Repository.PaymentRepository;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Log4j2
class PaymentServiceImplTest {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    OrdersItemRepository ordersItemRepository;
    @Autowired
    OrdersRepository ordersRepository;

//    @Test
//    public void test(){
//        Long storeId = 1L;
//        List<PaymentEntity> b = paymentRepository.a(storeId);
//        log.info(b);
//    }

    @Test
    @Commit
    public void changeDateForDummy(){
        for(int date = 15; date>=1; date--){
            LocalDate newDate = LocalDate.of(2025, 5, date);

            List<PaymentEntity> aList = paymentRepository.findByRegTimeBetween(LocalDateTime.of(LocalDate.of(2025,5,16), LocalTime.of(0,0,0)),LocalDateTime.of(LocalDate.of(2025,5,16), LocalTime.of(20,0,0)));

            for(PaymentEntity a: aList){
                PaymentEntity paymentEntity = new PaymentEntity();
                paymentEntity.setPaidCheck(a.getPaidCheck());
                paymentEntity.setOrderType(a.getOrderType());
                paymentEntity.setUpdateTime(newDate.atTime(0,0));
                PaymentEntity newPayment = paymentRepository.save(paymentEntity);
                for(OrdersEntity b: ordersRepository.findByPaymentEntity_Id(a.getId())){
                    OrdersEntity ordersEntity = new OrdersEntity();
                    ordersEntity.setPaymentEntity(newPayment);
                    if(b.getCheckInEntity()!=null){
                        ordersEntity.setCheckInEntity(b.getCheckInEntity());
                    }
                    else if(b.getCheckOutLogEntity()!=null){
                        ordersEntity.setCheckOutLogEntity(b.getCheckOutLogEntity());

                    }
                    ordersEntity.setOrdersStatus(b.getOrdersStatus());
                    ordersEntity.setStoreEntity(b.getStoreEntity());
                    ordersEntity.setPendingTime(newDate.atTime(0,0));
                    if(b.getAwaitingTime()!=null){
                        ordersEntity.setAwaitingTime(newDate.atTime(0,0));
                        if(b.getPreparingTime()!=null){
                            ordersEntity.setPreparingTime(newDate.atTime(0,0));
                            if(b.getDeliveringTime()!=null){
                                ordersEntity.setDeliveringTime(newDate.atTime(0,0));
                                if(b.getDeliveredTime()!=null){
                                    ordersEntity.setDeliveredTime(newDate.atTime(0,0));
                                }
                            }
                        }

                    }
                    if(b.getMemo()!=null){
                        ordersEntity.setMemo(b.getMemo());
                    }



                    if(b.getOrderType()!=null){
                        ordersEntity.setOrderType(b.getOrderType());
                    }
                    OrdersEntity  newOrdersEntity = ordersRepository.save(ordersEntity);
                    Long totalPrice1 = 0L;
                    for(OrdersItemEntity oi : b.getOrdersItemEntities()){

                        OrdersItemEntity ordersItemEntity = new OrdersItemEntity();
                        ordersItemEntity.setQuantity((long)ThreadLocalRandom.current().nextInt(1, 7));
                        ordersItemEntity.setMenuEntity(oi.getMenuEntity());
                        ordersItemEntity.setOrdersEntity(newOrdersEntity);
                        totalPrice1 += ordersItemEntity.getQuantity()*ordersItemEntity.getMenuEntity().getPrice();
                        ordersItemRepository.save(ordersItemEntity);
                    }
                    newOrdersEntity.setTotalPrice(totalPrice1);

                    ordersRepository.save(newOrdersEntity);

                }
                newPayment.setRegTime(newDate.atTime(0,0));
                paymentRepository.save(newPayment);
            }

        }

    }

}