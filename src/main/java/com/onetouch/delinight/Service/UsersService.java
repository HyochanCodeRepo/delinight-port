/*********************************************************************
 * 클래스명 : UsersService
 * 기능 : 회원가입 , 비밀번호 변경 & 찾기 , 정보수정, 회원탈퇴
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Service;

import com.onetouch.delinight.DTO.UsersDTO;
import com.onetouch.delinight.Entity.UsersEntity;

public interface UsersService {

    void singUpUser(UsersDTO usersDTO);

    boolean passwordChange(UsersDTO usersDTO);

    String sendTemporaryPassword(UsersDTO usersDTO);

    void userUpdate(String email, UsersDTO usersDTO);

    void userDelete(String email);

    UsersDTO findUsersByEmail(String email);



}
