/*********************************************************************
 * 클래스명 : MembersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;

import com.onetouch.delinight.Entity.CartEntity;
import com.onetouch.delinight.Entity.MembersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartEntity, Long> {


        CartEntity findByUsersEntity_Email(String email);
        CartEntity findByUsersEntity_Id(Long id);
        CartEntity findByGuestEntity_Id(Long id);

        CartEntity findByGuestEntity_Phone(String phone);

}
