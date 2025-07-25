/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersDTO {

    private Long id;

    private String name;

    private String email;

    private String password;

    private String passwordOne;

    private String passwordTwo;

    private String phone;

    private String address;

}
