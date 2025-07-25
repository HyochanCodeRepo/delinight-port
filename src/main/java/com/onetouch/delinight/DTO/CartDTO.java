/*********************************************************************
 * 클래스명 : CartDTO
 * 기능 :
 * 작성자 : 이지호
 * 작성일 : 2025-04-01
 * 수정 : 2025-04-01     이지호
 *********************************************************************/
package com.onetouch.delinight.DTO;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long id;
    private List<MenuDTO> menuDTOList;
    private Long totalPrice;


}
