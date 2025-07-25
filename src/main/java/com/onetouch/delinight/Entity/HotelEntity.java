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

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotel")
public class HotelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(unique = false, nullable = false, length = 50)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private BranchEntity branchEntity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="members_id")
    @ToString.Exclude
    private MembersEntity membersEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private BrandEntity brandEntity;

    @OneToMany(mappedBy = "hotelEntity", fetch = FetchType.LAZY)
    private List<StoreEntity> stores = new ArrayList<>();

    public HotelEntity setMembersEntity(MembersEntity membersEntity) {
        this.membersEntity = membersEntity;
        return this;
    }

    public HotelEntity setBrandEntity(BrandEntity brandEntity) {
        this.brandEntity = brandEntity;
        return this;
    }









}
