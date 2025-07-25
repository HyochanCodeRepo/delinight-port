/*********************************************************************
 * 클래스명 : CartItemEntity
 * 기능 :
 * 작성자 : 이효찬
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30
 *********************************************************************/
package com.onetouch.delinight.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString(exclude = "ordersEntity")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ordersitem")
public class OrdersItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_item_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "orders_id")
    private OrdersEntity ordersEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private MenuEntity menuEntity;

    private Long quantity;
}
