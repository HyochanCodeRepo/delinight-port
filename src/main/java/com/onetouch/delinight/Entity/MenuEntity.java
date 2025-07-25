/*********************************************************************
 * 클래스명 : MenuEntity
 * 기능 : MenuEntity 항목 수정(price,stockNumber, MenuStatus 추가)
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import com.onetouch.delinight.Constant.Menu;
import com.onetouch.delinight.Constant.MenuStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "menu")
public class MenuEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id")
    private Long id; //메뉴 코드번호

    @Column(nullable = false, length = 50)
    private String name; //메뉴명

    @Column(nullable = false, length = 50)
    private String content; //내용

    @Column(nullable = false)
    private int price; //가격


    @Enumerated(EnumType.STRING)
    MenuStatus menuStatus; // 상품판매상태

    @Enumerated(EnumType.STRING)
    Menu menu; // 메뉴목록


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private StoreEntity storeEntity;






}
