/*********************************************************************
 * 클래스명 : UsersEntity
 * 기능 : 회원 시스템 구축
 * 작성자 : 이동건
 * 작성일 : 2025-04-01
 * 수정 : 2025-04-01      Entity 설계           이동건
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class UsersEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column
    private String password;
    
    @Column(nullable = false, length = 20)
    private String phone;

    @Column(nullable = false, length = 50)
    private String address;


}
