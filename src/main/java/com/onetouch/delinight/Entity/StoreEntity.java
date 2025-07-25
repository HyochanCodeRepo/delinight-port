/*********************************************************************
 * 클래스명 : MembersEntity
 * 기능 :
 * 작성자 :
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "store")
public class StoreEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = false, nullable = false, length = 50)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    @ToString.Exclude
    private HotelEntity hotelEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    @ToString.Exclude
    private MembersEntity membersEntity;

    public StoreEntity setMembersEntity(MembersEntity membersEntity){
        this.membersEntity = membersEntity;
        return  this;
    }



}
