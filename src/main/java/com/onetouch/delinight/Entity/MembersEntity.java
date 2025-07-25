/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.Role;
import com.onetouch.delinight.Constant.Status;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "members")
public class MembersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "members_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @Column(nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private Role role;

    @JoinColumn(name = "center_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CenterEntity centerEntity;


    @JoinColumn(name = "hotel_id")
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private HotelEntity hotelEntity;

    @JoinColumn(name = "store_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private StoreEntity storeEntity;

    public StoreEntity setStoreEntity(StoreEntity storeEntity){
        this.storeEntity = storeEntity;
        return this.storeEntity;
    }



}
