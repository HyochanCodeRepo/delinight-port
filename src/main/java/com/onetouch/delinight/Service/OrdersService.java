/*********************************************************************
 * 클래스명 : MembersService
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.Constant.OrdersStatus;
import com.onetouch.delinight.DTO.MembersDTO;
import com.onetouch.delinight.DTO.OrdersDTO;
import com.onetouch.delinight.DTO.StoreDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrdersService {

    OrdersDTO readOne(Long ordersId);
    List<OrdersDTO> processList(String email);
    List<OrdersDTO> dashboard(String email);
    List<OrdersDTO> completeList(String email);
    StoreDTO findStoreByADMINEmail(String email);

    void checkInToCheckOut(Long checkInId, Long checkOutId);

    List<OrdersDTO> read(Long paymentNum);

    void changePayLater(Long ordersId, String memo, String email);
    void changePayNow(Long ordersId, String memo, String email);

    void changeStatus(Long ordersId, String ordersStatus);
    OrdersStatus checkStatus(String ordersStatus);

    List<OrdersDTO> ordersListByEmail(String email);

}
