/*********************************************************************
 * 클래스명 : UsersRepository
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Repository;


import com.onetouch.delinight.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    @Query("select u from UsersEntity u where u.email = :email")
    UsersEntity selectEmail(String email); // Email 찾기

    boolean existsByEmail(String email); // 회원가입 여부확인


    void deleteByEmail(String email);

    // 로그인한 사용자를 username 으로 찾는 메서드
    Optional<UsersEntity> findByName(String name);

    UsersEntity findByEmail(String email);
}
