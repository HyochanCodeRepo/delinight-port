/*********************************************************************
 * 클래스명 : MembersDTO
 * 기능 :
 * 작성자 : 이동건
 * 작성일 : 2025-03-30
 * 수정 : 2025-03-30     이동건
 *********************************************************************/
package com.onetouch.delinight.DTO;

import lombok.*;
import org.springframework.boot.actuate.autoconfigure.amqp.RabbitHealthContributorAutoConfiguration;

import java.util.List;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDTO {

    private Long id;
    private String name;

    private String content;


    private Long imgNum;


    private String imgUrl;
    private HotelDTO hotelDTO;

    private MembersDTO membersDTO;

    private List<MenuDTO> menuDTOList;

    public StoreDTO setMenuDTOList(List<MenuDTO> menuDTOList){
        this.menuDTOList = menuDTOList;
        return this;
    }
    public StoreDTO setHotelDTO(HotelDTO hotelDTO){
        this.hotelDTO = hotelDTO;
        return this;
    }
    public StoreDTO setMemberDTO(MembersDTO membersDTO){
        this.membersDTO = membersDTO;
        return this;
    }
    public StoreDTO setImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
        return this;
    }


}
